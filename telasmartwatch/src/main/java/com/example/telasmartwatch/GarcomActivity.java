package com.example.telasmartwatch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GarcomActivity extends ComponentActivity {

    private static final String WAITER_ID = "w1";

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private TextView emptyView;
    private final List<NotificationItem> items = new ArrayList<>();
    private NotificationsAdapter adapter;
    private WearFirestoreService watchService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garcom_center);

        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this);
            }
        } catch (Throwable t) {
            TextView tv = findViewById(R.id.emptyView);
            tv.setText("Firebase não inicializado: " + t.getMessage());
            tv.setVisibility(View.VISIBLE);
            return;
        }

        ensureNotifPermission();

        watchService = new WearFirestoreService(this);
        watchService.start();

        androidx.wear.widget.WearableRecyclerView rv = findViewById(R.id.recycler);
        emptyView = findViewById(R.id.emptyView);

        androidx.wear.widget.WearableLinearLayoutManager manager =
                new androidx.wear.widget.WearableLinearLayoutManager(
                        this,
                        new androidx.wear.widget.WearableLinearLayoutManager.LayoutCallback() {
                            @Override
                            public void onLayoutFinished(View child, RecyclerView parent) {
                                float center = parent.getHeight() / 2f;
                                float y = child.getY() + child.getHeight() / 2f;
                                float dist = Math.abs(center - y) / center;
                                float scale = 1f - 0.2f * Math.min(1f, dist);
                                child.setScaleX(scale);
                                child.setScaleY(scale);
                                child.setAlpha(0.85f + (scale - 0.85f));
                            }
                        });

        rv.setLayoutManager(manager);
        rv.setEdgeItemsCenteringEnabled(true);
        rv.setCircularScrollingGestureEnabled(true);

        adapter = new NotificationsAdapter(items);
        rv.setAdapter(adapter);
        updateEmpty("Abrindo…");

        // swipe para ACK
        ItemTouchHelper ith = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override public boolean onMove(RecyclerView r, RecyclerView.ViewHolder vH, RecyclerView.ViewHolder t) { return false; }
                    @Override public void onSwiped(RecyclerView.ViewHolder vh, int dir) {
                        int pos = vh.getBindingAdapterPosition();
                        NotificationItem n = items.remove(pos);
                        adapter.notifyItemRemoved(pos);
                        updateEmpty("Dispensado.");
                        if ("calls".equals(n.collection)) {
                            db.collection("calls").document(n.id).update("status", "ACK");
                        } else if ("orders".equals(n.collection)) {
                            db.collection("orders").document(n.id).update("status", "ACK");
                        }
                    }
                });
        ith.attachToRecyclerView(rv);

        // Firebase
        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() != null) {
            updateEmpty("Conectado. Aguardando…");
            debugProbe();
            startWatchers();
        } else {
            updateEmpty("Conectando ao Firebase…");
            auth.signInAnonymously().addOnCompleteListener(t -> {
                if (t.isSuccessful()) {
                    updateEmpty("Conectado. Aguardando…");
                    debugProbe();
                    startWatchers();
                } else {
                    updateEmpty("Falha login anônimo: " +
                            (t.getException()!=null ? t.getException().getMessage() : "") +
                            " | Tentando leitura sem login…");
                    startWatchers();
                }
            });
        }

        // limpeza periódica (>60min)
        rv.postDelayed(new Runnable() {
            @Override public void run() {
                pruneOld();
                rv.postDelayed(this, 30_000);
            }
        }, 30_000);
    }

    // ---------- Firestore listeners ----------

    private void startWatchers() {
        final String waiter = WAITER_ID;

        // inicial: calls
        db.collection("calls")
                .whereEqualTo("assignedWaiterId", waiter)
                .whereEqualTo("status", "OPEN")
                .get()
                .addOnSuccessListener(snap -> {
                    int add = 0;
                    for (DocumentSnapshot d : snap.getDocuments()) {
                        add += addFromDoc(d, "calls", "Cliente chamou o garçom");
                    }
                    if (add > 0) adapter.notifyDataSetChanged();
                    updateEmpty("Calls inicial: " + add);
                })
                .addOnFailureListener(e -> updateEmpty("Erro calls(get): " + e.getMessage()));

        // snapshot: calls
        db.collection("calls")
                .whereEqualTo("assignedWaiterId", waiter)
                .whereEqualTo("status", "OPEN")
                .addSnapshotListener((snap, err) -> {
                    if (err != null) { updateEmpty("Erro calls(snapshot): " + err.getMessage()); return; }
                    if (snap == null)  { updateEmpty("Calls snapshot null"); return; }
                    int added = 0;
                    for (DocumentChange dc : snap.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            added += addFromDoc(dc.getDocument(), "calls", "Cliente chamou o garçom");
                        }
                    }
                    if (added > 0) adapter.notifyDataSetChanged();
                    pruneOld();
                    updateEmpty("Calls snapshot +" + added + " | total " + items.size());
                });

        // inicial: orders (READY)
        db.collection("orders")
                .whereEqualTo("assignedWaiterId", waiter)
                .whereEqualTo("status", "READY")
                .get()
                .addOnSuccessListener(snap -> {
                    int add = 0;
                    for (DocumentSnapshot d : snap.getDocuments()) {
                        add += addFromDoc(d, "orders", "Pedido pronto para entrega");
                    }
                    if (add > 0) adapter.notifyDataSetChanged();
                    updateEmpty("Orders inicial: " + add);
                })
                .addOnFailureListener(e -> updateEmpty("Erro orders(get): " + e.getMessage()));

        // snapshot: orders
        db.collection("orders")
                .whereEqualTo("assignedWaiterId", waiter)
                .whereEqualTo("status", "READY")
                .addSnapshotListener((snap, err) -> {
                    if (err != null) { updateEmpty("Erro orders(snapshot): " + err.getMessage()); return; }
                    if (snap == null)  { updateEmpty("Orders snapshot null"); return; }
                    int added = 0;
                    for (DocumentChange dc : snap.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            added += addFromDoc(dc.getDocument(), "orders", "Pedido pronto para entrega");
                        }
                    }
                    if (added > 0) adapter.notifyDataSetChanged();
                    pruneOld();
                    updateEmpty("Orders snapshot +" + added + " | total " + items.size());
                });
    }

    private int addFromDoc(DocumentSnapshot d, String collection, String body) {
        String id = d.getId();
        String table = String.valueOf(d.get("tableId"));
        Timestamp created = d.contains("createdAt") ? d.getTimestamp("createdAt") : Timestamp.now();
        addOrUpdate(new NotificationItem(id, collection, table,
                "Mesa " + normalizeMesa(table), body, created));
        return 1;
    }

    private void addOrUpdate(NotificationItem n) {
        for (int i = 0; i < items.size(); i++) {
            NotificationItem x = items.get(i);
            if (x.id.equals(n.id) && x.collection.equals(n.collection)) {
                items.set(i, n);
                return;
            }
        }
        items.add(0, n); // mais novo no topo
    }

    private void pruneOld() {
        long cutoff = System.currentTimeMillis() - 60L * 60L * 1000L; // 60min
        boolean changed = false;
        for (Iterator<NotificationItem> it = items.iterator(); it.hasNext(); ) {
            NotificationItem n = it.next();
            long t = (n.createdAt != null ? n.createdAt.toDate().getTime() : 0L);
            if (t < cutoff) { it.remove(); changed = true; }
        }
        if (changed) {
            adapter.notifyDataSetChanged();
            updateEmpty("Auto-limpeza (60min)");
        }
    }

    private String normalizeMesa(String tableId) {
        if (tableId == null) return "?";
        String digits = tableId.replaceAll("\\D+","");
        return digits.isEmpty() ? tableId : digits;
    }

    private void updateEmpty(String info) {
        if (items.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("Sem notificações recentes\n(" + info + ")");
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    private void debugProbe() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setText("Debug: consultando Firestore…");

        db.collection("calls")
                .whereEqualTo("assignedWaiterId", WAITER_ID)
                .whereEqualTo("status", "OPEN")
                .get()
                .addOnSuccessListener(snap -> {
                    int count = (snap == null) ? 0 : snap.size();
                    emptyView.setText("Debug: calls OPEN para " + WAITER_ID + " = " + count);
                    if (snap != null) {
                        for (DocumentSnapshot d : snap.getDocuments()) {
                            String table = String.valueOf(d.get("tableId"));
                            emptyView.append("\n- " + table);
                        }
                    }
                })
                .addOnFailureListener(e -> emptyView.setText("Debug: ERRO calls(get): " + e.getMessage()));
    }

    private static final int REQ_NOTIF = 1001;
    private void ensureNotifPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{ Manifest.permission.POST_NOTIFICATIONS }, REQ_NOTIF);
            }
        }
    }
}

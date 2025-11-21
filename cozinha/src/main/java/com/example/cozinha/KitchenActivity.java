package com.example.cozinha;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class KitchenActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private KitchenAdapter adapter;
    private TextView empty;

    private ListenerRegistration lNew;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);

        if (FirebaseApp.getApps(this).isEmpty()) FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();

        RecyclerView rv = findViewById(R.id.recycler);
        empty = findViewById(R.id.emptyView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new KitchenAdapter();
        rv.setAdapter(adapter);

        signInAndStart();
    }

    private void signInAndStart() {
        if (auth.getCurrentUser() != null) {
            startWatcher();
        } else {
            auth.signInAnonymously().addOnCompleteListener(t -> {
                if (t.isSuccessful()) {
                    startWatcher();
                } else {
                    empty.setText("Falha login anônimo: " +
                            (t.getException()!=null ? t.getException().getMessage() : ""));
                    empty.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void startWatcherNEW() {
        if (lNew != null) lNew.remove();
        lNew = db.collection("orders")
                .whereEqualTo("status", "NEW")
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .addSnapshotListener((snap, e) -> {
                    if (e != null) {
                        empty.setText("Erro Firestore: " + e.getMessage());
                        empty.setVisibility(View.VISIBLE);
                        adapter.submit(new ArrayList<>());
                        return;
                    }
                    if (snap == null) {
                        empty.setText("Snapshot nulo");
                        empty.setVisibility(View.VISIBLE);
                        adapter.submit(new ArrayList<>());
                        return;
                    }
                    List<OrderItem> list = new ArrayList<>();
                    for (DocumentSnapshot d : snap.getDocuments()) {
                        OrderItem o = d.toObject(OrderItem.class);
                        if (o == null) continue;
                        o.id = d.getId();
                        list.add(o);
                    }
                    adapter.submit(list);
                    empty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
                });
    }

    private void startWatcher() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("orders")
                .orderBy("createdAt", Query.Direction.ASCENDING);

        lNew = query.addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                empty.setText("Erro Firestore: " + e.getMessage());
                empty.setVisibility(View.VISIBLE);
                adapter.submit(new ArrayList<>());
                return;
            }

            if (snapshots == null || snapshots.isEmpty()) {
                empty.setText("Nenhum pedido na fila.");
                empty.setVisibility(View.VISIBLE);
                adapter.submit(new ArrayList<>());
                return;
            }

            List<OrderItem> list = new ArrayList<>();

            for (DocumentSnapshot d : snapshots.getDocuments()) {
                OrderItem o = d.toObject(OrderItem.class);
                if (o == null) continue;

                if (!"NEW".equals(o.status) && !"IN_PROGRESS".equals(o.status)) {
                    continue;
                }

                o.id = d.getId();
                list.add(o);
            }

            if (list.isEmpty()) {
                empty.setText("Nenhum pedido na fila.");
                empty.setVisibility(View.VISIBLE);
            } else {
                empty.setVisibility(View.GONE);
            }

            adapter.submit(list);
        });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (lNew != null) lNew.remove();
    }
}

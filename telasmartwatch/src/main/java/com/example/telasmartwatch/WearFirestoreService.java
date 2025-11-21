package com.example.telasmartwatch;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;

public class WearFirestoreService {
    private static final String CHANNEL_ID = "watch_local";
    private static final String WAITER_ID = "w1";
    private final Context ctx;
    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    public WearFirestoreService(Context ctx) {
        this.ctx = ctx.getApplicationContext();
        this.auth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        ensureChannel();
    }

    private void ensureChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_ID, "Watch", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.createNotificationChannel(ch);
        }
    }

    public void start() {
        if (auth.getCurrentUser() != null) {
            watch();
        } else {
            auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) watch();
                }
            });
        }
    }

    private void watch() {
        db.collection("calls")
                .whereEqualTo("assignedWaiterId", WAITER_ID)
                .whereEqualTo("status", "OPEN")
                .addSnapshotListener(MetadataChanges.EXCLUDE, (value, error) -> {
                    if (error != null || value == null) return;
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            String table = String.valueOf(dc.getDocument().get("tableId"));
                            notifyLocal("Mesa " + table, "Cliente chamou o garçom");
                        }
                    }
                });

        db.collection("orders")
                .whereEqualTo("assignedWaiterId", WAITER_ID)
                .whereEqualTo("status", "READY")
                .addSnapshotListener(MetadataChanges.EXCLUDE, (value, error) -> {
                    if (error != null || value == null) return;
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            String table = String.valueOf(dc.getDocument().get("tableId"));
                            notifyLocal("Mesa " + table, "Pedido pronto para entrega");
                        }
                    }
                });
    }

    private void notifyLocal(String title, String body) {
        Notification n = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        NotificationManagerCompat.from(ctx).notify((int) System.currentTimeMillis(), n);
    }
}

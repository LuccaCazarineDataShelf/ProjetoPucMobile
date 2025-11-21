package com.example.telasmartwatch;

import com.google.firebase.Timestamp;

public class NotificationItem {
    public String id;
    public String collection; // "calls" ou "orders"
    public String tableId;
    public String title;      // "Mesa 3"
    public String body;       // "Cliente chamou o garçom" | "Pedido pronto..."
    public Timestamp createdAt;

    public NotificationItem(String id, String collection, String tableId,
                            String title, String body, Timestamp createdAt) {
        this.id = id;
        this.collection = collection;
        this.tableId = tableId;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
    }
}

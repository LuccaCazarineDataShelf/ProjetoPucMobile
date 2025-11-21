package com.example.cozinha;

import com.google.firebase.Timestamp;
import java.util.List;
import java.util.Map;

public class OrderItem {
    public String id;
    public String tableId;
    public String status;
    public String assignedWaiterId;
    public Timestamp createdAt;
    public List<Map<String, Object>> items;

    public OrderItem() {}
}

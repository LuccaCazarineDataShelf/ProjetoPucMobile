package com.example.cozinha;

import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import java.util.*;

public class KitchenAdapter extends RecyclerView.Adapter<KitchenAdapter.Holder> {
    private final List<OrderItem> data = new ArrayList<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void submit(List<OrderItem> list){
        data.clear(); data.addAll(list); notifyDataSetChanged();
    }

    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View v1 = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_kitchen_order, p, false);
        return new Holder(v1);
    }

    @Override public void onBindViewHolder(@NonNull Holder h, int pos) {
        OrderItem o = data.get(pos);
        h.txtHeader.setText("Mesa " + o.tableId + " — " + o.status);

        // lista compacta dos itens
        StringBuilder sb = new StringBuilder();
        if (o.items != null) {
            for (Map<String,Object> m : o.items) {
                String name = (String) m.get("name");
                Object q = m.get("qty"); int qty = q==null?1:((Number)q).intValue();
                if (sb.length()>0) sb.append("   ");
                sb.append("• ").append(name).append(" x").append(qty);
            }
        }
        h.txtItems.setText(sb.toString());

        // botões
        h.btnStart.setEnabled(!"IN_PROGRESS".equals(o.status) && !"READY".equals(o.status));
        h.btnReady.setEnabled(!"READY".equals(o.status));

        h.btnStart.setOnClickListener(v ->
                db.collection("orders").document(o.id)
                        .update("status","IN_PROGRESS","updatedAt", FieldValue.serverTimestamp())
        );

        h.btnReady.setOnClickListener(v ->
                db.collection("orders").document(o.id)
                        .update("status","READY",
                                "readyAt", FieldValue.serverTimestamp(),
                                "updatedAt", FieldValue.serverTimestamp())
        );
    }

    @Override public int getItemCount() { return data.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        TextView txtHeader, txtItems; Button btnStart, btnReady;
        Holder(@NonNull View v) {
            super(v);
            txtHeader = v.findViewById(R.id.txtHeader);
            txtItems  = v.findViewById(R.id.txtItems);
            btnStart  = v.findViewById(R.id.btnStart);
            btnReady  = v.findViewById(R.id.btnReady);
        }
    }
}

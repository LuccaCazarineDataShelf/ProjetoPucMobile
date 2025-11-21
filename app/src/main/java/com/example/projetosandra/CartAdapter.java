package com.example.projetosandra;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Holder> {

    public interface Listener {
        void onInc(int position);
        void onDec(int position); // se chegar a 0, remover
    }

    private final List<CartLine> data;
    private final Listener listener;

    public CartAdapter(List<CartLine> data, Listener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_row, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        CartLine line = data.get(pos);
        h.txtName.setText(line.item.name);
        h.txtSub.setText(line.item.category + " • R$ " + String.format("%.2f", line.item.price));
        h.txtQty.setText(String.valueOf(line.qty));

        h.btnPlus.setOnClickListener(v -> {
            if (listener != null) listener.onInc(h.getAdapterPosition());
        });
        h.btnMinus.setOnClickListener(v -> {
            if (listener != null) listener.onDec(h.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        TextView txtName, txtSub, txtQty;
        Button btnPlus, btnMinus;
        Holder(View v) {
            super(v);
            txtName = v.findViewById(R.id.txtName);
            txtSub  = v.findViewById(R.id.txtSub);
            txtQty  = v.findViewById(R.id.txtQty);
            btnPlus = v.findViewById(R.id.btnPlus);
            btnMinus= v.findViewById(R.id.btnMinus);
        }
    }
}

package com.example.projetosandra;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.Holder> {
    private final List<MenuItemModel> data;
    private final List<MenuItemModel> cart = new ArrayList<>();

    public MenuAdapter(List<MenuItemModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        MenuItemModel m = data.get(pos);
        h.txtName.setText(m.name + " (" + m.category + ")");
        h.txtPrice.setText(String.format("R$ %.2f", m.price));
        h.btnAdd.setOnClickListener(v -> cart.add(m));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<MenuItemModel> getCartItems() {
        return new ArrayList<>(cart);
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        Button btnAdd;
        Holder(View v) {
            super(v);
            txtName = v.findViewById(R.id.txtName);
            txtPrice = v.findViewById(R.id.txtPrice);
            btnAdd   = v.findViewById(R.id.btnAdd);
        }
    }
}

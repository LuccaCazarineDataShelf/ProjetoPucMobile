package com.example.projetosandra;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuCardAdapter extends RecyclerView.Adapter<MenuCardAdapter.Holder> {

    public interface OnAddListener {
        void onAdd(MenuItemModel item);
    }

    private final List<MenuItemModel> data;
    private final OnAddListener listener;

    public MenuCardAdapter(List<MenuItemModel> data, OnAddListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_card, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        MenuItemModel m = data.get(pos);
        h.txtName.setText(m.name);
        h.txtCategory.setText(m.category);
        h.txtPrice.setText(String.format("R$ %.2f", m.price));
        h.img.setImageResource(m.imageRes);
        h.btnAdd.setOnClickListener(v -> listener.onAdd(m));
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName, txtCategory, txtPrice;
        MaterialButton btnAdd;
        Holder(View v) {
            super(v);
            img = v.findViewById(R.id.img);
            txtName = v.findViewById(R.id.txtName);
            txtCategory = v.findViewById(R.id.txtCategory);
            txtPrice = v.findViewById(R.id.txtPrice);
            btnAdd = v.findViewById(R.id.btnAdd);
        }
    }
}

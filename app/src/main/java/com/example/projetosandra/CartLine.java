package com.example.projetosandra;

public class CartLine {
    public MenuItemModel item;
    public int qty;

    public CartLine(MenuItemModel item, int qty) {
        this.item = item;
        this.qty = qty;
    }

    public double subtotal() {
        return item.price * qty;
    }
}

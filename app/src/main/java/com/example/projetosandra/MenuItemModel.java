package com.example.projetosandra;

public class MenuItemModel {
    public final String category;
    public final String name;
    public final double price;
    public final int imageRes;   // novo: imagem do item (drawable)

    public MenuItemModel(String category, String name, double price, int imageRes) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.imageRes = imageRes;
    }
}

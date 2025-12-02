package com.example.projetosandra;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DataProvider {
    private static final Map<String, ArrayList<MenuItemModel>> data = new LinkedHashMap<>();
    private static boolean inited = false;

    public static void init() {
        if (inited) return;

        // PRATOS
        put("Pratos",
                new MenuItemModel("Pratos","Spaghetti à Bolonhesa", 38.0, R.drawable.spageti2),
                new MenuItemModel("Pratos","Parmegiana", 42.0, R.drawable.parmegiana),
                new MenuItemModel("Pratos","Filé de Frango Grelhado", 34.0, R.drawable.frango),
                new MenuItemModel("Pratos","Risoto de Cogumelos",  39.0, R.drawable.cogumelos)
        );

        // SOBREMESAS
        put("Sobremesas",
                new MenuItemModel("Sobremesas","Pudim",     16.0, R.drawable.pudim),
                new MenuItemModel("Sobremesas","Brownie",   18.0, R.drawable.brownie),
                new MenuItemModel("Sobremesas","Sorvete",   14.0, R.drawable.sorvete),
                new MenuItemModel("Sobremesas","Cheesecake",19.0, R.drawable.cake)
        );

        // VEGETARIANOS
        put("Vegetarianos",
                new MenuItemModel("Vegetarianos","Hambúrguer de Grão-de-bico",36.00, R.drawable.bico),
                new MenuItemModel("Vegetarianos","Lasanha de Berinjela",37.00, R.drawable.berinjela),
                new MenuItemModel("Vegetarianos","Salada",29.00, R.drawable.salada)

        );

        // BEBIDAS

        put("Bebidas",
                new MenuItemModel("Bebidas","Suco de Laranja", 10.0, R.drawable.laranja),
                new MenuItemModel("Bebidas","Suco de Melancia",10.0, R.drawable.melancia),
                new MenuItemModel("Bebidas","Suco de Abacaxi", 10.0, R.drawable.abacaxi),
                new MenuItemModel("Bebidas","Coca-Cola",        8.0, R.drawable.coca),
                new MenuItemModel("Bebidas","Coca-Cola Zero",   8.0, R.drawable.cocazero),
                new MenuItemModel("Bebidas","Sprite",           8.0, R.drawable.sprite),
                new MenuItemModel("Bebidas","Água",             5.0, R.drawable.agua)
        );

        // ALCOÓLICOS
        put("Alcoólicos",
                new MenuItemModel("Alcoólicos","Cerveja",     12.0, R.drawable.cerveja),
                new MenuItemModel("Alcoólicos","Chopp",       14.0, R.drawable.chop),
                new MenuItemModel("Alcoólicos","Vinho (taça)",25.0, R.drawable.vinho),
                new MenuItemModel("Alcoólicos","Caipirinha",  22.0, R.drawable.capirinha)
        );

        inited = true;
    }

    private static void put(String cat, MenuItemModel... items) {
        ArrayList<MenuItemModel> list = new ArrayList<>();
        for (MenuItemModel m : items) list.add(m);
        data.put(cat, list);
    }

    public static ArrayList<MenuItemModel> byCategory(String cat) {
        init();
        ArrayList<MenuItemModel> list = data.get(cat);
        return list == null ? new ArrayList<>() : new ArrayList<>(list);
    }

    public static List<String> categories() {
        init();
        return new ArrayList<>(data.keySet());
    }
}

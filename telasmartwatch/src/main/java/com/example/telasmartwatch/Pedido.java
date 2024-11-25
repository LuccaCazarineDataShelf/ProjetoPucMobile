package com.example.telasmartwatch;

public class Pedido {

    private String categoria;
    private String item;
    private int quantidade;

    public Pedido(String categoria, String item, int quantidade) {
        this.categoria = categoria;
        this.item = item;
        this.quantidade = quantidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getItem() {
        return item;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}

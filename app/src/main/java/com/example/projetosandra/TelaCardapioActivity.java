package com.example.projetosandra;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TelaCardapioActivity extends AppCompatActivity {
    private ListView listaCardapio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cardapio);

        listaCardapio = findViewById(R.id.listaCardapio);

        String[] itensCardapio = {

                "Pratos",
                "----------------------",
                "X-burguer - R$ 25,00",
                "Salada Caesar - R$ 30,00",
                "Frango à Parmegiana - R$ 35,00",
                "Lasanha - R$ 45,00",
                "Bife à Milanesa - R$ 55,00",
                "Espaguete Carbonara - R$ 65,00",
                "Risoto de Frutos do Mar - R$ 72,00",
                "----------------------",

                "Bebidas",
                "----------------------",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "----------------------",

                "Sobremesas",
                "----------------------",
                "",
                "",
                "",
                "----------------------",


        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, itensCardapio);
        listaCardapio.setAdapter(adapter);
    }
}

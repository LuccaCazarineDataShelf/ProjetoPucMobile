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
                "Prato do Dia - R$ 25,00",
                "Pizza Margherita - R$ 30,00",
                "Hambúrguer Gourmet - R$ 35,00",
                "Sushi Variado - R$ 45,00",
                "Cerveja Artesanal - R$ 15,00",
                "Refrigerante - R$ 5,00",
                "Sobremesa de Chocolate - R$ 12,00"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, itensCardapio);
        listaCardapio.setAdapter(adapter);
    }
}

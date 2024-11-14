package com.example.telasmartwatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class GarcomActivity extends Activity {

    private Button botaoPratos, botaoBebidas, botaoSobremesas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garcom);

        botaoPratos = findViewById(R.id.botaoPratos);
        botaoBebidas = findViewById(R.id.botaoBebidas);
        botaoSobremesas = findViewById(R.id.botaoSobremesas);

        botaoPratos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GarcomActivity.this, "Pratos selecionados", Toast.LENGTH_SHORT).show();
            }
        });

        botaoBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GarcomActivity.this, "Bebidas selecionadas", Toast.LENGTH_SHORT).show();
            }
        });

        botaoSobremesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GarcomActivity.this, "Sobremesas selecionadas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

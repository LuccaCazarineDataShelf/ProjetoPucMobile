package com.example.telasmartwatch;

import android.app.Activity;
import android.content.Intent;
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
                acessarPratos();
            }
        });

        botaoBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acessarBebidas();
            }
        });

        botaoSobremesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acessarSobresas();
            }
        });
    }

    private void acessarPratos() {
        Intent intent = new Intent(GarcomActivity.this, PratosActivity.class);
        startActivity(intent);
        finish();
    }

    private void acessarBebidas() {
        Intent intent = new Intent(GarcomActivity.this, BebidasActivity.class);
        startActivity(intent);
        finish();
    }

    private void acessarSobresas() {
        Intent intent = new Intent(GarcomActivity.this, SobremesasActivity.class);
        startActivity(intent);
        finish();
    }
}


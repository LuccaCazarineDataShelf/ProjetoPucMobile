package com.example.telasmartwatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class GarcomActivity extends Activity {

    private Button botaoPratos, botaoBebidas, botaoSobremesas;
    private Spinner spinnerMesas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garcom);

        spinnerMesas = findViewById(R.id.spinnerMesas);
        botaoPratos = findViewById(R.id.botaoPratos);
        botaoBebidas = findViewById(R.id.botaoBebidas);
        botaoSobremesas = findViewById(R.id.botaoSobremesas);

        botaoPratos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acessarPratos("pratos");
            }
        });

        botaoBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acessarBebidas("bebidas");
            }
        });

        botaoSobremesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acessarSobresas("sobremesas");
            }
        });
    }

    private void acessarPratos(String categoria) {
        String mesaSelecionada = spinnerMesas.getSelectedItem().toString();

        if (mesaSelecionada.equals("Selecione uma mesa")) {
            Toast.makeText(this, "Por favor, selecione uma mesa antes de continuar", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(GarcomActivity.this, PratosActivity.class);
        intent.putExtra("mesa", mesaSelecionada);
        intent.putExtra("categoria", categoria);
        startActivity(intent);
        finish();
    }

    private void acessarBebidas(String categoria) {
        String mesaSelecionada = spinnerMesas.getSelectedItem().toString();

        if (mesaSelecionada.equals("Selecione uma mesa")) {
            Toast.makeText(this, "Por favor, selecione uma mesa antes de continuar", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(GarcomActivity.this, BebidasActivity.class);
        intent.putExtra("mesa", mesaSelecionada);
        intent.putExtra("categoria", categoria);
        startActivity(intent);
        finish();
    }

    private void acessarSobresas(String categoria) {
        String mesaSelecionada = spinnerMesas.getSelectedItem().toString();

        if (mesaSelecionada.equals("Selecione uma mesa")) {
            Toast.makeText(this, "Por favor, selecione uma mesa antes de continuar", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(GarcomActivity.this, SobremesasActivity.class);
        intent.putExtra("mesa", mesaSelecionada);
        intent.putExtra("categoria", categoria);
        startActivity(intent);
        finish();
    }
}


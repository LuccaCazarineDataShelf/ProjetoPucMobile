package com.example.telasmartwatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PratosActivity extends Activity {

    private Spinner spinnerPratos;
    private EditText campoQuantidade;
    private Button botaoAdicionar;
    private  String mesaSelecionada;
    private List<Pedido> pedidos;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pratos);

        mesaSelecionada = getIntent().getStringExtra("mesa");

        spinnerPratos = findViewById(R.id.spinnerPratos);
        campoQuantidade = findViewById(R.id.campoQuantidade);
        botaoAdicionar = findViewById(R.id.botaoAdicionar);

        dbHelper = new DatabaseHelper(this);
        pedidos = new ArrayList<>();

        botaoAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarPedido();
            }
        });
    }
    private void adicionarPedido() {
        String prato = spinnerPratos.getSelectedItem().toString();
        String quantidadeTexto = campoQuantidade.getText().toString();

        if (quantidadeTexto.isEmpty()) {
            Toast.makeText(this, "Informe a quantidade", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mesaSelecionada == null || mesaSelecionada.isEmpty()) {
            Toast.makeText(this, "Mesa não selecionada. Volte e escolha uma mesa.", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantidade = Integer.parseInt(quantidadeTexto);

        long resultado = dbHelper.inserirPedido(mesaSelecionada, prato, quantidade);

        if (resultado != -1) {
            Toast.makeText(this, "Pedido salvo: Mesa " + mesaSelecionada + ", Item: " + prato + " (x" + quantidade + ")", Toast.LENGTH_SHORT).show();
            campoQuantidade.setText("");
        } else {
            Toast.makeText(this, "Erro ao salvar pedido", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}

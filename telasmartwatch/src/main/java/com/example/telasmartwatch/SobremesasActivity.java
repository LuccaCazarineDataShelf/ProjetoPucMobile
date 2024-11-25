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

public class SobremesasActivity extends Activity {

    private Spinner spinnerSobremesas;
    private EditText campoQuantidade;
    private Button botaoAdicionar;
    private List<Pedido> pedidos;
    private  String mesaSelecionada;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobremesas);

        mesaSelecionada = getIntent().getStringExtra("mesa");

        spinnerSobremesas = findViewById(R.id.spinnerSobremesas);
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
        String sobremesas = spinnerSobremesas.getSelectedItem().toString();
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

        long resultado = dbHelper.inserirPedido(mesaSelecionada, sobremesas, quantidade);

        if (resultado != -1) {
            Toast.makeText(this, "Pedido salvo: Mesa " + mesaSelecionada + ", Item: " + sobremesas + " (x" + quantidade + ")", Toast.LENGTH_SHORT).show();
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

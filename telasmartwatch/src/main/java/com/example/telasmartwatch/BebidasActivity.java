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

public class BebidasActivity extends Activity {

    private Spinner spinnerBebidas;
    private EditText campoQuantidade;
    private Button botaoAdicionar;
    private List<Pedido> pedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebidas);

        spinnerBebidas = findViewById(R.id.spinnerBebidas);
        campoQuantidade = findViewById(R.id.campoQuantidade);
        botaoAdicionar = findViewById(R.id.botaoAdicionar);

        pedidos = new ArrayList<>();

        botaoAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarPedido();
            }
        });
    }

    private void adicionarPedido() {
        String bebida = spinnerBebidas.getSelectedItem().toString();
        String quantidadeTexto = campoQuantidade.getText().toString();

        if (quantidadeTexto.isEmpty()) {
            Toast.makeText(this, "Informe a quantidade", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantidade = Integer.parseInt(quantidadeTexto);
        Pedido pedido = new Pedido("Bebida", bebida, quantidade);

        pedidos.add(pedido);

        Toast.makeText(this, "Adicionado: " + bebida + " (x" + quantidade + ")", Toast.LENGTH_SHORT).show();
        campoQuantidade.setText("");
    }
}

package com.example.projetosandra;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuInicialActivity extends AppCompatActivity {
    private Button botaoCardapio, botaoReserva, botaoMinhasReservas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);

        botaoCardapio = findViewById(R.id.botaoCardapio);
        botaoReserva = findViewById(R.id.botaoReserva);
        botaoMinhasReservas = findViewById(R.id.botaoMinhasReservas);

        botaoCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acessarTelaCardapio();
            }
        });

        botaoReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acessarTelaReserva();
            }
        });

        botaoMinhasReservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acessarTelaMinhasReservas();
            }
        });
    }

    private void acessarTelaCardapio() {
        Intent intent = new Intent(MenuInicialActivity.this, TelaCardapioActivity.class);
        startActivity(intent);
    }

    private void acessarTelaReserva() {
        Intent intent = new Intent(MenuInicialActivity.this, TelaReservaActivity.class);
        startActivity(intent);
    }

    private void acessarTelaMinhasReservas(){
        Intent intent = new Intent(MenuInicialActivity.this, TelaMinhasReservasActivity.class);
        startActivity(intent);
    }
}

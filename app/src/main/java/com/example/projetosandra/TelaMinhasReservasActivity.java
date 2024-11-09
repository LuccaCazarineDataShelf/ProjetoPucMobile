package com.example.projetosandra;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class TelaMinhasReservasActivity extends AppCompatActivity {

    private EditText campoQuantidadePessoasEdit, campoHorarioReservaEdit;
    private Button botaoEditarReserva, botaoCancelarReserva, botaoSalvarAlteracoes;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_reservas);

        campoQuantidadePessoasEdit = findViewById(R.id.campoQuantidadePessoasEdit);
        campoHorarioReservaEdit = findViewById(R.id.campoHorarioReservaEdit);
        botaoEditarReserva = findViewById(R.id.botaoEditarReserva);
        botaoCancelarReserva = findViewById(R.id.botaoCancelarReserva);
        botaoSalvarAlteracoes = findViewById(R.id.botaoSalvarAlteracoes);

        dbHelper = new DatabaseHelper(this);

        buscarReserva();

        botaoEditarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campoQuantidadePessoasEdit.setEnabled(true);
                campoHorarioReservaEdit.setEnabled(true);
                botaoSalvarAlteracoes.setVisibility(View.VISIBLE);
            }
        });

        botaoCancelarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarReserva();
            }
        });

        botaoSalvarAlteracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarReserva();
            }
        });
    }

    private void buscarReserva() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.TABELA_RESERVAS, null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String quantidadePessoas = cursor.getString(cursor.getColumnIndex("quantidade_pessoas"));
                @SuppressLint("Range") String horarioReserva = cursor.getString(cursor.getColumnIndex("horario_reserva"));

                campoQuantidadePessoasEdit.setText(quantidadePessoas);
                campoHorarioReservaEdit.setText(horarioReserva);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    private void cancelarReserva() {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            int rowsDeleted = db.delete(DatabaseHelper.TABELA_RESERVAS, null, null);

            if (rowsDeleted > 0) {
                Toast.makeText(this, "Reserva cancelada com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao cancelar reserva", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    private void editarReserva() {
        String novaQuantidade = campoQuantidadePessoasEdit.getText().toString();
        String novoHorario = campoHorarioReservaEdit.getText().toString();

        if (novaQuantidade.isEmpty() || novoHorario.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put("quantidade_pessoas", novaQuantidade);
            valores.put("horario_reserva", novoHorario);

            int rowsUpdated = db.update(DatabaseHelper.TABELA_RESERVAS, valores, null, null);

            if (rowsUpdated > 0) {
                Toast.makeText(this, "Reserva atualizada com sucesso", Toast.LENGTH_SHORT).show();
                campoQuantidadePessoasEdit.setEnabled(false);
                campoHorarioReservaEdit.setEnabled(false);
                botaoSalvarAlteracoes.setVisibility(View.GONE); // Esconder botão de salvar após a atualização
            } else {
                Toast.makeText(this, "Erro ao atualizar reserva", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}





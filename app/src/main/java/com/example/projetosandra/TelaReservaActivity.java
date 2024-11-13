package com.example.projetosandra;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class TelaReservaActivity extends AppCompatActivity {

    private EditText campoQuantidadePessoas, campoHorario, campoDia;
    private Button botaoReservar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_reserva);

        campoQuantidadePessoas = findViewById(R.id.campoQuantidadePessoas);
        campoDia = findViewById(R.id.campoDia);
        campoHorario = findViewById(R.id.campoHorario);
        botaoReservar = findViewById(R.id.botaoReservar);

        dbHelper = new DatabaseHelper(this);

        botaoReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantidadePessoas = campoQuantidadePessoas.getText().toString().trim();
                String diaReserva = campoDia.getText().toString().trim();
                String horarioReserva = campoHorario.getText().toString().trim();
                if (!quantidadePessoas.isEmpty() && !horarioReserva.isEmpty()) {
                    salvarReserva(quantidadePessoas, diaReserva, horarioReserva);
                } else {
                    Toast.makeText(TelaReservaActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        campoDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        TelaReservaActivity.this,
                        (view, year1, month1, dayOfMonth) -> {
                            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                            campoDia.setText(selectedDate);
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void salvarReserva(String quantidadePessoas, String horarioReserva, String diaReserva) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUNA_QUANTIDADE_PESSOAS, quantidadePessoas);
            values.put(DatabaseHelper.COLUNA_DIA_RESERVA, diaReserva);
            values.put(DatabaseHelper.COLUNA_HORARIO_RESERVA, horarioReserva);

            long result = db.insert(DatabaseHelper.TABELA_RESERVAS, null, values);

            if (result != -1) {
                Toast.makeText(this, "Reserva feita com sucesso", Toast.LENGTH_SHORT).show();
                campoQuantidadePessoas.setText("");
                campoDia.setText("");
                campoHorario.setText("");
            } else {
                Toast.makeText(this, "Erro ao fazer reserva", Toast.LENGTH_SHORT).show();
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

    private int obterIdUsuarioLogado() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getInt(DatabaseHelper.COLUNA_ID_USUARIO_RESERVA, -1);
    }

}

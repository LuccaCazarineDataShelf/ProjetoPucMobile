package com.example.projetosandra;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class TelaLoginActivity extends AppCompatActivity {
    private EditText campoEmail, campoSenha;
    private Button botaoLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        campoEmail = findViewById(R.id.campoEmail);
        campoSenha = findViewById(R.id.campoSenha);
        botaoLogin = findViewById(R.id.botaoLogin);

        dbHelper = new DatabaseHelper(this);

        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();
                realizarLogin(email, senha);
            }
        });
    }

    public void realizarLogin(String email, String senha){
        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = dbHelper.getReadableDatabase();

            String[] projection = { DatabaseHelper.COLUNA_EMAIL, DatabaseHelper.COLUNA_SENHA };
            String selection = DatabaseHelper.COLUNA_EMAIL + " = ? AND " + DatabaseHelper.COLUNA_SENHA + " = ?";
            String[] selectionArgs = { email, senha };

            cursor = db.query(
                    DatabaseHelper.TABELA_USUARIOS,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {


                Toast.makeText(this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                campoEmail.setText("");
                campoSenha.setText("");
                acessarMenuInicial();
            } else {
                Toast.makeText(this, "E-mail ou senha incorretos", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
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

    private void acessarMenuInicial() {
        Intent intent = new Intent(TelaLoginActivity.this, MenuInicialActivity.class);
        startActivity(intent);
        finish();
    }
}

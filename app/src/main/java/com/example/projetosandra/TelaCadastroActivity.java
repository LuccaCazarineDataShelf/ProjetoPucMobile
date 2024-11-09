package com.example.projetosandra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TelaCadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar, botaoIrParaLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        campoNome = findViewById(R.id.campoNome);
        campoEmail = findViewById(R.id.campoEmail);
        campoSenha = findViewById(R.id.campoSenha);
        botaoCadastrar = findViewById(R.id.botaoCadastrar);
        botaoIrParaLogin = findViewById(R.id.botao_ir_para_login);

        dbHelper = new DatabaseHelper(this);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = campoNome.getText().toString().trim();
                String email = campoEmail.getText().toString().trim();
                String senha = campoSenha.getText().toString().trim();
                cadastrarUsuario(nome, email, senha);
            }
        });

        botaoIrParaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaCadastroActivity.this, TelaLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void cadastrarUsuario(String nome, String email, String senha){

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "E-mail inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = dbHelper.getReadableDatabase();

        String[] projection = { DatabaseHelper.COLUNA_EMAIL };
        String selection = DatabaseHelper.COLUNA_EMAIL + " = ?";
        String[] selectionArgs = { email };

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
            Toast.makeText(this, "E-mail já cadastrado", Toast.LENGTH_SHORT).show();
            return;
        }

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUNA_NOME, nome);
            values.put(DatabaseHelper.COLUNA_EMAIL, email);
            values.put(DatabaseHelper.COLUNA_SENHA, senha);


            long result = db.insert(DatabaseHelper.TABELA_USUARIOS, null, values);

            if (result != -1) {
                Toast.makeText(this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                campoNome.setText("");
                campoEmail.setText("");
                campoSenha.setText("");
                acessarTelaLogin();

            } else {
                Toast.makeText(this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
           if(db!=null) {
                db.close();
            }
        }

    }

    private void acessarTelaLogin() {
        Intent intent = new Intent(TelaCadastroActivity.this, TelaLoginActivity.class);
        startActivity(intent);
        finish();
    }
}

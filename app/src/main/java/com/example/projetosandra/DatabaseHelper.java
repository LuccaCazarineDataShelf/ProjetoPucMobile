package com.example.projetosandra;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "restaurante.db";
    private static final int VERSAO_BANCO = 1;

    public static final String TABELA_USUARIOS = "usuarios";
    public static final String COLUNA_ID2 = "id";
    public static final String COLUNA_EMAIL = "email";
    public static final String COLUNA_SENHA = "senha";
    public static final String COLUNA_NOME = "nome";

    public static final String TABELA_RESERVAS = "reservas";
    public static final String COLUNA_ID_RESERVA = "id";
    public static final String COLUNA_ID_USUARIO_RESERVA = "id_usuario";
    public static final String COLUNA_QUANTIDADE_PESSOAS = "quantidade_pessoas";
    public static final String COLUNA_HORARIO_RESERVA = "horario_reserva";
    public static final String COLUNA_DIA_RESERVA = "dia_reserva";


    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABELA_USUARIOS + " (" +
                    COLUNA_ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUNA_NOME + " TEXT, " +
                    COLUNA_EMAIL + " TEXT UNIQUE, " +
                    COLUNA_QUANTIDADE_PESSOAS + " INTEGER ," +
                    COLUNA_HORARIO_RESERVA + " INTEGER ," +
                    COLUNA_SENHA + " TEXT);";

    public static final String TABLE_CREATE_RESERVAS =
            "CREATE TABLE " + TABELA_RESERVAS + " (" +
                    COLUNA_ID_RESERVA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUNA_ID_USUARIO_RESERVA + " INTEGER, " +
                    COLUNA_QUANTIDADE_PESSOAS + " INTEGER, " +
                    COLUNA_HORARIO_RESERVA + " TEXT, " +
                    COLUNA_DIA_RESERVA + " TEXT, " +
                    "FOREIGN KEY (" + COLUNA_ID_USUARIO_RESERVA + ") REFERENCES " + TABELA_USUARIOS + "(" + COLUNA_ID2 + "));";

    public DatabaseHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_CREATE_RESERVAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_RESERVAS);
        onCreate(db);
    }
}

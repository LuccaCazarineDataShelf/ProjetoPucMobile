package com.example.telasmartwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "restaurante_smartwatch.db";
    private static final int VERSAO_BANCO = 1;

    public static final String TABELA_PEDIDOS = "pedidos";
    public static final String COLUNA_ID_PEDIDO = "id_pedido";
    public static final String COLUNA_MESA_PEDIDO = "mesa";
    public static final String COLUNA_ITEM_PEDIDO = "item";
    public static final String COLUNA_QUANTIDADE_PEDIDO = "quantidade";
    public static final String COLUNA_STATUS_PEDIDO = "status";

    private static final String CRIAR_TABELA_PEDIDOS = "CREATE TABLE " + TABELA_PEDIDOS + " (" +
            COLUNA_ID_PEDIDO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUNA_MESA_PEDIDO + " TEXT, " +
            COLUNA_ITEM_PEDIDO + " TEXT, " +
            COLUNA_QUANTIDADE_PEDIDO + " INTEGER, " +
            COLUNA_STATUS_PEDIDO + " TEXT DEFAULT 'Pendente');";

    public DatabaseHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CRIAR_TABELA_PEDIDOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_PEDIDOS);
        onCreate(db);
    }

    public long inserirPedido(String mesa, String item, int quantidade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUNA_MESA_PEDIDO, mesa);
        values.put(COLUNA_ITEM_PEDIDO, item);
        values.put(COLUNA_QUANTIDADE_PEDIDO, quantidade);

        long resultado = db.insert(TABELA_PEDIDOS, null, values);
        db.close();
        return resultado;
    }

    public Cursor listarPedidos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABELA_PEDIDOS, null, null, null, null, null, null);
    }

}

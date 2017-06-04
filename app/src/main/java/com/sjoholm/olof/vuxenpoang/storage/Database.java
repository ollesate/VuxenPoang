package com.sjoholm.olof.vuxenpoang.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

class Database extends SQLiteOpenHelper {
    private static final String DATABASE = "database.db";
    private static final int VERSION = 9;

    private final SalesTable salesTable;

    private final KeyValueTable keyValueTable;
    private final PurchaseTable purchaseTable;

    Database(Context context) {
        super(context, DATABASE, null, VERSION);
        salesTable = new SalesTable(this);
        purchaseTable = new PurchaseTable(this);
        keyValueTable = new KeyValueTable(this);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        salesTable.recreate(db);
        purchaseTable.recreate(db);
        keyValueTable.recreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        salesTable.recreate(db);
        purchaseTable.recreate(db);
        keyValueTable.recreate(db);
    }

    @NonNull
    SalesTable getSalesTable() {
        return salesTable;
    }

    @NonNull
    public PurchaseTable getPurchaseTable() {
        return purchaseTable;
    }

    @NonNull
    public KeyValueTable getKeyValueTable() {
        return keyValueTable;
    }
}

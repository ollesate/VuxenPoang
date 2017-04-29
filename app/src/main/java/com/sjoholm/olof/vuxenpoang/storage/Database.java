package com.sjoholm.olof.vuxenpoang.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

class Database extends SQLiteOpenHelper {
    private static final String DATABASE = "database.db";
    private static final int VERSION = 2;

    private final ExpensesTable itemTable;

    Database(Context context) {
        super(context, DATABASE, null, VERSION);
        itemTable = new ExpensesTable(this);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        itemTable.recreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        itemTable.recreate(db);
    }

    @NonNull
    ExpensesTable getItemTable() {
        return itemTable;
    }
}

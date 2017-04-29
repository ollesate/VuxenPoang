package com.sjoholm.olof.vuxenpoang.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.sjoholm.olof.vuxenpoang.model.Expense;

import java.util.ArrayList;
import java.util.List;

class ExpensesTable {
    private static final String TABLE = "item";
    private static final String COL_NAME = "name";
    private static final String COL_COST = "cost";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_TIMESTAMP = "timestamp";

    private final SQLiteOpenHelper sqlHelper;

    ExpensesTable(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqlHelper = sqLiteOpenHelper;
    }

    void update(List<Expense> items) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        db.delete(TABLE, null, null);
        bulkInsert(db, items);
    }

    private void bulkInsert(SQLiteDatabase db, List<Expense> items) {
        for (Expense item : items) {
            insert(db, item);
        }
    }

    private void insert(SQLiteDatabase db, Expense item) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, item.name);
        cv.put(COL_COST, item.cost);
        cv.put(COL_TIMESTAMP, System.currentTimeMillis());
        db.insertWithOnConflict(TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @NonNull
    List<Expense> getItems() {
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        String[] projection = {COL_NAME, COL_COST};
        List<Expense> items = new ArrayList<>();
        try (Cursor cursor = db.query(TABLE, projection, null, null, null, null, null)) {
            int colName = cursor.getColumnIndexOrThrow(COL_NAME);
            int colCost = cursor.getColumnIndexOrThrow(COL_COST);
            while (cursor.moveToNext()) {
                String name = cursor.getString(colName);
                int cost = cursor.getInt(colCost);
                items.add(new Expense(name, cost));
            }
        }
        return items;
    }

    void recreate(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE);
        db.execSQL("create table " + TABLE + " ("
                + COL_NAME + " TEXT PRIMARY KEY,"
                + COL_COST + " INTEGER NOT NULL,"
                + COL_DESCRIPTION + " TEXT,"
                + COL_TIMESTAMP + " INTEGER NOT NULL"
                + ")"
        );
    }
}

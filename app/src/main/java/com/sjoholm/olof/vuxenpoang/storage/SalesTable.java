package com.sjoholm.olof.vuxenpoang.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.sjoholm.olof.vuxenpoang.model.Sale;
import com.sjoholm.olof.vuxenpoang.model.Sale;

import java.util.ArrayList;
import java.util.List;

class SalesTable {
    private static final String TABLE = "sale";
    private static final String COL_NAME = "name";
    private static final String COL_COST = "cost";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_TIMESTAMP = "timestamp";

    private final SQLiteOpenHelper sqlHelper;

    SalesTable(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqlHelper = sqLiteOpenHelper;
    }

    void update(List<Sale> items) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        db.delete(TABLE, null, null);
        bulkInsert(db, items);
    }

    private void bulkInsert(SQLiteDatabase db, List<Sale> items) {
        for (Sale item : items) {
            insert(db, item);
        }
    }

    private void insert(SQLiteDatabase db, Sale item) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, item.name);
        cv.put(COL_COST, item.cost);
        cv.put(COL_TIMESTAMP, System.currentTimeMillis());
        db.insertWithOnConflict(TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @NonNull
    List<Sale> getItems() {
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        String[] projection = {COL_NAME, COL_COST};
        List<Sale> items = new ArrayList<>();
        try (Cursor cursor = db.query(TABLE, projection, null, null, null, null, null)) {
            int colName = cursor.getColumnIndexOrThrow(COL_NAME);
            int colCost = cursor.getColumnIndexOrThrow(COL_COST);
            while (cursor.moveToNext()) {
                String name = cursor.getString(colName);
                int cost = cursor.getInt(colCost);
                items.add(new Sale(name, cost));
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

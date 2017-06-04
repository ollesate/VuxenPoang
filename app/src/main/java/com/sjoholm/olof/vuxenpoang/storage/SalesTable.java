package com.sjoholm.olof.vuxenpoang.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sjoholm.olof.vuxenpoang.model.Sale;

import java.util.ArrayList;
import java.util.List;

import static com.sjoholm.olof.vuxenpoang.R.color.item;

class SalesTable {
    private static final String TABLE = "sale";
    private static final String COL_NAME = "name";
    private static final String COL_COST = "cost";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_TIMESTAMP = "timestamp";
    private static final String COL_ID = "id";

    private final SQLiteOpenHelper sqlHelper;

    SalesTable(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqlHelper = sqLiteOpenHelper;
    }

    public Sale createSale(String name, int cost) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        long id = insert(db, name, cost);
        return new Sale(name, cost, id);
    }

    void update(List<Sale> items) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        bulkUpdate(db, items);
    }

    private void bulkUpdate(SQLiteDatabase db, List<Sale> items) {
        for (Sale item : items) {
            update(db, item);
        }
    }

    private long insert(SQLiteDatabase db, String name, int cost) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_COST, cost);
        cv.put(COL_TIMESTAMP, System.currentTimeMillis());
        return db.insertOrThrow(TABLE, null, cv);
    }

    private void update(SQLiteDatabase db, Sale item) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, item.name);
        cv.put(COL_COST, item.cost);
        db.update(TABLE, cv, COL_ID + "=?", new String[]{String.valueOf(item.id)});
    }

    @NonNull
    List<Sale> getItems() {
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        String[] projection = {COL_ID, COL_NAME, COL_COST};
        List<Sale> items = new ArrayList<>();
        try (Cursor cursor = db.query(TABLE, projection, null, null, null, null, null)) {
            int colName = cursor.getColumnIndexOrThrow(COL_NAME);
            int colCost = cursor.getColumnIndexOrThrow(COL_COST);
            int colId = cursor.getColumnIndexOrThrow(COL_ID);
            while (cursor.moveToNext()) {
                String name = cursor.getString(colName);
                int cost = cursor.getInt(colCost);
                long id = cursor.getInt(colId);
                items.add(new Sale(name, cost, id));
            }
        }
        return items;
    }

    void recreate(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE);
        db.execSQL("create table " + TABLE + " ("
                + COL_ID + " INTEGER PRIMARY KEY,"
                + COL_NAME + " TEXT KEY,"
                + COL_COST + " INTEGER NOT NULL,"
                + COL_DESCRIPTION + " TEXT,"
                + COL_TIMESTAMP + " INTEGER NOT NULL"
                + ")"
        );
    }
}

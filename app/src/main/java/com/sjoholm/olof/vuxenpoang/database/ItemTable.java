package com.sjoholm.olof.vuxenpoang.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.sjoholm.olof.vuxenpoang.model.Expense;

import java.util.ArrayList;
import java.util.List;

public class ItemTable {
    private static final String TABLE = "item";
    private static final String COL_NAME = "name";
    private static final String COL_COST = "cost";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_TIMESTAMP = "timestamp";

    private SQLiteOpenHelper sqlHelper;

    public ItemTable(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqlHelper = sqLiteOpenHelper;
    }

    public void update(List<Expense> items) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        db.delete(TABLE, null, null);
        bulkInsert(db, items);
    }

    private void bulkInsert(SQLiteDatabase db, List<Expense> items) {
        for (Expense item : items) {
            insert(db, item);
        }
    }

    // TODO: Remove. Ineffective?
    public void add(Expense item) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        insert(db, item);
    }

    private void insert(SQLiteDatabase db, Expense item) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, item.name);
        cv.put(COL_COST, item.cost);
        cv.put(COL_TIMESTAMP, System.currentTimeMillis());
        db.insertWithOnConflict(TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @NonNull
    public List<Expense> getItems() {
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

    public void recreate(SQLiteDatabase db) {
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

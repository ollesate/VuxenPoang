package com.sjoholm.olof.vuxenpoang.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KeyValueTable {
    private static final String TABLE = "key_value";
    private static final String COL_KEY = "key";
    private static final String COL_VALUE = "value";
    private SQLiteOpenHelper openHelper;

    public KeyValueTable(SQLiteOpenHelper openHelper) {
        this.openHelper = openHelper;
    }

    public void putInt(String key, int value) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.insertWithOnConflict(TABLE, null, getContentValues(key, String.valueOf(value)),
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public int getInt(String key, int defaultValue) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        try (Cursor cursor = db.query(TABLE, new String[]{COL_VALUE}, "key = ?",
                new String[]{key}, null, null, null)) {
            int col = cursor.getColumnIndexOrThrow(COL_VALUE);
            if (cursor.getCount() == 0) {
                return defaultValue;
            }
            cursor.moveToFirst();
            return cursor.getInt(col);
        }
    }

    private ContentValues getContentValues(String key, String value) {
        ContentValues cv = new ContentValues();
        cv.put(COL_KEY, key);
        cv.put(COL_VALUE, value);
        return cv;
    }

    void recreate(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE);
        db.execSQL("create table " + TABLE + " (" +
                COL_KEY + " TEXT PRIMARY KEY," +
                COL_VALUE + " TEXT NOT NULL" +
                ")");
    }
}

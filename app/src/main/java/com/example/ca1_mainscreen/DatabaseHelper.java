package com.example.ca1_mainscreen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "journal.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_JOURNAL = "journal";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_JOURNAL + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_CONTENT + " TEXT, " +
                    COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNAL);
        onCreate(db);
    }

    // Method to add a journal entry
    public void addJournalEntry(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);

        db.insert(TABLE_JOURNAL, null, values);
        db.close();
    }

    // Method to fetch all journal entries
    public List<JournalEntry> getAllJournalEntries() {
        List<JournalEntry> entryList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_JOURNAL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                JournalEntry entry = new JournalEntry(
                        cursor.getLong(0), // ID
                        cursor.getString(1), // Title
                        cursor.getString(2), // Content
                        cursor.getString(3)  // Timestamp
                );
                entryList.add(entry);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return entryList;
    }

    // Method to update a journal entry
    public int updateJournalEntry(JournalEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, entry.getTitle());
        values.put(COLUMN_CONTENT, entry.getContent());

        // Updating row
        return db.update(TABLE_JOURNAL, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(entry.getId()) });
    }

    // Method to delete a journal entry
    public void deleteJournalEntry(JournalEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_JOURNAL, COLUMN_ID + " = ?",
                new String[] { String.valueOf(entry.getId()) });
        db.close();
    }

    // ...
}

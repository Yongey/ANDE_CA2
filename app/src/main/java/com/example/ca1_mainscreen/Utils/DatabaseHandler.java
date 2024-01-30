package com.example.ca1_mainscreen.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ca1_mainscreen.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String NAME = "todoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String USER_ID = "user_id";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                + USER_ID + " TEXT, " + TASK + " TEXT, " + STATUS + " INTEGER)";
    private SQLiteDatabase db;
    private static String USER_ID_VALUE;

    public DatabaseHandler(Context context) {

        super(context, NAME, null, VERSION);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            USER_ID_VALUE = currentUser.getUid();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);

        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task) {
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, USER_ID_VALUE);
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv);
    }

    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, USER_ID + "=?", new String[]{USER_ID_VALUE}, null, null, null, null);
            if (cur != null && cur.moveToFirst()) {
                do {
                    ToDoModel task = new ToDoModel();
                    task.setId(Integer.parseInt(cur.getString(0)));
                    task.setTask(cur.getString(2));
                    task.setStatus(cur.getInt(3));
                    taskList.add(task);
                } while (cur.moveToNext());
            }
        }
        finally {
            db.endTransaction();
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }


}
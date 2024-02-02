package com.example.ca1_mainscreen.Utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ca1_mainscreen.Model.HabitModel;
import com.example.ca1_mainscreen.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String NAME = "todoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String HABIT_TABLE = "habit";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String HABIT = "habit";
    private static final String STATUS = "status";
    private static final String USER_ID = "user_id";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                + USER_ID + " TEXT, " + TASK + " TEXT, " + STATUS + " INTEGER)";

    private static final String CREATE_HABIT_TABLE = "CREATE TABLE " + HABIT_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                + USER_ID + " TEXT, " + HABIT + " TEXT, " + STATUS + " INTEGER)";

    private SQLiteDatabase db;
    private static String USER_ID_VALUE;
    private static final String MONDAY = "monday";
    private static final String TUESDAY = "tuesday";
    private static final String WEDNESDAY = "wednesday";
    private static final String THURSDAY = "thursday";
    private static final String FRIDAY = "friday";
    private static final String SATURDAY = "saturday";
    private static final String SUNDAY = "sunday";

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
        db.execSQL(CREATE_HABIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + HABIT_TABLE);

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


    public void insertHabit(HabitModel habit) {
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, USER_ID_VALUE);
        cv.put(HABIT, habit.getName());
        cv.put(STATUS, 0);
        db.insert(HABIT_TABLE, null, cv);
    }

    public List<HabitModel> getAllHabits() {
        List<HabitModel> habitList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(HABIT_TABLE, null, USER_ID + "=?", new String[]{USER_ID_VALUE}, null, null, null, null);
            if (cur != null && cur.moveToFirst()) {
                do {
                    HabitModel habit = new HabitModel();
                    habit.setId(Integer.parseInt(cur.getString(0)));
                    habit.setName(cur.getString(2));
                    habit.setStatus(cur.getInt(3));
                    habitList.add(habit);
                } while (cur.moveToNext());
            }
        } finally {
            db.endTransaction();
            cur.close();
        }
        return habitList;
    }

    public void updateHabitStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(HABIT_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateHabit(int id, HabitModel habit) {
        ContentValues values = new ContentValues();
        values.put(HABIT, habit.getName());
        values.put(MONDAY, habit.isMondaySelected());
        values.put(TUESDAY, habit.isTuesdaySelected());
        values.put(WEDNESDAY, habit.isWednesdaySelected());
        values.put(THURSDAY, habit.isThursdaySelected());
        values.put(FRIDAY, habit.isFridaySelected());
        values.put(SATURDAY, habit.isSaturdaySelected());
        values.put(SUNDAY, habit.isSundaySelected());

        db.update(HABIT_TABLE, values, ID + " = ?", new String[]{String.valueOf(habit.getId())});
    }

    public void deleteHabit(int id) {
        db.delete(HABIT_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }
}
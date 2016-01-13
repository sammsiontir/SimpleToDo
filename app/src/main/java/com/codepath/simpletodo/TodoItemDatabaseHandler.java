package com.codepath.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengfu_lin on 1/12/16.
 */
public class TodoItemDatabaseHandler extends SQLiteOpenHelper {
    private static TodoItemDatabaseHandler sInstance;

    // Database Info
    private static final String DATABASE_NAME = "task.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TASKS = "_tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "_title";
    public static final String COLUMN_DUE_DATE = "_dueDate";
    public static final String COLUMN_PRIORITY = "_priority";

    private String[] allColumns = { COLUMN_ID, COLUMN_TITLE, COLUMN_DUE_DATE, COLUMN_PRIORITY };

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "create table " + TABLE_TASKS
                    + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_TITLE + " TEXT NOT NULL, "
                    + COLUMN_DUE_DATE + " INTEGER NOT NULL "
                    + COLUMN_PRIORITY + " TEXT NOT NULL "
                    + ");";


    public static synchronized TodoItemDatabaseHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new TodoItemDatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private TodoItemDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // These is where we need to write create table statements.
    // This is called when database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL for creating the tables
        db.execSQL(DATABASE_CREATE);
    }

    // This method is called when database is upgraded like
    // modifying the table structure,
    // adding constraints to database, etc
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // SQL for upgrading the tables
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }
    }


    public void insertTask(Task newTask) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, newTask.getId());
        values.put(COLUMN_TITLE, newTask.getTitle());
        values.put(COLUMN_DUE_DATE, newTask.getDueDate().getTimeInMillis());
        values.put(COLUMN_PRIORITY, newTask.getPriority());

        database.insert(TABLE_TASKS, null, values);
    }

    public void updateTask(Task updateTask) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, updateTask.getId());
        values.put(COLUMN_TITLE, updateTask.getTitle());
        values.put(COLUMN_DUE_DATE, updateTask.getDueDate().getTimeInMillis());
        values.put(COLUMN_PRIORITY, updateTask.getPriority());

        database.update(TABLE_TASKS, values, COLUMN_ID + " = '" + updateTask.getId() + "'", null);
    }

    public List<Task> getALLTasks(){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor =  database.query(TABLE_TASKS,
                allColumns,
                null, null, null, null, null);

        List<Task> tasks = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task newTask = cursorToTask(cursor);
            tasks.add(newTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return tasks;
    }

    public Cursor getALLTasksCursor(){
        SQLiteDatabase database = getWritableDatabase();
        return database.query(TABLE_TASKS,
                allColumns,
                null, null, null, null, null);
    }


    public Task getTaskById(String taskId){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor =  database.query(TABLE_TASKS,
                allColumns,
                COLUMN_ID + " = '" + taskId + "'", null, null, null, null);
        return cursorToTask(cursor);
    }

    static public Task cursorToTask(Cursor cursor) {
        Task newTask = new Task();
        newTask.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        newTask.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        newTask.getDueDate().setTimeInMillis(cursor.getLong(cursor.getColumnIndex(COLUMN_DUE_DATE)));
        newTask.setPriority(cursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY)));
        return newTask;
    }

    public void deleteTask(Task task){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_TASKS, COLUMN_ID + " = '" + task.getId() + "'", null);
    }
}

package com.oa.task2do;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe on 12/11/13.
 */


public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "taskManager";

    // Task table name
    private static final String TABLE_TASKS = "tasks";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_DATE_YEAR = "year";
    private static final String KEY_DATE_MONTH = "month";
    private static final String KEY_DATE_DAY = "day";
    private static final String KEY_TIME_HOUR = "hour";
    private static final String KEY_TIME_MINUTES = "minutes";
    private static final String KEY_LOCATION_LONGITUDE = "longitude";
    private static final String KEY_LOCATION_LATITUDE = "latitude";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS
                + "("
                + KEY_ID                + " INTEGER PRIMARY KEY, "
                + KEY_MESSAGE           + " , "
                + KEY_DATE_YEAR         + " , "
                + KEY_DATE_MONTH        + " , "
                + KEY_DATE_DAY          + " , "
                + KEY_TIME_HOUR         + " , "
                + KEY_TIME_MINUTES      + " , "
                + KEY_LOCATION_LONGITUDE+ " , "
                + KEY_LOCATION_LATITUDE
                + ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new task
    void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        /*
         * save task Message, Date(year+month+day), Time(Hour+Time), Location(Longitude+Latitude)
         * */
        values.put(KEY_MESSAGE, task.getTaskMessage());
        values.put(KEY_DATE_YEAR, task.get_date().getYear());
        values.put(KEY_DATE_MONTH, task.get_date().getMonth());
        values.put(KEY_DATE_DAY, task.get_date().getDay());
        values.put(KEY_TIME_HOUR, task.get_date().getHours());
        values.put(KEY_TIME_MINUTES, task.get_date().getMinutes());
        values.put(KEY_LOCATION_LONGITUDE, task.get_location().getLongtitude());
        values.put(KEY_LOCATION_LATITUDE, task.get_location().getLatitude());

        // Inserting Row
        db.insert(TABLE_TASKS, null, values );
        db.close(); // Closing database connection
    }


    // Getting single task
    Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS, new String[] { KEY_ID,
                KEY_MESSAGE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Task task = new Task(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1) );
        // return contact
        return task;
    }


    // Getting All Tasks
    public List<Task> getAllTasks() {
        List<Task> contactList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setID(Integer.parseInt(cursor.getString(0)));
                task.setTaskMessage(cursor.getString(1));
                // Adding contact to list
                contactList.add(task);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    // Deleting single contact
    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getID()) });
        db.close();
    }


    // Getting tasks Count
    public int getTaskCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();

        // return count
        return cursor.getCount();
    }

}

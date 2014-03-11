package com.oa.task2do;

import android.content.Context;

import java.util.ArrayList;


public class Singleton {

    //-----Singleton------------------------------------
    private static Singleton instance = null;

    private Context context;

    private ArrayList<Task> results = new ArrayList<Task>();
    private DatabaseHandler db;

    private Singleton(Context context) {
        this.context = context;
        db = new DatabaseHandler(context);
    }

    public static synchronized Singleton getInstance(Context context) {
        if (instance == null) {
            instance = new Singleton(context);
        }
        return instance;
    }

    //--------------------------------------------


    public void setArrayList(ArrayList<Task> results) {
        this.results = results;
    }

    public ArrayList<Task> getArrayList() {
        return results;
    }

    public DatabaseHandler getDb() {
        return db;
    }

    public void clearArrayList() {
        results.clear();
    }
}


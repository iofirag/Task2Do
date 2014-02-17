package com.oa.task2do;

import android.location.Location;

import java.util.Date;

/**
 * Created by joe on 25/10/13.
 */
public class Task {
    int _id;
    private String _taskMessage ;
    private Date _date;
    private Location _location;

    /*-----------Ctor---------------------------*/
    public Task(int id, String str, Date date) {
        this._id = id;
        this._taskMessage = str;
        this._date = date;
    }

    public Task(int id, String str, Date date, Location) {
        this._id = id;
        this._taskMessage = str;
        this._date = date;
    }

    public Task(int id, String str) {
        this._id = id;
        this._taskMessage = str;
        this._date= new Date();
    }

    public Task(String str, Date date) {
        this._taskMessage = str;
        this._date = date;
    }
    public Task(int _id) {
        this._id = _id;
        this._date= new Date();
    }

    public Task() {
    }
    /*-------------------------------------------*/

    // set/get taskMessage
    public String getTaskMessage() {
        return _taskMessage;
    }
    public void setTaskMessage(String name) {
        this._taskMessage = name;
    }

    // set/get ID
    public int getID(){
        return this._id;
    }
    public void setID(int id){
        this._id = id;
    }

    // set/get Date
    public Date get_date() {
        return _date;
    }
    public void set_date(Date _date) {
        this._date = _date;
    }

    @Override
    public String toString (){
        return this._taskMessage;
    }

    public static int Y(int year)  {return year-1900;}
    public static int M(int month) {return month-1;}
}

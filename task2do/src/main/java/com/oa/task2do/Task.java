package com.oa.task2do;

import java.sql.Time;
import java.util.Date;

/**
 * Created by joe on 25/10/13.
 */
public class Task {
    int _id;
    private String _taskMessage ;
    private Time _time;
    private Date _date;
    private myLocation _location;



    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }

    public Time get_time() {
        return _time;
    }
    public void set_time(Time _time) {
        this._time = _time;
    }

    public String get_taskMessage() {
        return _taskMessage;
    }
    public void set_taskMessage(String _taskMessage) {
        this._taskMessage = _taskMessage;
    }

    public myLocation get_location() {

        return _location;
    }
    public void set_location(myLocation _location) {
        this._location = _location;
    }



    /*-----------Ctor---------------------------*/
    public Task(int id, String str, Time time, Date date, Double longtitude , Double latitude) {
        this._id = id;
        this._taskMessage = str;
        this._time = time;
        this._date = date;
        this._location.setLongitude(longtitude);
        this._location.setLatitude(latitude);
    }
    public Task(int id, String str, Date date) {
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

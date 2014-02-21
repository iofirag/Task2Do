package com.oa.task2do;

/**
 * Created by joe on 25/10/13.
 */
public class Task {
    int _id;
    public String _taskMessage ;
    public int _dateYear;
    public int _dateMonth;
    public int _dateDay;
    public int _timeHour;
    public int _timeMinute;
    public double _mapLongitude;
    public double _mapLatitude;


    public Task(int id, String taskMessage, int dateYear, int dateMonth, int dateDay, int timeHour, int timeMinute, double mapLongitude, double mapLatitude) {
        this._id=id;
        this._taskMessage=taskMessage;

        this._dateYear= dateYear;
        this._dateMonth=dateMonth;
        this._dateDay=dateDay;

        this._timeHour=timeHour;
        this._timeMinute=timeMinute;

        this._mapLongitude=mapLongitude;
        this._mapLatitude=mapLatitude;
    }


    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
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
//    public Date get_date() {
//        return _date;
//    }
//    public void set_date(Date _date) {
//        this._date = _date;
//    }

    @Override
    public String toString (){
        return this._taskMessage;
    }

    public static int Y(int year)  {return year-1900;}
    public static int M(int month) {return month-1;}
}

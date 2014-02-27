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

    public int _alarm = -1;
    public int _done = -1;


    public Task(int id, int alarm, /* boolean done,*/ String taskMessage, int dateYear, int dateMonth, int dateDay, int timeHour, int timeMinute, double mapLongitude, double mapLatitude) {
       /* alarm */
//        if ( (dateYear!=-1&&dateMonth!=-1&&dateDay!=-1) || (timeHour!=-1&&timeMinute!=-1) || (mapLongitude!=-1&&mapLatitude!=-1) )
//            _alarm=true;

        /* done */
        //do something with done

        this._id=id;
        this._taskMessage=taskMessage;

        this._dateYear= dateYear;
        this._dateMonth=dateMonth;
        this._dateDay=dateDay;

        this._timeHour=timeHour;
        this._timeMinute=timeMinute;

        this._mapLongitude=mapLongitude;
        this._mapLatitude=mapLatitude;

        this._alarm=alarm;
    }

    public Task() {
    }

    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
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

    @Override
    public String toString (){
        return this._taskMessage;
    }
}

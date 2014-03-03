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
    public int _alarm;
    public int _done;


    public Task(int id, String taskMessage, int dateYear, int dateMonth, int dateDay, int timeHour, int timeMinute, double mapLongitude, double mapLatitude ,int alarm , int done) {
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
        this._done=done;
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

    // set/get Alarm
    public int get_alarm(){
        return this._alarm;
    }
    public void set_alarm(int alarm){
        this._alarm = alarm;
    }

    // set/get Done
    public int get_done(){
        return this._done;
    }

    public void set_done(int done){
        this._done = done;
    }

    public int get_dateYear() {
        return _dateYear;
    }

    public void set_dateYear(int _dateYear) {
        this._dateYear = _dateYear;
    }

    public int get_dateDay() {
        return _dateDay;
    }

    public void set_dateDay(int _dateDay) {
        this._dateDay = _dateDay;
    }

    public int get_dateMonth() {
        return _dateMonth;
    }

    public void set_dateMonth(int _dateMonth) {
        this._dateMonth = _dateMonth;
    }

    public int get_timeHour() {
        return _timeHour;
    }

    public void set_timeHour(int _timeHour) {
        this._timeHour = _timeHour;
    }

    public int get_timeMinute() {
        return _timeMinute;
    }

    public void set_timeMinute(int _timeMinute) {
        this._timeMinute = _timeMinute;
    }

    public double get_mapLongitude() {
        return _mapLongitude;
    }

    public void set_mapLongitude(double _mapLongitude) {
        this._mapLongitude = _mapLongitude;
    }

    public double get_mapLatitude() {
        return _mapLatitude;
    }

    public void set_mapLatitude(double _mapLatitude) {
        this._mapLatitude = _mapLatitude;
    }

}



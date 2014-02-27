package com.oa.task2do;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends FragmentActivity implements DialogListener {

    private static final int EDIT_TASK = 1232;
    private static final int RESULT_MAP = 1233;
    private static final int RESULT_SPEECH = 1234;

    Singleton singleton=null;
    private TaskListBaseAdapter currentList;
    private TextWatcher tw;

    private int taskIdSelected= -1;

    private int editTask_hasAlarm=0;        //0 no , 1 yes
    private int editTask_hasBeenDone=0;     //0 no , 1 yes

    private double mapLongitude= -1;
    private double mapLatitude= -1;

    private int timeHour = -1;
    private int timeMinute= -1;

    private int dateYear= -1;
    private int dateMonth= -1;
    private int dateDay= -1;

    //back key press event
    private long lastPressedTime;
    private static final int PERIOD = 2000;

    private int editTaskBoolean = -1;   //id of task in edit

    //location alert
    private LocationManager lm=null;



    /**
     * google analytics
     */
    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* avoid keyboard show automatic */
        ListView listView = (ListView) findViewById(R.id.listView);
        //close keyboard and set focusable false to etNetTask
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(listView.getWindowToken(), 0);

        /* If there is Tasks in the DataBase restore them */
        if (singleton.getInstance(this).getArrayList().isEmpty())
            restoreFromDb();

        currentList = new TaskListBaseAdapter(this, singleton.getInstance(this).getArrayList());

        /* when etNewTask pressed then show the extra buttons */
        EditText newTask = (EditText) findViewById(R.id.etNewTask);
        newTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).isActive()) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.extraOptions);
                    if (linearLayout.getVisibility()== View.VISIBLE ){
                        linearLayout.setVisibility(LinearLayout.GONE);
                    }
                    else linearLayout.setVisibility(LinearLayout.VISIBLE);
                }
            }
        });


        /* inflate extra bar when text change */
        tw = new TextWatcher() {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.extraOptions);
            public void afterTextChanged(Editable s){
                if (s.length()>0)
                linearLayout.setVisibility(LinearLayout.VISIBLE);
                else linearLayout.setVisibility(LinearLayout.GONE);
            }
            public void  beforeTextChanged(CharSequence s, int start, int count, int after){
                if (count<0)
                linearLayout.setVisibility(LinearLayout.GONE);
            }
            public void  onTextChanged (CharSequence s, int start, int before,int count) {
                if (count<0)
                linearLayout.setVisibility(LinearLayout.GONE);
            }
        };
        EditText et = (EditText) findViewById(R.id.etNewTask);
        et.addTextChangedListener(tw);
    }


    @Override
    protected void onResume(){
        super.onResume();
        updateListView();
        currentList.notifyDataSetChanged();
    }

    /* handel with back key
     * when pressed twice the application closed
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        finish();
                    } else {
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.extraOptions);
                        linearLayout.setVisibility(LinearLayout.GONE);
                        Toast.makeText(getApplicationContext(), "Press again to exit.",
                                Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }

    /**
     * All types of Dialogs
     */
    /* Time-Picker Dialog */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();

        // add details if user want to edit exist task
        if (timeHour != -1 && timeMinute != -1){
            Bundle dataBundle = new Bundle();
            dataBundle.putInt("timeHour", timeHour);
            dataBundle.putInt("timeMinute", timeMinute);
            newFragment.setArguments(dataBundle);
        }
        newFragment.show(getFragmentManager(), "timePicker");
    }

    //show alert dialog
    public void alertDialog(View v) {
        DialogFragment newFragment = new com.oa.task2do.AlertDialog();

        // add details if user want to edit exist task


        newFragment.show(getFragmentManager(), "alertPicker");
    }

    /* Date-Picker Dialog */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();

        // add details if user want to edit exist task
        if (dateYear != -1 && dateMonth != -1 && dateDay != -1){
            Bundle dataBundle = new Bundle();
            dataBundle.putInt("dateYear", dateYear);
            dataBundle.putInt("dateMonth", dateMonth);
            dataBundle.putInt("dateDay", dateDay);
            newFragment.setArguments(dataBundle);
            System.out.println("save bundle----------"+dateDay+"."+dateMonth+"."+dateYear);
        }
        newFragment.show(getFragmentManager(), "datePicker");
    }
    /* Location Activity */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showLocationDialog(View v) {
        Intent intent = new Intent(this, LocationActivity.class);

        // add details if user want to edit task
        if (mapLatitude != -1 && mapLongitude != -1){
            intent.putExtra("mapLatitude", mapLatitude);
            intent.putExtra("mapLongitude", mapLongitude);
        }

        try {
            startActivityForResult(intent, RESULT_MAP);
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Opps! Your device doesn't support Google-Maps",
                    Toast.LENGTH_SHORT);
            t.show();
        }
    }
    /* voice Dialog */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showVoiceDialog(View v) {
        /* Fire an intent to start the voice recognition activity. */
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition...");
        try {
            startActivityForResult(intent, RESULT_SPEECH);
            EditText et = (EditText) findViewById(R.id.etNewTask);
            et.setText("");
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Opps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }
    }

    /**
     *
     * Returning data from FragmentDialogs
     * (Time dialog)
     * (Date dialog)
     */
    @Override
    public void onFinishEditDialog(Intent data) {
        /* For TimePicker Fragment */
        if (data.getExtras().containsKey("hour"))
            timeHour = data.getExtras().getInt("hour");
        if (data.getExtras().containsKey("minute"))
            timeMinute = data.getExtras().getInt("minute");

        /* For DatePicker Fragment */
        if (data.getExtras().containsKey("year"))
            dateYear = data.getExtras().getInt("year");
        if (data.getExtras().containsKey("month"))
            dateMonth = data.getExtras().getInt("month");
        if (data.getExtras().containsKey("day"))
            dateDay = data.getExtras().getInt("day");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case EDIT_TASK: {
                if (resultCode == EDIT_TASK) {
                    // Receive String from Speach recognition
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    EditText editText_newTask = (EditText) findViewById(R.id.etNewTask);
                    editText_newTask.setText(text.get(0));
                }
                break;
            }
            case RESULT_MAP: {
                if (resultCode == RESULT_OK ) {
                    // Receive 2 parameters from MAP activity
                    mapLongitude= data.getDoubleExtra("longitude", 0);
                    mapLatitude= data.getDoubleExtra("latitude", 0);
                }
                break;
            }
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && data != null) {
                    // Receive String from Speach recognition
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    EditText editText_newTask = (EditText) findViewById(R.id.etNewTask);
                    editText_newTask.setText(text.get(0));
                }
                break;
            }

        }
    }

    public void restoreFromDb(){
        List<Task> list = singleton.getInstance(this).getDb().getAllTasks();
        for(Task task : list){
            String str = new String( task.getTaskMessage() );
            singleton.getInstance(this).getArrayList().add(0,task);
        }
        updateListView();
    }

    public void updateListView(){
        ListView lv = (ListView) findViewById(R.id.listView);
        TaskListBaseAdapter currentList = new TaskListBaseAdapter(this, singleton.getInstance(this).getArrayList());
        lv.setAdapter(currentList);
    }

    /* place all the details from the task in upper EditText and local variables that will send by intent to dialogs */
    public void editTask (View view) {

        /* close the upper bar */
        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.extraOptions);
        if (linearLayout1.getVisibility()== View.VISIBLE )
            linearLayout1.setVisibility(LinearLayout.GONE);

        /* Get the specific Task from listView place that user was touch */
        ListView listView = (ListView) findViewById(R.id.listView);
        int position = listView.getPositionForView(view);
        Task selectedTask = (Task) listView.getItemAtPosition(position);


        /* load all variables from task (if have) to local variables (use in dialogs) */
        // ID
        taskIdSelected = selectedTask._id;
        // if was Alarm
        editTask_hasAlarm = selectedTask._alarm;
        System.out.println("-------editTask_hasAlarm="+editTask_hasAlarm);
        // if was Done
        editTask_hasBeenDone = selectedTask._done;
        System.out.println("-------editTask_hasBeenDone="+editTask_hasBeenDone);
        // Message
        EditText message = (EditText) findViewById(R.id.etNewTask);
        message.setText( selectedTask._taskMessage );
        // Location
        if (selectedTask._mapLongitude != 0 )
        mapLongitude = selectedTask._mapLongitude;
        if (selectedTask._mapLatitude != 0 )
        mapLatitude = selectedTask._mapLatitude;
        // Date
        if (selectedTask._dateYear != 0 )
        dateYear = selectedTask._dateYear;
        if (selectedTask._dateMonth != 0 )
        dateMonth = selectedTask._dateMonth;
        if (selectedTask._dateDay != 0 )
        dateDay = selectedTask._dateDay;
        // Time
        if (selectedTask._timeHour != 0 )
        timeHour = selectedTask._timeHour;
        if (selectedTask._timeMinute != 0 )
        timeMinute = selectedTask._timeMinute;


        /* close keyboard and set focusable false to etNetTask */
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(listView.getWindowToken(), 0);

        editTaskBoolean=taskIdSelected;
        //System.out.println(timeHour+":"+timeMinute+" "+dateDay+"/"+dateMonth+"/"+dateYear+" ("+mapLongitude+","+mapLatitude+") -- "+message);
        currentList.notifyDataSetChanged();
    }


    // if user check the location Button
    private void createAlarmInLocation(){
        lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setAlaramLocation(mapLatitude,mapLongitude);
    }

    // if user check the date or time Button
    private void createAlarmAtDate(Task task){

        /* google analytics */
        // Set Context before using EasyTracker. Note that the SDK will
        // use the application context.
        EasyTracker.getInstance().setContext(this);
        EasyTracker.getInstance().activityStart(this); // Add this method.
        /* EasyTracker is now ready for use. */


        Intent intent = new Intent();
        intent.setAction("com.oa.task2do.ReminderBroadCastReceiver");
        intent.putExtra("taskMessage", task._taskMessage );
        intent.putExtra("taskId", task._id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, task.getID(), intent, 0);

        /* getting all parameters of create Date ob from task */
        Date date = new Date();
        // fix date before and set a default day
        date.setYear(date.getYear()+1900);
        date.setMonth(date.getMonth()+1);
        date.setDate(date.getDay());
        date.setHours(10);
        date.setMinutes(0);
        //System.out.println("----------------"+task._dateYear+" "+task._dateMonth+" "+task._dateDay+" "+task._timeHour+" "+task._timeMinute);
        if (task._dateDay != -1 && task._dateMonth != -1 && task._dateYear != -1){
            date.setYear(   task._dateYear  );
            date.setMonth(  task._dateMonth );
            date.setDate(   task._dateDay   );
        }
        if (task._timeHour != -1 && task._timeMinute != -1){
            date.setHours(  task._timeHour  );
            date.setMinutes(task._timeMinute);
        }
        if (task._timeHour == -1 && task._timeMinute == -1 && task._dateDay == -1 && task._dateMonth == -1 && task._dateYear == -1){
            return;
        }
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + millisecondsUntilDate() , pendingIntent);
    }

    public long millisecondsUntilDate(){
        Calendar cal = Calendar.getInstance();
        if (timeHour != -1 && timeMinute != -1 && dateDay == -1 && dateMonth == -1 && dateYear == -1){
            cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),timeHour,timeMinute);
        }

        if (dateDay != -1 && dateMonth != -1 && dateYear != -1 && timeHour != -1 && timeMinute != -1) {
            cal.set(dateYear, dateMonth, dateDay, timeHour, timeMinute);
        }

        if (dateDay != -1 && dateMonth != -1 && dateYear != -1 && timeHour == -1 && timeMinute == -1) {
            cal.set(dateYear,dateMonth,dateDay,10,0);
        }
        Calendar now = Calendar.getInstance();
        long diff_in_ms = cal.getTimeInMillis()-now.getTimeInMillis();
        return diff_in_ms;
    }

    public void done(View view) {

        ListView listView = (ListView) findViewById(R.id.listView);
        int position = listView.getPositionForView(view);
        Task selectedTask = (Task) listView.getItemAtPosition(position);
        selectedTask._done=1;

        /* update the task is done in DB */
        singleton.getInstance(this).getDb().updateTask(selectedTask);
        Toast.makeText(MainActivity.this, "item done! :  " +
                selectedTask.getTaskMessage(), Toast.LENGTH_LONG).show();
        currentList.notifyDataSetChanged();
        // updateListView() ?  to update the listView at the same moment
    }

    /*public void delete(View view) {
        ListView listView = (ListView) findViewById(R.id.listView);
        int position = listView.getPositionForView(view);
        Task selectedTask = (Task) listView.getItemAtPosition(position);
        Toast.makeText(MainActivity.this, "deleted item : " + " " +
                selectedTask.getTaskMessage(), Toast.LENGTH_LONG).show();
        DeleteFromDb(selectedTask, position);
        currentList.notifyDataSetChanged();
    }*/
    public void delete(View view) {
        ListView listView = (ListView) findViewById(R.id.listView);
        int position = listView.getPositionForView(view);
        Task selectedTask = (Task) listView.getItemAtPosition(position);

        /* delete alert dialog */
        showDelteAlert("DELETE", selectedTask.getTaskMessage(), view);
    }

    public void DeleteFromDb(Task taskToDelete, int position){
        singleton.getInstance(this).getArrayList().remove(position);
        singleton.getInstance(this).getDb().deleteTask( taskToDelete);
        updateListView();
    }

    public void saveTask(View view){
        /* only if user put text for the task - we continue to save it */
        EditText description = (EditText) findViewById(R.id.etNewTask);
        if (!description.getText().toString().isEmpty()){

            // change boolean value if task have any alarm
            if ((timeHour != -1 && timeMinute != -1 )                       // if time change
                    || (dateDay != -1 && dateMonth != -1 && dateYear != -1) // if date change
                    || (mapLatitude != -1 && mapLongitude != -1)) {         // if location change
                editTask_hasAlarm=1;
            }

            if ( editTaskBoolean == -1 ){
            /* not in edit mode */
                /* Create ID for the Task by get currentTimeMillis of this moment */
                int nowUseAsId = (int) (long) System.currentTimeMillis();
                if (nowUseAsId<0) nowUseAsId*=-1;

                /* Text Message */
                String taskMessage = description.getText().toString();

                //System.out.println(timeHour+":"+timeMinute+" "+dateDay+"/"+dateMonth+"/"+dateYear+" ("+mapLongitude+","+mapLatitude+") -- "+taskMessage);
                Task task = new Task(nowUseAsId, editTask_hasAlarm, taskMessage, dateYear, dateMonth, dateDay, timeHour, timeMinute, mapLongitude, mapLatitude);
                    System.out.println("********************************USUAL*********************************************************");
                    System.out.println(timeHour+":"+timeMinute+" "+dateDay+"/"+dateMonth+"/"+dateYear+" ("+mapLongitude+","+mapLatitude+") -- "+taskMessage);

                singleton.getInstance(this).getArrayList().add(0, task);

                //-----continue checking from here -> to register date & cancel alarmManager after if click done
                saveToDb(task);

                /* create alarm if time or date change */
              if ((timeHour != -1 && timeMinute != -1 ) || (dateDay != -1 && dateMonth != -1 && dateYear != -1) ) {
                    createAlarmAtDate(task);
                }
                /* create alarm if location change */
                if (mapLatitude != -1 && mapLongitude != -1){
                    createAlarmInLocation();
                }


            }
            else{
                /* is in edit mode */
                // try to update this task in DB

                /* Text Message */
                String taskMessage = description.getText().toString();

                Task editedTask = new Task(taskIdSelected, editTask_hasAlarm, taskMessage, dateYear, dateMonth, dateDay, timeHour, timeMinute, mapLongitude, mapLatitude);
                        //System.out.println("********************************EDITED*********************************************************");
                        //System.out.println(timeHour+":"+timeMinute+" "+dateDay+"/"+dateMonth+"/"+dateYear+" ("+mapLongitude+","+mapLatitude+") -- "+taskMessage);
                /* create alarm if time or date change */
                if ((timeHour != -1 && timeMinute != -1 ) || (dateDay != -1 && dateMonth != -1 && dateYear != -1) ) {
                    createAlarmAtDate(editedTask);
                }
                /* create alarm if location change */
                if (mapLatitude != -1 && mapLongitude != -1){
                    createAlarmInLocation();
                }
                /* update the DB */
                updateTaskInDb(editedTask);
                /* update the list */
                updateTaskInArray(editedTask);

                // initialize
                editTaskBoolean = -1;
            }
            // initialize Task Message
            description.setText("");
            initialize_variables();

            updateListView();
        }
    }

    public void initialize_variables(){
        /* initialize variables */
        // Map
        mapLongitude= -1;
        mapLatitude= -1;
        // Time
        timeHour= -1;
        timeMinute= -1;
        // Date
        dateYear= -1;
        dateMonth= -1;
        dateDay= -1;

        editTask_hasBeenDone = 0;
        editTask_hasAlarm = 0;
    }

    public void saveToDb(Task newTask){
        singleton.getInstance(this).getDb().addTask(newTask);
    }
    public void updateTaskInDb(Task editTask){
        singleton.getInstance(this).getDb().updateTask(editTask);
    }
    public void updateTaskInArray(Task editTask){
        for (int i=0 ; i< currentList.getCount();i++)
        {
            if (((Task)currentList.getItem(i)).getID()==editTaskBoolean)
            {
                ((Task)currentList.getItem(i)).setTaskMessage(editTask.getTaskMessage());
            }
        }
    }

    //Set Alaram Location func
    public void setAlaramLocation(Double mapLatitude ,Double mapLongitude){

        System.out.println("*********************LOCATION***************************");

        // new intent
        Intent intent = new Intent();
        intent.setAction("com.oa.task2do.ReminderBroadCastReceiver");
        //intent.setAction("com.oa.task2do.LocationNotification");
        intent.putExtra("taskMessage", currentList.getItemID(editTaskBoolean).getTaskMessage() );
        intent.putExtra("taskId", currentList.getItemID(editTaskBoolean).getID());
        intent.putExtra("codeLocation", Integer.toString(editTaskBoolean));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, currentList.getItemID(editTaskBoolean).getID(), intent, PendingIntent.FLAG_ONE_SHOT);

        lm.addProximityAlert(mapLatitude, mapLongitude,500, -1, pendingIntent);

    }

    public void showDelteAlert(String title, String message, final View view)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        ListView listView = (ListView) findViewById(R.id.listView);
                        int position = listView.getPositionForView(view);
                        Task selectedTask = (Task) listView.getItemAtPosition(position);
//                        Toast.makeText(MainActivity.this, "deleted item : " + " " +
//                                selectedTask.getTaskMessage(), Toast.LENGTH_LONG).show();
                        DeleteFromDb(selectedTask, position);
                        currentList.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing

                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        //return choice;
    }


    public void showEditAlert(String title, String message, final View view)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        ListView listView = (ListView) findViewById(R.id.listView);
                        int position = listView.getPositionForView(view);
                        Task selectedTask = (Task) listView.getItemAtPosition(position);

                        currentList.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing

                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        //return choice;
    }
}

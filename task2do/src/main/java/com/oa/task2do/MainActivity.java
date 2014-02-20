package com.oa.task2do;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity  {

    private static final int RESULT_TIME = 1231;
    private static final int RESULT_DATE = 1232;
    private static final int RESULT_MAP = 1233;
    private static final int RESULT_SPEECH = 1234;

    Singleton singleton=null;
    private TaskListBaseAdapter currentList;
    private TextWatcher tw;

    private TimePicker timePicker;
    private DatePicker datePicker;
    private double longitude=0;
    private double latitude=0;


    //private Task newTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (singleton.getInstance(this).getArrayList().isEmpty())
            restoreFromDb();

        currentList = new TaskListBaseAdapter(this, singleton.getInstance(this).getArrayList());
//        ListView listView = (ListView) findViewById(R.id.listView);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
//                //editTask(v);
//                //System.out.println("list view itemClick");
//            }
//        });




        /* inflate bar by text change */
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

//        /* try to inflate by focus change */
//        EditText etNewTask = (EditText) findViewById(R.id.etNewTask);
//        etNewTask.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.extraOptions);
//                if(hasFocus){
//                    linearLayout.setVisibility(LinearLayout.VISIBLE);
//                } else {
//                    linearLayout.setVisibility(LinearLayout.GONE);
//                }
//            }
//        });
    }

    /* Time-Picker Dialog */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
    /* Date-Picker Dialog */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    /* Location Activity */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showLocationDialog(View v) {
        Intent intent = new Intent(this, LocationActivity.class);
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
//        DialogFragment newFragment = new VoiceFragment();
//        newFragment.show(getFragmentManager(), "voice");
        /**
         * Fire an intent to start the voice recognition activity.
         */
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



/*    public void showExtraOptions(View view, boolean hasFocus)
    {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.extraOptions);
        if (hasFocus){
            linearLayout.setVisibility(LinearLayout.VISIBLE);
        }
        else{
            linearLayout.setVisibility(LinearLayout.GONE);
        }
    }*/


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_TIME: {
                if (resultCode == RESULT_OK ) {
                    // Receive Time parameters from TimePickerFragments

                }
                break;
            }
            case RESULT_DATE: {
                if (resultCode == RESULT_OK ) {
                    // Receive Date parameters from DatePickerFragments

                }
                break;
            }
            case RESULT_MAP: {
                if (resultCode == RESULT_OK ) {
                    // Receive 2 parameters from MAP activity
                    longitude= data.getDoubleExtra("longitude", 0);
                    latitude= data.getDoubleExtra("latitude", 0);
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
//    public static class PlaceholderFragment extends Fragment {
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            return rootView;
//        }
//    }




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

    /* inflate edit options bar of the task clicked */
    public void editTask (View view) {

        ListView listView = (ListView) findViewById(R.id.listView);
        int position = listView.getPositionForView(view);
        Task selectedTask = (Task) listView.getItemAtPosition(position);
        Toast.makeText(MainActivity.this, "edited item : " + " " +
                selectedTask.getTaskMessage(), Toast.LENGTH_LONG).show();
        currentList.notifyDataSetChanged();

        //LinearLayout linearLayout = (LinearLayout) findViewById(R.id.listViewExtraOptions);
        /*
        if (linearLayout.getVisibility()==View.GONE)
            linearLayout.setVisibility(View.VISIBLE);
        else
            linearLayout.setVisibility(View.GONE); */
    }

    public void done(View view) {

        ListView listView = (ListView) findViewById(R.id.listView);
        int position = listView.getPositionForView(view);
        Task selectedTask = (Task) listView.getItemAtPosition(position);
        Toast.makeText(MainActivity.this, "deleted item : " + " " +
                selectedTask.getTaskMessage(), Toast.LENGTH_LONG).show();
        Delete(selectedTask, position);
        currentList.notifyDataSetChanged();

    }

    public void Delete(Task taskToDelete, int position){
        singleton.getInstance(this).getArrayList().remove(position);
        singleton.getInstance(this).getDb().deleteTask( taskToDelete);
        updateListView();
    }


    public void saveTask(View view){
        EditText description = (EditText) findViewById(R.id.etNewTask);
        if (!description.getText().toString().isEmpty()){

            //TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
            //DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
            //Date date= new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour() , timePicker.getCurrentMinute() );

            int nowUseAsId = (int) (long) System.currentTimeMillis();
            if (nowUseAsId<0) nowUseAsId*=-1;

            //System.out.println("nowUseAsId="+nowUseAsId);

            String taskMessage = description.getText().toString();
            Task task = new Task(nowUseAsId, taskMessage);

            singleton.getInstance(this).getArrayList().add(0, task);

            //-----continue checking from here -> to register date & cancel alarmManager after if click done
            saveToDb(task);

            description.setText("");
            updateListView();

            //finish();
        }
    }

    public void saveToDb(Task newTask){
        singleton.getInstance(this).getDb().addTask(newTask);
    }
}

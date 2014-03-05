package com.oa.task2do;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by joe on 25/10/13.
 */
public class TaskListBaseAdapter extends BaseAdapter {

    private static ArrayList<Task> taskDetailsArrayList;
    private LayoutInflater l_Inflater;
    private static int ifEditTask=-1;
    public TaskListBaseAdapter(Context context, ArrayList<Task> results) {
        taskDetailsArrayList = results;
        l_Inflater = LayoutInflater.from(context);
    }

    public void setTaskDetailsArrayList(ArrayList<Task> taskDetailsArrayList) {
        TaskListBaseAdapter.taskDetailsArrayList = taskDetailsArrayList;
    }

    public int getCount() {
        return taskDetailsArrayList.size();
    }

    public Object getItem(int position) {
        return taskDetailsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return taskDetailsArrayList.get(position).getID();
    }

    public Task getItemByID(long id) {
        for(Iterator<Task> i = taskDetailsArrayList.iterator(); i.hasNext(); ) {
            Task item = i.next();
            if (item.getID() == (int)id ){
                return item;
            }
        }
        return null;
    }

    private static class ViewHolder {
        TextView remindMessage;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            //change layout by alarm param
            if(taskDetailsArrayList.get(position).get_alarm()==1)
                convertView = l_Inflater.inflate(R.layout.task_format_alert_inflate, null);
            //change layout by done param
            else if(taskDetailsArrayList.get(position).get_done()==1)
                convertView = l_Inflater.inflate(R.layout.task_format_done_inflate, null);
            else if (taskDetailsArrayList.get(position).getID() == ifEditTask)
                convertView = l_Inflater.inflate(R.layout.task_format_extras, null);
            else convertView = l_Inflater.inflate(R.layout.task_format, null);

            holder = new ViewHolder();
            holder.remindMessage = (TextView) convertView.findViewById(R.id.taskText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.remindMessage.setText(taskDetailsArrayList.get(position).getTaskMessage());

        return convertView;
    }


    public ArrayList<Task> getTaskDetailsArrayList(){
        return taskDetailsArrayList;
    }

    public void getIfEditTask(int edit){
        this.ifEditTask=edit;
    }

}
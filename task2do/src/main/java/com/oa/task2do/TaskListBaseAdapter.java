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

    public TaskListBaseAdapter(Context context, ArrayList<Task> results) {
        taskDetailsArrayList = results;
        l_Inflater = LayoutInflater.from(context);
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
            convertView = l_Inflater.inflate(R.layout.task_format, null);
            holder = new ViewHolder();
            holder.remindMessage = (TextView) convertView.findViewById(R.id.taskText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.remindMessage.setText(taskDetailsArrayList.get(position).getTaskMessage());

        return convertView;
    }
}
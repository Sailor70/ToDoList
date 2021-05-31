package com.pawelchmielarski.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {

    Context context;
    public TaskAdapter(Context context, ArrayList<Task> tasks) {

        super(context, 0, tasks);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvDeadline = (TextView) convertView.findViewById(R.id.tvDeadline);
        // Populate the data into the template view using the data object
        tvName.setText(task.getName());
        tvDeadline.setText(task.getDeadline().toString());

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//                if(view.)  // reakcje na klikanie poszczególnych elementów itemu na liście (

                Intent i=new Intent(context, TaskDetailsActivity.class);

//                i.putExtra(task);
                context.startActivity(i);

                Toast.makeText(context, "kliknięto task o pozycji: " + task.getName(), Toast.LENGTH_LONG).show();
//                User user = getItem(position);

                // Do what you want here...

            }

        });
        // Return the completed view to render on screen

        return convertView;
    }
}

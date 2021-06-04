package com.pawelchmielarski.todolist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TaskAdapter extends BaseAdapter { // implements View.OnClickListener ?

    Context context;
    ArrayList<Task> tasks;

    // może ten adapter zrobić singletonem i wywalić serwis?
    public TaskAdapter(Context ctx) {
        this.context = ctx;
        this.tasks = TasksService.getInstance().getTasks();
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Task getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        Task task = getItem(pos);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvDeadline = (TextView) convertView.findViewById(R.id.tvDeadline);
        CheckBox checkBoxDone = (CheckBox) convertView.findViewById(R.id.checkBoxDone);
        ImageView btnDelete = (ImageView) convertView.findViewById(R.id.ivDelete);
        // Populate the data into the template view using the data object
        tvName.setText(tasks.get(pos).getName().concat(" "));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

        tvDeadline.setText(sdf.format(tasks.get(pos).getDeadline()));
        checkBoxDone.setChecked(tasks.get(pos).isDone());

        // Click events
        checkBoxDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.setDone(!task.isDone());
                Toast.makeText(context, "task done " + task.isDone(), Toast.LENGTH_LONG).show();
                TasksService.getInstance().setTaskDone(pos, task.isDone());
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasks.remove(task);
                TasksService.getInstance().setTasks(tasks);
                notifyDataSetChanged();
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "kliknięto task o pozycji: " + pos, Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, TaskDetailsActivity.class);
                //PASS INDEX OR POS
                i.putExtra("Position", pos);
                context.startActivity(i);
            }
        });

        return convertView;
    }
}

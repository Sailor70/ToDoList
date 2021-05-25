package com.pawelchmielarski.todolist;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> tasks;
    private ArrayAdapter<Task> tasksAdapter;
    private ListView lvTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), TaskDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        tasks = new ArrayList<Task>();
        tasksAdapter = new ArrayAdapter<Task>(this, android.R.layout.simple_list_item_1, tasks); // dodać custom list item (custom adapter)
        lvTasks = (ListView) findViewById(R.id.tasksList);
        lvTasks.setAdapter(tasksAdapter);

        createItems();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createItems() {
        tasks.add(new Task("task1", "descr1",null, null, Priority.HIGH, null )); // new Date(System.currentTimeMillis() + 100000)
        tasks.add(new Task("task2", "descr2",null, null, Priority.HIGH, null ));
        tasks.add(new Task("task3", "descr3",null, null, Priority.HIGH, null ));
        tasks.add(new Task("task4", "descr4",null, null, Priority.HIGH, null ));
    }
}
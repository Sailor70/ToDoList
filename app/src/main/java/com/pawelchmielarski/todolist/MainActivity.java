package com.pawelchmielarski.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> tasks;
//    private TaskAdapter tasksAdapter;
    private ListView lvTasks;
    private TaskAdapter taskAdapter;

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

//        tasks = new ArrayList<Task>();
//        createItems();
//        tasksAdapter = new TaskAdapter(this, tasks);

        lvTasks = (ListView) findViewById(R.id.tasksList);

        taskAdapter = new TaskAdapter(this);
        lvTasks.setAdapter(taskAdapter);

//        lvTasks.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View v, int pos,
//                                    long id) {
//                // TODO Auto-generated method stub
//
//                Task item = taskAdapter.getItem(pos);
//                Toast.makeText(getApplicationContext(), "kliknięto task o pozycji: " + pos, Toast.LENGTH_LONG).show();
//
//                Intent i = new Intent(getApplicationContext(), TaskDetailsActivity.class);
//
//                //PASS INDEX OR POS
//                i.putExtra("Position", pos);
//                startActivity(i);
//
//            }
//        });

    }

    @Override
    public void onResume(){
        super.onResume();
        taskAdapter.notifyDataSetChanged();
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
        for (int i = 0; i < 20; i++) {
            tasks.add(new Task("task" + i, "descr" + i, false, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 100000), Priority.HIGH, null ));
        }
    }
}
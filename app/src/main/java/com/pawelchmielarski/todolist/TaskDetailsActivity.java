package com.pawelchmielarski.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class TaskDetailsActivity extends AppCompatActivity {

    EditText taskName;
    EditText taskDeadline;
    EditText task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
    }
}
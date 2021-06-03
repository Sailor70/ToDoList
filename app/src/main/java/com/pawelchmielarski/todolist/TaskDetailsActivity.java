package com.pawelchmielarski.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.MenuPopupWindow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class TaskDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Task task;
    EditText etTaskName;
    EditText etTaskDeadline;
    EditText etTaskDescription;
    CheckBox checkBoxDone;
    EditText etCreatedAt;
    MenuPopupWindow.MenuDropDownListView priority;

    TaskAdapter taskAdapter;
    int pos = -1;

    Button btnSave;
    Button btnCancel;

    // 2 tryby działania - wczytanie danych z listy lub nowe zadanie
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        taskAdapter = new TaskAdapter(this); // czy to potrzebne?

        etTaskName = (EditText) findViewById(R.id.editTextTextPersonName);
        etTaskDeadline = (EditText) findViewById(R.id.editTextDate);
        checkBoxDone = (CheckBox) findViewById(R.id.checkBoxDetailsDone);
        etTaskDescription = (EditText) findViewById(R.id.editTextMultiLineDescription);
        btnSave = (Button) findViewById(R.id.buttonSave);
        btnCancel = (Button) findViewById(R.id.buttonCancel);

        //GET PASSED DATA
        Intent i = getIntent();
        if (i.getExtras() == null) {
            initNewTask();
        } else {
            pos = i.getExtras().getInt("Position");
            loadTask();
        }

        etTaskName.setOnClickListener(this);
        etTaskDeadline.setOnClickListener(this);
        checkBoxDone.setOnClickListener(this);
        etTaskDescription.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    public void initNewTask() {

    }

    public void loadTask() {
        task = TasksService.getInstance().getTasks().get(pos);
        Toast.makeText(this, "task description " + task.getDescription(), Toast.LENGTH_LONG).show();
        etTaskName.setText(task.getName());
        etTaskDeadline.setText(task.getDeadline().toString());
        etTaskDescription.setText(task.getDescription());
        checkBoxDone.setChecked(task.isDone());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == checkBoxDone.getId()) {
            task.setDone(!task.isDone());
            Toast.makeText(this, "task done " + task.isDone(), Toast.LENGTH_LONG).show();
            TasksService.getInstance().setTaskDone(pos, task.isDone());
        } else if (view.getId() == btnSave.getId()) {
            saveTask();
            Toast.makeText(this, "task saved " + task.getName(), Toast.LENGTH_LONG).show();
            finish();
        } else if (view.getId() == btnCancel.getId()) {
            finish();
        }
    }

    private void saveTask() {
        inputsToTask();
        if (pos == -1) {
            TasksService.getInstance().addNewTask(task);
        } else {
            TasksService.getInstance().updateTask(pos, task);
        }
    }

    private void inputsToTask() {
        task = new Task();
        task.setName(etTaskName.getText().toString());
        task.setDescription(etTaskDescription.getText().toString());
        if (etTaskDescription.getText().length() > 0) {
            task.setDeadline(new Date(etTaskDeadline.getText().toString()));
        }
    }
}
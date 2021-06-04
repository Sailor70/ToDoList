package com.pawelchmielarski.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskDetailsActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Task task;
    EditText etTaskName;
    EditText etTaskDeadline;
    EditText etTaskDescription;
    CheckBox checkBoxDone;
    TextView tvCreatedAt;

    TaskAdapter taskAdapter;
    int pos = -1;

    Button btnSave;
    Button btnCancel;
    Button btnDeadlinePicker;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

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
        tvCreatedAt = (TextView) findViewById(R.id.textViewCreatedAt);
        btnSave = (Button) findViewById(R.id.buttonSave);
        btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnDeadlinePicker = (Button) findViewById(R.id.buttonDatePicker);
        //GET PASSED DATA
        Intent i = getIntent();
        if (i.getExtras() == null) {
            initNewTask();
        } else {
            pos = i.getExtras().getInt("Position");
            loadTask();
        }

        etTaskName.setOnClickListener(this);
        checkBoxDone.setOnClickListener(this);
        etTaskDescription.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        etTaskDeadline.setEnabled(false); // wybór daty tylko z pickera


        btnDeadlinePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR); // wartości ustawiane na początku - zmienić na te z danego taska
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
//                day = task.getDeadline().getDay();
                DatePickerDialog datePickerDialog = new DatePickerDialog(TaskDetailsActivity.this, TaskDetailsActivity.this,year, month,day);
                datePickerDialog.show();
            }
        });

    }

    public void initNewTask() {
        tvCreatedAt.setVisibility(View.INVISIBLE);
    }

    public void loadTask() {
        task = TasksService.getInstance().getTasks().get(pos);
        Toast.makeText(this, "task description " + task.getDescription(), Toast.LENGTH_LONG).show();
        etTaskName.setText(task.getName());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        etTaskDeadline.setText(sdf.format(task.getDeadline()));
        etTaskDescription.setText(task.getDescription());
        checkBoxDone.setChecked(task.isDone());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        tvCreatedAt.setText(String.format("Zadanie utworzono: %s", sdf.format(task.getCreatedAt())));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == checkBoxDone.getId()) {
            task.setDone(!task.isDone());
            Toast.makeText(this, "task done " + task.isDone(), Toast.LENGTH_LONG).show();
            TasksService.getInstance().setTaskDone(pos, task.isDone());
        }
        else if (view.getId() == btnSave.getId()) {
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

    private void inputsToTask()  {
        task = new Task();
        task.setName(etTaskName.getText().toString());
        task.setDescription(etTaskDescription.getText().toString());
        if (etTaskDescription.getText().length() > 0) {
            try {
                task.setDeadline(sdf.parse(etTaskDeadline.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        task.setDone(checkBoxDone.isChecked());
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        task.setCreatedAt(new Date(System.currentTimeMillis()));
    }

    private void updateLabel(Calendar myCalendar) {
        String myFormat = "MM/dd/yy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etTaskDeadline.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month + 1;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(TaskDetailsActivity.this, TaskDetailsActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        String date1 = String.valueOf(myYear);
        if(myMonth > 9) {
            date1 = date1.concat("-" + myMonth);
        } else {
            date1 = date1.concat("-0" + myMonth);
        }
        if(myday > 9) {
            date1 = date1.concat("-" + myday);
        } else {
            date1 = date1.concat("-0" + myday);
        }
        if(myHour > 9) {
            date1 = date1.concat(" " + myHour);
        } else {
            date1 = date1.concat(" 0" + myHour);
        }
        if(myMinute > 9) {
            date1 = date1.concat(":" + myMinute);
        } else {
            date1 = date1.concat(":0" + myMinute);
        }
//        etTaskDeadline.setText(myYear + "-" + myMonth + "-" + myday + " " + myHour + ":" + myMinute);
        etTaskDeadline.setText(date1);
    }
}
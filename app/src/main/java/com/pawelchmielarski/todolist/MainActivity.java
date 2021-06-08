package com.pawelchmielarski.todolist;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter taskAdapter;
    private static final int PERMISSION_REQUEST_CODE = 1967;

    private boolean notification = true;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
            }
        });

        ListView lvTasks = (ListView) findViewById(R.id.tasksList);
        taskAdapter = new TaskAdapter(this);
        lvTasks.setAdapter(taskAdapter);

        getPermissions();

        TasksService.getInstance().readTasksFromFile(this);
        TasksService.getInstance().sortTasksByDone();
        taskAdapter.notifyDataSetChanged();

        Intent i = getIntent();
        if (i.getExtras() != null) {
            notification = i.getExtras().getBoolean("notification");
        }
        if (notification) {
            this.setDelayedNotification();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        TasksService.getInstance().sortTasksByDone();
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        TasksService.getInstance().writeTasksToFile(this);
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_export_tasks) {
            TasksService.getInstance().writeTasksToFile(this);
            taskAdapter.notifyDataSetChanged();
        } else if (id == R.id.action_import_tasks) {
            TasksService.getInstance().readTasksFromFile(this);
            taskAdapter.notifyDataSetChanged();
        } else if (id == R.id.action_sort_name) {
            TasksService.getInstance().sortTasksByName();
            taskAdapter.notifyDataSetChanged();
        } else if (id == R.id.action_sort_deadline) {
            TasksService.getInstance().sortTasksByDeadline();
            taskAdapter.notifyDataSetChanged();
        } else if (id == R.id.action_sort_done) {
            TasksService.getInstance().sortTasksByDone();
            taskAdapter.notifyDataSetChanged();
        } else if (id == R.id.action_sort_priority) {
            TasksService.getInstance().sortTasksByPriority();
            taskAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    public void getPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);

        }
    }

    //odpowiedź na zarządanie przyznania uprawnień
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Przyznano uprawnienia", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Musisz przyznać uprawnienia aby używać tej aplikacji", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        }

    }

    private void setDelayedNotification() {
        AlarmManager alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        TasksNotificationReceiver receiver = new TasksNotificationReceiver();
        IntentFilter filter = new IntentFilter("ALARM_ACTION");
        registerReceiver(receiver, filter);

        Intent intent = new Intent("ALARM_ACTION");
        intent.putExtra("param", "My scheduled action");
        PendingIntent operation = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarms.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, operation);
    }

}

class TasksNotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "NTF";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        int numberOfTasks = numberOftasksToDo();
        if (numberOfTasks > 0) {
            Intent intentNotification = new Intent(context, MainActivity.class);
            intentNotification.putExtra("notification", false);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentNotification, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builderNotificationCompat = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setContentTitle("Przypomnienie o zadaniach")
                    .setContentText("Liczba zadań do wykonania na dziś: " + numberOfTasks)
                    .setSmallIcon(android.R.drawable.btn_star_big_on);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Tasks notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(0, builderNotificationCompat.build());
        }
    }

    private int numberOftasksToDo() {
        ArrayList<Task> tasks = TasksService.getInstance().getTasks();
        int toDo = 0;
        Date nextDay = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(nextDay);
        c.add(Calendar.DATE, 1);
        nextDay = c.getTime();
        for (Task tsk : tasks) {
            if ((tsk.getDeadline() != null) && (tsk.getDeadline().before(nextDay))) {
                toDo++;
            }
        }
        return toDo;
    }

}


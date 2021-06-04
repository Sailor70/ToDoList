package com.pawelchmielarski.todolist;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;

public class TasksService {

    private static final TasksService INSTANCE = new TasksService(); // singleton pattern

    private ArrayList<Task> tasks;

    public TasksService() {
        tasks = new ArrayList<Task>();
//        for (int i = 0; i < 20; i++) {
//            tasks.add(new Task("task" + i, "descr" + i, false, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 100000), Priority.HIGH));
//        }
    }

    public static TasksService getInstance() {
        return INSTANCE;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void setTaskDone(int index, boolean done) {
        tasks.get(index).setDone(done);
    }

    public void addNewTask(Task task) {
        this.tasks.add(task);
    }

    public void updateTask(int index, Task task) {
        tasks.set(index, task);
    }

    public void writeTasksToFile(Context ctx) {
        try {
            FileOutputStream fileOutputStream = ctx.openFileOutput("tasks.txt", Context.MODE_PRIVATE);
//            PrintWriter writer = new PrintWriter(new OutputStreamWriter(fileOutputStream));
//            writer.print(""); // czyszczenie pliku
//            writer.close();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            for (Task task : tasks) {
                bw.write(task.getName() + "," + task.getDescription() + "," + task.isDone() + "," + task.getCreatedAt() + "," + task.getDeadline()
                        + "," + task.getPriority());
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readTasksFromFile(Context ctx) {

        if(!(tasks.size() > 0)) {
            try {
                InputStream inputStream = ctx.openFileInput("tasks.txt");

                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null) {
                        String[] fields = line.split(",");
                        Log.i("Date : ", fields[4]);
                        Task tsk = new Task();
                        tsk.setName(fields[0]);
                        tsk.setDescription(fields[1]);
                        tsk.setDone(Boolean.parseBoolean(fields[2]));
                        tsk.setCreatedAt(new Date(fields[3]));
                        if (!fields[4].equals("null")) {
                            Log.i("Date : ", "Wlazło tam gdzie nie trzeba");
                            tsk.setDeadline(new Date(fields[4]));
                        } else {
                            tsk.setDeadline(null);
                        }
                        tsk.setPriority(Priority.valueOf(fields[5]));
                        tasks.add(tsk);
//                    tasks.add(new Task(fields[0],fields[1],Boolean.parseBoolean(fields[2]), new Date(fields[3]),
//                            fields[3] == null ? null : new Date(fields[4]), Priority.valueOf(fields[5])));
                    }

                    inputStream.close();
                }
            } catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
        }

    }
}

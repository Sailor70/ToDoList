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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class TasksService {

    private static final TasksService INSTANCE = new TasksService(); // singleton pattern

    private ArrayList<Task> tasks;

    public TasksService() {
        tasks = new ArrayList<Task>();
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

    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    public void writeTasksToFile(Context ctx) {
        try {
            FileOutputStream fileOutputStream = ctx.openFileOutput("tasks.txt", Context.MODE_PRIVATE);
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
                        Task tsk = new Task();
                        tsk.setName(fields[0]);
                        tsk.setDescription(fields[1]);
                        tsk.setDone(Boolean.parseBoolean(fields[2]));
                        tsk.setCreatedAt(new Date(fields[3]));
                        if (!fields[4].equals("null")) {
                            tsk.setDeadline(new Date(fields[4]));
                        } else {
                            tsk.setDeadline(null);
                        }
                        tsk.setPriority(Priority.valueOf(fields[5]));
                        tasks.add(tsk);
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

    public void sortTasksByName() {
        Comparator<Task> compareByName = (Task t1, Task t2) ->
                t1.getName().compareTo(t2.getName());
        tasks.sort(compareByName);
    }

    public void sortTasksByDeadline() {
        tasks.sort(new Comparator<Task>() {
            public int compare(Task t1, Task t2) {
                if (t1.getDeadline() == null || t2.getDeadline() == null)
                    return -1;
                return t1.getDeadline().compareTo(t2.getDeadline());
            }
        });
    }

    public void sortTasksByDone() {
        Comparator<Task> compareByDone =
                Comparator.comparing(Task::isDone, Boolean::compare);
//                Comparator.comparing(Task::isDone, Boolean::compare).reversed(); // najpierw zrobione
        tasks.sort(compareByDone);
    }

    public void sortTasksByPriority() {
        tasks.sort(new Comparator<Task>() {
            public int compare(Task t1, Task t2) {
                int i = t1.getPriority().compareTo(t2.getPriority());
                if(i != 0) return -i;  // reverse sort
                return t1.getPriority().compareTo(t2.getPriority());
            }
        });
    }
}

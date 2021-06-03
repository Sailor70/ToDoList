package com.pawelchmielarski.todolist;

import java.util.ArrayList;
import java.util.Date;

public class TasksService {

    // + zapis i odczyt do pliku

    private static final TasksService INSTANCE = new TasksService(); // singleton pattern

    private ArrayList<Task> tasks;

    public TasksService() {
        tasks = new ArrayList<Task>();
        for (int i = 0; i < 20; i++) {
            tasks.add(new Task("task" + i, "descr" + i, false, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 100000), Priority.HIGH, null));
        }
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
}

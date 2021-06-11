package com.pawelchmielarski.todolist;

import java.util.Date;

public class Task {

    private String name;
    private String description;
    private boolean done;
    private Date createdAt;
    private Date deadline;
    private Priority priority;

    public Task() {
    }

    public Task(String name, String description, boolean done, Date createdAt, Date deadline, Priority priority) { // String[] attachments
        this.name = name;
        this.description = description;
        this.done = done;
        this.createdAt = createdAt;
        this.deadline = deadline;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

}

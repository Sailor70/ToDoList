package com.pawelchmielarski.todolist;

import java.util.Date;

public class Task {

    private String name;
    private String description;
    private Date createdAt;
    private Date deadline;
    private Priority priority;
    private String[] attachments;

    public Task(String name, String description, Date createdAt, Date deadline, Priority priority, String[] attachments) {
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.deadline = deadline;
        this.priority = priority;
        this.attachments = attachments;
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

    public String[] getAttachments() {
        return attachments;
    }

    public void setAttachments(String[] attachments) {
        this.attachments = attachments;
    }
}

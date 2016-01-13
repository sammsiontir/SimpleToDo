package com.codepath.simpletodo;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by chengfu_lin on 1/12/16.
 */
public class Task implements Serializable {
    public static final int HIGH_PRIORITY = 0;
    public static final int LOW_PRIORITY = 1;

    private static final long serialVersionUID = 5177222050535318633L;

    private int id;
    private String title;
    private Calendar dueDate;
    private int priority;

    public Task(int id, String title, Calendar dueDate, int priority) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public Task() {
        this.id = 0;
        this.title = "";
        this.dueDate = Calendar.getInstance();
        this.priority = HIGH_PRIORITY;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    public void setPriority(int priorityLevel) {
        this.priority = priorityLevel;
    }
}

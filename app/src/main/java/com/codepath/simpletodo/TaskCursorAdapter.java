package com.codepath.simpletodo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chengfu_lin on 1/13/16.
 */
public class TaskCursorAdapter extends CursorAdapter{
    public TaskCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
        TextView tvPriority = (TextView) view.findViewById(R.id.tvPriority);
        // Extract properties from cursor
        Task task = TodoItemDatabaseHandler.cursorToTask(cursor);
        // Populate fields with extracted properties
        tvBody.setText(task.getTitle());

        String priority = "";
        if(task.getPriority()==Task.HIGH_PRIORITY) {
            priority = "High";
        }
        if(task.getPriority()==Task.LOW_PRIORITY) {
            priority = "Low";
        }
        tvPriority.setText(priority);
    }

    public List<Task>  notifyDataSetChanged(TodoItemDatabaseHandler databaseHandler) {
        List<Task> items = databaseHandler.getALLTasks();
        Cursor taskCurSor = databaseHandler.getALLTasksCursor();
        this.changeCursor(taskCurSor);
        return items;
    }
}

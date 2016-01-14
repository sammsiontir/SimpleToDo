package com.codepath.simpletodo.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.adapter.TaskCursorAdapter;
import com.codepath.simpletodo.data.Task;
import com.codepath.simpletodo.helper.TodoItemDatabaseHandler;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TodoItemDatabaseHandler databaseHandler;
    ListView lvViewAllTasks;
    List<Task> items;
    Cursor taskCurSor;
    TaskCursorAdapter taskAdapter;
    Task selectTask;



    private final int REQUEST_EDITITEM = 20;
    private final int REQUEST_ADDITEM = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
         * For Database
         */
        databaseHandler = TodoItemDatabaseHandler.getInstance(this);
        // database = databaseHandler.getWritableDatabase();
        items = databaseHandler.getALLTasks();
        taskCurSor = databaseHandler.getALLTasksCursor();
        /*
         * For Layout Display
         */
        lvViewAllTasks = (ListView) findViewById(R.id.lvViewAllTasks);
        taskAdapter = new TaskCursorAdapter(this, taskCurSor, 0);
        lvViewAllTasks.setAdapter(taskAdapter);

        /*
         * For interact items
         */
        setupListViewListener();
    }

    private void setupListViewListener() {
        // delete on long click
        lvViewAllTasks.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        selectTask = items.get(position);
                        deleteTask();
                        return true;
                    }
                }
        );

        // jump to edit page on click
        lvViewAllTasks.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent EditTaskActivity = new Intent(MainActivity.this, AddTaskActivity.class);
                        EditTaskActivity.putExtra("EDIT", true);
                        EditTaskActivity.putExtra("Task", items.get(position));
                        startActivityForResult(EditTaskActivity, REQUEST_EDITITEM);
                    }
                }
        );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_EDITITEM is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_EDITITEM) {
            Task editTask = (Task) data.getSerializableExtra("Task");
            databaseHandler.updateTask(editTask);
            items = taskAdapter.notifyDataSetChanged(databaseHandler);
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_ADDITEM) {
            Task newTask = (Task) data.getSerializableExtra("Task");
            databaseHandler.insertTask(newTask);
            items = taskAdapter.notifyDataSetChanged(databaseHandler);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void clickAddBotton(View view) {
        // call AddTaskActivity to edit new task
        Intent AddTaskActivity = new Intent(MainActivity.this, AddTaskActivity.class);
        startActivityForResult(AddTaskActivity, REQUEST_ADDITEM);
    }


    public void deleteTask(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.delete_alert_title)
                .setNegativeButton(R.string.delete_alert_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Cancel button
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.delete_alert_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Delete button
                        databaseHandler.deleteTask(selectTask);
                        items = taskAdapter.notifyDataSetChanged(databaseHandler);
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}

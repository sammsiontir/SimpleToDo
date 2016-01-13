package com.codepath.simpletodo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TodoItemDatabaseHandler databaseHandler;
    ListView lvViewAllTasks;
    List<Task> items;
    Cursor taskCurSor;
    TaskCursorAdapter taskAdapter;



    private final int REQUEST_EDITITEM = 20;
    private final int REQUEST_ADDITEM = 21;
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
         * For Database
         */
        uid = 8;
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
                        Task clickedTask = items.get(position);
                        databaseHandler.deleteTask(clickedTask);
                        items = taskAdapter.notifyDataSetChanged(databaseHandler);
                        return true;
                    }
                }
        );

        // jump to edit page on click
        /*
        lvViewAllTasks.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent editActivity = new Intent(MainActivity.this, EditTaskActivity.class);
                        editActivity.putExtra("todoItem", items.get(position));
                        editActivity.putExtra("position", position);
                        startActivityForResult(editActivity, REQUEST_EDITITEM);
                    }
                }
        );
        */

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
        // FIXME: add a default task
        Task newTask = new Task();
        newTask.setId(uid);
        String title = "Task " + uid;
        newTask.setTitle(title);
        uid++;

        // update database
        databaseHandler.insertTask(newTask);
        items = taskAdapter.notifyDataSetChanged(databaseHandler);
    }
}

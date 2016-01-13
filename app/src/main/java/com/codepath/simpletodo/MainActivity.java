package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView lvViewAllTasks;
    List<Task> items;
    ArrayList<String> items_title;
    ArrayAdapter items_titleAdapter;
    TodoItemDatabaseHandler databaseHandler;


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
        uid = 1;
        databaseHandler = TodoItemDatabaseHandler.getInstance(this);
        // database = databaseHandler.getWritableDatabase();
        items = databaseHandler.getALLTasks();
        //items = new ArrayList<>();
        /*
         * For Layout Display
         */
        lvViewAllTasks = (ListView) findViewById(R.id.lvViewAllTasks);
        // FIXME: display title only for now
        items_title = new ArrayList<>();
        for(Task i:items) {
            items_title.add(i.getTitle());
        }
        // setup adapter for ListView
        items_titleAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, items_title);
        lvViewAllTasks.setAdapter(items_titleAdapter);

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
                        items_title.remove(position);
                        items_titleAdapter.notifyDataSetChanged();
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

        items.add(newTask);
        items_title.add(newTask.getTitle());
        items_titleAdapter.notifyDataSetChanged();

        // update database
        databaseHandler.insertTask(newTask);
    }
}

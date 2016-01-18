package com.codepath.simpletodo.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.codepath.simpletodo.Fragment.DatePickerFragment;
import com.codepath.simpletodo.R;
import com.codepath.simpletodo.data.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private Task task;
    private boolean MODE_EDIT;

    private EditText etTitle;
    private TextView tvDate;
    private Switch swPriority;

    private final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Catch passed data to tell if it's a new or edit task
        MODE_EDIT = getIntent().getBooleanExtra("EDIT", false);
        String title = "";
        if(MODE_EDIT) {
            task = (Task) getIntent().getSerializableExtra("Task");
            title = "Edit Task";
        }
        else {
            task = new Task();
            title = "Add New Task";
        }
        getSupportActionBar().setTitle(title);

        // Get each editable field
        etTitle = (EditText) findViewById(R.id.etTitle);
        tvDate = (TextView) findViewById(R.id.tvDate);
        swPriority = (Switch) findViewById(R.id.swPriority);
        /*
         * Display existing task item if in edit mode
         * Display hint if in add mode
         */
        if(MODE_EDIT) {
            etTitle.setText(task.getTitle());
            displayDueDate();
            swPriority.setChecked(task.getPriority()==Task.HIGH_PRIORITY);
            updateSwitchText();
        }
        else {
            etTitle.setHint("Enter new task...");
            displayDueDate();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        task.getDueDate().set(Calendar.YEAR, year);
        task.getDueDate().set(Calendar.MONTH, monthOfYear);
        task.getDueDate().set(Calendar.DAY_OF_MONTH, dayOfMonth);

        displayDueDate();
    }

    public void displayDueDate() {
        tvDate.setText(FORMAT.format(task.getDueDate().getTime()));
    }

    public void editDate(View view) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("DueDate", task.getDueDate());
        datePickerFragment.setArguments(bundle);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    public void swPriority(View view) {
        updateSwitchText();
    }

    private void updateSwitchText() {
        if(swPriority.isChecked()) {
            swPriority.setText("High Priority?");
        }
        else {
            swPriority.setText("Low Priority?");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(MODE_EDIT)
            getMenuInflater().inflate(R.menu.menu_edit_task, menu);
        else
            getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            btnSave();
        }

        if (id == R.id.action_cancel) {
            btnCancel();
        }

        if (id == R.id.action_delete) {
            deleteTask();
        }

        return super.onOptionsItemSelected(item);
    }

    private void btnCancel() {
        // Simply return to main activity
        this.finish();
    }

    private void btnSave() {
        // collect data from EditText field
        task.setTitle(etTitle.getText().toString());
        if(swPriority.isChecked()) {
            task.setPriority(Task.HIGH_PRIORITY);
        }
        else {
            task.setPriority(Task.LOW_PRIORITY);
        }

        // if task is not empty, pass task back to main activity
        if(!task.getTitle().isEmpty()) {
            Intent result = new Intent();
            result.putExtra("Task", task);
            setResult(RESULT_OK, result);
        }

        // return to main activity
        this.finish();
    }

    private void btnDelete() {
        Intent result = new Intent();
        result.putExtra("REQUEST_DELETEITEM", true);
        setResult(RESULT_OK, result);
        this.finish();
    }

    private void deleteTask() {
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
                        btnDelete();
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}

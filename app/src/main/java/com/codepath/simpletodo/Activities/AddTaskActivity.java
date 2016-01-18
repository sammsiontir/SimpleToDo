package com.codepath.simpletodo.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

    public void btnCancel(View view) {
        // Simply return to main activity
        this.finish();
    }

    public void btnSave(View view) {
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
}

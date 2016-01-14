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

    private Calendar dueDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Display sample due date
        dueDate = Calendar.getInstance();
        displayDueDate();
    }

    public void btnCancel(View view) {
        // Simply return to main activity
        this.finish();
    }

    public void btnSave(View view) {
        Task newTask = new Task();
        // collect data from EditText field
        newTask.setTitle(((EditText) findViewById(R.id.etTitle)).getText().toString());
        newTask.setDueDate(dueDate);
        Switch swPriority = (Switch) findViewById(R.id.swPriority);
        if(swPriority.isChecked()) {
            newTask.setPriority(Task.HIGH_PRIORITY);
        }
        else {
            newTask.setPriority(Task.LOW_PRIORITY);
        }

        // if newTask is not empty, pass task back to main activity
        if(!newTask.getTitle().isEmpty()) {
            Intent result = new Intent();
            result.putExtra("newTask", newTask);
            setResult(RESULT_OK, result);
        }

        // return to main activity
        this.finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        dueDate.set(Calendar.YEAR, year);
        dueDate.set(Calendar.MONTH, monthOfYear);
        dueDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        displayDueDate();
    }

    public void displayDueDate() {
        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        tvDate.setText(format.format(dueDate.getTime()));
    }

    public void editDate(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

}

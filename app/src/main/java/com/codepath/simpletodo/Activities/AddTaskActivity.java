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

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.data.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        Calendar today = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        tvDate.setText(format.format(today.getTime()));
    }

    public void btnCancel(View view) {
        // Simply return to main activity
        this.finish();
    }

    public void btnSave(View view) {
        Task newTask = new Task();
        // collect data from EditText field
        newTask.setTitle(((EditText) findViewById(R.id.etTitle)).getText().toString());

        // FIXME: use fragment to edit date
        /*
        DatePicker dpDueDate = (DatePicker) findViewById(R.id.dpDueDate);
        newTask.getDueDate().set(Calendar.DATE, dpDueDate.getDayOfMonth());
        newTask.getDueDate().set(Calendar.MONTH, dpDueDate.getMonth());
        newTask.getDueDate().set(Calendar.YEAR, dpDueDate.getYear());
        */

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
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    public void editDueDate(View view) {

    }
}

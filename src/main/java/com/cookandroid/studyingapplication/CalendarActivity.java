package com.cookandroid.studyingapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private RecyclerView calendarRecyclerView;
    private CalendarAdapter calendarAdapter;
    private Calendar calendar;
    private TextView year, month;
    private Button preybtn, nxtybtn, prembtn, nxtmbtn, back;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar = Calendar.getInstance();
        sharedPreferences = getSharedPreferences("Studyhistory", Context.MODE_PRIVATE);

        year = findViewById(R.id.yearTextView);
        month = findViewById(R.id.monthTextView);
        preybtn = findViewById(R.id.prevYearButton);
        nxtybtn = findViewById(R.id.nextYearButton);
        prembtn = findViewById(R.id.prevMonthButton);
        nxtmbtn = findViewById(R.id.nextMonthButton);
        back = findViewById(R.id.BackButton);

        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(this, 7));

        calendarAdapter = new CalendarAdapter(calendar, sharedPreferences);
        calendarRecyclerView.setAdapter(calendarAdapter);

        updateCalendarDisplay();

        preybtn.setOnClickListener(v -> {
            calendar.add(Calendar.YEAR, -1);
            updateCalendarDisplay();
        });

        nxtybtn.setOnClickListener(v -> {
            calendar.add(Calendar.YEAR, 1);
            updateCalendarDisplay();
        });

        prembtn.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            updateCalendarDisplay();
        });

        nxtmbtn.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            updateCalendarDisplay();
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void updateCalendarDisplay() {
        year.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        month.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        calendarAdapter.setCalendar(calendar);
    }
}

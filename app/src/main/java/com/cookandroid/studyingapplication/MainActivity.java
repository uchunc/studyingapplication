package com.cookandroid.studyingapplication;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView timerTextView;
    private Button start, stop, recordbtn, resetbtn;

    private Handler handler;
    private long startTime = 0L;
    private boolean isRunning = false;

    public static int targettime = 60; //min
    private timesettingpopup timesettingpopup;
    private SharedPreferences studyhistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        start = findViewById(R.id.startButton);
        stop = findViewById(R.id.stopButton);
        recordbtn = findViewById(R.id.viewRecordsButton);
        resetbtn = findViewById(R.id.resetButton);

        studyhistory = getSharedPreferences("Studyhistory", Context.MODE_PRIVATE);

        handler = new Handler();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });

        recordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetSharedPreferences();
            }
        });

        stop.setEnabled(false);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "목표시간 설정");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                timesettingpopup = new timesettingpopup(this,targettime);
                timesettingpopup.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startTimer() {
        if (!isRunning) {
            startTime = SystemClock.elapsedRealtime();
            handler.postDelayed(updateTimerThread, 0);
            isRunning = true;
            start.setEnabled(false);
            stop.setEnabled(true);
            timerTextView.setText("00:00:00");
        }
    }

    private void stopTimer() {
        if (isRunning) {
            handler.removeCallbacks(updateTimerThread);
            String studyingtime = timerTextView.getText().toString();
            String time[]=studyingtime.split(":");
            int totaltime = Integer.parseInt(time[0]) * 360 + Integer.parseInt(time[1]) * 60 + Integer.parseInt(time[2]);
            saveElapsedTime(totaltime);
            isRunning = false;
        }
        start.setEnabled(true);
        stop.setEnabled(false);

    }

    private boolean isPhoneLocked() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.isKeyguardLocked();
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            if (isPhoneLocked()) {
                long currentTime = SystemClock.elapsedRealtime();
                long elapsedTime = currentTime - startTime;

                int seconds = (int) (elapsedTime / 1000);
                int minutes = seconds / 60;
                int hours = minutes / 60;
                seconds = seconds % 60;
                minutes = minutes % 60;

                timerTextView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }
            handler.postDelayed(this, 1000);
        }
    };

    private void saveElapsedTime(int targettime) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        String key = year + "_" + (month + 1) + "_" + day;
        System.out.println(studyhistory.getInt(key,0));
        SharedPreferences.Editor editor = studyhistory.edit();
        if (studyhistory.getInt(key,0) == 0){
            editor.putInt(key, targettime);
            editor.apply();
        }
        else {
            int recoredtime = studyhistory.getInt(key,0);
            editor.putInt(key, recoredtime + targettime);
            editor.apply();
        }
        System.out.println(studyhistory.getInt(key,0));

    }

    private void resetSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Studyhistory", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}

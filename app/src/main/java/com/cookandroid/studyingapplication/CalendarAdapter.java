package com.cookandroid.studyingapplication;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DayViewHolder> {

    private Calendar calendar;
    private int daysinmonth;
    public int curyear;
    public int curmonth;
    public static int recordedTime;
    private final SharedPreferences sharedPreferences;

    public CalendarAdapter(Calendar calendar, SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        setCalendar(calendar);
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        curyear = calendar.get(Calendar.YEAR);
        curmonth = calendar.get(Calendar.MONTH);
        daysinmonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        System.out.println(MainActivity.targettime);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        int day = position + 1;
        holder.daytextview.setText(String.valueOf(day));

        float timerRatio = getRecordedTimeRatio(day);

        int recordedTimeForDay = sharedPreferences.getInt(getKey(day), 0);
        String toastMessage = getToastMessage(recordedTimeForDay);

        holder.itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                holder.itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setTimerBackgroundHeight(holder.timerBackgroundView, timerRatio);
            }
        });
        holder.bindToastMessage(toastMessage);
    }

    @Override
    public int getItemCount() {
        return daysinmonth;
    }

    private String getToastMessage(int recordedTime) {
        return (recordedTime / 60 / 60) + "시간 " + (recordedTime / 60) + "분 " + recordedTime % 60 + "초";
    }

    private String getKey(int day) {
        return curyear + "_" + (curmonth + 1) + "_" + day;
    }

    private float getRecordedTimeRatio(int day) {
        String key = curyear + "_" + (curmonth + 1) + "_" + day;
    recordedTime = sharedPreferences.getInt(key, 0);
        if (recordedTime != 0) {
            System.out.println(recordedTime);
        }
        return (float) recordedTime / MainActivity.targettime / 60; // 비율 계산
    }

    private void setTimerBackgroundHeight(View view, float ratio) {
        if (ratio != 0) {
            System.out.println(ratio);
        }

        int parentHeight = ((View) view.getParent()).getHeight() ;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (ratio < 1) {
            layoutParams.height = (int) (parentHeight * ratio);

        }
        else {
            layoutParams.height = parentHeight;
        }
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(getBackgroundColorBasedOnRatio(ratio));
    }

    private int getBackgroundColorBasedOnRatio(float ratio) {
        if (ratio == 0) {
            return Color.TRANSPARENT;
        } else if (ratio > 0.75) {
            return 0xFF0000FF;
        } else if (ratio > 0.5) {
            return 0xFF0077FF;
        } else if (ratio > 0.25) {
            return 0xFF00BBFF;
        } else {
            return 0xFF00FFFF;
        }
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView daytextview;
        View timerBackgroundView;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            daytextview = itemView.findViewById(R.id.dayTextView);
            timerBackgroundView = itemView.findViewById(R.id.timerBackgroundView);
        }

        public void bindToastMessage(String toastMessage) {
            daytextview.setOnClickListener(v -> Toast.makeText(v.getContext(), toastMessage, Toast.LENGTH_SHORT).show());
        }
    }
}

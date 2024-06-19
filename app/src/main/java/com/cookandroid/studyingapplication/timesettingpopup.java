package com.cookandroid.studyingapplication;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

public class timesettingpopup extends Dialog {
    private NumberPicker hourp, minp;
    private Button closebtn, applybtn;
    private int curtargeth, curtargetm;

    public timesettingpopup(@NonNull Context context, int contents) {
        super(context);
        setContentView(R.layout.timepickerpopup);
        curtargeth = contents/60;
        curtargetm = contents%60;
        hourp = findViewById(R.id.np1);
        minp = findViewById(R.id.np2);
        closebtn = findViewById(R.id.clsbtn);
        applybtn = findViewById(R.id.setbtn);

        hourp.setMinValue(0);
        hourp.setMaxValue(23);
        minp.setMinValue(0);
        minp.setMaxValue(59);

        hourp.setValue(curtargeth);
        minp.setValue(curtargetm);

        applybtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.targettime = hourp.getValue()*60+minp.getValue();
                System.out.println(MainActivity.targettime);
                dismiss();
            }
        });

        closebtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        }));
    }
}
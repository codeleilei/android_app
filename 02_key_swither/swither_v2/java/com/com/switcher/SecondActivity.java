package com.com.switcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.hardware.Control.Switcher_manager;


public class SecondActivity extends Activity {
    private TextView[] tv = new TextView[13];
    private CheckBox[] cb = new CheckBox[13];
    public Switcher_manager swirch=new Switcher_manager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hardwarekey);
        init_view();
        check_event();
    }

    public void init_view() {
        int[] tvid = {R.id.textView, R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView5, R.id.textView6,
                R.id.textView7, R.id.textView8, R.id.textView9, R.id.textView10, R.id.textView11, R.id.textView12};

        int[] cbid = {R.id.checkBox1, R.id.checkBox2, R.id.checkBox3, R.id.checkBox4, R.id.checkBox5, R.id.checkBox6,
                R.id.checkBox7, R.id.checkBox8, R.id.checkBox9, R.id.checkBox10, R.id.checkBox11, R.id.checkBox12};
        for (int i = 0; i < 12; i++) {
            tv[i] = (TextView) findViewById(tvid[i]);
            cb[i] = (CheckBox) findViewById(cbid[i]);
        }


    }

    public void check_event() {
        int i, y;
        for (i = 0; i < 12; i++) {
            y = i % 2;
            final int m=i;
            if (y == 0) {
                cb[i].setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                            cb[m+1].setChecked(false);
                            tv[m/2+6].setText("S"+(m+2)/2+":H");
                        try {
                            swirch.set_Mode_value(m/2,true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "修改失败需要root权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                cb[i].setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                        cb[m-1].setChecked(false);
                        tv[m/2+6].setText("S"+((m+2)/2+":L"));
                        try {
                            swirch.set_Mode_value(m/2,false);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "修改失败需要root权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }

    }


}

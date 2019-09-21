package com.com.switcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.hardware.Control.Switcher_manager;


public class PowerActivity extends Activity {
    private TextView tv1,tv2=null;
    private Button bt1,bt2=null;
    public Switcher_manager power=new Switcher_manager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.power);
        view_init();
    }

    public void view_init(){
        bt1 = (Button) findViewById(R.id.button);
        bt2 = (Button) findViewById(R.id.button2);

        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
    }

    public void button_event(){
        bt1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                tv1.setText("Vol1:  "+ power.getMain_Pwr() + ",Vol2:  " + power.getEx12v_AD());
            }
        });

        bt2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                tv1.setText("Vol1:  "+ power.getMain_Pwr() + ",Vol2:  " + power.getEx12v_AD()+ ",Lum:" +power.getLum());
            }
        });


    }

}

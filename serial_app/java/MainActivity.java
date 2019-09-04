package com.example.serialtool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button bt1,bt2,bt3,bt4= null;
    private CheckBox cb1,cb2,cb3,cb4= null;
    private TextView tv1= null;
    private EditText ed1,ed2=null;
    private serial_util serial;
    public String path = "/dev/ttyXRM0";
    public int baudrate = 115200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*init*/
        view_init();
        serial = new serial_util();

        /*add listener*/
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setText("");
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText("");
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = ed1.getText().toString();
                try {
                    serial.SendDate(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bt4.getText().equals("打开串口")) {
                    if(!ed2.getText().toString().equals(null))
                        //path = ed2.getText().toString();
                        ;
                    try {
                        serial.OpenSerialport(path, baudrate);
                        bt4.setText("关闭串口");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    serial.closeSerialPort();
                    bt4.setText("打开串口");
                }
            }
        });

    }

    public void view_init() {
        /*button_init*/
        bt1 = (Button) findViewById(R.id.button1);
        bt2 = (Button) findViewById(R.id.button2);
        bt3 = (Button) findViewById(R.id.button3);
        bt4 = (Button) findViewById(R.id.button4);

        /*checkbox init*/
        cb1 = (CheckBox) findViewById(R.id.checkBox1);
        cb2 = (CheckBox) findViewById(R.id.checkBox2);
        cb3 = (CheckBox) findViewById(R.id.checkBox3);
        cb4 = (CheckBox) findViewById(R.id.checkBox4);

        /*view init*/
        tv1 = (TextView) findViewById(R.id.textView1);

        ed1 = (EditText) findViewById(R.id.editText);
        ed2 = (EditText) findViewById(R.id.editText2);
    }


}

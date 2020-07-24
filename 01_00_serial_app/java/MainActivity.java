package com.example.serialtool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static android.os.SystemClock.sleep;

public class MainActivity extends AppCompatActivity {
    private Button bt1,bt2,bt3,bt4= null;
    private CheckBox cb1,cb2,cb3,cb4= null;
    private TextView tv1= null;
    private EditText ed1,ed2=null;
    private serial_util serial;
    private boolean gbk,gbk2=false;
    public String path ;
    public int baudrate = 115200;

    /** 信号处理 **/
    public  Handler myHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {   //通过消息句柄 可以实现循环搜索  当一轮搜索完成以后会到这个地方
                switch (msg.what) {
                    case 0x01:
                        tv1.setText(code_change(serial.buffer_show));
                        break;
                    case 0x02:
                        serial.buffer_show="";
                        break;

                        default:
                            tv1.setText("");
                }
            }
    };
    /**主执行函数**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*init*/
        serial = new serial_util(myHandler);
        view_init();

        showSoftInputFromWindow(this,ed2);
        showSoftInputFromWindow(this,ed1);

        button_event();
        check_event();

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
    /**  EditText获取焦点并显示软键盘 **/
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /** UTF-8 转 GBK编码 to String*/
    public String code_change(String code){

        if(!gbk)
            return code;
        else {
            try {
                String gbk_code = new String(code.getBytes(),"GBK");
                return gbk_code;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /** UTF-8 转 GBK编码 to byte[]*/
    public byte[] code2byte(String code){
        byte[] sendData  = code.getBytes();
        try {
        if(!gbk2)
            sendData  = code.getBytes("UTF-8");
        else {
                sendData  = code.getBytes("GBK");
            }
        } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        }
        return sendData;
    }
    /**按钮监听事件**/
    public void button_event(){
        /*add listener*/
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();// 消息
                msg.what = 0x02;// 消息类别
                myHandler.sendMessage(msg);
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
                String data = ed1.getText().toString() + "\r\n";
                try {
                    serial.SendDate( code2byte(data) );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean state=false;
                if (bt4.getText().equals("打开串口")) {
                    if(!ed2.getText().toString().equals(null))
                        path = ed2.getText().toString();
                    Toast.makeText(getApplicationContext(), "正在打开串口", Toast.LENGTH_SHORT).show();
                    sleep(100);
                    bt4.setText("关闭串口");
                    try {
                        serial.OpenSerialport(path, baudrate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    serial.closeSerialPort();
                    Toast.makeText(getApplicationContext(), "正在关闭串口", Toast.LENGTH_SHORT).show();
                    sleep(100);
                    bt4.setText("打开串口");
                }
            }
        });

    }

    /**勾选框监听事件**/
    public void check_event(){
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    gbk=false;
                    cb2.setChecked(false);
                }
            }
        });

        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    gbk=true;
                    cb1.setChecked(false);
                }
            }
        });
        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    gbk2=false;
                    cb4.setChecked(false);
                }
            }
        });
        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    gbk2=true;
                    cb3.setChecked(false);
                }
            }
        });

    }
}

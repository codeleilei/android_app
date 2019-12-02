package com.c.secret_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    java.lang.Process por;
    private TextView tv=null;
    private EditText ed=null;
    private Button bt1,bt2,bt3=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view_init();
        button_event();
    }

    public boolean Write_data(String param){
        try {
            String[] cmd = new String[]{"su","-c","/system/WriteAVR"+" "+param};
            por = Runtime.getRuntime().exec(cmd);
            por.waitFor();
            if (por.exitValue() != 0) {
                System.out.println("ip link down");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "写入成功", Toast.LENGTH_SHORT).show();
        return true;
    }


    public String Read_data(){
        try {
            String[] cmd = new String[]{"su","-c","/system/ReadAVR"};
            por = Runtime.getRuntime().exec(cmd);
            por.waitFor();
            if (por.exitValue() != 0) {
                System.out.println("excute error");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String data = "";
        BufferedReader ie = new BufferedReader(new InputStreamReader(por.getErrorStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(por.getInputStream()));
        String error = null;
        try {
            while ((error = ie.readLine()) != null
                    && !error.equals("null")) {
                data += error + "\n";
            }
            String line = null;
            while ((line = in.readLine()) != null
                    && !line.equals("null")) {
                data += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.v("ls", data);
        return data;
    }

    public void view_init(){
        bt1 = (Button) findViewById(R.id.button);
        bt2 = (Button) findViewById(R.id.button2);
        bt3 = (Button) findViewById(R.id.button3);

        ed = (EditText) findViewById(R.id.editText);
        tv = (TextView) findViewById(R.id.textView);
    }
    /**基本信息*/
    public void button_event(){
        bt1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String param = ed.getText().toString();
                if(Check_putin(0,param)) {

                    Write_data(param);
                }else
                    Toast.makeText(getApplicationContext(), "请按格式输入：日期 型号 机身号 0", Toast.LENGTH_SHORT).show();
            }
        });
        /**注册号*/
        bt2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String param = ed.getText().toString();
                if(Check_putin(1,param)) {
                    Write_data(param);
                }else
                    Toast.makeText(getApplicationContext(), "请输入注册号", Toast.LENGTH_SHORT).show();


            }
        });
        /**读取加密芯片信息*/
        bt3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                tv.setText(Read_data());
            }
        });

    }
    public boolean Check_putin(int opt,String data){
        switch (opt)
        {
            case 0:
                String[] newStr = data.split(" ");
                if(newStr.length == 4)
                    return true;
                break;
            case 1:
                String[] newStr2 = data.split(" ");
                if(newStr2.length ==1)
                    return true;
                break;
             default:
                 return false;
        }
        return false;
    }
}

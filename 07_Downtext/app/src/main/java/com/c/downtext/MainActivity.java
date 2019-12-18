package com.c.downtext;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView tv,tv2=null;
    private String[] version_loc=null;
    private String[] version_online=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.textView);
        tv2 = (TextView)findViewById(R.id.textView2);
        MyThread myThread = new MyThread();
        new Thread(myThread).start();
        Readfile read =new Readfile();
        new Thread(read).start();
    }

    class MyThread implements Runnable{
        @Override
        public void run() {
            Log.i("MyThread", Thread.currentThread().getName());
            try {
                //建立连接
                URL url = new URL("http://mc.zhdbds.com:55555/iEVT/update_info.txt");
                HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
                httpUrlConn.setDoInput(true);
                httpUrlConn.setRequestMethod("GET");
                httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                //获取输入流
                InputStream input = httpUrlConn.getInputStream();
                //将字节输入流转换为字符输入流
                InputStreamReader read = new InputStreamReader(input, "utf-8");
                //为字符输入流添加缓冲
                BufferedReader br = new BufferedReader(read);
                // 读取返回结果
                String data = br.readLine();
                while (data != null ) {
                    if(data.contains("Firmware Version:"))
                        version_online = data.split(":");
                        //tv.setText(data);
                    data = br.readLine();
                }
                tv.setText(version_online[1]);
                // 释放资源
                br.close();
                read.close();
                input.close();
                httpUrlConn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    class Readfile implements Runnable{
        @Override
        public void run() {
            try {
                    File file = new File("/sdcard/update_info.txt");
                FileInputStream input = new FileInputStream(file);
                //将字节输入流转换为字符输入流
                InputStreamReader read = new InputStreamReader(input, "utf-8");
                //为字符输入流添加缓冲
                BufferedReader br = new BufferedReader(read);
                // 读取返回结果
                String data = br.readLine();
                while (data != null) {
                    if (data.contains("Firmware Version:"))
                        version_loc = data.split(":");
                    //tv.setText(data);
                    data = br.readLine();
                }
                tv2.setText(version_loc[1]);
                // 释放资源
                br.close();
                read.close();
                input.close();
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}

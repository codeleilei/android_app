package com.watch.simple_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG="cl---";
    private final String StroeFile = "/sdcard/cl.pcm";
    private Button bt,bt2,bt3,bt4,bt5;
    private AudioManager am=null;
    private AudioRecord rd=null;
    private AudioTrack at=null;
    private Thread rdthred=null;
    private Thread plthred=null;
    private boolean isread =false;

    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS_IN = AudioFormat.CHANNEL_IN_STEREO;
    private static final int RECORDER_CHANNELS_OUT = AudioFormat.CHANNEL_OUT_STEREO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    private int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS_IN, RECORDER_AUDIO_ENCODING);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        am=(AudioManager)getApplicationContext().getSystemService( Context.AUDIO_SERVICE );
        initView();
    }


    public void initView(){
        bt = (Button)findViewById( R.id.button );
        bt2 = (Button)findViewById( R.id.button2 );
        bt3 = (Button)findViewById( R.id.button3 );
        bt4 = (Button)findViewById( R.id.button4);
        bt5 = (Button)findViewById( R.id.button5);

        bt.setOnClickListener( this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button:
                am.setMicrophoneMute( true );
                break;
            case R.id.button2:
                rd = new AudioRecord(AUDIO_SOURCE, RECORDER_SAMPLERATE,RECORDER_CHANNELS_IN,RECORDER_AUDIO_ENCODING ,bufferSize);
                rd.startRecording();
                rdthred = new Thread( new Runnable() {
                    @Override
                    public void run() {
                        WriteData();
                    }
                } );
                isread=true;
                rdthred.start();

                break;
            case R.id.button3:
                if(rd != null) {
                    isread = false;
                    rd.stop();
                    rd.release();
                    rd=null;
                    rdthred =null;
                }
                break;

            case R.id.button4:
                if(rd==null)
                    playaudio();
                else
                    Log.e(TAG,"正在录音 请先停止录音");
                break;

            case R.id.button5:
                stopplay();
                break;
            default:
                break;
        }
    }


    private void WriteData(){
        FileOutputStream os=null;
        byte audiobuff[]=new byte[bufferSize];
        try {
             os = new FileOutputStream( StroeFile );
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        while(isread){
            rd.read(audiobuff,0,bufferSize );

            try{
                os.write(audiobuff, 0, bufferSize);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        try{
            os.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    private void playaudio(){
        File file = new  File(StroeFile);
        final byte[] bytedata = new byte[(int)file.length()]; //数据中转站
        FileInputStream in=null;
        try{
            in =new FileInputStream( file);
            in.read(bytedata);
            in.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        int intSize = android.media.AudioTrack.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS_OUT, RECORDER_AUDIO_ENCODING);//对于audiostrck的数据缓冲区
        at=new AudioTrack(AudioManager.STREAM_MUSIC, RECORDER_SAMPLERATE,RECORDER_CHANNELS_OUT,RECORDER_AUDIO_ENCODING,intSize,AudioTrack.MODE_STREAM );
        at.play();
        plthred =new Thread( new Runnable() {
            @Override
            public void run() {
                if(at !=null){
                    at.write( bytedata,0,bytedata.length );
                    at.stop();
                    at.release();
                    at=null;
                }
            }
        } );
        plthred.start();

    }


    private void stopplay(){
        if(at!=null) {
            at.stop();
            at.release();
            at = null;
            plthred = null;
        }
    }
}

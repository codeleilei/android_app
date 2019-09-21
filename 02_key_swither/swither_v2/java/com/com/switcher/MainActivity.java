package com.com.switcher;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private Button  bt,bt1,bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view_init();
        button_event();
    }


    public void view_init(){
        bt = (Button) findViewById(R.id.button);
        bt1 = (Button) findViewById(R.id.button2);
        bt2 = (Button) findViewById(R.id.button3);
    }

    public void button_event(){
        bt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
            });

        bt1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PowerActivity.class));
            }
        });


    }
}

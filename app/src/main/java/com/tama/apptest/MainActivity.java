package com.tama.apptest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.apptest.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view){
        Intent intent  = new Intent(this, DisplayMessageActivity.class);
        EditText textBox = (EditText) findViewById(R.id.inputMessageText);
        String msg = textBox.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, msg);
        startActivity(intent);
    }

    public void reset(View view){
        Context context = getApplicationContext();
        File dir = context.getFilesDir();
        File[] content = dir.listFiles();
        for (File f : content){
            if (f.getName().equals(GameActivity.dataFile)){
                f.delete();
            }
        }
    }

    public void startGame(View view){
        startActivity(new Intent(this, GameActivity.class));
    }
}

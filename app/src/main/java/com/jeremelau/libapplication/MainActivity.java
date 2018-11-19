package com.jeremelau.libapplication;

import android.os.Bundle;
import android.widget.TextView;

import com.guoguang.flickview.FlickHelper;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.hello_world);
        FlickHelper.getFlickHelper().startFlick(textView);
    }
}

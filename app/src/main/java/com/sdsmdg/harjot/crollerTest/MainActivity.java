package com.sdsmdg.harjot.crollerTest;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    Croller croller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        croller = (Croller) findViewById(R.id.croller);

        croller.setIndicatorWidth(20);
    }
}

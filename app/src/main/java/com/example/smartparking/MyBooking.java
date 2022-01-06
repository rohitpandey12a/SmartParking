package com.example.smartparking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyBooking extends AppCompatActivity implements View.OnClickListener {
private FloatingActionButton MyBookingHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);
        MyBookingHome=findViewById(R.id.MybookingHome);
        MyBookingHome.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        if (v == MyBookingHome){
            startActivity(new Intent(this,Home.class));
        }
    }
}

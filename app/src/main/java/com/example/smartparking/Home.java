package com.example.smartparking;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private Button buttonbooking,buttonMybooking,buttonaccount,buttonroute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
	    FirebaseAuth firebaseAuth2 = FirebaseAuth.getInstance();
	    if (firebaseAuth2.getCurrentUser()==null){
		    finish();
		    startActivity(new Intent(getApplicationContext(),Login.class));
	    }
        buttonaccount=findViewById(R.id.buttonAccount);
        buttonbooking=findViewById((R.id.buttonBooking));
        buttonMybooking=findViewById(R.id.buttonMybooking);
        buttonroute=findViewById(R.id.buttonRoute);

        buttonaccount.setOnClickListener(this);
        buttonbooking.setOnClickListener(this);
        buttonMybooking.setOnClickListener(this);
        buttonroute.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            if (v==buttonaccount){
                startActivity(new Intent(this,Account.class));
            }else if (v==buttonbooking){
                startActivity(new Intent(this,Booking.class));
            }else if (v==buttonMybooking){
                startActivity(new Intent(this,MyBooking.class));
            }else if (v==buttonroute){
                startActivity(new Intent(this, Route.class));
            }
        }
        else{
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }
        
    }
}

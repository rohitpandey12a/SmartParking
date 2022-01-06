package com.example.smartparking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Route extends AppCompatActivity implements View.OnClickListener{
	
	private String ir_1;
	private String ir_2;
	private String ir_3;
	private String ir_4;
	private String ir_5;
	private String ir_6;
	private String ir_7;
	private String ir_8;
	private Button slot1,slot2,slot3,slot4, ShowSlot, ShowRoute;
	private FloatingActionButton Rhome;
	private TextView route1,route2,route3,route4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
		slot1=findViewById(R.id.slot1);
		slot2=findViewById(R.id.slot2);
		slot3=findViewById(R.id.slot3);
		slot4=findViewById(R.id.slot4);
		Rhome=findViewById(R.id.RHome);
		ShowSlot =findViewById(R.id.showslot);
		ShowRoute =findViewById(R.id.showroute);
		route1=findViewById(R.id.route1);
		route2=findViewById(R.id.loginlabel);
		route3=findViewById(R.id.route3);
		route4=findViewById(R.id.route4);
		ShowSlot.setOnClickListener(this);
		Rhome.setOnClickListener(this);
		ShowRoute.setOnClickListener(this);
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference myreference = database.getReference("sensor data");
		myreference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()) {
					ir_1 = dataSnapshot.child("ir-1").getValue(String.class);
					ir_2 = dataSnapshot.child("ir-2").getValue(String.class);
					ir_3 = dataSnapshot.child("ir-3").getValue(String.class);
					ir_4 = dataSnapshot.child("ir-4").getValue(String.class);
					ir_5 = dataSnapshot.child("ir-5").getValue(String.class);
					ir_6 = dataSnapshot.child("ir-6").getValue(String.class);
					ir_7 = dataSnapshot.child("ir-7").getValue(String.class);
					ir_8 = dataSnapshot.child("ir-8").getValue(String.class);
				}
			}
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			}
		});
		slotColor();
	}
	
	@Override
	public void onClick(View v) {
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
				connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
			if(v== ShowSlot){
				showSlot();
			}else if(v== ShowRoute){
				showRoute();
			}else if(v== Rhome){
				startActivity(new Intent(this,Home.class));
			}
		}
		else{
			Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show();
		}
	}
	
	private void slotColor() {
		slot1.setBackgroundColor(getResources().getColor(R.color.primarySlotColor));
		slot2.setBackgroundColor(getResources().getColor(R.color.primarySlotColor));
		slot3.setBackgroundColor(getResources().getColor(R.color.primarySlotColor));
		slot4.setBackgroundColor(getResources().getColor(R.color.primarySlotColor));
	}
	
	private void showRoute() {
		if(ir_5.equals("1")){
			route1.setBackgroundColor(Color.GREEN);
		}else if(ir_5.equals("0")){
			route1.setBackgroundColor(Color.RED);
		}
		if(ir_6.equals("1")){
			route2.setBackgroundColor(Color.GREEN);
		}else if(ir_6.equals("0")){
			route2.setBackgroundColor(Color.RED);
		}
		if(ir_7.equals("1")){
			route3.setBackgroundColor(Color.GREEN);
		}else if(ir_7.equals("0")){
			route3.setBackgroundColor(Color.RED);
		}
		if(ir_8.equals("1")){
			route4.setBackgroundColor(Color.GREEN);
		}else if(ir_8.equals("0")){
			route4.setBackgroundColor(Color.RED);
		}
	}
	private void showSlot() {
		if(ir_1.equals("1")){
			slot1.setBackgroundColor(Color.GREEN);
		}else if(ir_1.equals("0")){
			slot1.setBackgroundColor(Color.RED);
		}
		if(ir_2.equals("1")){
			slot2.setBackgroundColor(Color.GREEN);
		}else if(ir_2.equals("0")){
			slot2.setBackgroundColor(Color.RED);
		}
		if(ir_3.equals("1")){
			slot3.setBackgroundColor(Color.GREEN);
		}else if(ir_3.equals("0")){
			slot3.setBackgroundColor(Color.RED);
		}
		if(ir_4.equals("1")){
			slot4.setBackgroundColor(Color.GREEN);
		}else if(ir_4.equals("0")){
			slot4.setBackgroundColor(Color.RED);
		}
	}
}
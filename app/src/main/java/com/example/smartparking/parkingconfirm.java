package com.example.smartparking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class parkingconfirm extends AppCompatActivity implements View.OnClickListener {
	private Button b2;
	private Button b1;
	private FloatingActionButton PChome;
	private static String avail;
	private static String cost;
	private TextView t1;
	private TextView t2;
	private FirebaseFirestore db = FirebaseFirestore.getInstance();
	private String UserId;
	private DocumentReference dbref,dr;
	private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();;
	private static String count;
	private String st;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parkingconfirm);
		if (firebaseAuth.getCurrentUser()==null){
			finish();
			startActivity(new Intent(getApplicationContext(),Login.class));
		}else{
			UserId=firebaseAuth.getCurrentUser().getUid();
		}
		b1=findViewById(R.id.costdetail);
		b2=findViewById(R.id.Buttonbook);
		t1=findViewById(R.id.TextViewAvailbilty);
		t2=findViewById(R.id.TextViewCost);
		PChome=findViewById(R.id.PChome);
		dbref = db.collection("User").document("UserId")
				.collection(UserId).document("Details");
		dbref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
			@Override
			public void onSuccess(DocumentSnapshot documentSnapshot) {
				if (documentSnapshot.exists()){
					count =documentSnapshot.getString("Count");
					st="booking"+count;
				}
			}
		});
		b2.setOnClickListener(this);
		b1.setOnClickListener(this);
		PChome.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
				connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
			if(v==b1){
				detailDisplay();
			}else if (v==b2){
				startActivity(new Intent(this, Route.class));
			}else if (v == PChome){
				startActivity(new Intent(this,Home.class));
			}
		}
		else{
			Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show();
		}
	}
	@SuppressLint("SetTextI18n")
	private void detailDisplay() {
		detail();
		
	}
	private void detail() {
		dr = db.collection("User").document("UserId")
				.collection(UserId).document(st);
		dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onSuccess(DocumentSnapshot documentSnapshot) {
				if (documentSnapshot.exists()) {
					avail = documentSnapshot.getString("t1");
					cost = documentSnapshot.getString("t2");
					t1.setText(avail);
					t2.setText("RS "+cost);
				}
			}
		});
	}
}

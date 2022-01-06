package com.example.smartparking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.smartparking.R.id;
import static com.example.smartparking.R.layout;
@SuppressLint("SetTextI18n")
public class Account extends AppCompatActivity  implements View.OnClickListener {
	
	private static final String KEY_NAME = "NAME";
	private static final String KEY_NUMBER = "MOBILE NUMBER";
	private FirebaseAuth firebaseAuth2;
	private Button buttonLogout,buttonSave;
	private FloatingActionButton AccountHome;
	private EditText editTextName,editTextNumber;
	private TextView textViewDetails;
	private FirebaseFirestore db = FirebaseFirestore.getInstance();
	DocumentReference dref;
	String UserId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_account);
		firebaseAuth2 = FirebaseAuth.getInstance();
		if (firebaseAuth2.getCurrentUser()==null){
			finish();
			startActivity(new Intent(getApplicationContext(),Login.class));
		}
		UserId=firebaseAuth2.getCurrentUser().getUid();
		dref = db.collection("User").document("UserId")
				.collection(UserId).document("Details");
		dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
			@Override
			public void onSuccess(DocumentSnapshot documentSnapshot) {
				if (documentSnapshot.exists()) {
					String num = documentSnapshot.getString(KEY_NUMBER);
					String name = documentSnapshot.getString(KEY_NAME);
					textViewDetails.setText("\nName: "+name+"\nNumber: "+num);
				}
			}
		});
		FirebaseUser user = firebaseAuth2.getCurrentUser();
		editTextName=findViewById(id.editTextNameacc);
		editTextNumber=findViewById(id.editTextNumber);
		TextView textViewUserEmail = findViewById(id.textViewUserEmail3);
		textViewDetails = findViewById(id.textViewUserDetails);
		textViewUserEmail.setText(user.getEmail());
		buttonLogout = findViewById(id.buttonLogout1);
		buttonSave=findViewById(id.buttonAddPeople);
		AccountHome = findViewById(id.AccountHome);
		textViewUserEmail.setText("Email: "+user.getEmail());
		buttonSave.setOnClickListener(this);
		buttonLogout.setOnClickListener(this);
		AccountHome.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
				connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
			if (v==buttonLogout){
				firebaseAuth2.signOut();
				finish();
				startActivity(new Intent(getApplicationContext(),Login.class));
			}else if (v==buttonSave){
				saveUserInformation();
			}else if (v==AccountHome){
				startActivity(new Intent(this,Home.class));
			}
		}
		else{
			Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show();
		}
	}
	private void saveUserInformation(){
		String name = editTextName.getText().toString().trim();
		String number = editTextNumber.getText().toString().trim();
		Map<String, Object> note = new HashMap<>();
		note.put(KEY_NAME, name);
		note.put(KEY_NUMBER, number);
		dref.update(note)
				.addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						Toast.makeText(Account.this,"Info Saved...",Toast.LENGTH_LONG).show();
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Toast.makeText(Account.this,"Info not Saved...",Toast.LENGTH_LONG).show();

					}
		});
		dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
			@Override
			public void onSuccess(DocumentSnapshot documentSnapshot) {
				if (documentSnapshot.exists()) {
					String num = documentSnapshot.getString(KEY_NUMBER);
					String name = documentSnapshot.getString(KEY_NAME);
					textViewDetails.setText("\nName: "+name+"\nNumber: "+num);
				}
			}
		});
	}
}
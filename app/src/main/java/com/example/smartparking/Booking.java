package com.example.smartparking;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public class Booking extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Button b1;
	private FloatingActionButton BookHome;
    private EditText Date, Time;
    private static final String KEY_DATE = "DATE";
    private static final String KEY_TIME = "TIME";
	int slot,t2;
	private FirebaseFirestore fstore = FirebaseFirestore.getInstance();
	DocumentReference dr,dref;
	Map<String,Object> notebook = new HashMap<>();
	private String t1;
	private String text;
	private static String ir_1,ir_2,ir_3,ir_4,rtype,rs;
	private Integer t;
	String UserId;
	DatePickerDialog datePickerDialog;
	private ImageButton buttondate;
	private static Integer count;
	private static int check;
	@SuppressLint("SetTextI18n")
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
	    Spinner s1 = findViewById(R.id.spinner);
        b1=findViewById(R.id.ButtonSearch);
        Date =findViewById(R.id.editTextDate);
        Time =findViewById(R.id.editTextTime);
        buttondate=findViewById(R.id.buttondate);
        BookHome=findViewById(R.id.BookHome);
		FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();
        if (fireBaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(getApplicationContext(),Login.class));
        }
        b1.setOnClickListener(this);
        BookHome.setOnClickListener(this);
        s1.setOnItemSelectedListener(Booking.this);
        buttondate.setOnClickListener(this);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array
		        .Select,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
		fetch_data();
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		datePickerDialog = new DatePickerDialog(Booking.this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						Date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
					}
				}, mYear, mMonth, mDay);
		UserId = (fireBaseAuth.getCurrentUser()).getUid();
    }
	
	@Override
	protected void onStart() {
		super.onStart();
		dr = fstore.collection("User").document("UserId")
				.collection(UserId).document("Details");
		dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
			public void onSuccess(DocumentSnapshot documentSnapshot) {
				if (documentSnapshot.exists()) {
					count = Integer.parseInt(documentSnapshot.getString("Count"));
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		ConnectivityManager cM = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cM.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
				cM.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
			if (v==b1) {
				parkingSearch();
			}else if(v == BookHome){
				startActivity(new Intent(this,Home.class));
			}else if(v ==buttondate){
				datePickerDialog.show();
			}
		}
		else{
			Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show();
		}
	}
	

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		text = parent.getItemAtPosition(position).toString();
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		Toast.makeText(parent.getContext(),"Please Select The Type of parking Slot",Toast.
				LENGTH_SHORT).show();
	}
	private void fetch_data() {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference myref = database.getReference("sensor data");
		myref.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()) {
					ir_1 = dataSnapshot.child("ir-1").getValue(String.class);
					ir_2 = dataSnapshot.child("ir-2").getValue(String.class);
					ir_3 = dataSnapshot.child("ir-3").getValue(String.class);
					ir_4 = dataSnapshot.child("ir-4").getValue(String.class);
					rs = dataSnapshot.child("rs").getValue(String.class);
				}
			}
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			}
		});
	}
	private void parkingSearch(){
		rtype=text;
		if(isEmpty(Date.getText().toString().trim()) && isEmpty(Time.getText().toString().trim())){
			Toast.makeText(this,"Please Select date and Enter Time",Toast.LENGTH_SHORT).show();
		}else if(isEmpty(Date.getText().toString().trim())){
			Toast.makeText(this,"Please Select Date",Toast.LENGTH_SHORT).show();
		}else if (isEmpty(Time.getText().toString().trim())){
			Toast.makeText(this,"Please Enter time",Toast.LENGTH_SHORT).show();
		} else {
			String date = Date.getText().toString().trim();
			String time = Time.getText().toString().trim();
			try {
				t = Integer.valueOf(time);
				search();
				String countr = String.valueOf((count+1));
				dr = fstore.collection("User").document("UserId")
						.collection(UserId).document("Details");
				notebook.put("Count",countr);
				String booking = "booking" + countr;
				dref = fstore.collection("User").document("UserId")
								.collection(UserId).document(booking);
				Map<String,Object> note = new HashMap<>();
				note.put(KEY_DATE, date);
				note.put(KEY_TIME, time);
				note.put("TYPE",rtype);
				note.put("slot",slot);
				note.put("t1",t1);
				note.put("t2",String.valueOf(t2));
				dref.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						dr.update(notebook).addOnSuccessListener(new OnSuccessListener<Void>() {
							@Override
							public void onSuccess(Void aVoid) {startActivity(new Intent(getApplicationContext(),Home.class));
								Toast.makeText(Booking.this,"Success",Toast.LENGTH_SHORT).show();
								startActivity(new Intent(Booking.this,parkingconfirm.class));
							}
						});
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						//Toast.makeText(Booking.this,"Info not Saved...",Toast.LENGTH_LONG).show();
					}
				});
			}catch (Exception e){
				Toast.makeText(Booking.this,"Info not Saved... Try Again",Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void search() {
		if (rs.equals("1")){
			if (ir_1.equals("1")) {
				t1 = "Shaded Slot 1 Available";
				slot = 1;
				t2 = t * 200;
			} else if (ir_2.equals("1")) {
				t1 = "Shaded Slot 2 is Available";
				slot = 2;
				t2 = t * 200;
			} else if(ir_1.equals("0") && ir_2.equals("0")) {
				if (ir_3.equals("1")) {
					t1 = "Unshaded Slot 1 Available";
					slot = 3;
					t2 = t * 100;
				} else if (ir_4.equals("1")) {
					t1 = "Unshaded Slot 2 is Available";
					slot = 4;
					t2 = t * 100;
				} else if (ir_3.equals("0") && ir_4.equals("0")) {
					t1 = "parking in not available";
					slot = 10;
					t2 = 0;
				}
			}
		}else if(rs.equals("0")){
			
			switch (rtype) {
				case "Shaded":
					//Toast.makeText(Booking.this,"searching...",Toast.LENGTH_LONG).show();
					if (ir_1.equals("1")) {
						t1 = "Shaded Slot 1 Available";
						slot = 1;
						t2 = t * 200;
					} else if (ir_2.equals("1")) {
						t1 = "Shaded Slot 2 is Available";
						slot = 2;
						t2 = t * 200;
					} else if(ir_1.equals("0") && ir_2.equals("0")){
						t1 = "shaded parking in not available";
						slot = 0;
						t2 = 0;
					}
					break;
				case "UnShaded":
					//Toast.makeText(Booking.this,"searching...",Toast.LENGTH_LONG).show();
					if (ir_3.equals("1")) {
						t1 = "Unshaded Slot 1 Available";
						slot = 3;
						t2 = t * 100;
					} else if (ir_4.equals("1")) {
						t1 = "Unshaded Slot 2 is Available";
						slot = 4;
						t2 = t * 100;
					} else if(ir_3.equals("0") && ir_4.equals("0")){
						t1 = "parking in not available";
						slot = 10;
						t2 = 0;
					}break;
				default: Toast.makeText(Booking.this,"search incomplete",Toast.LENGTH_SHORT).show();
			}
		}
	}
}

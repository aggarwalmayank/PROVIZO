package com.appsaga.provizo;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyTempooBookings extends AppCompatActivity {

    RecyclerView bookingsList;
    ProgressDialog progressDialog;
    TextView nobooking;
    DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tempoo_bookings);
        progressDialog = ProgressDialog.show(MyTempooBookings.this, "Loading", "Please Wait...", true);
        mref = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Bookings").child("TempooBooking");
        android.support.v7.widget.Toolbar toolbar = (
                android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#bec1c2"));
        setSupportActionBar(toolbar);
        nobooking = findViewById(R.id.nobook);
        toolbar.setNavigationIcon(R.drawable.backicon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nobooking.setVisibility(View.VISIBLE);
        bookingsList = findViewById(R.id.bookings_list);
        reterivefromDB();

    }

    public void reterivefromDB() {
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Tempoo> tempo=new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Tempoo t= new Tempoo(ds.getValue(Tempoo.class), ds.getKey());
                    tempo.add(t);
                }
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        progressDialog.dismiss();
                    }
                }, 1800);
                if (tempo.isEmpty()) {
                    nobooking.setVisibility(View.VISIBLE);

                } else {
                    nobooking.setVisibility(View.INVISIBLE);
                    bookingsList.setHasFixedSize(true);
                    bookingsList.setLayoutManager(new LinearLayoutManager(MyTempooBookings.this));
                    bookingsList.addItemDecoration(new DividerItemDecoration(MyTempooBookings.this, LinearLayoutManager.VERTICAL));
                    TempooAdapter adapter = new TempooAdapter(tempo,MyTempooBookings.this);
                    bookingsList.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

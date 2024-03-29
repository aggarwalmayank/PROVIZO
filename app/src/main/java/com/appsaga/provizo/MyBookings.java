package com.appsaga.provizo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyBookings extends AppCompatActivity {

    DatabaseReference databaseReference;
    ListView bookingsList;
    ProgressDialog progressDialog;
    TextView nobooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        progressDialog = ProgressDialog.show(MyBookings.this, "Loading", "Please Wait...", true);
        android.support.v7.widget.Toolbar toolbar = (
                android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        nobooking = findViewById(R.id.nobook);
        toolbar.setNavigationIcon(R.drawable.go_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nobooking.setVisibility(View.VISIBLE);
        bookingsList = findViewById(R.id.bookings_list);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Bookings").child("TruckBooking");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final ArrayList<Bookings> bookings = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Bookings bookings1 = new Bookings(ds.getValue(Bookings.class), ds.getKey());
                    bookings.add(bookings1);
                }
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        progressDialog.dismiss();
                    }
                }, 1800);

                if (bookings.isEmpty()) {
                    nobooking.setVisibility(View.VISIBLE);

                } else {
                    nobooking.setVisibility(View.INVISIBLE);
                    BookingsAdapter bookingsAdapter = new BookingsAdapter(MyBookings.this, bookings);
                    bookingsList.setAdapter(bookingsAdapter);
                }
                bookingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Bookings booking = bookings.get(position);

                        Intent intent = new Intent(MyBookings.this, BookingsDetails.class);
                        intent.putExtra("booking", booking);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
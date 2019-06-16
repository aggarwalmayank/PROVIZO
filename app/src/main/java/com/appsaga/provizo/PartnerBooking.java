package com.appsaga.provizo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PartnerBooking extends AppCompatActivity {
    DatabaseReference databaseReference;
    ListView bookingsList;
    String partnerid;
    TextView nobooking;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_booking);
        bookingsList = findViewById(R.id.bookings_list);
        nobooking = findViewById(R.id.nobook);
        progressDialog = ProgressDialog.show(PartnerBooking.this, "Loading", "Please Wait...", true);
        partnerid=getIntent().getStringExtra("partner id");
        android.support.v7.widget.Toolbar toolbar = (
                android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#bec1c2"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backicon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("partners")
                .child(partnerid).child("Bookings");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final ArrayList<PartnerOrders> bookings = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    PartnerOrders bookings1 = new PartnerOrders(ds.getValue(PartnerOrders.class), ds.getKey());
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
                    PartnerAdapter bookingsAdapter = new PartnerAdapter(PartnerBooking.this, bookings);
                    bookingsList.setAdapter(bookingsAdapter);

                }
                bookingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        PartnerOrders booking = bookings.get(position);

                        Intent intent = new Intent(PartnerBooking.this, PartnerBookingDetails.class);
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

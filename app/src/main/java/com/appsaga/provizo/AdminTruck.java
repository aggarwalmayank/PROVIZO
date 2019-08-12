package com.appsaga.provizo;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminTruck extends AppCompatActivity {
    RecyclerView bookingsList;
    ProgressDialog progressDialog;
    TextView nobooking;
    static String date;
    android.support.v7.widget.Toolbar toolbar;
    DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_truck);
        toolbar=findViewById(R.id.toolbar);
        date=getIntent().getStringExtra("date");
        toolbar.setTitle(date);
        progressDialog = ProgressDialog.show(AdminTruck.this, "Loading", "Please Wait...", true);
        nobooking = findViewById(R.id.nobook);
        nobooking.setVisibility(View.VISIBLE);
        bookingsList = findViewById(R.id.bookings_list);
        reterivefromDB();

    }
    public void reterivefromDB() {
        mref.child("AdminTruck").child(getIntent().getStringExtra("date"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<AdminTruckHelper> array = new ArrayList<>();

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            AdminTruckHelper t = new AdminTruckHelper(ds.getValue(AdminTruckHelper.class), ds.getKey());
                            array.add(t);

                        }
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                progressDialog.dismiss();
                            }
                        }, 1800);
                        if (array.isEmpty()) {
                            nobooking.setVisibility(View.VISIBLE);

                        } else {
                            nobooking.setVisibility(View.INVISIBLE);
                            bookingsList.setHasFixedSize(true);
                            bookingsList.setLayoutManager(new LinearLayoutManager(AdminTruck.this));
                            AdminTruckAdapter adapter = new AdminTruckAdapter(array, AdminTruck.this);
                            bookingsList.setAdapter(adapter);
                            toolbar.setTitle(getIntent().getStringExtra("date")+" #bookings: "+adapter.getItemCount());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}

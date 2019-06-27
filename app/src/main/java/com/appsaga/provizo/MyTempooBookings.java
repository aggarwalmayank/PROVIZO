package com.appsaga.provizo;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MyTempooBookings extends AppCompatActivity {

    RecyclerView bookingsList;
    ProgressDialog progressDialog;
    TextView nobooking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tempoo_bookings);
        progressDialog = ProgressDialog.show(MyTempooBookings.this, "Loading", "Please Wait...", true);

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
        bookingsList.setHasFixedSize(true);
        bookingsList.setLayoutManager(new LinearLayoutManager(this));
        bookingsList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        TempooBookingAdapter adapter= new TempooBookingAdapter();
        bookingsList.setAdapter(adapter);
    }
}

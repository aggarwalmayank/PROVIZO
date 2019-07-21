package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CompletedTempoo extends AppCompatActivity {

    ImageView home;
    String drop, pick, pickdate, amount, dim, orderid;
    DatabaseReference mref, ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        setFont();
        initl();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getIntentData();
        DateFormat df = new SimpleDateFormat("yyMMddHHmmssZ", java.util.Locale.getDefault());
        Date today = Calendar.getInstance().getTime();
        orderid = df.format(today);
        orderid = orderid.substring(0, 12);
        addtoDB();
        addtoadmindb();
        sendmail();

    }

    public void initl() {

        home = findViewById(R.id.home);
    }

    public void setFont() {
        TextView tv = findViewById(R.id.appname);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);
    }

    public void getIntentData() {
        drop = getIntent().getStringExtra("Droploc");
        pick = getIntent().getStringExtra("Pickloc");
        pickdate = getIntent().getStringExtra("PickDate");
        amount = getIntent().getStringExtra("Price");
        dim = getIntent().getStringExtra("Dimension");
    }

    public void addtoadmindb() {
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("AdminTempoo").child(pickdate).child(orderid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ref.child("AdminTempoo").child(pickdate).child(orderid).child("DropLocation").setValue(drop);
                        ref.child("AdminTempoo").child(pickdate).child(orderid).child("PickUpLocation").setValue(pick);
                        ref.child("AdminTempoo").child(pickdate).child(orderid).child("Dimension").setValue(dim);
                        ref.child("AdminTempoo").child(pickdate).child(orderid).child("EstimateAmount").setValue(amount);
                        ref.child("AdminTempoo").child(pickdate).child(orderid).child("Status").setValue("pending");
                        ref.child("AdminTempoo").child(pickdate).child(orderid).child("User").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void addtoDB() {

        mref = FirebaseDatabase.getInstance().getReference();
        mref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Bookings").child("TempooBooking")
                .child(orderid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Bookings").child("TempooBooking")
                        .child(orderid).child("DropLocation").setValue(drop);
                mref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Bookings").child("TempooBooking")
                        .child(orderid).child("PickUpLocation").setValue(pick);
                mref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Bookings").child("TempooBooking")
                        .child(orderid).child("Dimension").setValue(dim);
                mref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Bookings").child("TempooBooking")
                        .child(orderid).child("EstimateAmount").setValue(amount);
                mref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Bookings").child("TempooBooking")
                        .child(orderid).child("PickUpDate").setValue(pickdate);
                mref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Bookings").child("TempooBooking")
                        .child(orderid).child("Status").setValue("pending");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendmail() {
        SendMail sm = new SendMail(CompletedTempoo.this, FirebaseAuth.getInstance().getCurrentUser().getEmail(), "Tempoo Booking Confirmed ",
                "Dear Sir/Ma'am\n\nYour Booking of Tempoo with Booking ID: " + orderid + " of estimated amount Rs " + amount + " only is confirmed.\nPlease keep this Email for future reference\n\n\nTeam PROVIZO");
        sm.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CompletedTempoo.this,
                Bookingchoice.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}

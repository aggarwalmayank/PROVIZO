package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class completed extends AppCompatActivity {
    DatabaseReference mref;
    String number, orderid, currentuser, amount, company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        mref = FirebaseDatabase.getInstance().getReference();
        orderid = getIntent().getStringExtra("Order ID");
        currentuser = getIntent().getStringExtra("Current User");
        company = getIntent().getStringExtra("company");
        amount = getIntent().getStringExtra("amount");
        mref.child("users").child(currentuser).child("Bookings").child(orderid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mref.child("users").child(currentuser).child("Bookings").child(orderid).child("PaymentStatus").setValue("Successfull");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SendMail sm = new SendMail(completed.this, FirebaseAuth.getInstance().getCurrentUser().getEmail(), "Booking Confirmed ",
                "Dear Sir/Ma'am\n\nCongrats!!! Your Booking With "+company+" with Booking ID "+orderid+" of amount "+amount+" only is confirmed.\nPlease keep this Email for future reference");
        sm.execute();

        TextView tv = findViewById(R.id.appname);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(completed.this,
                DeliveryLocation.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}

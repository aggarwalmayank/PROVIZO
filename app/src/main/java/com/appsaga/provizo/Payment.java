package com.appsaga.provizo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Payment extends AppCompatActivity {

    RadioButton rb1, rb2;
    Button confirm;

    final int UPI_PAYMENT = 0;
    String amount, note, orderid, currentuser, company, emailandname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        amount = getIntent().getStringExtra("amount");
        orderid = getIntent().getStringExtra("Order ID");
        currentuser = getIntent().getStringExtra("Current User");
        company = getIntent().getStringExtra("company");
        note = "Order ID: " + orderid + " Customer ID: " + currentuser + " for Company: " + company;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emailandname = dataSnapshot.child("Email").getValue() + "   " + dataSnapshot.child("Name").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        confirm = findViewById(R.id.confirm);
        rb1.setEnabled(false);
        DatabaseReference mref=FirebaseDatabase.getInstance().getReference();
        mref.child("LR").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    if(ds.getValue(String.class).equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                    {
                        rb1.setEnabled(true);
                        break;
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rb1.isChecked()) {

                    Intent i = new Intent(Payment.this, completed.class);
                    i.putExtra("Order ID", orderid);
                    i.putExtra("Current User", currentuser);
                    i.putExtra("type of service", getIntent().getStringExtra("type of service"));
                    i.putExtra("pickup", getIntent().getStringExtra("pickup"));
                    i.putExtra("drop", getIntent().getStringExtra("drop"));
                    i.putExtra("date", getIntent().getStringExtra("date"));
                    i.putExtra("weight", getIntent().getStringExtra("weight"));
                    i.putExtra("Material", getIntent().getStringExtra("Material"));
                    i.putExtra("truck", getIntent().getStringExtra("truck"));
                    i.putExtra("company", getIntent().getStringExtra("company"));
                    i.putExtra("consignorname", getIntent().getStringExtra("consignorname"));
                    i.putExtra("consignoraddress", getIntent().getStringExtra("consignoraddress"));
                    i.putExtra("consignorphone", getIntent().getStringExtra("consignorphone"));
                    i.putExtra("consignor gst", getIntent().getStringExtra("consignor gst"));
                    i.putExtra("amount", amount);
                    i.putExtra("ownerrisk", getIntent().getStringExtra("ownerrisk"));
                    i.putExtra("doordelivery", getIntent().getStringExtra("doordelivery"));
                    i.putExtra("drop", getIntent().getStringExtra("drop"));
                    i.putExtra("consignee gst", getIntent().getStringExtra("consignee gst"));
                    i.putExtra("consigneename", getIntent().getStringExtra("consigneename"));
                    i.putExtra("consigneeaddress", getIntent().getStringExtra("consigneeaddress"));
                    i.putExtra("consigneephone", getIntent().getStringExtra("consigneephone"));
                    startActivity(i);
                } else if (rb2.isChecked()) {
                    // write code for razorpay;
                } else {
                    Toast.makeText(Payment.this, "Please select a payment option", Toast.LENGTH_LONG).show();
                }
            }
        });

        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (rb1.isChecked()) {
                    rb2.setChecked(false);
                } else {
                    rb2.setChecked(true);
                }
            }
        });

        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (rb2.isChecked()) {
                    rb1.setChecked(false);
                } else {
                    rb1.setChecked(true);
                }
            }
        });

                /*Intent i=new Intent(Payment.this,completed.class);

                i.putExtra("Order ID",orderid);
                i.putExtra("Current User",currentuser);
                i.putExtra("type of service",getIntent().getStringExtra("type of service"));
                i.putExtra("pickup",getIntent().getStringExtra("pickup"));
                i.putExtra("drop",getIntent().getStringExtra("drop"));
                i.putExtra("date",getIntent().getStringExtra("date"));
                i.putExtra("weight",getIntent().getStringExtra("weight"));
                i.putExtra("Material",getIntent().getStringExtra("Material"));
                i.putExtra("truck",getIntent().getStringExtra("truck"));
                i.putExtra("company",getIntent().getStringExtra("company"));
                i.putExtra("consignorname",getIntent().getStringExtra("consignorname"));
                i.putExtra("consignoraddress",getIntent().getStringExtra("consignoraddress"));
                i.putExtra("consignorphone",getIntent().getStringExtra("consignorphone"));
                i.putExtra("consignor gst",getIntent().getStringExtra("consignor gst"));
                i.putExtra("amount",amount);
                i.putExtra("ownerrisk",getIntent().getStringExtra("ownerrisk"));
                i.putExtra("doordelivery",getIntent().getStringExtra("doordelivery"));
                i.putExtra("drop",getIntent().getStringExtra("drop"));
                i.putExtra("consignee gst",getIntent().getStringExtra("consignee gst"));
                i.putExtra("consigneename",getIntent().getStringExtra("consigneename"));
                i.putExtra("consigneeaddress",getIntent().getStringExtra("consigneeaddress"));
                i.putExtra("consigneephone",getIntent().getStringExtra("consigneephone"));
                startActivity(i);*/
    }

    public static boolean isConnectionAvailable(Payment context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}
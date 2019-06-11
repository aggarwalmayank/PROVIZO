package com.appsaga.provizo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Confirmation extends AppCompatActivity {
    String pickup, droploc, pickupdate, price;
    TextView amount, pick, drop, date, gstprice, totalprice, baseprice;
    DatabaseReference myref;
    Button back, book;
    String currentuser, orderid, company, gstconsignor, gstconsignee;
    double rs;
    LinearLayout fulldetail;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        TextView tv = findViewById(R.id.appnamesigninup);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);
        myref = FirebaseDatabase.getInstance().getReference();


        amount = findViewById(R.id.amount);
        drop = findViewById(R.id.drop);
        pick = findViewById(R.id.pickup);
        date = findViewById(R.id.date);
        back = findViewById(R.id.back);
        book = findViewById(R.id.book);
        amount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ratechart, 0, 0, 0);
        fulldetail = findViewById(R.id.fulldetails);
        fulldetail.setVisibility(View.INVISIBLE);
        gstprice = findViewById(R.id.tax);
        baseprice = findViewById(R.id.baseprice);
        totalprice = findViewById(R.id.finalprice);


        price = getIntent().getStringExtra("amount");
        pickup = getIntent().getStringExtra("pickup");
        droploc = getIntent().getStringExtra("drop");
        pickupdate = getIntent().getStringExtra("date");
        orderid = getIntent().getStringExtra("Order ID");
        company = getIntent().getStringExtra("company");
        currentuser = getIntent().getStringExtra("Current User");
        gstconsignee = getIntent().getStringExtra("consignee gst");
        gstconsignor = getIntent().getStringExtra("consignee gst");
        rs = Double.parseDouble(price);
        if (gstconsignor.isEmpty() || gstconsignee.isEmpty())
            amount.setText("Rs " + price);
        else {
            if (rs > 1500) {

                amount.setText("Rs " + String.valueOf(rs + rs * 0.05));
                amount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ratechart, 0, R.drawable.arrowdown, 0);
                amount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (fulldetail.getVisibility() == View.INVISIBLE) {
                            fulldetail.setVisibility(View.VISIBLE);
                            baseprice.setText("Base Price: "+price);
                            gstprice.setText("GST(5%): "+String.valueOf(Double.parseDouble(price)*0.05));
                            totalprice.setText("Total Price: "+String.valueOf(Double.parseDouble(price)+ Double.parseDouble(price)* 0.05));
                        }
                        else if(fulldetail.getVisibility()==View.VISIBLE){
                            fulldetail.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            } else
                amount.setText("Rs " + rs);
        }


        rs = rs + rs * 0.18;


        pick.setText(pickup);
        drop.setText(droploc);
        date.setText(pickupdate);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Confirmation.this, Payment.class);
                addtofirebase();
                i.putExtra("amount", amount.getText().toString().substring(3));
                i.putExtra("Current User", currentuser);
                i.putExtra("Order ID", orderid);
                i.putExtra("company", company);
                startActivity(i);
            }
        });

    }
    public void addtofirebase(){
        myref.child("users").child(currentuser).child("Bookings").child(orderid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myref.child("users").child(currentuser).child("Bookings").child(orderid).child("amount").setValue(amount.getText().toString().substring(3));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myref.child("users").child(currentuser).child("Bookings").child(orderid).child("Consignee").removeValue();
        finish();
    }
}

package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Confirmation extends AppCompatActivity {
    String pickup, droploc, pickupdate, price;
    TextView amount, pick, drop, date;
    DatabaseReference myref;
    Button back,book;
    String currentuser,orderid,company;
    double rs;

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
        back=findViewById(R.id.back);
        book=findViewById(R.id.book);

        price = getIntent().getStringExtra("amount");
        pickup = getIntent().getStringExtra("pickup");
        droploc = getIntent().getStringExtra("drop");
        pickupdate = getIntent().getStringExtra("date");
        orderid=getIntent().getStringExtra("Order ID");
        company=getIntent().getStringExtra("company");
        currentuser=getIntent().getStringExtra("Current User");

         rs = Double.parseDouble(price);
        rs = rs + rs * 0.18;

        amount.setText("Rs " + rs);


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
                String amount=String.valueOf(rs);
                Intent i=new Intent(Confirmation.this,consignor_details.class);
                i.putExtra("amount", amount);
                i.putExtra("Current User", currentuser);
                i.putExtra("Order ID", orderid);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myref.child("users").child(currentuser).child("Bookings").child(orderid).child("TruckCompany").removeValue();
        finish();

    }
}

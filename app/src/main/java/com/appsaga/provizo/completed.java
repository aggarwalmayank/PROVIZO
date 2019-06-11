package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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


        //Toast.makeText(this, number, Toast.LENGTH_SHORT).show();
//        number=number.replaceAll("[^0-9]","");
       // Toast.makeText(this, number, Toast.LENGTH_SHORT).show();


        try {
            // Construct data
            //Toast.makeText(this, FirebaseAuth.getInstance().getCurrentUser().getUid()+"gf", Toast.LENGTH_LONG).show();
            mref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    number=dataSnapshot.child("Phone Number").getValue().toString();
                    number=number.substring(1);
                   // Toast.makeText(completed.this, number, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
         //   Toast.makeText(this, dataSnapshot.child("Phone Number").getValue().toString(), Toast.LENGTH_SHORT).show();
            String apiKey = "apikey=" + "YHI9M3C6dKE-M4GFP2WI84j5gsA4v9HZ2wCCJRvc5k";
            String message = "&message=" + "Congrats!!! Your Booking With "+company+" with Booking ID "+orderid+" of amount "+amount+" only is confirmed.";
            String sender = "&sender=" + "Provizo";
            String numbers = "&numbers=" +"918209819712";

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                //stringBuffer.append(line);
                Toast.makeText(this, line.toString(), Toast.LENGTH_SHORT).show();
            }
            rd.close();
          // Toast.makeText(this, "message sen successfully", Toast.LENGTH_SHORT).show();
            //return stringBuffer.toString();
        } catch (Exception e) {
            /*System.out.println("Error SMS "+e);
            return "Error "+e;*/
           Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mref.child("users").child(currentuser).child("Bookings").child(orderid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mref.child("users").child(currentuser).child("Bookings").child(orderid).child("PaymentStatus").setValue("Successfull");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    /*    SendMail sm = new SendMail(completed.this, FirebaseAuth.getInstance().getCurrentUser().getEmail(), "Booking Confirmed ",
                "Dear Sir/Ma'am\n\nCongrats!!! Your Booking With "+company+" with Booking ID "+orderid+" of amount "+amount+" only is confirmed.\nPlease keep this Email for future reference");
        sm.execute();*/

        TextView tv = findViewById(R.id.appname);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent intent = new Intent(completed.this,
                DeliveryLocation.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);*/
        finish();
    }
}

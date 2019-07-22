package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.StrictMode;
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

import java.util.HashMap;

public class completed extends AppCompatActivity {
    DatabaseReference mref;
    ImageView home;
    String number, orderid, currentuser, amount, company;
    String numbers;
    String partnerid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        mref = FirebaseDatabase.getInstance().getReference();
        orderid = getIntent().getStringExtra("Order ID");
        currentuser = getIntent().getStringExtra("Current User");
        company = getIntent().getStringExtra("company");
        amount = getIntent().getStringExtra("amount");
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final HashMap<String, Object> TypeOfService = new HashMap<>();
        TypeOfService.put("ServiceType", getIntent().getStringExtra("type of service"));
        TypeOfService.put("MaterialDescription", getIntent().getStringExtra("Material"));
        TypeOfService.put("Weight", getIntent().getStringExtra("weight"));
        TypeOfService.put("TruckType", getIntent().getStringExtra("truck"));

        final HashMap<String, Object> DeliveryLoc = new HashMap<>();
        DeliveryLoc.put("DropLocation", getIntent().getStringExtra("pickup"));
        DeliveryLoc.put("PickUpLocation", getIntent().getStringExtra("drop"));
        DeliveryLoc.put("PickUpDate", getIntent().getStringExtra("date"));


        final HashMap<String, Object> Consignor = new HashMap<>();
        Consignor.put("Address", getIntent().getStringExtra("consignoraddress"));
        Consignor.put("PhoneNumber", getIntent().getStringExtra("consignorphone"));
        Consignor.put("ConsignorName", getIntent().getStringExtra("consignorname"));
        Consignor.put("GST", getIntent().getStringExtra("consignor gst"));

        final HashMap<String, Object> Consignee = new HashMap<>();
        Consignee.put("Address", getIntent().getStringExtra("consigneeaddress"));
        Consignee.put("PhoneNumber", getIntent().getStringExtra("consigneephone"));
        Consignee.put("ConsigneeName", getIntent().getStringExtra("consigneename"));
        Consignee.put("GST", getIntent().getStringExtra("consignee gst"));
        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mref.child("users").child(currentuser).child("Bookings").child("TruckBooking").child(orderid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String mode;
                if(getIntent().getStringExtra("doordelivery").equals("no"))
                    mode="Godown Delivery";
                else
                    mode="Door Delivery";
                String risk;
                if(getIntent().getStringExtra("ownerrisk").equals("yes"))
                    risk="Owner's Risk";
                else
                    risk="Insured Goods";
                float rating=  3;
                mref.child("users").child(currentuser).child("Bookings").child("TruckBooking").child(orderid).child("PaymentStatus").setValue("Successfull");
                mref.child("users").child(currentuser).child("Bookings").child("TruckBooking").child(orderid).child("Consignee").setValue(Consignee);
                mref.child("users").child(currentuser).child("Bookings").child("TruckBooking").child(orderid).child("Consignor").setValue(Consignor);
                mref.child("users").child(currentuser).child("Bookings").child("TruckBooking").child(orderid).child("ServiceTruckDetails").setValue(TypeOfService);
                mref.child("users").child(currentuser).child("Bookings").child("TruckBooking").child(orderid).child("PickUpLocation").setValue(getIntent().getStringExtra("pickup"));
                mref.child("users").child(currentuser).child("Bookings").child("TruckBooking").child(orderid).child("DropLocation").setValue(getIntent().getStringExtra("drop"));
                mref.child("users").child(currentuser).child("Bookings").child("TruckBooking").child(orderid).child("PickUpDate").setValue(getIntent().getStringExtra("date"));
                mref.child("users").child(currentuser).child("Bookings").child("TruckBooking").child(orderid).child("amount").setValue(getIntent().getStringExtra("amount"));
                mref.child("users").child(currentuser).child("Bookings").child("TruckBooking").child(orderid).child("TruckCompany").setValue(company);
                mref.child("users").child(currentuser).child("Bookings").child("TruckBooking").child(orderid).child("DeliveryMode").setValue(mode);
                mref.child("users").child(currentuser).child("Bookings").child("TruckBooking").child(orderid).child("goodsRisk").setValue(risk);
                mref.child("users").child(currentuser).child("Bookings").child("TruckBooking").child(orderid).child("Rating").setValue(rating);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mref.child("PartnerID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                partnerid=dataSnapshot.child(company).getValue(String.class);

                mref.child("partners").child(partnerid).child("Bookings").child(orderid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String mode;
                        if(getIntent().getStringExtra("doordelivery").equals("no"))
                            mode="Godown Delivery";
                        else
                            mode="Door Delivery";
                        String risk;
                        if(getIntent().getStringExtra("ownerrisk").equals("yes"))
                            risk="Owner's Risk";
                        else
                            risk="Insured Goods";
                        mref.child("partners").child(partnerid).child("Bookings").child(orderid).child("Consignee").setValue(Consignee);
                        mref.child("partners").child(partnerid).child("Bookings").child(orderid).child("Consignor").setValue(Consignor);
                        mref.child("partners").child(partnerid).child("Bookings").child(orderid).child("ServiceTruckDetails").setValue(TypeOfService);
                        mref.child("partners").child(partnerid).child("Bookings").child(orderid).child("PickUpLocation").setValue(getIntent().getStringExtra("pickup"));
                        mref.child("partners").child(partnerid).child("Bookings").child(orderid).child("DropLocation").setValue(getIntent().getStringExtra("drop"));
                        mref.child("partners").child(partnerid).child("Bookings").child(orderid).child("PickUpDate").setValue(getIntent().getStringExtra("date"));
                        mref.child("partners").child(partnerid).child("Bookings").child(orderid).child("Status").setValue("Pending");
                        mref.child("partners").child(partnerid).child("Bookings").child(orderid).child("DeliveryMode").setValue(mode);
                        mref.child("partners").child(partnerid).child("Bookings").child(orderid).child("goodsRisk").setValue(risk);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SendMail sm = new SendMail(completed.this, FirebaseAuth.getInstance().getCurrentUser().getEmail().trim(), "Booking Confirmed ",
                "Dear Sir/Ma'am\n\nYour Booking With " + company + " with Booking ID: " + orderid + " of amount " + amount + " only is confirmed.\nPlease keep this Email for future reference\n\n\nTeam PROVIZO");
        sm.execute();

        TextView tv = findViewById(R.id.appname);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(completed.this,
                Bookingchoice.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}

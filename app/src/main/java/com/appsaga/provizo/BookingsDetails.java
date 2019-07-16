package com.appsaga.provizo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingsDetails extends AppCompatActivity {
    Bookings booking;
    RatingBar ratingbar;
    Button submit;
    DatabaseReference mref=FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings_details);

        ratingbar=findViewById(R.id.ratingBar);
        submit=findViewById(R.id.submit);
         booking = (Bookings) getIntent().getSerializableExtra("booking");
         ratingbar.setRating(booking.getRating());

         submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Bookings").child("TruckNooking").child(booking.getKey())
                         .addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 mref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Bookings")
                                         .child("TruckBooking").child(booking.getKey()).child("Rating").setValue(ratingbar.getRating());
                                 addtoRatings(ratingbar.getRating());

                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError databaseError) {

                             }
                         });

             }
         });





        TextView id = findViewById(R.id.order_id);
        TextView mode=findViewById(R.id.mode);
        TextView risk=findViewById(R.id.risk);
        TextView comp = findViewById(R.id.truck_comp);
        TextView pickup = findViewById(R.id.pick_loc);
        TextView drop = findViewById(R.id.drop_loc);
        TextView pickDate = findViewById(R.id.pick_date);
        TextView amount = findViewById(R.id.amount);
        TextView consigneeName = findViewById(R.id.consignee_name);
        TextView consigneeAddress = findViewById(R.id.consignee_address);
        TextView consigneePhone = findViewById(R.id.consignee_phone);
        TextView consigneeGST = findViewById(R.id.consignee_gst);
        TextView consignorName = findViewById(R.id.consignor_name);
        TextView consignorAddress = findViewById(R.id.consignor_address);
        TextView consignorPhone = findViewById(R.id.consignor_phone);
        TextView consignorGST = findViewById(R.id.consignor_gst);
        TextView serviceType = findViewById(R.id.service_type);
        TextView description = findViewById(R.id.desc);
        TextView weight = findViewById(R.id.weight);
        TextView truckType = findViewById(R.id.truck_type);

        id.setText("Booking ID: " + booking.getKey());
        comp.setText("Truck Company: " + booking.getTruckCompany());
        pickup.setText("Pick Up Location: " + booking.getPickUpLocation());
        drop.setText("Drop Location: " + booking.getDropLocation());
        pickDate.setText("Pick Up Date: " + booking.getPickUpDate());
        amount.setText("Amount: Rs " + booking.getAmount());
        consigneeName.setText("Name: " + booking.getConsignee().get("ConsigneeName"));
        consigneeAddress.setText("Address: " + booking.getConsignee().get("Address"));
        consigneeGST.setText("GST: " + booking.getConsignee().get("GST"));
        consigneePhone.setText("Phone Number: " + booking.getConsignee().get("PhoneNumber"));
        consignorName.setText("Name: " + booking.getConsignor().get("ConsignorName"));
        consignorAddress.setText("Address: " + booking.getConsignor().get("Address"));
        consignorGST.setText("GST: " + booking.getConsignor().get("GST"));
        consignorPhone.setText("Phone Number: " + booking.getConsignor().get("PhoneNumber"));
        serviceType.setText("Service Type: "+booking.getServiceTruckDetails().get("ServiceType"));
        description.setText("Description: "+booking.getServiceTruckDetails().get("MaterialDescription"));
        weight.setText("Weight: "+booking.getServiceTruckDetails().get("Weight"));
        truckType.setText("Truck Type: "+booking.getServiceTruckDetails().get("TruckType"));
        mode.setText("Delivery Mode: "+booking.getDeliveryMode());
        risk.setText("Goods Risk: "+booking.getGoodsRisk());
    }
    public void addtoRatings(final float a)
    {
        mref.child("Ratings").child(booking.getTruckCompany().replaceAll(" ","")).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mref.child("Ratings").child(booking.getTruckCompany().replaceAll(" ","")).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(a);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}

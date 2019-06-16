package com.appsaga.provizo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PartnerBookingDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_booking_details);

        PartnerOrders booking = (PartnerOrders) getIntent().getSerializableExtra("booking");

        TextView id = findViewById(R.id.order_id);
        TextView pickup = findViewById(R.id.pick_loc);
        TextView drop = findViewById(R.id.drop_loc);
        TextView pickDate = findViewById(R.id.pick_date);
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
        pickup.setText("Pick Up Location: " + booking.getPickUpLocation());
        drop.setText("Drop Location: " + booking.getDropLocation());
        pickDate.setText("Pick Up Date: " + booking.getPickUpDate());
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

    }
}

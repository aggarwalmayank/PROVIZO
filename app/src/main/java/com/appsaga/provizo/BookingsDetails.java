package com.appsaga.provizo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BookingsDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings_details);

        Bookings booking = (Bookings) getIntent().getSerializableExtra("booking");

        TextView id = findViewById(R.id.order_id);
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

        id.setText(booking.getKey());
        comp.setText(booking.getTruckCompany());
        pickup.setText(booking.getPickUpDate());
        drop.setText(booking.getDropLocation());
        pickDate.setText(booking.getPickUpDate());
        amount.setText(booking.getAmount());
        consigneeName.setText(booking.getConsignee().get("ConsigneeName"));
        consigneeAddress.setText(booking.getConsignee().get("Address"));
        consigneeGST.setText(booking.getConsignee().get("GST"));
        consigneePhone.setText(booking.getConsignee().get("PhoneNumber"));
        consignorName.setText(booking.getConsignor().get("ConsignorName"));
        consignorAddress.setText(booking.getConsignor().get("Address"));
        consignorGST.setText(booking.getConsignor().get("GST"));
        consignorPhone.setText(booking.getConsignor().get("PhoneNumber"));
    }
}

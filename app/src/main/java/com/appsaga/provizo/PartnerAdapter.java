package com.appsaga.provizo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PartnerAdapter extends ArrayAdapter<PartnerOrders> {


    public PartnerAdapter(Context context, ArrayList<PartnerOrders> bookings) {
        super(context, 0, bookings);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View bookingsView = convertView;

        if (bookingsView == null) {
            bookingsView = LayoutInflater.from(getContext()).inflate(R.layout.partnerbooking_view, parent, false);
        }

        final PartnerOrders currentBooking = getItem(position);

        TextView key = bookingsView.findViewById(R.id.orderid);
        TextView pickupLoc = bookingsView.findViewById(R.id.pick_loc);
        TextView dropLoc = bookingsView.findViewById(R.id.drop_loc);
        TextView pickDate = bookingsView.findViewById(R.id.pick_date);
        final TextView status = bookingsView.findViewById(R.id.status);

        key.setText("Booking ID: " + currentBooking.getKey());
        pickupLoc.setText("Pick Up Location:\n" + currentBooking.getPickUpLocation());
        dropLoc.setText("Drop Location:\n" + currentBooking.getDropLocation());
        pickDate.setText("Pick Up Date:\n" + currentBooking.getPickUpDate());
        if (currentBooking.getStatus().equals("Pending"))
            status.setTextColor(Color.parseColor("#FF0000"));
        else if (currentBooking.getStatus().equals("Delivered"))
            status.setTextColor(Color.parseColor("#008000"));
        status.setText(currentBooking.getStatus());


        return bookingsView;
    }
}

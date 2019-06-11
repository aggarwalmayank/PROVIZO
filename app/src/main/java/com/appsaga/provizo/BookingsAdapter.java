package com.appsaga.provizo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookingsAdapter extends ArrayAdapter<Bookings> {

    public BookingsAdapter(Context context, ArrayList<Bookings> bookings) {
        super(context, 0,bookings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View bookingsView = convertView;

        if(bookingsView == null)
        {
            bookingsView = LayoutInflater.from(getContext()).inflate(R.layout.bookings_view,parent,false);
        }

        Bookings currentBooking = getItem(position);

        TextView companyName = bookingsView.findViewById(R.id.company);
        TextView pickupLoc = bookingsView.findViewById(R.id.pick_loc);
        TextView dropLoc = bookingsView.findViewById(R.id.drop_loc);
        TextView pickDate = bookingsView.findViewById(R.id.pick_date);
        TextView amount = bookingsView.findViewById(R.id.amount);

        companyName.setText("Truck Company: "+currentBooking.getTruckCompany());
        pickupLoc.setText("Pick Up Location\n"+currentBooking.getPickUpLocation());
        dropLoc.setText("Drop Up Location\n"+currentBooking.getDropLocation());
        pickDate.setText("Pick Up Date\n"+currentBooking.getPickUpDate());
        amount.setText("Amount\n"+currentBooking.getAmount());

        return bookingsView;
    }
}

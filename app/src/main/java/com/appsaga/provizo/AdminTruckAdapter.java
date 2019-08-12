package com.appsaga.provizo;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminTruckAdapter extends RecyclerView.Adapter<AdminTruckAdapter.SearchViewHolder> {

    Context context;
    private ArrayList<AdminTruckHelper> list;
    DatabaseReference mref = FirebaseDatabase.getInstance().getReference(), ref = FirebaseDatabase.getInstance().getReference();

    public AdminTruckAdapter(ArrayList<AdminTruckHelper> list, Context context) {
        this.list = list;
        this.context = context;
    }

    AdminTruckAdapter() {

    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView bookingid, amount, pick, drop, date;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            bookingid= (TextView) itemView.findViewById(R.id.orderid);
            drop = (TextView) itemView.findViewById(R.id.drop_loc);
            amount = (TextView) itemView.findViewById(R.id.amount);
            date = (TextView) itemView.findViewById(R.id.pick_date);
            pick = (TextView) itemView.findViewById(R.id.pick_loc);

        }
    }

    @NonNull
    @Override
    public AdminTruckAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bookings_view, viewGroup, false);

        return new AdminTruckAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdminTruckAdapter.SearchViewHolder searchViewHolder, final int i) {
        searchViewHolder.bookingid.setText(list.get(i).getKey());
        searchViewHolder.drop.setText(list.get(i).getDropLocation());
        searchViewHolder.pick.setText(list.get(i).getPickUpLocation());
        searchViewHolder.date.setText(list.get(i).getPickUpDate());
        searchViewHolder.amount.setText(list.get(i).getAmount());

        final AdminTruckHelper ath=list.get(i);
        searchViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,AdminTruckDetails.class);
                i.putExtra("ath",ath);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();

    }
}

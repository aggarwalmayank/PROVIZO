package com.appsaga.provizo;


import android.content.Context;
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

public class AdminTempooAdapter extends RecyclerView.Adapter<AdminTempooAdapter.SearchViewHolder> {

    Context context;
    private ArrayList<AdminTempooHelper> list;
    DatabaseReference mref = FirebaseDatabase.getInstance().getReference(), ref = FirebaseDatabase.getInstance().getReference();

    public AdminTempooAdapter(ArrayList<AdminTempooHelper> list, Context context) {
        this.list = list;
        this.context = context;
    }

    AdminTempooAdapter() {

    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView drop, amount, order, dim, pick, id, status;
        CheckBox c;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.userid);
            drop = (TextView) itemView.findViewById(R.id.drop_loc);
            amount = (TextView) itemView.findViewById(R.id.amount);
            dim = (TextView) itemView.findViewById(R.id.Dimension);
            pick = (TextView) itemView.findViewById(R.id.pick_loc);
            order = (TextView) itemView.findViewById(R.id.orderid);
            status = (TextView) itemView.findViewById(R.id.status);
            c = itemView.findViewById(R.id.chkbox);
        }
    }

    @NonNull
    @Override
    public AdminTempooAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.admintempoobookinglist, viewGroup, false);

        return new AdminTempooAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdminTempooAdapter.SearchViewHolder searchViewHolder, final int i) {
        searchViewHolder.id.setText("User ID: " + list.get(i).getUser());
        searchViewHolder.drop.setText("Drop Location:\n" + list.get(i).getDropLocation());
        searchViewHolder.pick.setText("Pick Up Location:\n" + list.get(i).getPickUpLocation());
        searchViewHolder.order.setText("Order ID:\n" + (list.get(i).getKey()));
        searchViewHolder.amount.setText("Estimate Amount:\n" + (list.get(i).getEstimateAmount()));
        searchViewHolder.dim.setText("Dimension(L-B-H): " + (list.get(i).getDimension()));
        if (list.get(i).getStatus().equals("pending")) {
            searchViewHolder.status.setTextColor(Color.parseColor("#FF0000"));
            searchViewHolder.c.setChecked(false);
        } else {
            searchViewHolder.status.setTextColor(Color.parseColor("#008000"));
            searchViewHolder.c.setChecked(true);
        }
        searchViewHolder.status.setText((list.get(i).getStatus()));
        searchViewHolder.c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchViewHolder.c.isChecked()) {
                    searchViewHolder.status.setTextColor(Color.parseColor("#008000"));
                    searchViewHolder.status.setText("confirm");
                    mref.child("AdminTempoo").child(AdminTempoo.date).child(list.get(i).getKey())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mref.child("AdminTempoo").child(AdminTempoo.date).child(list.get(i).getKey()).child("Status").setValue("Confirm");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                    ref.child("users").child(list.get(i).getUser()).child("Bookings").child("TempooBooking").child(list.get(i).getKey())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ref.child("users").child(list.get(i).getUser()).child("Bookings").child("TempooBooking").child(list.get(i).getKey()).child("Status").setValue("Confirm");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                } else {
                    searchViewHolder.status.setTextColor(Color.parseColor("#FF0000"));
                    searchViewHolder.status.setText("pending");
                    mref.child("AdminTempoo").child(AdminTempoo.date).child(list.get(i).getKey())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mref.child("AdminTempoo").child(AdminTempoo.date).child(list.get(i).getKey()).child("Status").setValue("pending");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                    ref.child("users").child(list.get(i).getUser()).child("Bookings").child("TempooBooking").child(list.get(i).getKey())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ref.child("users").child(list.get(i).getUser()).child("Bookings").child("TempooBooking").child(list.get(i).getKey()).child("Status").setValue("pending");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();

    }
}

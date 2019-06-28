package com.appsaga.provizo;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TempooAdapter extends RecyclerView.Adapter<TempooAdapter.SearchViewHolder> {

    Context context;
    DatabaseCard helper;
    ArrayList<Tempoo> list;

    public TempooAdapter(ArrayList<Tempoo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    TempooAdapter() {

    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView drop, amount, date, dim, pick, id;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.orderid);
            drop = (TextView) itemView.findViewById(R.id.drop_loc);
            amount = (TextView) itemView.findViewById(R.id.amount);
            dim = (TextView) itemView.findViewById(R.id.Dimension);
            pick = (TextView) itemView.findViewById(R.id.pick_loc);
            date = (TextView) itemView.findViewById(R.id.pick_date);

        }
    }

    @NonNull
    @Override
    public TempooAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tempobookinglist, viewGroup, false);

        return new TempooAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TempooAdapter.SearchViewHolder searchViewHolder, int i) {
        final int p = i;
        searchViewHolder.id.setText("Booking ID: "+list.get(i).getKey());
        searchViewHolder.drop.setText("Drop Location: "+list.get(i).getDropLocation());
        searchViewHolder.pick.setText("Pick Up Location: "+list.get(i).getPickUpLocation());
        searchViewHolder.date.setText("Pick Up Date: "+(list.get(i).getPickUpDate()));
        searchViewHolder.amount.setText("Estimate Amount: RS"+ (list.get(i).getEstimateAmount()));
        searchViewHolder.dim.setText("Dimendion(L-B-H): "+(list.get(i).getDimension()));


    }

    @Override
    public int getItemCount() {
        return 0;

    }
}

package com.appsaga.provizo;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TempooAdapter extends RecyclerView.Adapter<TempooAdapter.SearchViewHolder> {

    Context context;
    private ArrayList<Tempoo> list;

    public TempooAdapter(ArrayList<Tempoo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    TempooAdapter() {

    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView drop, amount, date, dim, pick, id,status;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.orderid);
            drop = (TextView) itemView.findViewById(R.id.drop_loc);
            amount = (TextView) itemView.findViewById(R.id.amount);
            dim = (TextView) itemView.findViewById(R.id.Dimension);
            pick = (TextView) itemView.findViewById(R.id.pick_loc);
            date = (TextView) itemView.findViewById(R.id.pick_date);
            status=(TextView)itemView.findViewById(R.id.status);
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
        searchViewHolder.id.setText("Booking ID: "+list.get(i).getKey());
        searchViewHolder.drop.setText("Drop Location:\n"+list.get(i).getDropLocation());
        searchViewHolder.pick.setText("Pick Up Location:\n"+list.get(i).getPickUpLocation());
        searchViewHolder.date.setText("Pick Up Date:\n"+(list.get(i).getPickUpDate()));
        searchViewHolder.amount.setText("Estimate Amount:\n"+ (list.get(i).getEstimateAmount()));
        searchViewHolder.dim.setText("Dimension(L-B-H): "+(list.get(i).getDimension()));
        if(list.get(i).getStatus().equals("pending"))
            searchViewHolder.status.setTextColor(Color.parseColor("#FF0000"));
        else
            searchViewHolder.status.setTextColor(Color.parseColor("#008000"));
        searchViewHolder.status.setText((list.get(i).getStatus()));


    }

    @Override
    public int getItemCount() {
        return list.size();

    }
}

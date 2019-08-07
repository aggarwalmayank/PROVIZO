package com.appsaga.provizo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.SearchViewHolder> {
    Context context;
    ArrayList<Rating> rate;
    public ReviewAdapter(ArrayList<Rating> rate, Context context) {

        this.rate= rate;

    }

    ReviewAdapter() {

    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView star,text;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
        star= (TextView) itemView.findViewById(R.id.star);
            text= (TextView) itemView.findViewById(R.id.text);
        }
    }

    @NonNull
    @Override
    public ReviewAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.reviewlayout, viewGroup, false);

        return new ReviewAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.SearchViewHolder searchViewHolder, int i) {
        final int p = i;
        searchViewHolder.star.setText(rate.get(i).getStar()+"\n \u2605");
        searchViewHolder.text.setText(rate.get(i).getReview());

    }

    @Override
    public int getItemCount() {
        return rate.size();

    }
}
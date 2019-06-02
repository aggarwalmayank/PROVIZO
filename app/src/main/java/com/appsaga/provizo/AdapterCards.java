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

public class AdapterCards extends RecyclerView.Adapter<AdapterCards.SearchViewHolder> {
    ArrayList<String> Cvv;
    ArrayList<String> CardNumber;
    ArrayList<String> Exp;
    ArrayList<String> Holder;
    Context context;
    DatabaseCard helper;

    public AdapterCards(ArrayList<String> CardNumber, ArrayList<String> Holder, ArrayList<String> Exp, ArrayList<String> Cvv, Context context) {

        this.Exp = Exp;
        this.CardNumber = CardNumber;
        this.Cvv = Cvv;
        this.Holder = Holder;
        this.context = context;
    }

    AdapterCards() {

    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView holder, exp, number, cvv;
        ImageView type;
        Button remove;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            holder = (TextView) itemView.findViewById(R.id.holder);
            cvv = (TextView) itemView.findViewById(R.id.cvv);
            number = (TextView) itemView.findViewById(R.id.number);
            exp = (TextView) itemView.findViewById(R.id.exp);
            type = (ImageView) itemView.findViewById(R.id.type);
            remove=(Button)itemView.findViewById(R.id.remove);
        }
    }

    @NonNull
    @Override
    public AdapterCards.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_card_holder, viewGroup, false);

        return new AdapterCards.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCards.SearchViewHolder searchViewHolder, int i) {
        final int p = i;
        searchViewHolder.holder.setText(Holder.get(i));
        searchViewHolder.number.setText(CardNumber.get(i));
        searchViewHolder.exp.setText(Exp.get(i));
        searchViewHolder.cvv.setText((Cvv.get(i)));

        if (Integer.parseInt(CardNumber.get(i).substring(0, 2)) >= 40 && Integer.parseInt(CardNumber.get(i).substring(0, 2)) <= 49) {
            searchViewHolder.type.setImageResource(R.drawable.visa);
        } else if (Integer.parseInt(CardNumber.get(i).substring(0, 2)) >= 51 && Integer.parseInt(CardNumber.get(i).substring(0, 2)) <= 55) {
            searchViewHolder.type.setImageResource(R.drawable.mastercard);
        } else if (Integer.parseInt(CardNumber.get(i).substring(0, 2)) >= 56 && Integer.parseInt(CardNumber.get(i).substring(0, 2)) <= 59) {
            searchViewHolder.type.setImageResource(R.drawable.maestro);
        } else if (Integer.parseInt(CardNumber.get(i).substring(0, 2)) >= 60 && Integer.parseInt(CardNumber.get(i).substring(0, 2)) <= 69) {
            searchViewHolder.type.setImageResource(R.drawable.rupay);
        } else if (CardNumber.get(i).equals("")) {
            searchViewHolder.type.setImageDrawable(null);
        }

        searchViewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.alert_light_frame)
                        .setTitle("Confirm Delete")
                        .setMessage("Do you really want to delete ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helper = new DatabaseCard(context);
                                SQLiteDatabase db = helper.getWritableDatabase();
                                db.delete("debit_card", "card_number =?", new String[]{CardNumber.get(p)});

                                CardNumber.remove(p);
                                Holder.remove(p);
                                Exp.remove(p);
                                Cvv.remove(p);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return CardNumber.size();

    }
}
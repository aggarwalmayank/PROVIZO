package com.appsaga.provizo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.collection.LLRBNode;

import java.util.ArrayList;
import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class ViewCards extends AppCompatActivity {

    RecyclerView allList;

    static ArrayList<String> CardNumber;
    static ArrayList<String> Holder;
    static ArrayList<String> Cvv;
    static ArrayList<String> Exp;

    DatabaseCard db;
    String title;
    AdapterCards adapterClass;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cards);


        android.support.v7.widget.Toolbar toolbar = (
                android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#bec1c2"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backicon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        allList = findViewById(R.id.AllList);

        db = new DatabaseCard(this);

        Holder= new ArrayList<>();
        CardNumber= new ArrayList<>();
        Cvv= new ArrayList<>();
        Exp = new ArrayList<>();

        allList.setHasFixedSize(true);
        allList.setLayoutManager(new LinearLayoutManager(this));
        //allList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        setAdapter();

        title=this.getClass().getSimpleName();
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
    }


    private void setAdapter() {

        Cursor res = db.getAllData();
        CardNumber.clear();
        Holder.clear();
        Exp.clear();
        Cvv.clear();
        if (res.getCount() == 0) {
            Toast.makeText(ViewCards.this, "No Card Saved", Toast.LENGTH_SHORT).show();
            return;
        } else {
            while (res.moveToNext()) {
                CardNumber.add(res.getString(0));
                Holder.add(res.getString(1).toUpperCase());
                Exp.add(res.getString(2));
                Cvv.add(res.getString(3));
            }

            adapterClass = new AdapterCards(CardNumber,Holder,Exp,Cvv,ViewCards.this);
            allList.setAdapter(adapterClass);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}
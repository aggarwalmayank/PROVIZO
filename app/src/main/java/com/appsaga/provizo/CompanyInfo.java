package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompanyInfo extends AppCompatActivity {
    TextView owner, company, address, experience, amount, trucks;
    Button next;
    DatabaseReference mref;
    String cname, weight, trucktype;
    ReviewAdapter adapter;
    RecyclerView rtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        rtext = findViewById(R.id.reviews);
        owner = findViewById(R.id.owner);
        company = findViewById(R.id.name);
        address = findViewById(R.id.address);
        experience = findViewById(R.id.experience);
        amount = findViewById(R.id.amount);
        trucks = findViewById(R.id.nooftruck);
        next = findViewById(R.id.next);
        mref = FirebaseDatabase.getInstance().getReference();
        weight = getIntent().getStringExtra("weight");
        cname = getIntent().getStringExtra("company");
        company.setText(cname);
        rtext.setHasFixedSize(true);
        rtext.setLayoutManager(new LinearLayoutManager(CompanyInfo.this));
        rtext.addItemDecoration(new DividerItemDecoration(CompanyInfo.this, LinearLayoutManager.VERTICAL));


        trucktype = getIntent().getStringExtra("exacttruckwt");
        double wt = Double.parseDouble(getIntent().getStringExtra("weightnounit"));
        wt = wt * 0.1;
        double twt = Double.parseDouble(trucktype);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.go_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if ((int) Math.floor((wt / twt)) == 0)
            trucks.setText("Number Of Trucks Required: " + String.valueOf(1));
        else
            trucks.setText("Number Of Trucks Required: " + String.valueOf((int) Math.floor((wt / twt))));
        amount.setText("Amount: " + getIntent().getStringExtra("amount") + " Rs per truck");

        cname = cname.replaceAll("\\s+", "");
        mref.child("Company").child(cname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                address.setText("Address: " + dataSnapshot.child("Address").getValue().toString());
                owner.setText("Owner: " + dataSnapshot.child("Owner").getValue().toString());
                experience.setText("Experience: " + dataSnapshot.child("Experience").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mref.child("Ratings").child(cname.replaceAll(" ", "")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Rating> ratings = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Rating rate = new Rating(ds.getValue(Rating.class));
                    ratings.add(rate);
                }
                adapter = new ReviewAdapter(ratings, CompanyInfo.this);
                rtext.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CompanyInfo.this, consignor_details.class);
                i.putExtra("Order ID", getIntent().getStringExtra("Order ID"));
                i.putExtra("Current User", getIntent().getStringExtra("Current User"));
                i.putExtra("type of service", getIntent().getStringExtra("type of service"));
                i.putExtra("pickup", getIntent().getStringExtra("pickup"));
                i.putExtra("drop", getIntent().getStringExtra("drop"));
                i.putExtra("date", getIntent().getStringExtra("date"));
                i.putExtra("weight", getIntent().getStringExtra("weight"));
                i.putExtra("Material", getIntent().getStringExtra("Material"));
                i.putExtra("truck", getIntent().getStringExtra("truck"));
                i.putExtra("company", getIntent().getStringExtra("company"));
                i.putExtra("amount", getIntent().getStringExtra("amount"));
                startActivity(i);
            }
        });
    }
}

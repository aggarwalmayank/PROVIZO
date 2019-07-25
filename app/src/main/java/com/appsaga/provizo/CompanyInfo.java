package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompanyInfo extends AppCompatActivity {
    TextView owner, company, address, experience, amount, trucks, rating;
    Button next;
    DatabaseReference mref;
    String cname, weight, trucktype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);

        owner = findViewById(R.id.owner);
        company = findViewById(R.id.name);
        address = findViewById(R.id.address);
        experience = findViewById(R.id.experience);
        amount = findViewById(R.id.amount);
        trucks = findViewById(R.id.nooftruck);
        next = findViewById(R.id.next);
        rating = findViewById(R.id.rating);
        mref = FirebaseDatabase.getInstance().getReference();
        weight = getIntent().getStringExtra("weight");

        cname = getIntent().getStringExtra("company");
        company.setText(cname);
        trucktype = getIntent().getStringExtra("exacttruckwt");
        double wt = Double.parseDouble(getIntent().getStringExtra("weightnounit"));
        wt = wt * 0.1;
        double twt = Double.parseDouble(trucktype);

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

                    final ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //Log.d("mayank", String.valueOf(ds.getValue()));
                        arrayList.add(String.valueOf(ds.getValue()));
                    }
                if (!arrayList.isEmpty()) {
                    double sum = 0, average = 0, count = arrayList.size();
                    for (int i = 0; i < arrayList.size(); i++) {

                        //Log.d("mayanksum",String.valueOf(sum));
                        sum = sum + Double.parseDouble(arrayList.get(i));
                    }
                    average = sum / count;
                    //Toast.makeText(this, String.valueOf(average), Toast.LENGTH_SHORT).show();
                    rating.setText("Rating: " + String.valueOf(average));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Toast.makeText(this, arrayList.size(), Toast.LENGTH_SHORT).show();

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

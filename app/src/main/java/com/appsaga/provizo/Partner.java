package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Partner extends AppCompatActivity {

    TextView companyname;
    Spinner spinner1;
    Spinner spinner2;
    DatabaseReference databaseReference;
    TextView currentRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);

        String username = getIntent().getStringExtra("partner id");
        databaseReference = FirebaseDatabase.getInstance().getReference();

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

        companyname =findViewById(R.id.companyname);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        currentRate = findViewById(R.id.current_rate);

        databaseReference.child("partners").child(username).child("operations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final PartnerValue partnerValue = dataSnapshot.getValue(PartnerValue.class);

                companyname.setText(partnerValue.getCompanyName());

                ArrayList<String> source = new ArrayList<>();

                for(HashMap.Entry<String,HashMap<String,Long>> entry : partnerValue.getLocationMap().entrySet())
                {
                    source.add(entry.getKey());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Partner.this,
                        android.R.layout.simple_spinner_item, source);

                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(arrayAdapter);

                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        final HashMap<String,Long> source_dest = partnerValue.getLocationMap().get(parent.getItemAtPosition(position).toString());

                        ArrayList<String> dest = new ArrayList<>();

                        for(HashMap.Entry<String,Long> entry : source_dest.entrySet())
                        {
                            dest.add(entry.getKey());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Partner.this,
                                android.R.layout.simple_spinner_item, dest);

                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(arrayAdapter);

                        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                currentRate.setText(source_dest.get(parent.getItemAtPosition(position).toString())+"");
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

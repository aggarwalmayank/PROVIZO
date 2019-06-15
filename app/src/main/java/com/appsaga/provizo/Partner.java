package com.appsaga.provizo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    Button updaterate, updateavail, changerate, changeavail;
    TextView companyname, availability, currentRate, selectunit;
    Spinner spinner1, spinner2;
    EditText newrate;
    DatabaseReference databaseReference;
    private RadioGroup rgavail, rgunit;
    private RadioButton rbavail, rbunit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);

        newrate = findViewById(R.id.new_rate);
        updaterate = findViewById(R.id.confirmrate);
        updateavail = findViewById(R.id.confirmavail);
        changeavail = findViewById(R.id.changeavail);
        changerate = findViewById(R.id.ratechange);
        selectunit = findViewById(R.id.selectunit);
        availability = findViewById(R.id.currentstatus);
        rgavail = findViewById(R.id.radiogrp2);
        rgunit = findViewById(R.id.radiogrp1);
        updaterate.setVisibility(View.INVISIBLE);
        updateavail.setVisibility(View.INVISIBLE);
        rgunit.setVisibility(View.INVISIBLE);
        rgavail.setVisibility(View.INVISIBLE);
        selectunit.setVisibility(View.INVISIBLE);
        newrate.setVisibility(View.INVISIBLE);

        final String username = getIntent().getStringExtra("partner id");
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

        companyname = findViewById(R.id.companyname);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        currentRate = findViewById(R.id.current_rate);

        databaseReference.child("partners").child(username).child("operations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("Run.....", "Yes");
                final PartnerValue partnerValue = dataSnapshot.getValue(PartnerValue.class);

                companyname.setText(partnerValue.getCompanyName());
                availability.setText(partnerValue.getTruckStatus());
                if (availability.getText().equals("Available"))
                    availability.setTextColor(Color.parseColor("#008000"));
                else
                    availability.setTextColor(Color.parseColor("#ff0000"));

                ArrayList<String> source = new ArrayList<>();

                for (HashMap.Entry<String, HashMap<String, Long>> entry : partnerValue.getLocationMap().entrySet()) {
                    source.add(entry.getKey());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Partner.this,
                        android.R.layout.simple_spinner_item, source);

                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(arrayAdapter);

                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        final HashMap<String, Long> source_dest = partnerValue.getLocationMap().get(parent.getItemAtPosition(position).toString());

                        ArrayList<String> dest = new ArrayList<>();

                        for (HashMap.Entry<String, Long> entry : source_dest.entrySet()) {
                            dest.add(entry.getKey());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Partner.this,
                                android.R.layout.simple_spinner_item, dest);

                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(arrayAdapter);

                        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                currentRate.setText(source_dest.get(parent.getItemAtPosition(position).toString()) + " per Quintal");
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


        changeavail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgavail.setVisibility(View.VISIBLE);
                updateavail.setVisibility(View.VISIBLE);
            }
        });
        changerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgunit.setVisibility(View.VISIBLE);
                updaterate.setVisibility(View.VISIBLE);
                selectunit.setVisibility(View.VISIBLE);
                newrate.setVisibility(View.VISIBLE);
            }
        });

        updaterate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = rgunit.getCheckedRadioButtonId();
                rbunit = (RadioButton) findViewById(selectedId);
                if (rgunit.getCheckedRadioButtonId() == -1)
                    alertbox("Invalid Unit");
                else if (newrate.getText().toString().equalsIgnoreCase(""))
                    newrate.setError("Invalid Rate");
                else {

                    Log.d("Run....", "Yes2");
                    final DatabaseReference myRef;
                    myRef = databaseReference.child("partners").child(username).child("operations").child("locationMap").
                            child(spinner1.getSelectedItem().toString()).child(spinner2.getSelectedItem().toString());

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (rbunit.getText().toString().equals("Kg")) {
                                myRef.setValue(Long.parseLong(newrate.getText().toString()) * 0.01);
                                currentRate.setText(Long.parseLong(newrate.getText().toString()) * 0.01+" Quintal");
                            } else if (rbunit.getText().toString().equals("Tonne")) {
                                myRef.setValue(Long.parseLong(newrate.getText().toString()) * 10);
                                currentRate.setText(Long.parseLong(newrate.getText().toString()) * 10+" Quintal");
                            } else {
                                myRef.setValue(Long.parseLong(newrate.getText().toString()));
                                currentRate.setText(Long.parseLong(newrate.getText().toString())+" Quintal");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    rgunit.setVisibility(View.INVISIBLE);
                    selectunit.setVisibility(View.INVISIBLE);
                    updaterate.setVisibility(View.INVISIBLE);
                    newrate.setVisibility(View.INVISIBLE);
                }
            }
        });

        updateavail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rgavail.getCheckedRadioButtonId();
                rbavail = (RadioButton) findViewById(selectedId);
                if (rgavail.getCheckedRadioButtonId() == -1) {
                    alertbox("Invalid status");
                } else {
                    Log.d("Run....", "Yes3");
                    final DatabaseReference myRef;
                    myRef = databaseReference.child("partners").child(username).child("operations").child("truckStatus");

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            myRef.setValue(rbavail.getText());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    availability.setText(rbavail.getText());
                    if (availability.getText().equals("Available"))
                        availability.setTextColor(Color.parseColor("#008000"));
                    else
                        availability.setTextColor(Color.parseColor("#ff0000"));

                    rgavail.setVisibility(View.INVISIBLE);
                    updateavail.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void alertbox(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage(msg);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}

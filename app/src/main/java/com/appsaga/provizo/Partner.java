package com.appsaga.provizo;

//2nd

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Partner extends AppCompatActivity implements CityDialog.DialogListener{

    Button updaterate, updateavail, changerate, changeavail,addcity, deleteCity;
    TextView companyname, availability, currentRate, selectunit;
    Spinner spinner1, spinner2;
    EditText newrate;
    DatabaseReference databaseReference;
    private RadioGroup rgavail, rgunit;
    private RadioButton rbavail, rbunit;
    CheckBox checkBox1;
    CheckBox checkBox2;
    String username;
    Button done;
    Button delete;
    CheckBox deleteFullTruck , deletePartLoad;
    Dialog customDialog;
    ListView deleteListView;
    static LinearLayout l;
    CheckBox open,closed;
    CheckBox openPartner, closedPartner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);
        final Button viewbooking=findViewById(R.id.viewbooking);
        l=findViewById(R.id.layout);
        newrate = findViewById(R.id.new_rate);
        updaterate = findViewById(R.id.confirmrate);
        updateavail = findViewById(R.id.confirmavail);
        changeavail = findViewById(R.id.changeavail);
        changerate = findViewById(R.id.ratechange);
        selectunit = findViewById(R.id.selectunit);
        availability = findViewById(R.id.currentstatus);
        rgavail = findViewById(R.id.radiogrp2);
        rgunit = findViewById(R.id.radiogrp1);
        addcity=findViewById(R.id.addcity);
        updaterate.setVisibility(View.INVISIBLE);
        updateavail.setVisibility(View.INVISIBLE);
        rgunit.setVisibility(View.INVISIBLE);
        rgavail.setVisibility(View.INVISIBLE);
        selectunit.setVisibility(View.INVISIBLE);
        newrate.setVisibility(View.INVISIBLE);
        deleteCity = findViewById(R.id.delete_city);
        checkBox1 =findViewById(R.id.checkbox1);
        checkBox2=findViewById(R.id.checkbox2);
        openPartner=findViewById(R.id.open_partner);
        closedPartner=findViewById(R.id.closed_partner);

        username = getIntent().getStringExtra("partner id");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        addcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        customDialog = new Dialog(Partner.this);
        customDialog.setContentView(R.layout.delete_city_dialog_box);
        done = customDialog.findViewById(R.id.done);
        delete = customDialog.findViewById(R.id.delete);

        open=customDialog.findViewById(R.id.open);
        closed=customDialog.findViewById(R.id.closed);

        deleteFullTruck = customDialog.findViewById(R.id.delete_full_truck);
        deletePartLoad=customDialog.findViewById(R.id.delete_part_load);

        deleteFullTruck.setChecked(true);
        closed.setChecked(true);

        deleteListView = customDialog.findViewById(R.id.delete_city_list);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog.dismiss();
            }
        });

        deleteCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCitiesToDelete();
            }
        });

        deleteFullTruck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    deletePartLoad.setChecked(false);
                }
                else
                {
                    deletePartLoad.setChecked(true);
                }

                if(open.isChecked()||closed.isChecked())
                {
                    getCitiesToDelete();
                }
            }
        });

        deletePartLoad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    deleteFullTruck.setChecked(false);
                }
                else
                {
                    deleteFullTruck.setChecked(true);
                }

                if(open.isChecked()||closed.isChecked())
                {
                    getCitiesToDelete();
                }
            }
        });

        openPartner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    closedPartner.setChecked(false);
                }
                else
                {
                    closedPartner.setChecked(true);
                }

                performChange();
            }
        });

        closedPartner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    openPartner.setChecked(false);
                }
                else
                {
                    openPartner.setChecked(true);
                }

                performChange();
            }
        });

        open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    closed.setChecked(false);
                }
                else
                {
                    closed.setChecked(true);
                }
                getCitiesToDelete();
            }
        });

        closed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    open.setChecked(false);
                }
                else
                {
                    open.setChecked(true);
                }
                getCitiesToDelete();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.go_back);
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

        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(checkBox1.isChecked())
                {
                    checkBox2.setChecked(Boolean.FALSE);
                }
                else
                {
                    checkBox2.setChecked(Boolean.TRUE);
                }
                performChange();
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(checkBox2.isChecked())
                {
                    checkBox1.setChecked(Boolean.FALSE);
                }
                else
                {
                    checkBox1.setChecked(Boolean.TRUE);
                }
                performChange();
            }
        });

        databaseReference.child("partners").child(username).child("operations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final PartnerValue partnerValue = dataSnapshot.getValue(PartnerValue.class);


                HashMap.Entry<String,HashMap<String,HashMap<String,HashMap<String,Long>>>> entry_test = partnerValue.getLocationMap().entrySet().iterator().next();
                Log.d("Testing...3",entry_test.getKey()+"");

                companyname.setText(partnerValue.getCompanyName());
                availability.setText(partnerValue.getTruckStatus());
                if (availability.getText().equals("Available"))
                    availability.setTextColor(Color.parseColor("#008000"));
                else
                    availability.setTextColor(Color.parseColor("#ff0000"));

                final ArrayList<String> source = new ArrayList<>();

                if(checkBox1.isChecked())
                {
                    for (HashMap.Entry<String,HashMap<String,HashMap<String,HashMap<String,Long>>>> entry : partnerValue.getLocationMap().entrySet()) {

                        if(entry.getKey().equalsIgnoreCase("FullTruckLoad"))
                        {
                            for(HashMap.Entry<String,HashMap<String,HashMap<String,Long>>> entry1 : entry.getValue().entrySet())
                            {
                                if(closedPartner.isChecked()) {
                                    //Log.d("Testing...2", entry1.getValue() + "");

                                    if(entry1.getKey().equalsIgnoreCase("closed"))
                                    {
                                        for(HashMap.Entry<String,HashMap<String,Long>> entry2 : entry1.getValue().entrySet())
                                        if (!source.contains(entry2.getKey())) {
                                            source.add(entry2.getKey());
                                        }
                                    }
                                }
                                else if(openPartner.isChecked()) {
                                    //Log.d("Testing...2", entry1.getValue() + "");

                                    if(entry1.getKey().equalsIgnoreCase("open"))
                                    {
                                        for(HashMap.Entry<String,HashMap<String,Long>> entry2 : entry1.getValue().entrySet())
                                            if (!source.contains(entry2.getKey())) {
                                                source.add(entry2.getKey());
                                            }
                                    }
                                }
                            }
                        }
                    }
                }
                else if(checkBox2.isChecked())
                {
                    for (HashMap.Entry<String,HashMap<String,HashMap<String,HashMap<String,Long>>>> entry : partnerValue.getLocationMap().entrySet()) {

                        if(entry.getKey().equalsIgnoreCase("PartLoad"))
                        {
                            for(HashMap.Entry<String,HashMap<String,HashMap<String,Long>>> entry1 : entry.getValue().entrySet())
                            {
                                if(closedPartner.isChecked()) {
                                    //Log.d("Testing...2", entry1.getValue() + "");

                                    if(entry1.getKey().equalsIgnoreCase("closed"))
                                    {
                                        for(HashMap.Entry<String,HashMap<String,Long>> entry2 : entry1.getValue().entrySet())
                                            if (!source.contains(entry2.getKey())) {
                                                source.add(entry2.getKey());
                                            }
                                    }
                                }
                                else if(openPartner.isChecked()) {
                                    //Log.d("Testing...2", entry1.getValue() + "");

                                    if(entry1.getKey().equalsIgnoreCase("open"))
                                    {
                                        for(HashMap.Entry<String,HashMap<String,Long>> entry2 : entry1.getValue().entrySet())
                                            if (!source.contains(entry2.getKey())) {
                                                source.add(entry2.getKey());
                                            }
                                    }
                                }
                            }
                        }
                    }
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Partner.this,
                        android.R.layout.simple_spinner_item, source);

                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(null);
                spinner1.setAdapter(arrayAdapter);

                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                        final ArrayList<String> dest = new ArrayList<>();
                        final HashMap<String,HashMap<String, Long>>[] source_dest = new HashMap[]{new HashMap<>()};

                        if(checkBox1.isChecked()) {

                            if(closed.isChecked())
                            {
                                source_dest[0] = partnerValue.getLocationMap().get("FullTruckLoad").get("closed");
                            }
                            else
                            {
                                source_dest[0] = partnerValue.getLocationMap().get("FullTruckLoad").get("open");
                            }

                            if(source_dest[0]!=null)
                                for (HashMap.Entry<String,HashMap<String, Long>> entry : source_dest[0].entrySet()) {

                                    if(entry.getKey().equalsIgnoreCase(spinner1.getItemAtPosition(position).toString()))
                                    {
                                        HashMap<String,Long> hashMap = entry.getValue();

                                        for(HashMap.Entry<String,Long> entry1 : hashMap.entrySet())
                                        {
                                            if(!dest.contains(entry1.getKey()))
                                            {
                                                dest.add(entry1.getKey());
                                            }
                                        }
                                    }
                                }
                        }
                        else if(checkBox2.isChecked()) {

                            if(closed.isChecked())
                            {
                                source_dest[0] = partnerValue.getLocationMap().get("PartLoad").get("closed");
                            }
                            else
                            {
                                source_dest[0] = partnerValue.getLocationMap().get("PartLoad").get("open");
                            }
                            dest.clear();

                            if(source_dest[0]!=null)
                                for (HashMap.Entry<String,HashMap<String, Long>> entry : source_dest[0].entrySet()) {

                                    if(entry.getKey().equalsIgnoreCase(spinner1.getItemAtPosition(position).toString()))
                                    {
                                        HashMap<String,Long> hashMap = entry.getValue();

                                        for(HashMap.Entry<String,Long> entry1 : hashMap.entrySet())
                                        {
                                            if(!dest.contains(entry1.getKey()))
                                            {
                                                dest.add(entry1.getKey());
                                            }
                                        }
                                    }
                                }
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Partner.this,
                                android.R.layout.simple_spinner_item, dest);

                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(null);
                        spinner2.setAdapter(arrayAdapter);

                        final HashMap<String,HashMap<String, Long>> finalSource_dest = source_dest[0];
                        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if(checkBox1.isChecked())
                                {
                                    currentRate.setText(finalSource_dest.get(spinner1.getSelectedItem().toString()).get(parent.getItemAtPosition(position).toString()) + " per Quintal");
                                }
                                else if(checkBox2.isChecked())
                                {
                                    currentRate.setText(finalSource_dest.get(spinner1.getSelectedItem().toString()).get(parent.getItemAtPosition(position).toString()) + " per Quintal");
                                }
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
                rbunit = findViewById(selectedId);
                if (rgunit.getCheckedRadioButtonId() == -1)
                    alertbox("Invalid Unit");
                else if (newrate.getText().toString().equalsIgnoreCase(""))
                    newrate.setError("Invalid Rate");
                else {

                    DatabaseReference myRef = null;

                    if(checkBox1.isChecked())
                    {
                        if(closedPartner.isChecked())
                        {
                            myRef = databaseReference.child("partners").child(username).child("operations").child("locationMap").child("FullTruckLoad").
                                    child("closed").child(spinner1.getSelectedItem().toString()).child(spinner2.getSelectedItem().toString());
                        }
                        else
                        {
                            myRef = databaseReference.child("partners").child(username).child("operations").child("locationMap").child("FullTruckLoad").
                                    child("open").child(spinner1.getSelectedItem().toString()).child(spinner2.getSelectedItem().toString());
                        }
                    }
                    else if(checkBox2.isChecked())
                    {
                        if(closedPartner.isChecked())
                        {
                            myRef = databaseReference.child("partners").child(username).child("operations").child("locationMap").child("PartLoad").
                                    child("closed").child(spinner1.getSelectedItem().toString()).child(spinner2.getSelectedItem().toString());
                        }
                        else
                        {
                            myRef = databaseReference.child("partners").child(username).child("operations").child("locationMap").child("PartLoad").
                                    child("open").child(spinner1.getSelectedItem().toString()).child(spinner2.getSelectedItem().toString());
                        }
                    }

                    final DatabaseReference finalMyRef = myRef;

                    final DatabaseReference finalMyRef1 = myRef;
                    databaseReference.child("basePrice").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            final int baserate;

                            if(checkBox1.isChecked())
                            {
                                if(closedPartner.isChecked())
                                {
                                    baserate = dataSnapshot.child("Full Truck Load CLOSED").getValue(Integer.class);
                                }
                                else
                                {
                                    baserate = dataSnapshot.child("Full Truck Load OPEN").getValue(Integer.class);
                                }
                            }
                            else
                            {
                                if(closedPartner.isChecked())
                                {
                                    baserate = dataSnapshot.child("Part Load CLOSED").getValue(Integer.class);
                                }
                                else
                                {
                                    baserate = dataSnapshot.child("Part Load OPEN").getValue(Integer.class);
                                }
                            }

                            Log.d("bareRate",baserate+"");

                            finalMyRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (rbunit.getText().toString().equals("Kg")) {

                                        if(Long.parseLong(newrate.getText().toString()) * 0.01 >= (long)baserate) {
                                            finalMyRef.setValue(Long.parseLong(newrate.getText().toString()) * 0.01);
                                            currentRate.setText(Long.parseLong(newrate.getText().toString()) * 0.01 + " per Quintal");

                                            rgunit.setVisibility(View.INVISIBLE);
                                            selectunit.setVisibility(View.INVISIBLE);
                                            updaterate.setVisibility(View.INVISIBLE);
                                            newrate.setVisibility(View.INVISIBLE);

                                            Toast.makeText(Partner.this,"Rate Changed Successfully",Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(Partner.this,"The new rate is less than the base rate 60 Rs. per Quintal",Toast.LENGTH_LONG).show();
                                        }
                                    } else if (rbunit.getText().toString().equals("Tonne")) {

                                        if(Long.parseLong(newrate.getText().toString()) * 10 >= (long)baserate) {
                                            finalMyRef.setValue(Long.parseLong(newrate.getText().toString()) * 10);
                                            currentRate.setText(Long.parseLong(newrate.getText().toString()) * 10 + " per Quintal");

                                            rgunit.setVisibility(View.INVISIBLE);
                                            selectunit.setVisibility(View.INVISIBLE);
                                            updaterate.setVisibility(View.INVISIBLE);
                                            newrate.setVisibility(View.INVISIBLE);

                                            Toast.makeText(Partner.this,"Rate Changed Successfully",Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(Partner.this,"The new rate is less than the base rate 60 Rs. per Quintal",Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        if(Long.parseLong(newrate.getText().toString()) >=(long)baserate) {
                                            finalMyRef.setValue(Long.parseLong(newrate.getText().toString()));
                                            currentRate.setText(Long.parseLong(newrate.getText().toString()) + " per Quintal");

                                            rgunit.setVisibility(View.INVISIBLE);
                                            selectunit.setVisibility(View.INVISIBLE);
                                            updaterate.setVisibility(View.INVISIBLE);
                                            newrate.setVisibility(View.INVISIBLE);

                                            Toast.makeText(Partner.this,"Rate Changed Successfully",Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(Partner.this,"The new rate is less than the base rate 100 Rs. per Quintal",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
        viewbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Partner.this,PartnerBooking.class);
                i.putExtra("partner id",username);
                startActivity(i);
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

    public void performChange()
    {
        databaseReference.child("partners").child(username).child("operations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final PartnerValue partnerValue = dataSnapshot.getValue(PartnerValue.class);

                companyname.setText(partnerValue.getCompanyName());
                availability.setText(partnerValue.getTruckStatus());
                if (availability.getText().equals("Available"))
                    availability.setTextColor(Color.parseColor("#008000"));
                else
                    availability.setTextColor(Color.parseColor("#ff0000"));

                final ArrayList<String> source = new ArrayList<>();
                currentRate.setText("");

                if(checkBox1.isChecked())
                {
                    for (HashMap.Entry<String,HashMap<String,HashMap<String,HashMap<String,Long>>>> entry : partnerValue.getLocationMap().entrySet()) {

                        if(entry.getKey().equalsIgnoreCase("FullTruckLoad"))
                        {
                            for(HashMap.Entry<String,HashMap<String,HashMap<String,Long>>> entry1 : entry.getValue().entrySet())
                            {
                                if(closedPartner.isChecked()) {
                                    //Log.d("Testing...2", entry1.getValue() + "");

                                    if(entry1.getKey().equalsIgnoreCase("closed"))
                                    {
                                        for(HashMap.Entry<String,HashMap<String,Long>> entry2 : entry1.getValue().entrySet())
                                            if (!source.contains(entry2.getKey())) {
                                                source.add(entry2.getKey());
                                            }
                                    }
                                }
                                else if(openPartner.isChecked()) {
                                    //Log.d("Testing...2", entry1.getValue() + "");

                                    if(entry1.getKey().equalsIgnoreCase("open"))
                                    {
                                        for(HashMap.Entry<String,HashMap<String,Long>> entry2 : entry1.getValue().entrySet())
                                            if (!source.contains(entry2.getKey())) {
                                                source.add(entry2.getKey());
                                            }
                                    }
                                }
                            }
                        }
                    }
                }
                else if(checkBox2.isChecked())
                {
                    for (HashMap.Entry<String,HashMap<String,HashMap<String,HashMap<String,Long>>>> entry : partnerValue.getLocationMap().entrySet()) {

                        if(entry.getKey().equalsIgnoreCase("PartLoad"))
                        {
                            for(HashMap.Entry<String,HashMap<String,HashMap<String,Long>>> entry1 : entry.getValue().entrySet())
                            {
                                if(closedPartner.isChecked()) {
                                    //Log.d("Testing...2", entry1.getValue() + "");

                                    if(entry1.getKey().equalsIgnoreCase("closed"))
                                    {
                                        for(HashMap.Entry<String,HashMap<String,Long>> entry2 : entry1.getValue().entrySet())
                                            if (!source.contains(entry2.getKey())) {
                                                source.add(entry2.getKey());
                                            }
                                    }
                                }
                                else if(openPartner.isChecked()) {
                                    //Log.d("Testing...2", entry1.getValue() + "");

                                    if(entry1.getKey().equalsIgnoreCase("open"))
                                    {
                                        for(HashMap.Entry<String,HashMap<String,Long>> entry2 : entry1.getValue().entrySet())
                                            if (!source.contains(entry2.getKey())) {
                                                source.add(entry2.getKey());
                                            }
                                    }
                                }
                            }
                        }
                    }
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Partner.this,
                        android.R.layout.simple_spinner_item, source);

                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(null);
                spinner2.setAdapter(null);
                spinner1.setAdapter(arrayAdapter);

                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                        final ArrayList<String> dest = new ArrayList<>();
                        final HashMap<String,HashMap<String, Long>>[] source_dest = new HashMap[]{new HashMap<>()};

                        if(checkBox1.isChecked()) {

                            if(closedPartner.isChecked())
                            {
                                source_dest[0] = partnerValue.getLocationMap().get("FullTruckLoad").get("closed");
                            }
                            else
                            {
                                source_dest[0] = partnerValue.getLocationMap().get("FullTruckLoad").get("open");
                            }

                            if(source_dest[0]!=null)
                            for (HashMap.Entry<String,HashMap<String, Long>> entry : source_dest[0].entrySet()) {

                                if(entry.getKey().equalsIgnoreCase(spinner1.getItemAtPosition(position).toString()))
                                {
                                    HashMap<String,Long> hashMap = entry.getValue();

                                    for(HashMap.Entry<String,Long> entry1 : hashMap.entrySet())
                                    {
                                        if(!dest.contains(entry1.getKey()))
                                        {
                                            dest.add(entry1.getKey());
                                        }
                                    }
                                }
                            }
                        }
                        else if(checkBox2.isChecked()) {

                            if(closedPartner.isChecked())
                            {
                                source_dest[0] = partnerValue.getLocationMap().get("PartLoad").get("closed");
                            }
                            else
                            {
                                source_dest[0] = partnerValue.getLocationMap().get("PartLoad").get("open");
                            }

                            dest.clear();

                            if(source_dest[0]!=null)
                            for (HashMap.Entry<String, HashMap<String, Long>> entry : source_dest[0].entrySet()) {

                                if(entry.getKey().equalsIgnoreCase(spinner1.getItemAtPosition(position).toString()))
                                {
                                    HashMap<String,Long> hashMap = entry.getValue();

                                    for(HashMap.Entry<String,Long> entry1 : hashMap.entrySet())
                                    {
                                        if(!dest.contains(entry1.getKey()))
                                        {
                                            dest.add(entry1.getKey());
                                        }
                                    }
                                }
                            }
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Partner.this,
                                android.R.layout.simple_spinner_item, dest);

                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(null);
                        spinner2.setAdapter(arrayAdapter);

                        final HashMap<String, HashMap<String, Long>> finalSource_dest = source_dest[0];
                        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if(checkBox1.isChecked())
                                {
                                    currentRate.setText(finalSource_dest.get(spinner1.getSelectedItem().toString()).get(parent.getItemAtPosition(position).toString()) + " per Quintal");
                                }
                                else if(checkBox2.isChecked())
                                {
                                    currentRate.setText(finalSource_dest.get(spinner1.getSelectedItem().toString()).get(parent.getItemAtPosition(position).toString()) + " per Quintal");
                                }
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

    public void openDialog() {
        CityDialog exampleDialog = new CityDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void addcitytoDB(String origin, String dest, long price,String type) {
        if(type.equals("Full Truck Load CLOSED"))
        {
            databaseReference.child("partners").child(username).child("operations").child("locationMap")
                    .child("FullTruckLoad").child("closed").child(origin.toLowerCase()).child(dest.toLowerCase()).setValue(price);

        }
        else if(type.equals("Part Load CLOSED"))
        {
            databaseReference.child("partners").child(username).child("operations").child("locationMap")
                    .child("PartLoad").child("closed").child(origin.toLowerCase()).child(dest.toLowerCase()).setValue(price);


        }
        else if(type.equals("Part Load OPEN")){

            databaseReference.child("partners").child(username).child("operations").child("locationMap")
                    .child("PartLoad").child("open").child(origin.toLowerCase()).child(dest.toLowerCase()).setValue(price);

        }
        else if(type.equals("Full Truck Load OPEN")){
            databaseReference.child("partners").child(username).child("operations").child("locationMap")
                    .child("FullTruckLoad").child("open").child(origin.toLowerCase()).child(dest.toLowerCase()).setValue(price);
        }

        finish();
        startActivity(getIntent());
    }

    void getCitiesToDelete()
    {
        if(deleteFullTruck.isChecked())
        {
            DatabaseReference myRef;

            if(closed.isChecked())
            {
                myRef = databaseReference.child("partners").child(username).child("operations").child("locationMap").child("FullTruckLoad").child("closed");
            }
            else
            {
                myRef = databaseReference.child("partners").child(username).child("operations").child("locationMap").child("FullTruckLoad").child("open");
            }

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                    final ArrayList<locMap> locMapArrayList = new ArrayList<>();
                    HashMap<String,Long> hashMap;

                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        hashMap = (HashMap<String, Long>) ds.getValue();
                        for(HashMap.Entry<String,Long> entry:hashMap.entrySet())
                        {
                            locMapArrayList.add(new locMap(ds.getKey(),entry.getKey(),entry.getValue()));
                        }
                    }

                    final DeleteCityAdapter deleteCityAdapter = new DeleteCityAdapter(Partner.this,locMapArrayList);
                    deleteListView.setAdapter(deleteCityAdapter);
                    customDialog.show();

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ProgressDialog progressDialog = ProgressDialog.show(Partner.this,"Deleting...","Please Wait");
                            int size = deleteListView.getChildCount();

                            for(int i=0;i<size;i++) {
                                View view = deleteListView.getChildAt(i);
                                CheckBox checkBox = view.findViewById(R.id.delete_checkbox);
                                TextView dest = view.findViewById(R.id.dest);
                                TextView source = view.findViewById(R.id.source);

                                if (checkBox.isChecked()) {

                                    if(closed.isChecked())
                                    {
                                        databaseReference.child("partners").child(username).child("operations").child("locationMap").child("FullTruckLoad").
                                                child("closed").child(source.getText().toString()).child(dest.getText().toString()).removeValue();
                                    }
                                    else
                                    {
                                        databaseReference.child("partners").child(username).child("operations").child("locationMap").child("FullTruckLoad").
                                                child("open").child(source.getText().toString()).child(dest.getText().toString()).removeValue();
                                    }
                                }
                            }

                            customDialog.dismiss();

                            progressDialog.dismiss();
                            Toast.makeText(Partner.this,"Selected city deleted",Toast.LENGTH_SHORT).show();

                            finish();
                            startActivity(getIntent());
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if(deletePartLoad.isChecked())
        {
            DatabaseReference myRef;

            if(closed.isChecked())
            {
                myRef = databaseReference.child("partners").child(username).child("operations").child("locationMap").child("PartLoad").child("closed");
            }
            else
            {
                myRef = databaseReference.child("partners").child(username).child("operations").child("locationMap").child("PartLoad").child("open");
            }

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    ArrayList<locMap> locMapArrayList = new ArrayList<>();
                    HashMap<String,Long> hashMap;

                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        hashMap = (HashMap<String, Long>) ds.getValue();
                        for(HashMap.Entry<String,Long> entry:hashMap.entrySet())
                        {
                            locMapArrayList.add(new locMap(ds.getKey(),entry.getKey(),entry.getValue()));
                        }
                    }

                    DeleteCityAdapter deleteCityAdapter = new DeleteCityAdapter(Partner.this,locMapArrayList);
                    deleteListView.setAdapter(deleteCityAdapter);
                    customDialog.show();

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ProgressDialog progressDialog = ProgressDialog.show(Partner.this,"Deleting...","Please Wait");
                            int size = deleteListView.getChildCount();

                            for(int i=0;i<size;i++) {
                                View view = deleteListView.getChildAt(i);
                                CheckBox checkBox = view.findViewById(R.id.delete_checkbox);
                                TextView dest = view.findViewById(R.id.dest);
                                TextView source = view.findViewById(R.id.source);

                                if (checkBox.isChecked()) {

                                    if(closed.isChecked())
                                    {
                                        databaseReference.child("partners").child(username).child("operations").child("locationMap").child("PartLoad").
                                                child("closed").child(source.getText().toString()).child(dest.getText().toString()).removeValue();
                                    }
                                    else
                                    {
                                        databaseReference.child("partners").child(username).child("operations").child("locationMap").child("PartLoad").
                                                child("open").child(source.getText().toString()).child(dest.getText().toString()).removeValue();
                                    }
                                }
                            }

                            customDialog.dismiss();

                            progressDialog.dismiss();
                            Toast.makeText(Partner.this,"Selected city deleted",Toast.LENGTH_SHORT).show();

                            finish();
                            startActivity(getIntent());
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

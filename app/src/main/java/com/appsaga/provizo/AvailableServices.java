package com.appsaga.provizo;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AvailableServices extends AppCompatActivity {

    String serviceType;
    DatabaseReference databaseReference;
    ArrayList<Services> services;
    ListView serviceView;
    ServiceAdapter serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_services);

        TextView tv = findViewById(R.id.appnamesigninup);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);

        serviceType = getIntent().getStringExtra("type of service");
        serviceView = findViewById(R.id.service_list);
        services = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("typesOfServices").child(serviceType).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final ArrayList<String> companies = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    companies.add(ds.getValue(String.class));
                }

                databaseReference.child("partners").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (String s : companies) {

                            String companyName = null;
                            String price = "0";

                            companyName = dataSnapshot.child(s).child("operations").child("companyName").getValue(String.class);

                            HashMap<String, Long> locationMap = (HashMap<String, Long>) dataSnapshot.
                                    child(s).child("operations").child("locationMap").child(getIntent()
                                    .getStringExtra("pickup")).getValue();

                            String priceVal = "0";

                            for (HashMap.Entry<String, Long> entry : locationMap.entrySet()) {
                                if (entry.getKey().equalsIgnoreCase(getIntent().getStringExtra("drop"))) {
                                    priceVal = entry.getValue() + "";
                                    break;
                                }
                            }

                            price = "" + Double.parseDouble(getIntent().getStringExtra("weight")) * Double.parseDouble(priceVal);

                            services.add(new Services(companyName,price));
                        }

                        serviceAdapter = new ServiceAdapter(AvailableServices.this,services);
                        serviceView.setAdapter(serviceAdapter);
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

        serviceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView company = view.findViewById(R.id.comp_name);

                Toast.makeText(AvailableServices.this,company.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}

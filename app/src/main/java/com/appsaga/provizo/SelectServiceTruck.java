package com.appsaga.provizo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SelectServiceTruck extends AppCompatActivity implements com.appsaga.provizo.ProfileDialog.DialogListener {
    EditText material, weight;
    ImageButton service;
    private RadioGroup radioWeightGroup;
    private RadioButton radioWeightButton;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    ImageView menuicon;
    String orderid, currentuser;
    DatabaseReference myref;
    Spinner spinner, trucktype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service_truck);

        radioWeightGroup = (RadioGroup) findViewById(R.id.radiogroup);
        myref = FirebaseDatabase.getInstance().getReference();
        material = findViewById(R.id.materialdesp);
        weight = findViewById(R.id.weight);

        TextView tv = findViewById(R.id.appnamesignupsecond);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);

        Bundle bundle = getIntent().getExtras();
        orderid = bundle.getString("Order ID");
        currentuser = bundle.getString("Current User");

        menuicon = findViewById(R.id.menuicon);
        service = findViewById(R.id.servicesbuttons);


        spinner = (Spinner) findViewById(R.id.servicetype);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.service, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        trucktype = (Spinner) findViewById(R.id.truckspinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.trucktype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trucktype.setAdapter(adapter2);

        dl = (DrawerLayout) findViewById(R.id.deliverylocation);
        t = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);
        dl.addDrawerListener(t);
        t.syncState();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView) findViewById(R.id.nv);
        menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.openDrawer(Gravity.LEFT);
            }
        });
        View headerview = nv.getHeaderView(0);
        TextView profilename = (TextView) headerview.findViewById(R.id.profile);
        TextView mobno = (TextView) headerview.findViewById(R.id.mob_no);
        mobno.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        profilename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {

                    case R.id.wallet:
                        Toast.makeText(SelectServiceTruck.this, "Wallet", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.partnerlogin:
                        Toast.makeText(SelectServiceTruck.this, "partenr login", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mybooking:
                        Toast.makeText(SelectServiceTruck.this, "My Booking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.newbooking:
                        onBackPressed();
                        Toast.makeText(SelectServiceTruck.this, "New Booking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.ratechart:
                        Toast.makeText(SelectServiceTruck.this, "Rate Chart", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notification:
                        Toast.makeText(SelectServiceTruck.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addcard:
                        startActivity(new Intent(SelectServiceTruck.this, AddCard.class));
                        Toast.makeText(SelectServiceTruck.this, "add card", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.support:
                        startActivity(new Intent(SelectServiceTruck.this, Support.class));
                        Toast.makeText(SelectServiceTruck.this, "Support", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        startActivity(new Intent(SelectServiceTruck.this, AboutUs.class));
                        Toast.makeText(SelectServiceTruck.this, "about us", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.signout:
                        Toast.makeText(SelectServiceTruck.this, "SignOut", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        //startActivity(new Intent(DeliveryLocation.this,SignInUp.class));
                        finish();
                        break;
                    default:
                        return true;
                }
                return true;

            }
        });


        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioWeightGroup.getCheckedRadioButtonId();
                radioWeightButton = (RadioButton) findViewById(selectedId);
                if (spinner.getSelectedItem().toString().equals("Type of Service"))
                    alertbox("Please Select Service");
                else if (spinner.getSelectedItem().toString().equals("Housing Material") || spinner.getSelectedItem().toString().equals("Parcel Services")) {
                    alertbox("Housing and Parcel Service is Currently Unavailable");
                    spinner.setSelection(0);
                } else if (material.getText().toString().equalsIgnoreCase(""))
                    material.setError("Invalid Description");
                else if (weight.getText().toString().equalsIgnoreCase(""))
                    weight.setError("Invalid weight");
                else if (radioWeightGroup.getCheckedRadioButtonId()==-1)
                    alertbox("No Unit Selected");
                else if (trucktype.getSelectedItem().toString().equals("Select Truck Type"))
                    alertbox("Please Select Truck");
                else {
                    //AddtoFirebase();
                    String weight1="0";

                    if(radioWeightGroup.getCheckedRadioButtonId()==R.id.radiobtn1)
                    {
                        weight1 = String.valueOf(Double.parseDouble(weight.getText().toString())*(0.01));
                    }
                    else if(radioWeightGroup.getCheckedRadioButtonId()==R.id.radiobtn2)
                    {
                        weight1 = weight.getText().toString();
                    }
                    else if(radioWeightGroup.getCheckedRadioButtonId()==R.id.radiobtn3)
                    {
                        weight1 = String.valueOf(Double.parseDouble(weight.getText().toString())*(10));
                    }

                    Intent i=new Intent(SelectServiceTruck.this,AvailableServices.class);
                    i.putExtra("Order ID",orderid);
                    i.putExtra("Current User",currentuser);
                    i.putExtra("type of service",spinner.getSelectedItem().toString());
                    i.putExtra("pickup",getIntent().getStringExtra("pickup"));
                    i.putExtra("drop",getIntent().getStringExtra("drop"));
                    i.putExtra("date",getIntent().getStringExtra("date"));
                    i.putExtra("weight",weight1+" "+radioWeightButton.getText());
                    i.putExtra("weightnounit",weight1);
                    i.putExtra("Material",material.getText().toString());
                    i.putExtra("truck",trucktype.getSelectedItem().toString());
                    startActivity(i);
                }
            }
        });
    }

    public void AddtoFirebase() {
        int selectedId = radioWeightGroup.getCheckedRadioButtonId();
        radioWeightButton = (RadioButton) findViewById(selectedId);
        Log.d("DeliveryLocation", "i am here");
        final HashMap<String, Object> insert = new HashMap<>();
        insert.put("ServiceType", spinner.getSelectedItem().toString());
        insert.put("MaterialDescription", material.getText().toString());
        insert.put("Weight", weight.getText().toString() + " " + radioWeightButton.getText());
        insert.put("TruckType", trucktype.getSelectedItem().toString());

        myref.child("users").child(currentuser).child("Bookings").child(orderid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("Delivery Location", "heyy");


                myref.child("users").child(currentuser).child("Bookings").child(orderid).child("ServiceTruckDetails").setValue(insert);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void openDialog() {
        ProfileDialog exampleDialog = new ProfileDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void applyTexts() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // myref.child("users").child(currentuser).child("Bookings").child(orderid).removeValue();
        finish();

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


}

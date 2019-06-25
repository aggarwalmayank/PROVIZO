package com.appsaga.provizo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AvailableServices extends AppCompatActivity implements com.appsaga.provizo.ProfileDialog.DialogListener {

    String serviceType;
    DatabaseReference databaseReference;
    ArrayList<Services> services;
    ListView serviceView;
    ServiceAdapter serviceAdapter;
    TextView nobooking;
    String currentuser, orderid;
    ImageView menuicon;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_services);
        menuicon = findViewById(R.id.menuicon);
        nobooking=findViewById(R.id.nobook);
        TextView tv = findViewById(R.id.appnamesigninup);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);
        menuicon = findViewById(R.id.menuicon);
        serviceType = getIntent().getStringExtra("type of service");
        serviceView = findViewById(R.id.service_list);
        services = new ArrayList<>();

        orderid = getIntent().getStringExtra("Order ID");
        currentuser = getIntent().getStringExtra("Current User");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        dl = (DrawerLayout) findViewById(R.id.deliverylocation);
        t = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);
        dl.addDrawerListener(t);
        t.syncState();

        nv = (NavigationView) findViewById(R.id.nv);
        menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.openDrawer(Gravity.LEFT);
            }
        });
        View headerview = nv.getHeaderView(0);
        TextView profilename = (TextView) headerview.findViewById(R.id.profile);
        profilename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("profile");
            }
        });
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {

                    case R.id.wallet:
                        Toast.makeText(AvailableServices.this, "Wallet", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.partnerlogin:
                       // Toast.makeText(AvailableServices.this, "partenr login", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mybooking:
                        startActivity(new Intent(AvailableServices.this, MyBookings.class));
                        break;
                    case R.id.newbooking:
                        Intent i = new Intent(AvailableServices.this, Bookingchoice.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                       // Toast.makeText(AvailableServices.this, "New Booking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.ratechart:
                        Toast.makeText(AvailableServices.this, "Rate Chart", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notification:

                        Toast.makeText(AvailableServices.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addcard:
                        startActivity(new Intent(AvailableServices.this, AddCard.class));
                      //  Toast.makeText(AvailableServices.this, "add card", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.support:
                      //  Toast.makeText(AvailableServices.this, "Support", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        startActivity(new Intent(AvailableServices.this, AboutUs.class));
                       // Toast.makeText(AvailableServices.this, "about us", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.signout:
                        Toast.makeText(AvailableServices.this, "SignOut", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(AvailableServices.this, SignInUp.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        return true;
                }
                return true;

            }
        });
        final ProgressDialog progressDialog = ProgressDialog.show(AvailableServices.this,"Loading","Please Wait...",true);
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

                            if (!dataSnapshot.child(s).child("operations").child("truckStatus").getValue(String.class).equalsIgnoreCase("All Booked")) {
                                String companyName = null;
                                String price = "0";

                                companyName = dataSnapshot.child(s).child("operations").child("companyName").getValue(String.class);

                                HashMap<String, Long> locationMap = (HashMap<String, Long>) dataSnapshot.
                                        child(s).child("operations").child("locationMap").child(getIntent()
                                        .getStringExtra("pickup")).getValue();

                                String priceVal = "0";

                                if (locationMap != null) {
                                    for (HashMap.Entry<String, Long> entry : locationMap.entrySet()) {
                                        if (entry.getKey().equalsIgnoreCase(getIntent().getStringExtra("drop"))) {
                                            priceVal = entry.getValue() + "";
                                            break;
                                        }
                                    }
                                }

                                price = "" + Double.parseDouble(getIntent().getStringExtra("weightnounit")) * Double.parseDouble(priceVal);

                                services.add(new Services(companyName, price));
                            }
                            if (!services.isEmpty()) {
                                nobooking.setVisibility(View.INVISIBLE);
                                serviceAdapter = new ServiceAdapter(AvailableServices.this, services);
                                serviceView.setAdapter(serviceAdapter);
                            }
                            else{
                                nobooking.setVisibility(View.VISIBLE);
                            }
                        }
                        Handler h=new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                progressDialog.dismiss();
                            }
                        },1800);

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

                final TextView company = view.findViewById(R.id.comp_name);
                TextView price = view.findViewById(R.id.price);

                Intent i = new Intent(AvailableServices.this, CompanyInfo.class);
                i.putExtra("Order ID",orderid);
                i.putExtra("Current User",currentuser);
                i.putExtra("type of service",getIntent().getStringExtra("type of service"));
                i.putExtra("pickup",getIntent().getStringExtra("pickup"));
                i.putExtra("drop",getIntent().getStringExtra("drop"));
                i.putExtra("date",getIntent().getStringExtra("date"));
                i.putExtra("weight",getIntent().getStringExtra("weight"));
                i.putExtra("Material",getIntent().getStringExtra("Material"));
                i.putExtra("truck",getIntent().getStringExtra("truck"));
                i.putExtra("company",company.getText());
                i.putExtra("amount", price.getText().toString());
                startActivity(i);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    public void openDialog(String a) {
        if (a.equals("partner")) {
            PartnerDialog dialog = new PartnerDialog();
            dialog.show(getSupportFragmentManager(), "example dialog");
        } else {
            ProfileDialog exampleDialog = new ProfileDialog();
            exampleDialog.show(getSupportFragmentManager(), "example dialog");
        }
    }

    @Override
    public void applyTexts() {

    }
}

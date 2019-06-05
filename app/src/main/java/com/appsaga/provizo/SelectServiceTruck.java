package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectServiceTruck extends AppCompatActivity implements com.appsaga.provizo.ProfileDialog.DialogListener {

    ImageButton service;
    private RadioGroup radioWeightGroup;
    private RadioButton radioWeightButton;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    ImageView menuicon;
    String orderid,currentuser;
    DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service_truck);

        radioWeightGroup=(RadioGroup)findViewById(R.id.radiogroup);
        myref = FirebaseDatabase.getInstance().getReference();

        TextView tv=findViewById(R.id.appnamesignupsecond);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);

        Bundle bundle = getIntent().getExtras();
        orderid=bundle.getString("Order ID");
        currentuser=bundle.getString("Current User");

        menuicon=findViewById(R.id.menuicon);
        service = findViewById(R.id.servicesbuttons);
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=radioWeightGroup.getCheckedRadioButtonId();
                radioWeightButton=(RadioButton)findViewById(selectedId);
                Toast.makeText(SelectServiceTruck.this,radioWeightButton.getText(),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SelectServiceTruck.this,com.appsaga.provizo.consignor_details.class));
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.boxspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.service, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        Spinner trucktype = (Spinner) findViewById(R.id.truckspinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.trucktype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trucktype.setAdapter(adapter2);

        dl = (DrawerLayout)findViewById(R.id.deliverylocation);
        t = new ActionBarDrawerToggle(this, dl,R.string.open, R.string.close);
        dl.addDrawerListener(t);
        t.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView)findViewById(R.id.nv);
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
                openDialog();
            }
        });
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {

                    case R.id.wallet:
                        Toast.makeText(SelectServiceTruck.this, "Wallet",Toast.LENGTH_SHORT).show();break;
                    case R.id.partnerlogin:
                        Toast.makeText(SelectServiceTruck.this, "partenr login",Toast.LENGTH_SHORT).show();break;
                    case R.id.mybooking:
                        Toast.makeText(SelectServiceTruck.this, "My Booking",Toast.LENGTH_SHORT).show();break;
                    case R.id.newbooking:
                        Toast.makeText(SelectServiceTruck.this, "New Booking",Toast.LENGTH_SHORT).show();break;
                    case R.id.ratechart:
                        Toast.makeText(SelectServiceTruck.this, "Rate Chart",Toast.LENGTH_SHORT).show();break;
                    case R.id.notification:
                        Toast.makeText(SelectServiceTruck.this, "Notification",Toast.LENGTH_SHORT).show();break;
                    case R.id.addcard:
                        startActivity(new Intent(SelectServiceTruck.this,AddCard.class));
                        Toast.makeText(SelectServiceTruck.this, "add card",Toast.LENGTH_SHORT).show();break;
                    case R.id.support:
                        Toast.makeText(SelectServiceTruck.this, "Support",Toast.LENGTH_SHORT).show();break;
                    case R.id.about:
                        startActivity(new Intent(SelectServiceTruck.this,AboutUs.class));
                        Toast.makeText(SelectServiceTruck.this, "about us",Toast.LENGTH_SHORT).show();break;
                    case R.id.signout:
                        Toast.makeText(SelectServiceTruck.this, "SignOut",Toast.LENGTH_SHORT).show();
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
      myref.child("users").child(currentuser).child("Bookings").child(orderid).removeValue();

    }
}

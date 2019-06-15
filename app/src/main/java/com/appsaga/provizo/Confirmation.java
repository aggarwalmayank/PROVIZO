package com.appsaga.provizo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Confirmation extends AppCompatActivity implements com.appsaga.provizo.ProfileDialog.DialogListener {
    String pickup, droploc, pickupdate, price;
    TextView amount, pick, drop, date, gstprice, totalprice, baseprice;
    DatabaseReference myref;
    Button back, book;
    String currentuser, orderid, company, gstconsignor, gstconsignee;
    double rs;
    LinearLayout fulldetail;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    ImageView menuicon;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        TextView tv = findViewById(R.id.appnamesigninup);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);
        myref = FirebaseDatabase.getInstance().getReference();
        dl = (DrawerLayout) findViewById(R.id.deliverylocation);
        t = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);
        dl.addDrawerListener(t);
        t.syncState();
        menuicon = findViewById(R.id.menuicon);
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
                openDialog();
            }
        });
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {

                    case R.id.wallet:
                        Toast.makeText(Confirmation.this, "Wallet", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.partnerlogin:
                        Toast.makeText(Confirmation.this, "partenr login", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mybooking:
                        Toast.makeText(Confirmation.this, "My Booking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.newbooking:
                        Intent gotoScreenVar = new Intent(Confirmation.this, DeliveryLocation.class);
                        gotoScreenVar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        myref.child("users").child(currentuser).child("Bookings").child(orderid).removeValue();
                        startActivity(gotoScreenVar);
                        Toast.makeText(Confirmation.this, "New Booking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.ratechart:
                        Toast.makeText(Confirmation.this, "Rate Chart", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notification:
                        Toast.makeText(Confirmation.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addcard:
                        startActivity(new Intent(Confirmation.this, AddCard.class));
                        Toast.makeText(Confirmation.this, "add card", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.support:
                        Toast.makeText(Confirmation.this, "Support", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        startActivity(new Intent(Confirmation.this, AboutUs.class));
                        Toast.makeText(Confirmation.this, "about us", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.signout:
                        Toast.makeText(Confirmation.this, "SignOut", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Confirmation.this,SignInUp.class));
                        finish();
                        break;
                    default:
                        return true;
                }
                return true;

            }
        });

        amount = findViewById(R.id.amount);
        drop = findViewById(R.id.drop);
        pick = findViewById(R.id.pickup);
        date = findViewById(R.id.date);
        back = findViewById(R.id.back);
        book = findViewById(R.id.book);
        amount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ratechart, 0, 0, 0);
        fulldetail = findViewById(R.id.fulldetails);
        fulldetail.setVisibility(View.INVISIBLE);
        gstprice = findViewById(R.id.tax);
        baseprice = findViewById(R.id.baseprice);
        totalprice = findViewById(R.id.finalprice);


        price = getIntent().getStringExtra("amount");
        pickup = getIntent().getStringExtra("pickup");
        droploc = getIntent().getStringExtra("drop");
        pickupdate = getIntent().getStringExtra("date");
        orderid = getIntent().getStringExtra("Order ID");
        company = getIntent().getStringExtra("company");
        currentuser = getIntent().getStringExtra("Current User");
        gstconsignee = getIntent().getStringExtra("consignee gst");
        gstconsignor = getIntent().getStringExtra("consignee gst");
        rs = Double.parseDouble(price);
        if (gstconsignor.isEmpty() || gstconsignee.isEmpty())
            amount.setText("Rs " + price);
        else {
            if (rs > 1500) {

                amount.setText("Rs " + String.valueOf(rs + rs * 0.05));
                amount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ratechart, 0, R.drawable.arrowdown, 0);
                amount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (fulldetail.getVisibility() == View.INVISIBLE) {
                            fulldetail.setVisibility(View.VISIBLE);
                            baseprice.setText("Base Price: "+price);
                            gstprice.setText("GST(5%): "+String.valueOf(Double.parseDouble(price)*0.05));
                            totalprice.setText("Total Price: "+String.valueOf(Double.parseDouble(price)+ Double.parseDouble(price)* 0.05));
                        }
                        else if(fulldetail.getVisibility()==View.VISIBLE){
                            fulldetail.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            } else
                amount.setText("Rs " + rs);
        }


        rs = rs + rs * 0.18;


        pick.setText(pickup);
        drop.setText(droploc);
        date.setText(pickupdate);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Confirmation.this, Payment.class);
               // addtofirebase();
                i.putExtra("amount", amount.getText().toString().substring(3));
                i.putExtra("Order ID",orderid);
                i.putExtra("Current User",currentuser);
                i.putExtra("type of service",getIntent().getStringExtra("type of service"));
                i.putExtra("pickup",getIntent().getStringExtra("pickup"));
                i.putExtra("drop",getIntent().getStringExtra("drop"));
                i.putExtra("date",getIntent().getStringExtra("date"));
                i.putExtra("weight",getIntent().getStringExtra("weight"));
                i.putExtra("Material",getIntent().getStringExtra("Material"));
                i.putExtra("truck",getIntent().getStringExtra("truck"));
                i.putExtra("company",getIntent().getStringExtra("company"));
                i.putExtra("consignorname",getIntent().getStringExtra("consignorname"));
                i.putExtra("consignoraddress",getIntent().getStringExtra("consignoraddress"));
                i.putExtra("consignorphone",getIntent().getStringExtra("consignorphone"));
                i.putExtra("consignor gst",gstconsignor);
                i.putExtra("drop",getIntent().getStringExtra("drop"));
                i.putExtra("consignee gst",gstconsignee);
                i.putExtra("consigneename",getIntent().getStringExtra("consigneename"));
                i.putExtra("consigneeaddress",getIntent().getStringExtra("consigneeaddress"));
                i.putExtra("consigneephone",getIntent().getStringExtra("consigneephone"));
                startActivity(i);
            }
        });

    }
    public void addtofirebase(){
        myref.child("users").child(currentuser).child("Bookings").child(orderid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myref.child("users").child(currentuser).child("Bookings").child(orderid).child("amount").setValue(amount.getText().toString().substring(3));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      //  myref.child("users").child(currentuser).child("Bookings").child(orderid).child("Consignee").removeValue();
        finish();
    }
    public void openDialog() {
        ProfileDialog exampleDialog = new ProfileDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void applyTexts() {

    }

}

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class consignee_details extends AppCompatActivity implements com.appsaga.provizo.ProfileDialog.DialogListener {

    ImageButton pay;
    EditText name, address, number, gst;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    ImageView menuicon;
    String currentuser, orderid, amount,company,gstconsignor;
    DatabaseReference mRef;
    String regex = "\\d+";
    String regex2 = "^[a-zA-Z]*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignee_details);

        TextView tv = findViewById(R.id.appnamesignupsecond);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);
        menuicon = findViewById(R.id.menuicon);

        gst = findViewById(R.id.consigneegst);
        number = findViewById(R.id.consigneephone);
        name = findViewById(R.id.consigneename);
        address = findViewById(R.id.consigneeadd);

        pay = findViewById(R.id.pay);

        currentuser = getIntent().getStringExtra("Current User");
        orderid = getIntent().getStringExtra("Order ID");
        amount = getIntent().getStringExtra("amount");
        company=getIntent().getStringExtra("company");
        gstconsignor=getIntent().getStringExtra("consignor gst");
        mRef = FirebaseDatabase.getInstance().getReference();



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
        TextView mobno = (TextView) headerview.findViewById(R.id.mob_no);
        mobno.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
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
                        Toast.makeText(consignee_details.this, "Wallet", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.partnerlogin:
                        Toast.makeText(consignee_details.this, "partenr login", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mybooking:
                        Toast.makeText(consignee_details.this, "My Booking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.newbooking:
                        Intent gotoScreenVar = new Intent(consignee_details.this, DeliveryLocation.class);
                        gotoScreenVar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mRef.child("users").child(currentuser).child("Bookings").child(orderid).removeValue();
                        startActivity(gotoScreenVar);
                        Toast.makeText(consignee_details.this, "New Booking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.ratechart:
                        Toast.makeText(consignee_details.this, "Rate Chart", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notification:
                        Toast.makeText(consignee_details.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addcard:
                        startActivity(new Intent(consignee_details.this, AddCard.class));
                        Toast.makeText(consignee_details.this, "add card", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.support:
                        startActivity(new Intent(consignee_details.this, Support.class));
                        Toast.makeText(consignee_details.this, "Support", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        startActivity(new Intent(consignee_details.this, AboutUs.class));
                        Toast.makeText(consignee_details.this, "about us", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.signout:
                        Toast.makeText(consignee_details.this, "SignOut", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(consignee_details.this,SignInUp.class));
                        finish();
                        break;
                    default:
                        return true;
                }
                return true;

            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = gst.getText().toString();
                if (name.getText().toString().equalsIgnoreCase(""))
                    name.setError("Enter Name");
                else if (address.getText().toString().equalsIgnoreCase(""))
                    address.setError("Enter Address");
                else if (!gst.getText().toString().isEmpty()) {
                    if (gst.getText().toString().length() != 15)
                        gst.setError("Invalid Number");
                  /*  else if (!(p.substring(5, 9)).matches(regex) || !(p.substring(2, 7)).matches(regex2) || !(p.substring(11)).matches(regex2) || !(p.substring(13)).matches(regex2)
                            || !(p.substring(0, 2)).matches(regex) || !(p.substring(14)).matches(regex) || !(p.substring(12)).matches(regex))
                        gst.setError("Invalid Number");*/
                    else if (number.getText().toString().equalsIgnoreCase(""))
                        number.setError("Enter Number");
                    else if (number.getText().toString().length() != 10)
                        number.setError("Invalid Error");
                    else {
                      //  addtofirebase();
                        Intent i = new Intent(consignee_details.this, Confirmation.class);
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
                        i.putExtra("amount", getIntent().getStringExtra("amount"));
                        i.putExtra("consignorname",getIntent().getStringExtra("consignorname"));
                        i.putExtra("consignoraddress",getIntent().getStringExtra("consignoraddress"));
                        i.putExtra("consignorphone",getIntent().getStringExtra("consignorphone"));
                        i.putExtra("consignor gst",gstconsignor);
                        i.putExtra("drop",getIntent().getStringExtra("drop"));
                        i.putExtra("consignee gst",gst.getText().toString());
                        i.putExtra("consigneename",name.getText().toString());
                        i.putExtra("consigneeaddress",address.getText().toString());
                        i.putExtra("consigneephone",number.getText().toString());

                        startActivity(i);
                    }
                } else if (number.getText().toString().equalsIgnoreCase(""))
                    number.setError("Enter Number");
                else if (number.getText().toString().length() != 10)
                    number.setError("Invalid Error");
                else {
                    //addtofirebase();
                    Intent i = new Intent(consignee_details.this, Confirmation.class);
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
                    i.putExtra("amount", getIntent().getStringExtra("amount"));
                    i.putExtra("consignorname",getIntent().getStringExtra("consignorname"));
                    i.putExtra("consignoraddress",getIntent().getStringExtra("consignoraddress"));
                    i.putExtra("consignorphone",getIntent().getStringExtra("consignorphone"));
                    i.putExtra("consignor gst",gstconsignor);
                    i.putExtra("drop",getIntent().getStringExtra("drop"));
                    i.putExtra("consignee gst",gst.getText().toString());
                    i.putExtra("consigneename",name.getText().toString());
                    i.putExtra("consigneeaddress",address.getText().toString());
                    i.putExtra("consigneephone",number.getText().toString());
                    startActivity(i);
                }
            }
        });
    }

    public void addtofirebase() {
        final HashMap<String, Object> insert = new HashMap<>();
        insert.put("ConsigneeName", name.getText().toString());
        insert.put("PhoneNumber", number.getText().toString());
        insert.put("Address", address.getText().toString());
        insert.put("GST", gst.getText().toString());


        mRef.child("users").child(currentuser).child("Bookings").child(orderid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mRef.child("users").child(currentuser).child("Bookings").child(orderid).child("Consignee").setValue(insert);

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
       // mRef.child("users").child(currentuser).child("Bookings").child(orderid).child("Consignor").removeValue();
        finish();
    }
}

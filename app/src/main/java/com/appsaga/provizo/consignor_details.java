package com.appsaga.provizo;

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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
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


public class consignor_details extends AppCompatActivity implements com.appsaga.provizo.ProfileDialog.DialogListener {

    ImageButton consignor;
    EditText name, number, gst, address;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    ImageView menuicon;
    String regex = "\\d+";
    String regex2 = "^[a-zA-Z]*$";
    CheckBox c1, c2, c3, c4;
    DatabaseReference mRef;
    String orderid, currentuser, amount, company;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignor_details);

        TextView tv = findViewById(R.id.appnamesignupsecond);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);
        name = findViewById(R.id.consignorname);
        number = findViewById(R.id.consignorphone);
        gst = findViewById(R.id.consignorgst);
        menuicon = findViewById(R.id.menuicon);
        consignor = findViewById(R.id.consignorr);
        address = findViewById(R.id.consignoradd);
        c1 = findViewById(R.id.chkbox1);
        c2 = findViewById(R.id.chkbox2);
        c3 = findViewById(R.id.chkbox3);
        c4 = findViewById(R.id.chkbox4);

        mRef = FirebaseDatabase.getInstance().getReference();

        amount = getIntent().getStringExtra("amount");
        currentuser = getIntent().getStringExtra("Current User");
        orderid = getIntent().getStringExtra("Order ID");
        company = getIntent().getStringExtra("company");


        consignor.setOnClickListener(new View.OnClickListener() {
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
                  /*else if (!(p.substring(5, 9)).matches(regex) || !(p.substring(2, 7)).matches(regex2) || !(p.substring(11)).matches(regex2) || !(p.substring(13)).matches(regex2)
                        || !(p.substring(0, 2)).matches(regex) || !(p.substring(14)).matches(regex) || !(p.substring(12)).matches(regex))
                    gst.setError("Invalid Number");*/
                    else if (number.getText().toString().equalsIgnoreCase(""))
                        number.setError("Enter Number");
                    else if (number.getText().toString().length() != 10)
                        number.setError("Invalid Error");
                    else if (!c1.isChecked() || !c2.isChecked() || !c3.isChecked() || !c4.isChecked())
                        alertbox("Plese Check all boxes");
                    else {
                       // addtofirebase();

                        Intent i = new Intent(consignor_details.this, com.appsaga.provizo.consignee_details.class);
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
                        i.putExtra("consignor gst",gst.getText().toString());
                        i.putExtra("consignorname",name.getText().toString());
                        i.putExtra("consignoraddress",address.getText().toString());
                        i.putExtra("consignorphone",number.getText().toString());
                        i.putExtra("drop",getIntent().getStringExtra("drop"));

                        startActivity(i);
                    }
                } else if (number.getText().toString().equalsIgnoreCase(""))
                    number.setError("Enter Number");
                else if (number.getText().toString().length() != 10)
                    number.setError("Invalid Error");
                else if (!c1.isChecked() || !c2.isChecked() || !c3.isChecked() || !c4.isChecked())
                    alertbox("Plese Check all boxes");
                else {
                  //  addtofirebase();

                    Intent i = new Intent(consignor_details.this, com.appsaga.provizo.consignee_details.class);
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
                    i.putExtra("consignor gst",gst.getText().toString());
                    i.putExtra("consignorname",name.getText().toString());
                    i.putExtra("consignoraddress",address.getText().toString());
                    i.putExtra("consignorphone",number.getText().toString());
                    i.putExtra("drop",getIntent().getStringExtra("drop"));
                    startActivity(i);
                }
            }
        });

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
                        Toast.makeText(consignor_details.this, "Wallet", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.partnerlogin:
                        Toast.makeText(consignor_details.this, "partenr login", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mybooking:
                        Toast.makeText(consignor_details.this, "My Booking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.newbooking:
                        Intent gotoScreenVar = new Intent(consignor_details.this, DeliveryLocation.class);
                        gotoScreenVar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mRef.child("users").child(currentuser).child("Bookings").child(orderid).removeValue();
                        startActivity(gotoScreenVar);
                        Toast.makeText(consignor_details.this, "New Booking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.ratechart:
                        Toast.makeText(consignor_details.this, "Rate Chart", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notification:
                        Toast.makeText(consignor_details.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addcard:
                        startActivity(new Intent(consignor_details.this, AddCard.class));
                        Toast.makeText(consignor_details.this, "add card", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.support:

                        startActivity(new Intent(consignor_details.this, Support.class));
                        Toast.makeText(consignor_details.this, "Support", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        startActivity(new Intent(consignor_details.this, AboutUs.class));
                        Toast.makeText(consignor_details.this, "about us", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.signout:
                        Toast.makeText(consignor_details.this, "SignOut", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(consignor_details.this,SignInUp.class));
                        finish();
                        break;
                    default:
                        return true;
                }
                return true;

            }
        });
    }

    public void addtofirebase() {
        final HashMap<String, Object> insert = new HashMap<>();
        insert.put("ConsignorName", name.getText().toString());
        insert.put("PhoneNumber", number.getText().toString());
        insert.put("Address", address.getText().toString());
        insert.put("GST", gst.getText().toString());

        mRef.child("users").child(currentuser).child("Bookings").child(orderid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mRef.child("users").child(currentuser).child("Bookings").child(orderid).child("Consignor").setValue(insert);
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
    public void onBackPressed() {
        super.onBackPressed();
       // mRef.child("users").child(currentuser).child("Bookings").child(orderid).child("TruckCompany").removeValue();
        finish();

    }
}
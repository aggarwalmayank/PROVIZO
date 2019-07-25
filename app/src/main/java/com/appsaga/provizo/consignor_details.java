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
import android.widget.Button;
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


public class consignor_details extends AppCompatActivity implements com.appsaga.provizo.ProfileDialog.DialogListener, MyBookingDialog.DialogListener {

    Button consignor;
    EditText name, number, gst, address;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    ImageView menuicon;
    String regex = "\\d+";
    String regex2 = "^[a-zA-Z]*$";
    CheckBox c1, c2, c3, c4;
    DatabaseReference mRef;
    String orderid, currentuser, amount, company, weightinton = "", weight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignor_details);

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
        c4.setVisibility(View.INVISIBLE);
        c2.setChecked(true);

        mRef = FirebaseDatabase.getInstance().getReference();

        amount = getIntent().getStringExtra("amount");
        currentuser = getIntent().getStringExtra("Current User");
        orderid = getIntent().getStringExtra("Order ID");
        company = getIntent().getStringExtra("company");
        weight = getIntent().getStringExtra("weight");

        setvisibilityofdoordelivery(weight);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c3.isChecked()) {
                    c1.setChecked(true);
                    c3.setChecked(false);
                }
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c1.isChecked()) {
                    c3.setChecked(true);
                    c1.setChecked(false);
                }
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c4.setChecked(false);
                c2.setChecked(true);
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c2.setChecked(false);
                c4.setChecked(true);
                alertbox("Extra 3000 Rs will be charged");
            }
        });

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
                    else if (number.getText().toString().equalsIgnoreCase(""))
                        number.setError("Enter Number");
                    else if (number.getText().toString().length() != 10)
                        number.setError("Invalid Error");

                    else if (!(c1.isChecked() || c3.isChecked())) {

                        alertbox("Please Check the boxes");
                    } else {
                        Intent i = new Intent(consignor_details.this, com.appsaga.provizo.consignee_details.class);
                        if (c1.isChecked()) {
                            i.putExtra("ownerrisk","yes");
                        }
                        else{
                            i.putExtra("ownerrisk","no");
                        }
                        if (c4.isChecked()) {

                            i.putExtra("Order ID", orderid);
                            i.putExtra("Current User", currentuser);
                            i.putExtra("type of service", getIntent().getStringExtra("type of service"));
                            i.putExtra("pickup", getIntent().getStringExtra("pickup"));
                            i.putExtra("drop", getIntent().getStringExtra("drop"));
                            i.putExtra("date", getIntent().getStringExtra("date"));
                            i.putExtra("weight", getIntent().getStringExtra("weight"));
                            i.putExtra("Material", getIntent().getStringExtra("Material"));
                            i.putExtra("truck", getIntent().getStringExtra("truck"));
                            i.putExtra("company", getIntent().getStringExtra("company"));
                            i.putExtra("amount", String.valueOf(3000 + (Double.parseDouble(getIntent().getStringExtra("amount")))));
                            i.putExtra("consignor gst", gst.getText().toString());
                            i.putExtra("consignorname", name.getText().toString());
                            i.putExtra("consignoraddress", address.getText().toString());
                            i.putExtra("doordelivery", "yes");
                            i.putExtra("consignorphone", number.getText().toString());
                            i.putExtra("drop", getIntent().getStringExtra("drop"));
                            startActivity(i);
                        } else {
                            i = new Intent(consignor_details.this, com.appsaga.provizo.consignee_details.class);
                            if (c1.isChecked()) {
                                i.putExtra("ownerrisk","yes");
                            }
                            else{
                                i.putExtra("ownerrisk","no");
                            }
                            i.putExtra("Order ID", orderid);
                            i.putExtra("Current User", currentuser);
                            i.putExtra("type of service", getIntent().getStringExtra("type of service"));
                            i.putExtra("pickup", getIntent().getStringExtra("pickup"));
                            i.putExtra("drop", getIntent().getStringExtra("drop"));
                            i.putExtra("date", getIntent().getStringExtra("date"));
                            i.putExtra("weight", getIntent().getStringExtra("weight"));
                            i.putExtra("Material", getIntent().getStringExtra("Material"));
                            i.putExtra("truck", getIntent().getStringExtra("truck"));
                            i.putExtra("company", getIntent().getStringExtra("company"));
                            i.putExtra("amount", getIntent().getStringExtra("amount"));
                            i.putExtra("consignor gst", gst.getText().toString());
                            i.putExtra("consignorname", name.getText().toString());
                            i.putExtra("consignoraddress", address.getText().toString());
                            i.putExtra("consignorphone", number.getText().toString());
                            i.putExtra("drop", getIntent().getStringExtra("drop"));
                            i.putExtra("doordelivery", "no");
                            startActivity(i);
                        }
                    }
                } else if (number.getText().toString().equalsIgnoreCase(""))
                    number.setError("Enter Number");
                else if (number.getText().toString().length() != 10)
                    number.setError("Invalid Error");
                else if (!(c1.isChecked() || c3.isChecked())) {

                    alertbox("Please Check the boxes");
                } else {

                    Intent i= new Intent(consignor_details.this, com.appsaga.provizo.consignee_details.class);
                    if (c1.isChecked()) {
                        i.putExtra("ownerrisk","yes");
                    }
                    else{
                        i.putExtra("ownerrisk","no");
                    }
                    if (c4.isChecked()) {

                        i.putExtra("Order ID", orderid);
                        i.putExtra("Current User", currentuser);
                        i.putExtra("type of service", getIntent().getStringExtra("type of service"));
                        i.putExtra("pickup", getIntent().getStringExtra("pickup"));
                        i.putExtra("drop", getIntent().getStringExtra("drop"));
                        i.putExtra("date", getIntent().getStringExtra("date"));
                        i.putExtra("weight", getIntent().getStringExtra("weight"));
                        i.putExtra("Material", getIntent().getStringExtra("Material"));
                        i.putExtra("truck", getIntent().getStringExtra("truck"));
                        i.putExtra("company", getIntent().getStringExtra("company"));
                        i.putExtra("amount", String.valueOf(3000 + (Double.parseDouble(getIntent().getStringExtra("amount")))));
                        i.putExtra("consignor gst", gst.getText().toString());
                        i.putExtra("consignorname", name.getText().toString());
                        i.putExtra("consignoraddress", address.getText().toString());
                        i.putExtra("consignorphone", number.getText().toString());
                        i.putExtra("drop", getIntent().getStringExtra("drop"));
                        i.putExtra("doordelivery", "yes");
                        startActivity(i);
                    } else {
                        i = new Intent(consignor_details.this, com.appsaga.provizo.consignee_details.class);
                        if (c1.isChecked()) {
                            i.putExtra("ownerrisk","yes");
                        }
                        else{
                            i.putExtra("ownerrisk","no");
                        }
                        i.putExtra("Order ID", orderid);
                        i.putExtra("Current User", currentuser);
                        i.putExtra("type of service", getIntent().getStringExtra("type of service"));
                        i.putExtra("pickup", getIntent().getStringExtra("pickup"));
                        i.putExtra("drop", getIntent().getStringExtra("drop"));
                        i.putExtra("date", getIntent().getStringExtra("date"));
                        i.putExtra("weight", getIntent().getStringExtra("weight"));
                        i.putExtra("Material", getIntent().getStringExtra("Material"));
                        i.putExtra("truck", getIntent().getStringExtra("truck"));
                        i.putExtra("company", getIntent().getStringExtra("company"));
                        i.putExtra("amount", getIntent().getStringExtra("amount"));
                        i.putExtra("consignor gst", gst.getText().toString());
                        i.putExtra("consignorname", name.getText().toString());
                        i.putExtra("consignoraddress", address.getText().toString());
                        i.putExtra("consignorphone", number.getText().toString());
                        i.putExtra("drop", getIntent().getStringExtra("drop"));
                        i.putExtra("doordelivery", "no");
                        startActivity(i);
                    }
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
                openDialog("profile");
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
                        openDialog("Booking");
                        break;
                    case R.id.newbooking:
                        Intent gotoScreenVar = new Intent(consignor_details.this, Bookingchoice.class);
                        gotoScreenVar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(gotoScreenVar);
                        break;
                    case R.id.ratechart:
                        Toast.makeText(consignor_details.this, "Rate Chart", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notification:
                        Toast.makeText(consignor_details.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addcard:
                        startActivity(new Intent(consignor_details.this, AddCard.class));
                        break;
                    case R.id.support:
                        startActivity(new Intent(consignor_details.this, Support.class));
                        break;
                    case R.id.about:
                        startActivity(new Intent(consignor_details.this, AboutUs.class));
                        break;
                    case R.id.signout:
                        Toast.makeText(consignor_details.this, "SignOut", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(consignor_details.this, SignInUp.class);
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
    }


    public void openDialog(String a) {
        if (a.equals("partner")) {
            PartnerDialog dialog = new PartnerDialog();
            dialog.show(getSupportFragmentManager(), "example dialog");
        } else if (a.equals("Booking")) {
            MyBookingDialog dialog = new MyBookingDialog();
            dialog.show(getSupportFragmentManager(), "example dialog");
        } else {
            ProfileDialog exampleDialog = new ProfileDialog();
            exampleDialog.show(getSupportFragmentManager(), "example dialog");
        }
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

    public void setvisibilityofdoordelivery(String weight) {
        for (int i = 0; i < weight.length(); i++) {
            if ((weight.charAt(i) == 'Q')) {
                break;
            } else {
                weightinton = weightinton + weight.charAt(i);
            }
        }
        double wtinton = Double.parseDouble(weightinton) * 0.1;
        if (wtinton > 3) {
            c4.setVisibility(View.VISIBLE);
        } else {
            c4.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
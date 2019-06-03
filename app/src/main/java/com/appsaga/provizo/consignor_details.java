package com.appsaga.provizo;

import android.content.Intent;
import android.database.Cursor;
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


public class consignor_details extends AppCompatActivity implements com.appsaga.provizo.ProfileDialog.DialogListener{

    ImageButton consignor;
    DatabaseHelperUser db;
    EditText name, number,gst;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    ImageView menuicon;
    boolean isupdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignor_details);
        db=new DatabaseHelperUser(this);

        TextView tv = findViewById(R.id.appnamesignupsecond);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);
        name = findViewById(R.id.consignorname);
        number = findViewById(R.id.consignorphone);
        gst=findViewById(R.id.consignorgst);
        menuicon = findViewById(R.id.menuicon);
        consignor = findViewById(R.id.consignorr);
        setDetails();
        consignor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(consignor_details.this, FirebaseAuth.getInstance().getCurrentUser().getEmail().trim(), Toast.LENGTH_SHORT).show();
                Cursor res1 = db.GetOneData( FirebaseAuth.getInstance().getCurrentUser().getEmail());
                if (res1.getCount() == 0) {
                    Toast.makeText(consignor_details.this, "No Data", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    while (res1.moveToNext()) {
                        if(!(res1.getString(6).equals("")))
                            isupdate=db.updateSingleCol("GST",gst.getText().toString(),
                                    FirebaseAuth.getInstance().getCurrentUser().getEmail().trim());
                        if(isupdate)
                            Toast.makeText(consignor_details.this, "hogya update", Toast.LENGTH_SHORT).show();
                    }
                }

                startActivity(new Intent(consignor_details.this, com.appsaga.provizo.consignee_details.class));
            }
        });

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
                        Toast.makeText(consignor_details.this, "Support", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        startActivity(new Intent(consignor_details.this, AboutUs.class));
                        Toast.makeText(consignor_details.this, "about us", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.signout:
                        Toast.makeText(consignor_details.this, "SignOut", Toast.LENGTH_SHORT).show();
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

    public void setDetails(){
        Cursor res = db.GetOneData(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        if (res.getCount() == 0) {
            Toast.makeText(consignor_details.this, "No Data", Toast.LENGTH_SHORT).show();
            return;
        } else {
            while (res.moveToNext()) {
                name.setText(res.getString(1));
                number.setText(res.getString(2));
                if(!(res.getString(6).equals("")))
                    gst.setText(res.getString(6));
            }
        }
    }
    public void openDialog() {
        ProfileDialog exampleDialog = new ProfileDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void applyTexts() {

    }
}
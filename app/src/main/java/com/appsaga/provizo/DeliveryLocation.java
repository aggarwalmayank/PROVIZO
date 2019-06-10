package com.appsaga.provizo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.support.design.widget.NavigationView;

public class DeliveryLocation extends AppCompatActivity implements com.appsaga.provizo.ProfileDialog.DialogListener{

    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;
    ImageButton deliveryNext;
    EditText pickupdate, droploc, pickuploc;
    DatabaseHelperUser db;
    ImageView menuicon;
    String  orderid;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private static final String TAG = "DeliveryLoc";
    private  ValueEventListener mQueryListener;
    Intent tonext;

    private static final int ERROR_DIALOG_REQUEST = 9001;

    DatabaseReference myref = FirebaseDatabase.getInstance().getReference();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_location);

        db = new DatabaseHelperUser(this);

        TextView tv = findViewById(R.id.appnamesignupsecond);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);

        menuicon = findViewById(R.id.menuicon);
        pickuploc = findViewById(R.id.pickuploc);
        droploc = findViewById(R.id.droploc);
        pickupdate = findViewById(R.id.pickupdate);
        deliveryNext = findViewById(R.id.delivery_next);

        final Calendar calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);
        pickupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
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
                        Toast.makeText(DeliveryLocation.this, "Wallet", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.partnerlogin:
                        dl.closeDrawer(Gravity.LEFT);
                        //openDialog("partner");
                        Toast.makeText(DeliveryLocation.this, "partenr login", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mybooking:
                        Toast.makeText(DeliveryLocation.this, "My Booking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.newbooking:
                        dl.closeDrawer(Gravity.LEFT);
                        Toast.makeText(DeliveryLocation.this, "New Booking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.ratechart:
                        Toast.makeText(DeliveryLocation.this, "Rate Chart", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notification:

                        Toast.makeText(DeliveryLocation.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addcard:
                        startActivity(new Intent(DeliveryLocation.this, AddCard.class));
                        Toast.makeText(DeliveryLocation.this, "add card", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.support:
                        Toast.makeText(DeliveryLocation.this, "Support", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        startActivity(new Intent(DeliveryLocation.this, AboutUs.class));
                        Toast.makeText(DeliveryLocation.this, "about us", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.signout:
                        Toast.makeText(DeliveryLocation.this, "SignOut", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        break;
                    default:
                        return true;
                }
                return true;

            }
        });
        /*FirebaseUser mAuth=FirebaseAuth.getInstance().getCurrentUser();

        current_user = mAuth.getDisplayName()+"-"+mAuth.getPhoneNumber();
        Toast.makeText(this, current_user, Toast.LENGTH_SHORT).show();
        myref.child("users").child(current_user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().child("Email Verification").setValue("Verified");


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
*/
        /*Cursor res = db.GetOneData(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        if (res.getCount() == 0) {
            Toast.makeText(DeliveryLocation.this, "error", Toast.LENGTH_SHORT).show();
            return;
        } else {
            while (res.moveToNext()) {
                current_user = res.getString(1) + "-" + res.getString(3) + "-" + res.getString(2);

            }

        }
*/

        deliveryNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickuploc.getText().toString().equalsIgnoreCase(""))
                    pickuploc.setError("Invalid Location");
                else if (droploc.getText().toString().equalsIgnoreCase(""))
                    droploc.setError("Invalid Location");
                else if (pickupdate.getText().toString().equalsIgnoreCase(""))
                    pickupdate.setError("Invalid Date");
                else {
                    DateFormat df = new SimpleDateFormat("yyMMddHHmmssZ", java.util.Locale.getDefault());
                    Date today = Calendar.getInstance().getTime();
                    orderid = df.format(today);
                    tonext= new Intent(DeliveryLocation.this, SelectServiceTruck.class);
                    AddtoFirebase();

                    tonext.putExtra("Order ID", orderid);
                    tonext.putExtra("pickup",pickuploc.getText().toString().toLowerCase());
                    tonext.putExtra("drop",droploc.getText().toString().toLowerCase());
                    tonext.putExtra("date",pickupdate.getText().toString());
                    //    Toast.makeText(DeliveryLocation.this, orderid, Toast.LENGTH_SHORT).show();
                    startActivity(tonext);
                }

            }
        });

    }

    public void AddtoFirebase() {

        Log.d("Delivery Location", "i am here");
        final HashMap<String, Object> insert = new HashMap<>();
        String pick,drop,date;
        pick=pickuploc.getText().toString().toLowerCase();
        drop=droploc.getText().toString().toLowerCase();
        date=pickupdate.getText().toString();
        insert.put("Pick Up Location", pick);
        insert.put("Drop Location", drop);
        insert.put("Pick Up Date", date);


        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        //Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
        tonext.putExtra("Current User", user.getUid());
        myref.child("users").child(user.getUid()).child("Bookings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("Delivery Location", "heyy");

                myref.child("users").child(user.getUid()).child("Bookings").child(orderid).setValue(insert);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        }
        return null;

    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month + 1;
            day_x = dayOfMonth;
            setpickup();
        }
    };

    public void setpickup() {
        Boolean flag = Boolean.TRUE;
        long timeInMilliseconds, timeinmuli = Calendar.getInstance().getTimeInMillis();
        String selectedDate = day_x + "-" + month_x + "-" + year_x;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            java.util.Date mDate = sdf.parse(selectedDate);
            timeInMilliseconds = mDate.getTime();
            if (timeInMilliseconds < timeinmuli)
                flag = Boolean.FALSE;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!flag) {
            alertbox("INVALID DATE");
        } else {
            pickupdate.setText(selectedDate);
        }

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


   /* @Override
    public void loginid(final String username) {
    mQueryListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(username))
                    {
                        Intent i=new Intent(DeliveryLocation.this,Partner.class);
                        i.putExtra("partner id",username);
                        startActivity(i);
                        break;
                    }
                }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
        Query query=mref.child("partners").orderByChild("partners");
        query.addValueEventListener(mQueryListener);
    }*/



}

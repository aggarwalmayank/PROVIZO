package com.appsaga.provizo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import android.support.design.widget.NavigationView;

public class DeliveryLocation extends AppCompatActivity implements com.appsaga.provizo.ProfileDialog.DialogListener ,MyBookingDialog.DialogListener {

    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;
    Button deliveryNext;
    EditText pickupdate;
    AutoCompleteTextView droploc;
    AutoCompleteTextView    pickuploc;
    DatabaseHelperUser db;
    String orderid;
    ImageView menuicon;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private static final String TAG = "DeliveryLoc";
    Intent tonext;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    FirebaseUser user;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_location);

        db = new DatabaseHelperUser(this);
        menuicon = findViewById(R.id.menuicon);
        pickuploc = findViewById(R.id.pickuploc);
        droploc = findViewById(R.id.droploc);
        pickupdate = findViewById(R.id.pickupdate);
        deliveryNext = findViewById(R.id.delivery_next);
        databaseReference = FirebaseDatabase.getInstance().getReference();

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
        TextView mobno = (TextView) headerview.findViewById(R.id.mob_no);
        mobno.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
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
                        Toast.makeText(DeliveryLocation.this, "partenr login", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mybooking:
                        openDialog("Booking");
                        break;
                    case R.id.newbooking:
                        onBackPressed();
                        break;
                    case R.id.ratechart:
                        Toast.makeText(DeliveryLocation.this, "Rate Chart", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notification:

                        Toast.makeText(DeliveryLocation.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addcard:
                        startActivity(new Intent(DeliveryLocation.this, AddCard.class));
                        break;
                    case R.id.support:
                        startActivity(new Intent(DeliveryLocation.this, Support.class));
                        break;
                    case R.id.about:
                        startActivity(new Intent(DeliveryLocation.this, AboutUs.class));
                        break;
                    case R.id.signout:
                        Toast.makeText(DeliveryLocation.this, "SignOut", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(DeliveryLocation.this, SignInUp.class);
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

        databaseReference.child("partners").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final ArrayList<String> picklocs = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    HashMap<String, ArrayList<String>> hashMap;
                    HashMap<String, ArrayList<String>> hashMap1;
                    hashMap = (HashMap<String, ArrayList<String>>) ds.child("operations").child("locationMap").child("FullTruckLoad").child("open").getValue();

                    hashMap1 = (HashMap<String, ArrayList<String>>) ds.child("operations").child("locationMap").child("PartLoad").child("open").getValue();

                    HashMap<String, ArrayList<String>> hashMap2;
                    HashMap<String, ArrayList<String>> hashMap3;
                    hashMap2 = (HashMap<String, ArrayList<String>>) ds.child("operations").child("locationMap").child("FullTruckLoad").child("closed").getValue();

                    hashMap3 = (HashMap<String, ArrayList<String>>) ds.child("operations").child("locationMap").child("PartLoad").child("closed").getValue();
                    if (hashMap != null) {

                        for(HashMap.Entry<String, ArrayList<String>> entry : hashMap.entrySet())
                        {
                            if(!picklocs.contains(entry.getKey().trim()))
                            {
                                picklocs.add(entry.getKey().trim());
                            }
                        }
                    }
                    if (hashMap1 != null) {

                        for(HashMap.Entry<String, ArrayList<String>> entry : hashMap1.entrySet())
                        {
                            if(!picklocs.contains(entry.getKey().trim()))
                            {
                                picklocs.add(entry.getKey().trim());
                            }
                        }
                    }
                    if (hashMap2 != null) {

                        for(HashMap.Entry<String, ArrayList<String>> entry : hashMap2.entrySet())
                        {
                            if(!picklocs.contains(entry.getKey().trim()))
                            {
                                picklocs.add(entry.getKey().trim());
                            }
                        }
                    }
                    if (hashMap3 != null) {

                        for(HashMap.Entry<String, ArrayList<String>> entry : hashMap3.entrySet())
                        {
                            if(!picklocs.contains(entry.getKey().trim()))
                            {
                                picklocs.add(entry.getKey().trim());
                            }
                        }
                    }
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(DeliveryLocation.this, android.R.layout.simple_list_item_1, picklocs);
                pickuploc.setAdapter(arrayAdapter);

                pickuploc.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        databaseReference.child("partners").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final ArrayList<String> droplocs = new ArrayList<>();

                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    HashMap<String, HashMap<String, Integer>> hashMap;
                                    HashMap<String, HashMap<String, Integer>> hashMap1;
                                    HashMap<String, HashMap<String, Integer>> hashMap3;
                                    HashMap<String, HashMap<String, Integer>> hashMap4;

                                    hashMap = (HashMap<String, HashMap<String, Integer>>) ds.child("operations").child("locationMap").child("FullTruckLoad").child("open").getValue();
                                    hashMap1 = (HashMap<String, HashMap<String, Integer>>) ds.child("operations").child("locationMap").child("PartLoad").child("open").getValue();

                                    hashMap3 = (HashMap<String, HashMap<String, Integer>>) ds.child("operations").child("locationMap").child("FullTruckLoad").child("closed").getValue();
                                    hashMap4 = (HashMap<String, HashMap<String, Integer>>) ds.child("operations").child("locationMap").child("PartLoad").child("closed").getValue();

                                    if (hashMap != null) {

                                        for(HashMap.Entry<String, HashMap<String,Integer>> entry : hashMap.entrySet())
                                        {
                                            if(picklocs.contains(entry.getKey().trim()))
                                            {
                                                HashMap<String, Integer> hashMap2 = entry.getValue();
                                                for (HashMap.Entry<String, Integer> entry1 : hashMap2.entrySet()) {

                                                    if(!droplocs.contains(entry1.getKey()))
                                                    {
                                                        droplocs.add(entry1.getKey().trim());
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (hashMap1 != null) {

                                        for(HashMap.Entry<String, HashMap<String,Integer>> entry : hashMap1.entrySet())
                                        {
                                            if(picklocs.contains(entry.getKey().trim()))
                                            {
                                                HashMap<String, Integer> hashMap2 = entry.getValue();
                                                for (HashMap.Entry<String, Integer> entry1 : hashMap2.entrySet()) {

                                                    if(!droplocs.contains(entry1.getKey()))
                                                    {
                                                        droplocs.add(entry1.getKey().trim());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (hashMap3 != null) {

                                        for(HashMap.Entry<String, HashMap<String,Integer>> entry : hashMap3.entrySet())
                                        {
                                            if(picklocs.contains(entry.getKey().trim()))
                                            {
                                                HashMap<String, Integer> hashMap2 = entry.getValue();
                                                for (HashMap.Entry<String, Integer> entry1 : hashMap2.entrySet()) {

                                                    if(!droplocs.contains(entry1.getKey()))
                                                    {
                                                        droplocs.add(entry1.getKey().trim());
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (hashMap4 != null) {

                                        for(HashMap.Entry<String, HashMap<String,Integer>> entry : hashMap4.entrySet())
                                        {
                                            if(picklocs.contains(entry.getKey().trim()))
                                            {
                                                HashMap<String, Integer> hashMap2 = entry.getValue();
                                                for (HashMap.Entry<String, Integer> entry1 : hashMap2.entrySet()) {

                                                    if(!droplocs.contains(entry1.getKey()))
                                                    {
                                                        droplocs.add(entry1.getKey().trim());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(DeliveryLocation.this, android.R.layout.simple_list_item_1, droplocs);
                                droploc.setAdapter(null);
                                droploc.setAdapter(arrayAdapter);
                                droploc.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        if (droploc.getAdapter().isEmpty()) {
                                            droploc.setError("Please select other drop loc");
                                        }

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                deliveryNext.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (pickuploc.getText().toString().equalsIgnoreCase(""))
                                            pickuploc.setError("Invalid pick-up location");
                                        else if (droploc.getText().toString().equalsIgnoreCase("") || !droplocs.contains(droploc.getText().toString()))
                                            droploc.setError("Invalid drop location");
                                        else if (pickupdate.getText().toString().equalsIgnoreCase(""))
                                            pickupdate.setError("Invalid Date");
                                        else {
                                            DateFormat df = new SimpleDateFormat("yyMMddHHmmssZ", java.util.Locale.getDefault());
                                            Date today = Calendar.getInstance().getTime();
                                            orderid = df.format(today);
                                            orderid = orderid.substring(0, 12);
                                            tonext = new Intent(DeliveryLocation.this, SelectServiceTruck.class);
                                            tonext.putExtra("Order ID", orderid);
                                            tonext.putExtra("pickup", pickuploc.getText().toString().toLowerCase());
                                            tonext.putExtra("drop", droploc.getText().toString().toLowerCase());
                                            tonext.putExtra("date", pickupdate.getText().toString());
                                            startActivity(tonext);
                                        }

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            hideKeyboardwithoutPopulate(DeliveryLocation.this);
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
        long timeInMilliseconds, timeinmuli;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        timeinmuli = (c.getTimeInMillis() - 86400000/*-System.currentTimeMillis()*/);


        String finaldate = day_x + "-" + month_x + "-" + year_x;
        String selectedDate = day_x + "-" + month_x + "-" + year_x + " " + "00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        try {
            java.util.Date mDate = sdf.parse(selectedDate);
            timeInMilliseconds = mDate.getTime();
            // Toast.makeText(this, day_x + "-" + month_x + "-" + year_x+" "+"00:00:00"+"   "+timeInMilliseconds+"    "+timeinmuli, Toast.LENGTH_SHORT).show();
            if (timeInMilliseconds < timeinmuli)
                flag = Boolean.FALSE;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!flag) {
            alertbox("INVALID DATE");
        } else {
            pickupdate.setText(finaldate);

            if (pickuploc.getText().toString().equalsIgnoreCase(""))
                pickuploc.setError("Invalid pick-up location");
            else if (droploc.getText().toString().equalsIgnoreCase(""))
                droploc.setError("Invalid drop location");
            else if (pickupdate.getText().toString().equalsIgnoreCase(""))
                pickupdate.setError("Invalid Date");
            else {
                DateFormat df = new SimpleDateFormat("yyMMddHHmmssZ", java.util.Locale.getDefault());
                Date today = Calendar.getInstance().getTime();
                orderid = df.format(today);
                orderid = orderid.substring(0, 12);
                tonext = new Intent(DeliveryLocation.this, SelectServiceTruck.class);
                tonext.putExtra("Order ID", orderid);
                tonext.putExtra("pickup", pickuploc.getText().toString().toLowerCase());
                tonext.putExtra("drop", droploc.getText().toString().toLowerCase());
                tonext.putExtra("date", pickupdate.getText().toString());
                startActivity(tonext);
            }
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
        }
        else if(a.equals("Booking")){
            MyBookingDialog dialog = new MyBookingDialog();
            dialog.show(getSupportFragmentManager(), "example dialog");
        }
        else {
            ProfileDialog exampleDialog = new ProfileDialog();
            exampleDialog.show(getSupportFragmentManager(), "example dialog");
        }
    }

    @Override
    public void applyTexts() {

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText())
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
    }
}

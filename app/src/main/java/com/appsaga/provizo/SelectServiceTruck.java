package com.appsaga.provizo;

import android.annotation.SuppressLint;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.layout.simple_spinner_item;

public class SelectServiceTruck extends AppCompatActivity implements com.appsaga.provizo.ProfileDialog.DialogListener {
    EditText material, weight;
    ImageButton service;
    private RadioGroup radioWeightGroup;
    private RadioButton radioWeightButton;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    ImageView menuicon;
    String orderid, currentuser, WEIGHT, unit;
    DatabaseReference myref;
    Spinner spinner, trucktype;

    ArrayAdapter adapter2;

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
                R.array.service, simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardwithoutPopulate(SelectServiceTruck.this);
                return false;
            }
        });

        trucktype = (Spinner) findViewById(R.id.truckspinner);
        adapter2 = ArrayAdapter.createFromResource(SelectServiceTruck.this,
                R.array.trucktype, simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trucktype.setAdapter(adapter2);


        trucktype.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardwithoutPopulate(SelectServiceTruck.this);
                int selectedId = radioWeightGroup.getCheckedRadioButtonId();
                radioWeightButton = (RadioButton) findViewById(selectedId);
                if (weight.getText().toString().equalsIgnoreCase("")||selectedId == -1) {
                    weight.setError("Invalid Weight and unit ");
                }

                else {
                    WEIGHT = weight.getText().toString();
                    unit = radioWeightButton.getText().toString();
                    double wt = 0;
                    if (unit.equals("Kg")) {
                        wt = Double.parseDouble(WEIGHT) * 0.001;
                    } else if (unit.equals("Quintal")) {
                        wt = Double.parseDouble(WEIGHT) * 0.10;
                    } else wt = Double.parseDouble(WEIGHT);
                    setSpinner(wt);
                }
                return false;
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
                        startActivity(new Intent(SelectServiceTruck.this, MyBookings.class));
                        //Toast.makeText(SelectServiceTruck.this, "My Booking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.newbooking:
                        Intent i = new Intent(SelectServiceTruck.this, Bookingchoice.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                       // Toast.makeText(SelectServiceTruck.this, "New Booking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.ratechart:
                        Toast.makeText(SelectServiceTruck.this, "Rate Chart", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notification:
                        Toast.makeText(SelectServiceTruck.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addcard:
                        startActivity(new Intent(SelectServiceTruck.this, AddCard.class));
                        //Toast.makeText(SelectServiceTruck.this, "add card", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.support:
                        startActivity(new Intent(SelectServiceTruck.this, Support.class));
                       // Toast.makeText(SelectServiceTruck.this, "Support", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        startActivity(new Intent(SelectServiceTruck.this, AboutUs.class));
                        //Toast.makeText(SelectServiceTruck.this, "about us", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.signout:
                        Toast.makeText(SelectServiceTruck.this, "SignOut", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(SelectServiceTruck.this, SignInUp.class);
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
                else if (radioWeightGroup.getCheckedRadioButtonId() == -1)
                    alertbox("No Unit Selected");
                else if (trucktype.getSelectedItem().toString().equals("Select Truck Type"))
                    alertbox("Please Select Truck");
                else {
                    //AddtoFirebase();
                    String weight1 = "0";

                    if (radioWeightGroup.getCheckedRadioButtonId() == R.id.radiobtn1) {
                        weight1 = String.valueOf(Double.parseDouble(weight.getText().toString()) * (0.01));
                    } else if (radioWeightGroup.getCheckedRadioButtonId() == R.id.radiobtn2) {
                        weight1 = weight.getText().toString();
                    } else if (radioWeightGroup.getCheckedRadioButtonId() == R.id.radiobtn3) {
                        weight1 = String.valueOf(Double.parseDouble(weight.getText().toString()) * (10));
                    }

                    Intent i = new Intent(SelectServiceTruck.this, AvailableServices.class);
                    i.putExtra("Order ID", orderid);
                    i.putExtra("Current User", currentuser);
                    i.putExtra("type of service", spinner.getSelectedItem().toString());
                    i.putExtra("pickup", getIntent().getStringExtra("pickup"));
                    i.putExtra("drop", getIntent().getStringExtra("drop"));
                    i.putExtra("date", getIntent().getStringExtra("date"));
                    i.putExtra("weight", weight1 + " " + "Quintal");
                    i.putExtra("weightnounit", weight1);
                    i.putExtra("Material", material.getText().toString());
                    i.putExtra("truck", trucktype.getSelectedItem().toString());
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

    public void setSpinner(double wt) {
        if (wt <= 2.5) {
            List<String> list1 = new ArrayList<>();
            list1.clear();
            list1.add("Select Truck Type");
            list1.add("2.5 MT Closed");
            list1.add("2.5 MT Open");
            list1.add("3.5 MT Closed");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, android.R.layout.simple_spinner_item, list1);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt > 2.5 && wt <= 3.5) {
            List<String> list2 = new ArrayList<>();
            list2.clear();
            list2.add("Select Truck Type");
            list2.add("2.5 MT Closed");
            list2.add("2.5 MT Open");
            list2.add("3.5 MT Closed");
            list2.add("4 MT Open");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, android.R.layout.simple_spinner_item, list2);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt > 3.5 && wt <= 4) {
            List<String> list3 = new ArrayList<>();
            list3.clear();
            list3.add("Select Truck Type");
            list3.add("3.5 MT Closed");
            list3.add("4 MT Open");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, android.R.layout.simple_spinner_item, list3);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt > 4 && wt <= 5) {
            List<String> list4 = new ArrayList<>();
            list4.clear();
            list4.add("Select Truck Type");
            list4.add("4 MT Open");
            list4.add("5 MT Open");
            list4.add("7 MT Open");
            list4.add("7 MT Closed");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, android.R.layout.simple_spinner_item, list4);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt > 5 && wt <= 7) {
            List<String> list5 = new ArrayList<>();
            list5.clear();
            list5.add("Select Truck Type");
            list5.add("5 MT Open");
            list5.add("7 MT Open");
            list5.add("7 MT Closed");
            if (wt >= 6) {
                list5.add("9 MT Open");
                list5.add("9 MT Closed");
            }
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, android.R.layout.simple_spinner_item, list5);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt > 7 && wt <= 9) {
            List<String> list6 = new ArrayList<>();
            list6.clear();
            list6.add("Select Truck Type");
            list6.add("9 MT Open");
            list6.add("9 MT Closed");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, android.R.layout.simple_spinner_item, list6);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt > 9 && wt <= 16) {
            List<String> list7 = new ArrayList<>();
            list7.clear();
            list7.add("Select Truck Type");
            list7.add("9 MT Open");
            list7.add("9 MT Closed");
            list7.add("16 MT Open");
            list7.add("16 MT Closed");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, android.R.layout.simple_spinner_item, list7);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt > 16 && wt <= 21) {
            List<String> list8 = new ArrayList<>();
            list8.clear();
            list8.add("Select Truck Type");
            list8.add("16 MT Open");
            list8.add("16 MT Closed");
            list8.add("21 MT Open");
            list8.add("21 MT Closed");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, android.R.layout.simple_spinner_item, list8);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt > 21 && wt <= 26) {
            List<String> list9 = new ArrayList<>();
            list9.clear();
            list9.add("Select Truck Type");
            list9.add("21 MT Open");
            list9.add("21 MT Closed");
            list9.add("26 MT Open");
            list9.add("26 MT Closed");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, android.R.layout.simple_spinner_item, list9);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else {
            List<String> list10 = new ArrayList<>();
            list10.clear();
            list10.add("Select Truck Type");
            list10.add("2.5 MT Closed");
            list10.add("2.5 MT Open");
            list10.add("3.5 MT Closed");
            list10.add("4 MT Open");
            list10.add("5 MT Open");
            list10.add("7 MT Open");
            list10.add("7 MT Closed");
            list10.add("9 MT Open");
            list10.add("9 MT Closed");
            list10.add("16 MT Open");
            list10.add("16 MT Closed");
            list10.add("21 MT Open");
            list10.add("21 MT Closed");
            list10.add("26 MT Open");
            list10.add("26 MT Closed");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, android.R.layout.simple_spinner_item, list10);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        }
    }
    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText())
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
    }
}

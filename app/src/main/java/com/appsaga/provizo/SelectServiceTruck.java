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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class SelectServiceTruck extends AppCompatActivity implements com.appsaga.provizo.ProfileDialog.DialogListener , MyBookingDialog.DialogListener{
    EditText material, weight;
    Button service;
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

        Bundle bundle = getIntent().getExtras();
        orderid = bundle.getString("Order ID");
        currentuser = bundle.getString("Current User");

        menuicon = findViewById(R.id.menuicon);
        service = findViewById(R.id.servicesbuttons);


        spinner = (Spinner) findViewById(R.id.servicetype);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.service, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardwithoutPopulate(SelectServiceTruck.this);
                return false;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.go_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        trucktype = (Spinner) findViewById(R.id.truckspinner);
        adapter2 = ArrayAdapter.createFromResource(SelectServiceTruck.this,
                R.array.trucktype, R.layout.spinner_item);
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
                        Toast.makeText(SelectServiceTruck.this, "Wallet", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.partnerlogin:
                        Toast.makeText(SelectServiceTruck.this, "partenr login", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mybooking:
                       openDialog("Booking");
                        break;
                    case R.id.newbooking:
                        Intent i = new Intent(SelectServiceTruck.this, Bookingchoice.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                        break;
                    case R.id.ratechart:
                        Toast.makeText(SelectServiceTruck.this, "Rate Chart", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notification:
                        Toast.makeText(SelectServiceTruck.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addcard:
                        startActivity(new Intent(SelectServiceTruck.this, AddCard.class));
                        break;
                    case R.id.support:
                        startActivity(new Intent(SelectServiceTruck.this, Support.class));
                        break;
                    case R.id.about:
                        startActivity(new Intent(SelectServiceTruck.this, AboutUs.class));
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
                    i.putExtra("exacttruckwt",getweight(trucktype.getSelectedItem().toString()));
                    startActivity(i);
                }
            }
        });
    }

    public String getweight(String a){
        String b = "";
        if(a.equals("2.5 MT Closed")||a.equals("2.5 MT Open"))
            b="2.5";
        else if (a.equals("3.5 MT Closed")||a.equals("3.5 MT Open"))
            b="3.5";
        else if (a.equals("4 MT Closed")||a.equals("4 MT Open"))
            b="4";
        else if (a.equals("5.5 MT Open"))
            b="5.5";
        else if (a.equals("7 MT Open"))
            b="7";
        else if (a.equals("15 MT Closed")||a.equals("15 MT Open"))
            b="15";
        else if (a.equals("30 MT Closed")||a.equals("30 MT Open"))
            b="30";
        else if (a.equals("36 MT Closed")||a.equals("36 MT Open"))
            b="36";
        else if (a.equals("21 MT Closed")||a.equals("21 MT Open"))
            b="21";
        else if (a.equals("40 MT (Trailor)"))
            b="40";
        else if (a.equals("35 MT (Trailor)"))
            b="35";

        return b;
    }
    public void openDialog(String a) {
        if (a.equals("partner")) {
            PartnerDialog dialog = new PartnerDialog();
            dialog.show(getSupportFragmentManager(), "example dialog");
        }else if(a.equals("Booking")){
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
        if (wt == 2.5) {
            List<String> list1 = new ArrayList<>();
            list1.clear();
            list1.add("Select Truck Type");
            list1.add("2.5 MT Open");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, R.layout.spinner_item, list1);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt== 3.5) {
            List<String> list2 = new ArrayList<>();
            list2.clear();
            list2.add("Select Truck Type");
            list2.add("3.5 MT Open");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, R.layout.spinner_item, list2);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt== 4) {
            List<String> list3 = new ArrayList<>();
            list3.clear();
            list3.add("Select Truck Type");
            list3.add("4 MT Open");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this,R.layout.spinner_item, list3);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt== 5.5) {
            List<String> list4 = new ArrayList<>();
            list4.clear();
            list4.add("Select Truck Type");
            list4.add("5.5 MT Open");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, R.layout.spinner_item, list4);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt > 5.5 && wt <= 10) {
            List<String> list5 = new ArrayList<>();
            list5.clear();
            list5.add("Select Truck Type");
            list5.add("7 MT Open");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, R.layout.spinner_item, list5);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt > 10 && wt <= 18) {
            List<String> list6 = new ArrayList<>();
            list6.clear();
            list6.add("Select Truck Type");
            list6.add("15 MT Open");
            list6.add("15 MT Closed");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, R.layout.spinner_item, list6);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt > 18 && wt <= 25) {
            List<String> list7 = new ArrayList<>();
            list7.clear();
            list7.add("Select Truck Type");
            list7.add("21 MT Open");
            list7.add("21 MT Closed");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, R.layout.spinner_item, list7);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt > 26 && wt <= 34) {
            List<String> list8 = new ArrayList<>();
            list8.clear();
            list8.add("Select Truck Type");
            list8.add("30 MT Open");
            list8.add("30 MT Closed");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, R.layout.spinner_item, list8);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt==35) {
            List<String> list9 = new ArrayList<>();
            list9.clear();
            list9.add("Select Truck Type");
            list9.add("35 MT (Trailor)");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, R.layout.spinner_item, list9);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        }else if (wt==40) {
            List<String> list9 = new ArrayList<>();
            list9.clear();
            list9.add("Select Truck Type");
            list9.add("40 MT (Trailor)");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, R.layout.spinner_item, list9);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        } else if (wt > 35 && wt < 40) {
            List<String> list8 = new ArrayList<>();
            list8.clear();
            list8.add("Select Truck Type");
            list8.add("36 MT Open");
            list8.add("36 MT Closed");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, R.layout.spinner_item, list8);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trucktype.setAdapter(adapter2);
        }else  if (wt>40){
            List<String> list10 = new ArrayList<>();
            list10.clear();
            list10.add("Select Truck Type");
            list10.add("2.5 MT Open");
            list10.add("3.5 MT Open");
            list10.add("4 MT Open");
            list10.add("5.5 MT Open");
            list10.add("7 MT Open");
            list10.add("15 MT Open");
            list10.add("15 MT Closed");
            list10.add("21 MT Open");
            list10.add("21 MT Closed");
            list10.add("30 MT Open");
            list10.add("30 MT Closed");
            list10.add("36 MT Open");
            list10.add("36 MT Closed");
            list10.add("35 MT (Trailor)");
            list10.add("40 MT (Trailor)");
            adapter2 = new ArrayAdapter<>(SelectServiceTruck.this, R.layout.spinner_item, list10);
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

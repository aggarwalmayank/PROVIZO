package com.appsaga.provizo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.support.design.widget.NavigationView;

public class DeliveryLocation extends AppCompatActivity implements com.appsaga.provizo.ProfileDialog.DialogListener{

    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;
    ImageButton deliveryNext;
    EditText pickupdate;

    ImageView menuicon;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_location);

        TextView tv = findViewById(R.id.appnamesignupsecond);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);
        menuicon=findViewById(R.id.menuicon);
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
                        Toast.makeText(DeliveryLocation.this, "Wallet",Toast.LENGTH_SHORT).show();break;
                    case R.id.partnerlogin:
                        Toast.makeText(DeliveryLocation.this, "partenr login",Toast.LENGTH_SHORT).show();break;
                    case R.id.mybooking:
                        Toast.makeText(DeliveryLocation.this, "My Booking",Toast.LENGTH_SHORT).show();break;
                    case R.id.newbooking:
                        Toast.makeText(DeliveryLocation.this, "New Booking",Toast.LENGTH_SHORT).show();break;
                    case R.id.ratechart:
                        Toast.makeText(DeliveryLocation.this, "Rate Chart",Toast.LENGTH_SHORT).show();break;
                    case R.id.notification:
                        Toast.makeText(DeliveryLocation.this, "Notification",Toast.LENGTH_SHORT).show();break;
                    case R.id.addcard:
                        Toast.makeText(DeliveryLocation.this, "add card",Toast.LENGTH_SHORT).show();break;
                    case R.id.support:
                        Toast.makeText(DeliveryLocation.this, "Support",Toast.LENGTH_SHORT).show();break;
                    case R.id.about:
                        Toast.makeText(DeliveryLocation.this, "about us",Toast.LENGTH_SHORT).show();break;
                    case R.id.signout:
                        Toast.makeText(DeliveryLocation.this, "SignOut",Toast.LENGTH_SHORT).show();
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

        deliveryNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DeliveryLocation.this, SelectServiceTruck.class));
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
    private DatePickerDialog.OnDateSetListener dpickerListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x=year;
            month_x=month+1;
            day_x=dayOfMonth;
            setpickup();
        }
    };
    public void setpickup(){
        Boolean flag= Boolean.TRUE;
        long timeInMilliseconds,timeinmuli=Calendar.getInstance().getTimeInMillis();
        String selectedDate=day_x+"-"+month_x+"-"+year_x;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            java.util.Date mDate = sdf.parse(selectedDate);
            timeInMilliseconds = mDate.getTime();
            if(timeInMilliseconds<timeinmuli)
                flag=Boolean.FALSE;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!flag){
            alertbox("INVALID DATE");
        }
        else
        {
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

    public void openDialog() {
        ProfileDialog exampleDialog = new ProfileDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }
    @Override
    public void applyTexts() {

    }
}

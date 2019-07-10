package com.appsaga.provizo;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

@SuppressWarnings("deprecation")
public class Bookingchoice extends FragmentActivity implements com.appsaga.provizo.ProfileDialog.DialogListener, OnMapReadyCallback,
        MyBookingDialog.DialogListener {

    Location currentlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    Button truck, tempoo;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    boolean doubleBackToExitPressedOnce = false;
    ImageView menuicon;
    RelativeLayout splashRelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingchoice);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        TextView tv = findViewById(R.id.appnamesigninup);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);
        truck = findViewById(R.id.truck);
        menuicon = findViewById(R.id.menuicon);
        tempoo = findViewById(R.id.tempoo);
        truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Bookingchoice.this, DeliveryLocation.class));
            }
        });
        tempoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Bookingchoice.this, DeliveryTempoo.class));
            }
        });

        splashRelative = findViewById(R.id.splash_relative);

        splashRelative.post(new Runnable() {
            @Override
            public void run() {

                performAnimation();
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
                        Toast.makeText(Bookingchoice.this, "Wallet", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.partnerlogin:
                        Toast.makeText(Bookingchoice.this, "partenr login", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mybooking:
                        openDialog("Booking");
                        // startActivity(new Intent(Bookingchoice.this, MyBookings.class));
                        // Toast.makeText(Bookingchoice.this, "My Booking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.newbooking:
                        dl.closeDrawer(Gravity.LEFT);
                        // Toast.makeText(Bookingchoice.this, "New Booking", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.ratechart:
                        Toast.makeText(Bookingchoice.this, "Rate Chart", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notification:
                        Toast.makeText(Bookingchoice.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addcard:
                        startActivity(new Intent(Bookingchoice.this, AddCard.class));
                        //  Toast.makeText(Bookingchoice.this, "add card", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.support:
                        startActivity(new Intent(Bookingchoice.this, Support.class));
                        // Toast.makeText(Bookingchoice.this, "Support", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        startActivity(new Intent(Bookingchoice.this, AboutUs.class));
                        //   Toast.makeText(Bookingchoice.this, "about us", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.signout:
                        Toast.makeText(Bookingchoice.this, "SignOut", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Bookingchoice.this, SignInUp.class);
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
        if (a.equals("profile")) {
            ProfileDialog exampleDialog = new ProfileDialog();
            exampleDialog.show(getSupportFragmentManager(), "example dialog");
        } else if (a.equals("Booking")) {
            MyBookingDialog dialog = new MyBookingDialog();
            dialog.show(getSupportFragmentManager(), "example dialog");
        }
    }

    @Override
    public void applyTexts() {

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void performAnimation() {
        //splashRelative = findViewById(R.id.pro_text);

        final int cx = splashRelative.getWidth() / 2;
        final int cy = splashRelative.getHeight() / 2;

        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(splashRelative, cx, cy, 0, finalRadius);

            splashRelative.setVisibility(View.VISIBLE);
            anim.start();
        }
    }

    private void fetchLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentlocation=location;
                    SupportMapFragment supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.googlemap);
                    supportMapFragment.getMapAsync(Bookingchoice.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng=new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions().position(latLng)
                .title("Your Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }break;
        }
    }
}
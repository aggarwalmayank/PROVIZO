package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class TempooService extends AppCompatActivity implements com.appsaga.provizo.ProfileDialog.DialogListener , MyBookingDialog.DialogListener {
    ImageView box;
    Spinner s;
    EditText l, b, h;
    TextView price;
    Button next;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    ImageView menuicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempoo_service);

        initi();
        setProvizofont();
        setBoxAnimation();
        setSpinner();
        setnavigationdrawer();
        setTextWatcher();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l.getText().toString().equalsIgnoreCase(""))
                    l.setError("Enter Length");
                else if (b.getText().toString().equalsIgnoreCase(""))
                    b.setError("Enter Width");
                else if (h.getText().toString().equalsIgnoreCase(""))
                    h.setError("Enter Height");
                else {
                    Intent i = new Intent(TempooService.this, CompletedTempoo.class);
                    i.putExtra("Droploc", getIntent().getStringExtra("Droploc"));
                    i.putExtra("Pickloc", getIntent().getStringExtra("Pickloc"));
                    i.putExtra("PickDate", getIntent().getStringExtra("PickDate"));
                    i.putExtra("Dimension", l.getText().toString() + "-" + b.getText().toString() + "-" + h.getText().toString());
                    i.putExtra("Price", price.getText());
                    startActivity(i);
                }
            }
        });


    }

    public void initi() {
        box = findViewById(R.id.box);
        s = findViewById(R.id.spinner);
        l = findViewById(R.id.length);
        b = findViewById(R.id.width);
        h = findViewById(R.id.height);
        price = findViewById(R.id.price);
        next = findViewById(R.id.delivery_next);
        menuicon = findViewById(R.id.menuicon);

    }

    public void setProvizofont() {
        TextView tv = findViewById(R.id.appnamesignupsecond);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);
    }

    public void setSpinner() {
        List<String> list = new ArrayList<>();
        list.add("feet");
        list.add("metre");
        list.add("centimetre");
        list.add("inch");
        ArrayAdapter adapter2 = new ArrayAdapter<>(TempooService.this, android.R.layout.simple_spinner_item, list);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter2);

    }
    public void setnavigationdrawer(){
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
                        Toast.makeText(TempooService.this, "Wallet", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.partnerlogin:
                        Toast.makeText(TempooService.this, "partenr login", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mybooking:
                        openDialog("Booking");
                        break;
                    case R.id.newbooking:
                        Intent i = new Intent(TempooService.this, Bookingchoice.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                        break;
                    case R.id.ratechart:
                        Toast.makeText(TempooService.this, "Rate Chart", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notification:
                        Toast.makeText(TempooService.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addcard:
                        startActivity(new Intent(TempooService.this, AddCard.class));
                        break;
                    case R.id.support:
                        startActivity(new Intent(TempooService.this, Support.class));
                        break;
                    case R.id.about:
                        startActivity(new Intent(TempooService.this, AboutUs.class));
                        break;
                    case R.id.signout:
                        Toast.makeText(TempooService.this, "SignOut", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(TempooService.this, SignInUp.class);
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

    public void setBoxAnimation() {
        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);
        final RotateAnimation animRotate = new RotateAnimation(100.0f, -90.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animRotate.setRepeatCount(Animation.INFINITE);
        animRotate.setRepeatMode(Animation.REVERSE);
        animRotate.setDuration(abs(4000));
        animRotate.setFillAfter(true);
        animSet.addAnimation(animRotate);
        box.startAnimation(animSet);
    }

    public String amountcalc(double l, double b, double h) {
        double p = (l * b * h) * 5;
        double a = (double) Math.round(p * 100) / 100;
        return String.valueOf(a);
    }

    public double convert(String a, String unit) {
        double mes;
        if (unit.equals("inch")) {
            mes = Double.parseDouble(a) * 0.0833333;
        } else if (unit.equals("metre")) {
            mes = Double.parseDouble(a) * 3.28084;
        } else if (unit.equals("centimetre")) {
            mes = Double.parseDouble(a) * 0.0328084;
        } else {
            mes = Double.parseDouble(a);
        }

        return mes;
    }

    public void setTextWatcher() {
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String u = s.getItemAtPosition(position).toString();
                if (!b.getText().toString().isEmpty() && !h.getText().toString().isEmpty() && !l.getText().toString().isEmpty())
                    price.setText(amountcalc(convert(l.getText().toString(), u), convert(b.getText().toString(), u), convert(h.getText().toString(), u)) + " Rs");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        l.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable ss) {
                if (!b.getText().toString().isEmpty() && !h.getText().toString().isEmpty() && !l.getText().toString().isEmpty())
                    price.setText(amountcalc(convert(ss.toString(), s.getSelectedItem().toString()), convert(b.getText().toString(), s.getSelectedItem().toString()), convert(h.getText().toString(), s.getSelectedItem().toString())) + " Rs");
            }
        });
        b.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable ss) {
                if (!b.getText().toString().isEmpty() && !h.getText().toString().isEmpty() && !l.getText().toString().isEmpty())
                    price.setText(amountcalc(convert(l.getText().toString(), s.getSelectedItem().toString()), convert(ss.toString(), s.getSelectedItem().toString()), convert(h.getText().toString(), s.getSelectedItem().toString())) + " Rs");
            }
        });
        h.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable ss) {
                if (!b.getText().toString().isEmpty() && !h.getText().toString().isEmpty() && !l.getText().toString().isEmpty())
                    price.setText(amountcalc(convert(l.getText().toString(), s.getSelectedItem().toString()), convert(h.getText().toString(), s.getSelectedItem().toString()), convert(ss.toString(), s.getSelectedItem().toString())) + " Rs");
            }
        });

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

}

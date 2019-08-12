package com.appsaga.provizo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminProvizo extends AppCompatActivity {
    Button addpartner, viewbook,addlr,add,truckbook;
    LinearLayout lout;
    EditText email;
    int year_x, month_x, day_x;
    Boolean flag=false;
    static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_provizo);
        addpartner = findViewById(R.id.partner);
        viewbook = findViewById(R.id.bookings);
        lout=findViewById(R.id.lout);
        truckbook=findViewById(R.id.truckbooking);
        lout.setVisibility(View.INVISIBLE);
        addlr=findViewById(R.id.lr);
        add=findViewById(R.id.add);
        email=findViewById(R.id.mail);
        truckbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
                showDialog(DIALOG_ID);
            }
        });
        viewbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
                flag=true;
                showDialog(DIALOG_ID);
            }
        });
        addlr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setText("");
                if(lout.getVisibility()==View.VISIBLE)
                    lout.setVisibility(View.INVISIBLE);
                else
                lout.setVisibility(View.VISIBLE);

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equalsIgnoreCase("")&&!email.getText().toString().contains("@"))
                {
                    email.setError("INVALID ID");
                }
                else
                {
                    final DatabaseReference mref=FirebaseDatabase.getInstance().getReference();
                    mref.child("LR").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mref.child("LR").push().setValue(email.getText().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    lout.setVisibility(View.INVISIBLE);
                    Toast.makeText(AdminProvizo.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                }

            }

        });
        addpartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminProvizo.this,AddPartnerToDB.class));
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
            String selectedDate = day_x + "-" + month_x + "-" + year_x;
            if(flag){
            Intent i = new Intent(AdminProvizo.this, AdminTempoo.class);
            i.putExtra("date", selectedDate);
            startActivity(i);}
            else
            {
                Intent i = new Intent(AdminProvizo.this, AdminTruck.class);
                i.putExtra("date", selectedDate);
                startActivity(i);
        }
            Toast.makeText(AdminProvizo.this, selectedDate, Toast.LENGTH_SHORT).show();
        }
    };


}

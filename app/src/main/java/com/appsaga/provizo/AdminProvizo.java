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

public class AdminProvizo extends AppCompatActivity implements AdminAddPartnerDialog.DialogListener {
    Button addpartner, viewbook,addlr,add;
    LinearLayout lout;
    EditText email;
    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_provizo);
        addpartner = findViewById(R.id.partner);
        viewbook = findViewById(R.id.bookings);
        lout=findViewById(R.id.lout);
        lout.setVisibility(View.INVISIBLE);
        addlr=findViewById(R.id.lr);
        add=findViewById(R.id.add);
        email=findViewById(R.id.mail);
        viewbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
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
                openDialog();
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
            Intent i = new Intent(AdminProvizo.this, AdminTempoo.class);
            i.putExtra("date", selectedDate);
            startActivity(i);
            Toast.makeText(AdminProvizo.this, selectedDate, Toast.LENGTH_SHORT).show();
        }
    };

    public void openDialog() {
        AdminAddPartnerDialog exampleDialog = new AdminAddPartnerDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void addpartner(final String id, String name, final String exp, final String add, final String owner, final Boolean part, Boolean full
                            , final String origin, final String dest, final long price, final String type) {
        String[] splitted = name.split(" ");
        String namenospace = "",correctname="";
        for (String a : splitted) {
            namenospace += a.substring(0, 1).toUpperCase() + a.substring(1);
        }
        for (String a : splitted) {
            correctname+= a.substring(0, 1).toUpperCase() + a.substring(1);
            correctname=correctname+" ";
        }
        final DatabaseReference mref = FirebaseDatabase.getInstance().getReference();

        final String finalCorrectname = correctname;
        mref.child("partners").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mref.child("partners").child(id).child("operations").child("companyName").setValue(finalCorrectname);
                mref.child("partners").child(id).child("operations").child("truckStatus").setValue("Available");
                mref.child("partners").child(id).child("operations").child("locationMap").child(type)
                        .child(origin.toLowerCase()).child(dest.toLowerCase()).setValue(price);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mref.child("PartnerID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mref.child("PartnerID").child(finalCorrectname).setValue(id);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final String finalNamenospace = namenospace;
        mref.child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mref.child("Company").child(finalNamenospace).child("Address").setValue(add);
                mref.child("Company").child(finalNamenospace).child("Experience").setValue(exp);
                mref.child("Company").child(finalNamenospace).child("Owner").setValue(owner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (part) {
            final String finalNamenospace1 = namenospace;
            mref.child("typesOfServices").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mref.child("typesOfServices").child("Part Load").child(finalNamenospace1).setValue(id);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        if (full) {

            final String finalNamenospace2 = namenospace;
            mref.child("typesOfServices").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mref.child("typesOfServices").child("Full Truck Load").child(finalNamenospace2).setValue(id);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}

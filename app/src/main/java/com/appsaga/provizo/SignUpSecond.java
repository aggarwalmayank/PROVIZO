package com.appsaga.provizo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpSecond extends AppCompatActivity {

    private EditText name,password,email,dob;
    private ProgressBar pbar;
    private Button signup;
    DatabaseHelperUser myDb;
    FirebaseAuth firebaseAuth;
    String emailid,pass,dateofbirth,sex,fullname,MobileNo;
    int year_x,month_x,day_x;
    static final int DIALOG_ID=0;
      Spinner spinner;
    //FirebaseDatabase mDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_second);

        myDb=new DatabaseHelperUser(this);

       // mDatabase=FirebaseDatabase.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();

        name=findViewById(R.id.fullname);
        dob=findViewById(R.id.dob);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        signup=findViewById(R.id.signupsecond);
        pbar=findViewById(R.id.pbar);
        firebaseAuth=FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.go_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        final Calendar calendar=Calendar.getInstance();
        year_x=calendar.get(Calendar.YEAR);
        month_x=calendar.get(Calendar.MONTH);
        day_x=calendar.get(Calendar.DAY_OF_MONTH);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });

        spinner = findViewById(R.id.genderspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().equalsIgnoreCase(""))
                {
                    name.setError("Enter name");
                }
                else if(spinner.getSelectedItem().toString().equalsIgnoreCase("gender"))
                {
                    Toast.makeText(SignUpSecond.this,"Please select a gender",Toast.LENGTH_SHORT).show();
                }
                else if(dob.getText().toString().equalsIgnoreCase(""))
                {
                    dob.setText("Enter Date of Birth");
                }
                else if(email.getText().toString().equalsIgnoreCase(""))
                {
                    email.setError("Enter Id");
                }
                else if(password.getText().toString().equalsIgnoreCase(""))
                {
                    password.setError("Enter Password");
                }
                else {
                    pbar.setVisibility(View.VISIBLE);
                    emailid = email.getText().toString().trim();
                    pass = password.getText().toString().trim();
                    sex = spinner.getSelectedItem().toString().trim();
                    dateofbirth = dob.getText().toString().trim();
                    fullname = name.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(emailid, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pbar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if ((task.isSuccessful())) {
                                            Bundle bundle = getIntent().getExtras();
                                            MobileNo = bundle.getString("phnumber");
                                            addToLocalDatabase();
                                            addToFirebaseDatabase(firebaseAuth.getCurrentUser());
                                            Toast.makeText(SignUpSecond.this, "Registered Successfully. Please Verify Your Email...", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(SignUpSecond.this, SignInUp.class);

                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(SignUpSecond.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(SignUpSecond.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    public void addToLocalDatabase() {

        boolean isInserted = myDb.insertData(email.getText().toString(),name.getText().toString(),
                MobileNo,dob.getText().toString(),spinner.getSelectedItem().toString(),"not","");

        if (isInserted == true) {
            Toast.makeText(SignUpSecond.this, "Saved", Toast.LENGTH_LONG).show();


        } else
            Toast.makeText(SignUpSecond.this, "Data is not inserted", Toast.LENGTH_LONG).show();

    }

    void addToFirebaseDatabase(final FirebaseUser u)
    {
        final HashMap<String,Object> insert=new HashMap<>();
        insert.put("Name",name.getText().toString());
        insert.put("Email",email.getText().toString());
        insert.put("Phone Number",MobileNo);
        insert.put("Gender",spinner.getSelectedItem().toString());
        insert.put("Date of Birth",dob.getText().toString());
        insert.put("Email Verification","Not Verified");
        insert.put("Bookings","");

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Toast.makeText(SignUpSecond.this, u.getUid(), Toast.LENGTH_SHORT).show();
                databaseReference.child("users").child(u.getUid()).setValue(insert);
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
    private DatePickerDialog.OnDateSetListener dpickerListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x=year;
            month_x=month+1;
            day_x=dayOfMonth;
            setdob();
        }
    };
    public void setdob(){
        Boolean flag= Boolean.TRUE;
        long timeInMilliseconds,timeinmuli=Calendar.getInstance().getTimeInMillis();
        String selectedDate=day_x+"-"+month_x+"-"+year_x;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            java.util.Date mDate = sdf.parse(selectedDate);
            timeInMilliseconds = mDate.getTime();
            if(timeInMilliseconds>=timeinmuli)
                flag=Boolean.FALSE;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!flag){
            alertbox("INVALID DATE");
        }
        else
        {
            dob.setText(selectedDate);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
    }
}

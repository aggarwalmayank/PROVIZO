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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    String emailid,pass,dateofbirth,sex,fullname;
    int year_x,month_x,day_x;
    static final int DIALOG_ID=0;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef,databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_second);

        mDatabase=FirebaseDatabase.getInstance();
        databaseReference=mDatabase.getReference();
        mRef=databaseReference.child("users");

        name=findViewById(R.id.fullname);
        dob=findViewById(R.id.dob);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        signup=findViewById(R.id.signupsecond);
        pbar=findViewById(R.id.pbar);
        firebaseAuth=FirebaseAuth.getInstance();

        TextView tv=findViewById(R.id.appnamesignupsecond);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);

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

        final Spinner spinner = findViewById(R.id.genderspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                emailid=email.getText().toString().trim();
                pass=password.getText().toString().trim();
                sex=spinner.getSelectedItem().toString().trim();
                dateofbirth=dob.getText().toString().trim();
                fullname=name.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(emailid,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pbar.setVisibility(View.GONE);
                        if(task.isSuccessful())
                        {
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if ((task.isSuccessful())){
                                        Bundle bundle = getIntent().getExtras();
                                        Toast.makeText(SignUpSecond.this, "Registered Successfully. Please Verify Your Email...", Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(SignUpSecond.this,SignInUp.class);
                                      // addToLocalDataBase(emailid,fullname,bundle.getString("phnumber"),sex,dateofbirth);
                                       addToFirebaseDatabase(emailid,fullname,bundle.getString("phnumber"),sex,dateofbirth);
                                        startActivity(i);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(SignUpSecond.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(SignUpSecond.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
    void addToFirebaseDatabase(String email,String name,String number,String sex,String dob)
    {

        Map<String,Object> insert=new HashMap<>();
        insert.put("Name",name);
        insert.put("Email",email);
        insert.put("Phone Number",number);
        insert.put("Gender",sex);
        insert.put("Date of Birth",dob);
        insert.put("Email Verification","NOT KNOWN");

        mRef.child(email).setValue(name);
    }
  /*  void addToLocalDataBase(String email,String name,String number,String sex,String dob) {
        boolean isInserted = myDb.insertData(email, name, number, sex, dob);
        if(isInserted)
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
    }*/
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

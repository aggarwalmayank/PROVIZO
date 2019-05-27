package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SignUpSecond extends AppCompatActivity {
    private EditText name,password,email,dob;
    private ProgressBar pbar;
    private Button signup;
    DatabaseHelperUser myDb;
    FirebaseAuth firebaseAuth;
    String emailid,pass,dateofbirth,sex,fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_second);

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
                                       addToLocalDataBase(emailid,fullname,bundle.getString("phnumber"),sex,dateofbirth);
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
    void addToLocalDataBase(String email,String name,String number,String sex,String dob) {
        boolean isInserted = myDb.insertData(email, name, number, sex, dob);
        if(isInserted)
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
    }
}

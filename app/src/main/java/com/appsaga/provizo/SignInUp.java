package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInUp extends AppCompatActivity implements Dialog.DialogListener {

    Button signUp,signIn;
    Typeface typeface;
    ProgressBar pbar;
    EditText email,pass;
    FirebaseAuth mAuth;
    TextView forgot;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_up);

        email=findViewById(R.id.login_id_text);
        pass=findViewById(R.id.password_text);
        signIn=findViewById(R.id.sign_in);
        mAuth=FirebaseAuth.getInstance();
        pbar=findViewById(R.id.pbar);
        forgot=findViewById(R.id.forgot);

        TextView tv=findViewById(R.id.appnamesigninup);
        typeface=Typeface.createFromAsset(getAssets(),"fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null&&mAuth.getCurrentUser().isEmailVerified()){
                    startActivity(new Intent(SignInUp.this,DeliveryLocation.class));
                }
            }
        };

        signUp = findViewById(R.id.sign_up);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignInUp.this,com.appsaga.provizo.SignUp.class));
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email.getText().toString().trim(),pass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pbar.setVisibility(View.GONE);
                        if(task.isSuccessful())
                        {
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                startActivity(new Intent(SignInUp.this,DeliveryLocation.class));
                            }
                            else
                            {
                                Toast.makeText(SignInUp.this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(SignInUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot.setTextColor(Color.parseColor("#800080"));
                openDialog();
            }
        });
    }
    public void openDialog() {
        Dialog exampleDialog = new Dialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void applyTexts(String username) {
        mAuth.sendPasswordResetEmail(username.trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(SignInUp.this, "Reset Password link sent to Registered Email", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(SignInUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onStop(){
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);

        }
    }
}

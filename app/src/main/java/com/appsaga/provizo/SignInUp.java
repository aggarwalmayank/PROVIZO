package com.appsaga.provizo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignInUp extends AppCompatActivity implements Dialog.DialogListener, com.appsaga.provizo.PartnerDialog.DialogListener  {

    Button signUp,signIn,partner;
    Typeface typeface;
    ProgressBar pbar;
    boolean doubleBackToExitPressedOnce = false;
    EditText email,pass;
    FirebaseAuth mAuth;
    TextView forgot;
    private  ValueEventListener mQueryListener;
    DatabaseReference mref= FirebaseDatabase.getInstance().getReference();
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
        Button b=findViewById(R.id.temp);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
        partner=findViewById(R.id.partnerlogin);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null&&mAuth.getCurrentUser().isEmailVerified()){
                    startActivity(new Intent(SignInUp.this,Bookingchoice.class));
                    finish();
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
                hideKeyboardwithoutPopulate(SignInUp.this);
                pbar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email.getText().toString().trim(),pass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pbar.setVisibility(View.GONE);
                        if(task.isSuccessful())
                        {
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                startActivity(new Intent(SignInUp.this,Bookingchoice.class));
                                finish();
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
                openDialog("");
            }
        });
        partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("partner");
            }
        });

    }
    public void openDialog(String a) {
        if (a.equals("partner")) {
            PartnerDialog dialog = new PartnerDialog();
            dialog.show(getSupportFragmentManager(), "example dialog");
        } else {
            Dialog exampleDialog = new Dialog();
            exampleDialog.show(getSupportFragmentManager(), "example dialog");
        }

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
        partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
    @Override
    public void loginid(final String username) {
        final Boolean[] flag = {false};
        final ProgressDialog dialog = ProgressDialog.show(SignInUp.this, "Confirming ID", "Please wait...", true);

        mQueryListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(username))
                    {
                        flag[0] =true;
                        dialog.dismiss();
                        break;
                    }
                }
                if(flag[0])
                {
                    Intent i=new Intent(SignInUp.this,Partner.class);
                    i.putExtra("partner id",username);
                    startActivity(i);
                    dialog.dismiss();
                }
                else
                {
                    alertbox("Invalid Partner ID");
                    dialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        Query query=mref.child("partners").orderByChild("partners");
        query.addValueEventListener(mQueryListener);
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
    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText())
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
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
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}

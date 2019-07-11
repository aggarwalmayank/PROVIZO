package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;


public class SignUp extends AppCompatActivity {

    Typeface typeface;
    ImageButton signup;
    Button getOTP;
    Button resendOTP;
    EditText phone_num, enterOTP;
    ProgressBar pbar;
    String phonenumber, verificationId;

    FirebaseAuth mAuth;
    PhoneAuthProvider.ForceResendingToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView tv = findViewById(R.id.appname);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);

        signup = findViewById(R.id.nextsignup);
        getOTP = findViewById(R.id.getotp);
        enterOTP = findViewById(R.id.enter_otp);
        phone_num = findViewById(R.id.mobile_num);
        pbar = findViewById(R.id.pbar);
        resendOTP = findViewById(R.id.resendotp);

        mAuth = FirebaseAuth.getInstance();
        signup.setEnabled(Boolean.FALSE);
        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone_num.getText().toString().equalsIgnoreCase("") && phone_num.getText().toString().length() < 10) {
                    phone_num.setError("Invalid Number");

                } else {
                    phonenumber = "+91" + phone_num.getText().toString().trim();
                    sendVerificationCode(phonenumber);
                    pbar.setVisibility(View.VISIBLE);
                    getOTP.setVisibility(View.GONE);
                    signup.setEnabled(Boolean.TRUE);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = enterOTP.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    enterOTP.setError("Enter Code...");
                    enterOTP.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phonenumber = "+91" + phone_num.getText().toString().trim();
                resendVerificationCode(phonenumber, token);
            }
        });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallBack,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            verificationId = s;
            token = forceResendingToken;

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                enterOTP.setText(code);
                verifyCode(verificationId);
            }
            else {
                Toast.makeText(SignUp.this,"Phone Number was already verified",Toast.LENGTH_LONG).show();
                Intent i = new Intent(SignUp.this, SignUpSecond.class);
                i.putExtra("phnumber", phonenumber);
                startActivity(i);
                finish();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);

    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Test.....","no");
                            Intent i = new Intent(SignUp.this, SignUpSecond.class);
                            i.putExtra("phnumber",phonenumber);
                            startActivity(i);
                            finish();
                        }

                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
    }
}

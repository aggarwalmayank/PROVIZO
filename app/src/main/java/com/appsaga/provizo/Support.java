package com.appsaga.provizo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Support extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        android.support.v7.widget.Toolbar toolbar = (
                android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#bec1c2"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backicon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final EditText support=findViewById(R.id.support);
        Button send=findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(support.getText().toString().equalsIgnoreCase(""))
                    support.setError("Please Enter your Query");
                else
                {
                    hideKeyboardwithoutPopulate(Support.this);
                    SendMail sm = new SendMail(Support.this, "provizo.booking@gmail.com", FirebaseAuth.getInstance().getCurrentUser().getEmail()+"  Support Query",
                            support.getText().toString()+"\n\n\n"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                    sm.execute();
                     final ProgressDialog progressDialog = ProgressDialog.show(Support.this, "Sending", "Please Wait...", true);
                    Handler h=new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(Support.this, "Send", Toast.LENGTH_SHORT).show();
                        }
                    },1500);
                    support.getText().clear();
                }
            }
        });
    }
    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText())
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
    }
}

package com.appsaga.provizo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                    SendMail sm = new SendMail(Support.this, FirebaseAuth.getInstance().getCurrentUser().getEmail(), FirebaseAuth.getInstance().getCurrentUser().getEmail()+"  Support Query",
                            support.getText().toString()+"\n\n\n"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                    sm.execute();
                }
            }
        });
    }
}

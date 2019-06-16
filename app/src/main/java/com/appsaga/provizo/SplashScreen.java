package com.appsaga.provizo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        TextView tv = findViewById(R.id.splashtext);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);
        final int DELAY = 2000;
        Handler handler = new Handler();
        final View myView = findViewById(R.id.view);



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
             Handler h=new Handler();
                startActivity(new Intent(SplashScreen.this, com.appsaga.provizo.SignInUp.class));
                finish();

            }
        }, DELAY);
    }
}

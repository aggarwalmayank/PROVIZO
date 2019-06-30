package com.appsaga.provizo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashScreenCircular extends AppCompatActivity {

    TextView proText;
    RelativeLayout splashRelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_circular);
        proText = findViewById(R.id.pro_text);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        proText.setTypeface(typeface);
        splashRelative = findViewById(R.id.splash_relative);

        splashRelative.post(new Runnable() {
            @Override
            public void run() {

                performAnimation();
            }
        });
    }

    public void performAnimation() {
        //splashRelative = findViewById(R.id.pro_text);

        final int cx = splashRelative.getWidth() / 2;
        final int cy = splashRelative.getHeight() / 2;

        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(splashRelative, cx, cy,  finalRadius,0);

            splashRelative.setVisibility(View.VISIBLE);
            anim.start();
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            float initialRadius = (float) Math.hypot(cx, cy);

                            Animator finalAnim = ViewAnimationUtils.createCircularReveal(splashRelative, cx, cy, 0, initialRadius);
                            finalAnim.start();

                            finalAnim.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    splashRelative.setVisibility(View.INVISIBLE);

                                    startActivity(new Intent(SplashScreenCircular.this, com.appsaga.provizo.SignInUp.class));
                                    finish();
                                }
                            });
                        }
                    }, 1500);
                }
            });
        }
    }


}

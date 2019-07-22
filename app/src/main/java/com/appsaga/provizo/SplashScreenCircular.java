package com.appsaga.provizo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.IntentSender;
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

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import static com.sun.activation.registries.LogSupport.log;

public class SplashScreenCircular extends AppCompatActivity {

    TextView proText;
    RelativeLayout splashRelative;
    final int MY_REQUEST_CODE = 101;

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

                checkForUpdates();
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
            anim = ViewAnimationUtils.createCircularReveal(splashRelative, cx, cy,0  ,finalRadius);

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

                            Animator finalAnim = ViewAnimationUtils.createCircularReveal(splashRelative, cx, cy, initialRadius,0 );
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

    void checkForUpdates()
    {
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(SplashScreenCircular.this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.

        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {

                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                appUpdateInfo,
                                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                AppUpdateType.IMMEDIATE,
                                // The current activity making the update request.
                                SplashScreenCircular.this,
                                // Include a request code to later monitor this update request.
                                MY_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                log("Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
                checkForUpdates();
            }
        }
    }

}

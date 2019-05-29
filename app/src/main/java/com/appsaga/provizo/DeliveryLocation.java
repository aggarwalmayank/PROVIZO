package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class DeliveryLocation extends AppCompatActivity {

    ImageButton deliveryNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_location);

        TextView tv=findViewById(R.id.appnamesignupsecond);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);

        deliveryNext = findViewById(R.id.delivery_next);

        deliveryNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DeliveryLocation.this,SelectServiceTruck.class));
            }
        });
    }
}

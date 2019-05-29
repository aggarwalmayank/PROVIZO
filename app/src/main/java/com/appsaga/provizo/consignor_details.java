package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;



public class consignor_details extends AppCompatActivity {

    ImageButton consignor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignor_details);

        TextView tv=findViewById(R.id.appnamesignupsecond);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);

        consignor = findViewById(R.id.consignor);
        consignor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(consignor_details.this,com.appsaga.provizo.consignee_details.class));
            }
        });
    }
}

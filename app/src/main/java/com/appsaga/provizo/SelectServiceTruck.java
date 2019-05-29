package com.appsaga.provizo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectServiceTruck extends AppCompatActivity {

    ImageButton service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service_truck);

        TextView tv=findViewById(R.id.appnamesignupsecond);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);

        service = findViewById(R.id.servicesbutton);
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SelectServiceTruck.this,com.appsaga.provizo.consignor_details.class));
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.boxspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.services, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}

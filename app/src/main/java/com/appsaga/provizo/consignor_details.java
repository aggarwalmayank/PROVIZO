package com.appsaga.provizo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class consignor_details extends AppCompatActivity {

    ImageButton consignor;
    DatabaseHelperUser db;
    EditText name,number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignor_details);

        TextView tv=findViewById(R.id.appnamesignupsecond);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);
        name=findViewById(R.id.consignorname);
        number=findViewById(R.id.consignorphone);
        db=new DatabaseHelperUser(this);
        Cursor res = db.getAllData();
        while(res.moveToNext()){
            name.setText(res.getString(1));
            number.setText(res.getString(2));
            Toast.makeText(this, res.getString(1)+" "+res.getString(2), Toast.LENGTH_SHORT).show();
        }
        consignor = findViewById(R.id.consignorr);
        consignor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(consignor_details.this,com.appsaga.provizo.consignee_details.class));
            }
        });
    }
}

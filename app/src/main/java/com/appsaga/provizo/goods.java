package com.appsaga.provizo;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class goods extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        TextView tv=findViewById(R.id.appnamesignupsecond);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);

    }
}

package com.appsaga.provizo;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class TempooService extends AppCompatActivity {
    ImageView box;
    Spinner s;
    EditText l, b, h;
    TextView price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempoo_service);

        initi();
        setProvizofont();
        setBoxAnimation();
        setSpinner();
        setTextWatcher();


    }

    public void initi() {
        box = findViewById(R.id.box);
        s = findViewById(R.id.spinner);
        l = findViewById(R.id.length);
        b = findViewById(R.id.width);
        h = findViewById(R.id.height);
        price = findViewById(R.id.price);

    }

    public void setProvizofont() {
        TextView tv = findViewById(R.id.appnamesignupsecond);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);
    }

    public void setSpinner() {
        List<String> list = new ArrayList<>();
        list.add("feet");
        list.add("metre");
        list.add("centimetre");
        list.add("inch");
        ArrayAdapter adapter2 = new ArrayAdapter<>(TempooService.this, android.R.layout.simple_spinner_item, list);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter2);

    }

    public void setBoxAnimation() {
        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);
        final RotateAnimation animRotate = new RotateAnimation(100.0f, -90.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animRotate.setRepeatCount(Animation.INFINITE);
        animRotate.setRepeatMode(Animation.REVERSE);
        animRotate.setDuration(abs(4000));
        animRotate.setFillAfter(true);
        animSet.addAnimation(animRotate);
        box.startAnimation(animSet);
    }

    public String amountcalc(double l, double b, double h) {
        double p=(l*b*h)*5;
        double a=(double) Math.round(p * 100) / 100;
        return String.valueOf(a);
    }

    public double convert(String a,String unit) {
        double mes;
        if (unit.equals("inch")) {
            mes = Double.parseDouble(a) * 0.0833333;
        } else if (unit.equals("metre")) {
            mes = Double.parseDouble(a) * 3.28084;
        } else if (unit.equals("centimetre")) {
            mes = Double.parseDouble(a) * 0.0328084;
        } else {
            mes = Double.parseDouble(a);
        }

        return mes;
    }

    public void setTextWatcher() {
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String u=s.getItemAtPosition(position).toString();
                if(!b.getText().toString().isEmpty()&&!h.getText().toString().isEmpty()&&!l.getText().toString().isEmpty())
                    price.setText(amountcalc(convert(l.getText().toString(),u), convert(b.getText().toString(),u), convert(h.getText().toString(),u))+" Rs");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        l.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable ss) {
                if(!b.getText().toString().isEmpty()&&!h.getText().toString().isEmpty()&&!l.getText().toString().isEmpty())
                    price.setText(amountcalc(convert(ss.toString(),s.getSelectedItem().toString()), convert(b.getText().toString(),s.getSelectedItem().toString()), convert(h.getText().toString(),s.getSelectedItem().toString()))+" Rs");
            }
        });
        b.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable ss) {
                if(!b.getText().toString().isEmpty()&&!h.getText().toString().isEmpty()&&!l.getText().toString().isEmpty())
                    price.setText(amountcalc(convert(l.getText().toString(),s.getSelectedItem().toString()), convert(ss.toString(),s.getSelectedItem().toString()), convert(h.getText().toString(),s.getSelectedItem().toString()))+" Rs");
            }
        });
        h.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable ss) {
                if(!b.getText().toString().isEmpty()&&!h.getText().toString().isEmpty()&&!l.getText().toString().isEmpty())
                    price.setText(amountcalc(convert(l.getText().toString(),s.getSelectedItem().toString()), convert(h.getText().toString(),s.getSelectedItem().toString()), convert(ss.toString(),s.getSelectedItem().toString()))+" Rs");
            }
        });

    }
}

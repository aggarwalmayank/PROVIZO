package com.appsaga.provizo;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCard extends AppCompatActivity {
    EditText number, name, cvv, exp;
    ImageView typecard;
    Button save, view;
    Boolean flag = false;
    TextView cardnumber, holdername, cvvno, expdate;
    int count = 0;
    long time;
    String monthYearStr, validthru = "";
    DatabaseCard mydb;
    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
    AdapterCards adapterClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        mydb = new DatabaseCard(this);
        number = findViewById(R.id.number);
        name = findViewById(R.id.holder);
        cvv = findViewById(R.id.cvvno);
        exp = findViewById(R.id.expiry);
        cardnumber = findViewById(R.id.cardnumber);
        holdername = findViewById(R.id.holdername);
        cvvno = findViewById(R.id.cvv);
        expdate = findViewById(R.id.expdate);
        typecard = findViewById(R.id.typecard);
        final EasyFlipView fp = findViewById(R.id.flip);
        save = findViewById(R.id.save);
        view = findViewById(R.id.view);
        adapterClass = new AdapterCards(ViewCards.CardNumber, ViewCards.Holder, ViewCards.Exp, ViewCards.Cvv, this);
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

        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
                if (fp.isBackSide())
                    fp.flipTheView();
                if (count <= number.getText().toString().length()
                        && (number.getText().toString().length() == 4
                        || number.getText().toString().length() == 10
                        || number.getText().toString().length() == 16)) {
                    number.setText(number.getText().toString() + "  ");
                    int pos = number.getText().length();
                    number.setSelection(pos);
                } else if (count >= number.getText().toString().length()
                        && (number.getText().toString().length() == 4
                        || number.getText().toString().length() == 9
                        || number.getText().toString().length() == 14)) {
                    number.setText(number.getText().toString().substring(0, number.getText().toString().length() - 1));
                    int pos = number.getText().length();
                    number.setSelection(pos);
                }
                count = number.getText().toString().length();
                if (mEdit.length() > 2) {
                    if (Integer.parseInt(mEdit.toString().substring(0, 2)) >= 40 && Integer.parseInt(mEdit.toString().substring(0, 2)) <= 49) {
                        typecard.setImageResource(R.drawable.visa);
                    } else if (Integer.parseInt(mEdit.toString().substring(0, 2)) >= 51 && Integer.parseInt(mEdit.toString().substring(0, 2)) <= 55) {
                        typecard.setImageResource(R.drawable.mastercard);
                    } else if (Integer.parseInt(mEdit.toString().substring(0, 2)) >= 56 && Integer.parseInt(mEdit.toString().substring(0, 2)) <= 59) {
                        typecard.setImageResource(R.drawable.maestro);
                    } else if (Integer.parseInt(mEdit.toString().substring(0, 2)) >= 60 && Integer.parseInt(mEdit.toString().substring(0, 2)) <= 69) {
                        typecard.setImageResource(R.drawable.rupay);
                    }
                } else if (mEdit.toString().equals("")) {
                    typecard.setImageDrawable(null);
                }
                cardnumber.setText(mEdit.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
                if (fp.isBackSide())
                    fp.flipTheView();
                holdername.setText(mEdit.toString().toUpperCase());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        cvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
                if (fp.isFrontSide())
                    fp.flipTheView();
                cvvno.setText(mEdit.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fp.isBackSide())
                    fp.flipTheView();
                MonthYearPickerDialog pickerDialog = new MonthYearPickerDialog();
                pickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
                        monthYearStr = year + "-" + (month + 1) + "-" + i2;
                        validthru = monthYearStr;
                        try {
                            java.util.Date mDate = input.parse(monthYearStr);
                            time = mDate.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        expdate.setText(formatMonthYear(monthYearStr));
                        exp.setText(formatMonthYear(monthYearStr));
                    }
                });
                pickerDialog.show(getSupportFragmentManager(), "MonthYearPickerDialog");

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                if (number.getText().toString().equalsIgnoreCase("")) {
                    number.setError("Please enter Number");
                    flag = true;
                } else if (exp.getText().toString().equalsIgnoreCase("")) {
                    exp.setError("Please enter Exipry Info");
                    flag = true;
                } else if (name.getText().toString().equalsIgnoreCase("")) {
                    flag = true;
                    name.setError("Please Enter Holder Name");
                } else if (cvv.getText().toString().equalsIgnoreCase("")) {
                    flag = true;
                    cvv.setError("Please Enter CVV");
                }

                String str = number.getText().toString();
                String numberOnly = str.replaceAll("[^0-9]", "");
                if (numberOnly.length() != 16) {
                    flag = true;
                    number.setError("Invalid Number");
                }
                if (!flag)
                    AddData();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddCard.this, ViewCards.class));
            }
        });

    }

    public void AddData() {

        boolean isInserted = mydb.insertData(
                number.getText().toString(), name.getText().toString(), exp.getText().toString(), cvv.getText().toString());

        if (isInserted == true) {
            Toast.makeText(AddCard.this, "Saved", Toast.LENGTH_LONG).show();
            ViewCards.CardNumber.add(number.getText().toString());
            ViewCards.Holder.add(name.getText().toString().toUpperCase());
            ViewCards.Exp.add(exp.getText().toString());
            ViewCards.Cvv.add(cvv.getText().toString());
            adapterClass.notifyDataSetChanged();
            finish();

        } else
            Toast.makeText(AddCard.this, "Data is not inserted", Toast.LENGTH_LONG).show();

    }

    String formatMonthYear(String str) {
        Date date = null;
        try {
            date = input.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(date);
    }
}

package com.appsaga.provizo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminProvizo extends AppCompatActivity {
    Button addpartner,viewbook;
    int year_x,month_x,day_x;
    static final int DIALOG_ID=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_provizo);
        addpartner=findViewById(R.id.partner);
        viewbook=findViewById(R.id.bookings);
        viewbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                year_x=calendar.get(Calendar.YEAR);
                month_x=calendar.get(Calendar.MONTH);
                day_x=calendar.get(Calendar.DAY_OF_MONTH);
                showDialog(DIALOG_ID);
            }
        });
        addpartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(AdminProvizo.this);
                builder.setCancelable(true);
                builder.setMessage("Coming Soon...");
                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        }
        return null;

    }
    private DatePickerDialog.OnDateSetListener dpickerListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x=year;
            month_x=month+1;
            day_x=dayOfMonth;
            String selectedDate=day_x+"-"+month_x+"-"+year_x;
            Intent i=new Intent(AdminProvizo.this,AdminTempoo.class);
            i.putExtra("date",selectedDate);
            startActivity(i);
            Toast.makeText(AdminProvizo.this, selectedDate, Toast.LENGTH_SHORT).show();
        }
    };

}

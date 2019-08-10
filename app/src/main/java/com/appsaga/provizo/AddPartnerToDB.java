package com.appsaga.provizo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;

import java.util.ArrayList;

public class AddPartnerToDB extends AppCompatActivity {

    Button selecttruck,submit;
    ArrayList<String> openlist = new ArrayList<>();
    ArrayList<String> closelist = new ArrayList<>();
    private EditText address,name,id,experience,owner,origin,dest,price;
    private CheckBox partload,fulltruckload;
    RadioGroup rg;
    RadioButton rb1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_partner_to_db);

        selecttruck = findViewById(R.id.select);
        id= findViewById(R.id.partnerid);
        name= findViewById(R.id.companyname);
        experience=findViewById(R.id.Experience);
        address=findViewById(R.id.address);
        owner=findViewById(R.id.owner);
        partload=findViewById(R.id.cb1);
        fulltruckload=findViewById(R.id.cb2);
        origin=findViewById(R.id.origin);
        dest= findViewById(R.id.dest);
        price=findViewById(R.id.price);
        rg=findViewById(R.id.rg);
        rb1=findViewById(R.id.rb1);
        rb1.setChecked(true);
        submit=findViewById(R.id.submit);

        ArrayList<MultiSelectModel> listOfCountries = new ArrayList<>();
        listOfCountries.add(new MultiSelectModel(1, "2.5 MT Open"));
        listOfCountries.add(new MultiSelectModel(2, "2.5 MT Closed"));
        listOfCountries.add(new MultiSelectModel(3, "3.5 MT Closed"));
        listOfCountries.add(new MultiSelectModel(4, "4 MT Open"));
        listOfCountries.add(new MultiSelectModel(5, "5 MT Open"));
        listOfCountries.add(new MultiSelectModel(6, "7 MT Closed"));
        listOfCountries.add(new MultiSelectModel(7, "7 MT Open"));
        listOfCountries.add(new MultiSelectModel(8, "9 MT Closed"));
        listOfCountries.add(new MultiSelectModel(9, "9 MT Open"));
        listOfCountries.add(new MultiSelectModel(10, "16 MT Open"));
        listOfCountries.add(new MultiSelectModel(11, "16 MT Closed"));
        listOfCountries.add(new MultiSelectModel(12, "21 MT Closed"));
        listOfCountries.add(new MultiSelectModel(13, "21 MT Open"));
        listOfCountries.add(new MultiSelectModel(14, "21 MT Closed"));
        listOfCountries.add(new MultiSelectModel(15, "26 MT Closed"));
        listOfCountries.add(new MultiSelectModel(16, "20 MT Trailor 40ft"));
        listOfCountries.add(new MultiSelectModel(17, "26 MT Trailor 40ft"));
        final MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Select Truck") //setting title for dialog
                .titleSize(25)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                .setMaxSelectionLimit(listOfCountries.size()) //you can set maximum checkbox selection limit (Optional)
                .multiSelectList(listOfCountries) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        for (int i = 0; i < selectedIds.size(); i++) {
                            if (selectedNames.get(i).contains("Open") || selectedNames.get(i).contains("open"))
                                openlist.add(selectedNames.get(i));
                            else
                                closelist.add(selectedNames.get(i));
                        }


                    }

                    @Override
                    public void onCancel() {

                    }


                });

        selecttruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/*
                id= findViewById(R.id.partnerid);
                name= findViewById(R.id.companyname);
                experience=findViewById(R.id.Experience);
                address=findViewById(R.id.address);
                owner=findViewById(R.id.owner);
                partload=findViewById(R.id.cb1);
                fulltruckload=findViewById(R.id.cb2);
                origin=findViewById(R.id.origin);
                dest= findViewById(R.id.dest);
                price=findViewById(R.id.price);
                rg=findViewById(R.id.rg);
                rb1=findViewById(R.id.rb1);*/
                if(id.getText().toString().equalsIgnoreCase("")&&name.getText().toString().equalsIgnoreCase("")
                &&experience.getText().toString().equalsIgnoreCase("")&&address.getText().toString().equalsIgnoreCase("")&&
                owner.getText().toString().equalsIgnoreCase("")&&origin.getText().toString().equalsIgnoreCase("")&&
                dest.getText().toString().equalsIgnoreCase("")&&price.getText().toString().equalsIgnoreCase(""))
                {
                    
                }
            }
        });

    }
}

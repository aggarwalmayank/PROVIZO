package com.appsaga.provizo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddPartnerToDB extends AppCompatActivity {

    Button selecttruck, submit;
    ArrayList<String> fullopenlist = new ArrayList<>();
    ArrayList<String> fullcloselist = new ArrayList<>();
    ArrayList<String> partopenlist = new ArrayList<>();
    ArrayList<String> partcloselist = new ArrayList<>();
    private EditText address, name, id, experience, owner, origin, dest, price, email;

    RadioGroup rg;
    RadioButton rb1, rb;
    DatabaseReference mref = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_partner_to_db);

        email = findViewById(R.id.owneremail);
        selecttruck = findViewById(R.id.select);
        id = findViewById(R.id.partnerid);
        name = findViewById(R.id.companyname);
        experience = findViewById(R.id.Experience);
        address = findViewById(R.id.address);
        owner = findViewById(R.id.owner);
        origin = findViewById(R.id.origin);
        dest = findViewById(R.id.dest);
        price = findViewById(R.id.price);
        rg = findViewById(R.id.rg);
        rb1 = findViewById(R.id.rb1);
        rb1.setChecked(true);
        submit = findViewById(R.id.submit);

        ArrayList<MultiSelectModel> listOfCountries = new ArrayList<>();
        listOfCountries.add(new MultiSelectModel(1, "2.5 MT Open Full Truck"));
        listOfCountries.add(new MultiSelectModel(2, "2.5 MT Closed Full Truck"));
        listOfCountries.add(new MultiSelectModel(3, "3.5 MT Closed Full Truck"));
        listOfCountries.add(new MultiSelectModel(4, "4 MT Open Full Truck"));
        listOfCountries.add(new MultiSelectModel(5, "5 MT Open Full Truck"));
        listOfCountries.add(new MultiSelectModel(6, "7 MT Closed Full Truck"));
        listOfCountries.add(new MultiSelectModel(7, "7 MT Open Full Truck"));
        listOfCountries.add(new MultiSelectModel(8, "9 MT Closed Full Truck"));
        listOfCountries.add(new MultiSelectModel(9, "9 MT Open Full Truck"));
        listOfCountries.add(new MultiSelectModel(10, "16 MT Open Full Truck"));
        listOfCountries.add(new MultiSelectModel(11, "16 MT Closed Full Truck"));
        listOfCountries.add(new MultiSelectModel(12, "21 MT Closed Full Truck"));
        listOfCountries.add(new MultiSelectModel(13, "21 MT Open Full Truck"));
        listOfCountries.add(new MultiSelectModel(14, "21 MT Closed Full Truck"));
        listOfCountries.add(new MultiSelectModel(15, "26 MT Closed Full Truck"));
        listOfCountries.add(new MultiSelectModel(16, "2.5 MT Open Part Load"));
        listOfCountries.add(new MultiSelectModel(17, "2.5 MT Closed Part Load"));
        listOfCountries.add(new MultiSelectModel(18, "3.5 MT Closed Part Load"));
        listOfCountries.add(new MultiSelectModel(19, "4 MT Open Part Load"));
        listOfCountries.add(new MultiSelectModel(20, "5 MT Open Part Load"));
        listOfCountries.add(new MultiSelectModel(21, "7 MT Closed Part Load"));
        listOfCountries.add(new MultiSelectModel(22, "7 MT Open Part Load"));
        listOfCountries.add(new MultiSelectModel(23, "9 MT Closed Part Load"));
        listOfCountries.add(new MultiSelectModel(24, "9 MT Open Part Load"));
        listOfCountries.add(new MultiSelectModel(25, "16 MT Open Part Load"));
        listOfCountries.add(new MultiSelectModel(26, "16 MT Closed Part Load"));
        listOfCountries.add(new MultiSelectModel(27, "21 MT Closed Part Load"));
        listOfCountries.add(new MultiSelectModel(28, "21 MT Open Part Load"));
        listOfCountries.add(new MultiSelectModel(29, "21 MT Closed Part Load"));
        listOfCountries.add(new MultiSelectModel(30, "26 MT Closed Part Load"));
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
                            if (selectedNames.get(i).contains("Open") && selectedNames.get(i).contains("Full"))
                                fullopenlist.add(selectedNames.get(i));
                            else if (selectedNames.get(i).contains("Closed") && selectedNames.get(i).contains("Full"))
                                fullcloselist.add(selectedNames.get(i));
                            if (selectedNames.get(i).contains("Open") && selectedNames.get(i).contains("Part"))
                                partopenlist.add(selectedNames.get(i));
                            else if (selectedNames.get(i).contains("Closed") && selectedNames.get(i).contains("Part"))
                                partcloselist.add(selectedNames.get(i));
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
            public void onClick(View v) {
                if (email.getText().toString().equalsIgnoreCase("") && id.getText().toString().equalsIgnoreCase("") && name.getText().toString().equalsIgnoreCase("")
                        && experience.getText().toString().equalsIgnoreCase("") && address.getText().toString().equalsIgnoreCase("") &&
                        owner.getText().toString().equalsIgnoreCase("") && origin.getText().toString().equalsIgnoreCase("") &&
                        dest.getText().toString().equalsIgnoreCase("") && price.getText().toString().equalsIgnoreCase("")) {
                    alertbox("Incomplete Details");
                } else {
                    mref.child("typesOfServices").child("Full Truck Load").child("Closed").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (int i = 0; i < fullcloselist.size(); i++) {
                                String value = fullcloselist.get(i);
                                String finalvalue = "";
                                int j;
                                for (j = 0; j < value.length(); j++) {
                                    if (value.charAt(j) == ' ') {
                                        break;
                                    }
                                }
                                finalvalue = value.substring(0, j);
                                finalvalue = String.valueOf(Integer.parseInt(finalvalue) * 10);
                                mref.child("typesOfServices").child("Full Truck Load").child("Closed").child(finalvalue).push().setValue(id.getText().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    mref.child("typesOfServices").child("Full Truck Load").child("Open").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (int i = 0; i < fullopenlist.size(); i++) {
                                String value = fullopenlist.get(i);
                                String finalvalue = "";
                                int j = 0;
                                for (j = 0; j < value.length(); j++) {
                                    if (value.charAt(j) == ' ') {
                                        break;
                                    }
                                }
                                finalvalue = value.substring(0, j);
                                finalvalue = String.valueOf(Integer.parseInt(finalvalue) * 10);
                                mref.child("typesOfServices").child("Full Truck Load").child("Open").child(finalvalue).push().setValue(id.getText().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    mref.child("typesOfServices").child("Part Load").child("Closed").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (int i = 0; i < partcloselist.size(); i++) {
                                String value = partcloselist.get(i);
                                String finalvalue = "";
                                int j = 0;
                                for (j = 0; j < value.length(); j++) {
                                    if (value.charAt(j) == ' ') {
                                        break;
                                    }
                                }
                                finalvalue = value.substring(0, j);
                                finalvalue = String.valueOf(Integer.parseInt(finalvalue) * 10);
                                mref.child("typesOfServices").child("Part Load").child("Closed").child(finalvalue).push().setValue(id.getText().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    mref.child("typesOfServices").child("Part Load").child("Open").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (int i = 0; i < partopenlist.size(); i++) {
                                String value = partopenlist.get(i);
                                String finalvalue = "";
                                int j = 0;
                                for (j = 0; j < value.length(); j++) {
                                    if (value.charAt(j) == ' ') {
                                        break;
                                    }
                                }
                                finalvalue = value.substring(0, j);
                                finalvalue = String.valueOf(Integer.parseInt(finalvalue) * 10);
                                mref.child("typesOfServices").child("Part Load").child("Open").child(finalvalue).push().setValue(id.getText().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    String ID = id.getText().toString();
                    String CNAME = name.getText().toString();
                    String EXP = experience.getText().toString();
                    String OWNER = owner.getText().toString();
                    String EMAIL = email.getText().toString();
                    String ADD = address.getText().toString();
                    String ORIGIN = origin.getText().toString().toLowerCase();
                    String DEST = dest.getText().toString().toLowerCase();
                    long PRICE = Long.parseLong(price.getText().toString());
                    int selectedId = rg.getCheckedRadioButtonId();
                    rb = (RadioButton) findViewById(selectedId);
                    ADDTODB(ID, CNAME, EXP, OWNER, EMAIL, ADD, ORIGIN, DEST, PRICE, rb.getText().toString());


                }
                Toast.makeText(AddPartnerToDB.this, "Added", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    public void ADDTODB(final String id, String cname, final String exp, final String owner, final String email, final String address, final String origin, final String dest, final long price, String type) {
        String[] splitted = cname.split(" ");
        String namenospace = "", correctname = "";
        for (String a : splitted) {
            namenospace += a.substring(0, 1).toUpperCase() + a.substring(1).toLowerCase();
        }
        for (String a : splitted) {
            correctname += a.substring(0, 1).toUpperCase() + a.substring(1).toLowerCase();
            correctname = correctname + " ";
        }
        final String finalNamenospace = namenospace;
        mref.child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mref.child("Company").child(finalNamenospace).child("Address").setValue(address);
                mref.child("Company").child(finalNamenospace).child("Experience").setValue(exp);
                mref.child("Company").child(finalNamenospace).child("Owner").setValue(owner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final String finalCorrectname = correctname;
        mref.child("PartnerID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mref.child("PartnerID").child(finalCorrectname).setValue(id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (type.equals("FullTruckLoad OPEN")) {
            final String finalCorrectname1 = correctname;
            mref.child("partners").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mref.child("partners").child(id).child("operations").child("Email").setValue(email);
                    mref.child("partners").child(id).child("operations").child("companyName").setValue(finalCorrectname1);
                    mref.child("partners").child(id).child("operations").child("truckStatus").setValue("Available");
                    mref.child("partners").child(id).child("operations").child("locationMap").child("FullTruckLoad").child("open")
                            .child(origin).child(dest).setValue(price);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (type.equals("FullTruckLoad CLOSED")) {
            final String finalCorrectname1 = correctname;
            mref.child("partners").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mref.child("partners").child(id).child("operations").child("companyName").setValue(finalCorrectname1);
                    mref.child("partners").child(id).child("operations").child("truckStatus").setValue("Available");
                    mref.child("partners").child(id).child("operations").child("Email").setValue(email);
                    mref.child("partners").child(id).child("operations").child("locationMap").child("FullTruckLoad").child("closed")
                            .child(origin).child(dest).setValue(price);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else if (type.equals("PartLoad CLOSED")) {
            final String finalCorrectname1 = correctname;
            mref.child("partners").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mref.child("partners").child(id).child("operations").child("Email").setValue(email);
                    mref.child("partners").child(id).child("operations").child("companyName").setValue(finalCorrectname1);
                    mref.child("partners").child(id).child("operations").child("truckStatus").setValue("Available");
                    mref.child("partners").child(id).child("operations").child("locationMap").child("PartLoad").child("closed")
                            .child(origin).child(dest).setValue(price);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (type.equals("PartLoad OPEN")) {
            final String finalCorrectname1 = correctname;
            mref.child("partners").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mref.child("partners").child(id).child("operations").child("Email").setValue(email);
                    mref.child("partners").child(id).child("operations").child("companyName").setValue(finalCorrectname1);
                    mref.child("partners").child(id).child("operations").child("truckStatus").setValue("Available");
                    mref.child("partners").child(id).child("operations").child("locationMap").child("PartLoad").child("open")
                            .child(origin).child(dest).setValue(price);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public void alertbox(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage(msg);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}

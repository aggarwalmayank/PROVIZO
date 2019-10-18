package com.appsaga.provizo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.R.layout.simple_spinner_item;

public class CityDialog extends DialogFragment {
    private EditText origin,dest,price,returnprice;
    private Spinner unit,runit;
    private DialogListener listener;
    private RadioGroup rg;
    LinearLayout lout;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.citydialog, null);

        builder.setView(view)
                .setTitle("Add City")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String ORIGIN= origin.getText().toString();
                        final String DEST= dest.getText().toString();
                        long PRICE = Long.parseLong(price.getText().toString());
                        long RPRICE=0;
                        if(!returnprice.getText().toString().equalsIgnoreCase(""))
                             RPRICE = Long.parseLong(returnprice.getText().toString());
                        String UNIT= unit.getSelectedItem().toString();
                        String RUNIT= runit.getSelectedItem().toString();

                        int selectedId = rg.getCheckedRadioButtonId();
                        RadioButton rb = (RadioButton) view.findViewById(selectedId);
                        String type=rb.getText().toString();
                        if(UNIT.equals("Per KG")){
                            PRICE= (long) (PRICE*0.01);
                        }
                        else if(UNIT.equals("Per Ton")){
                            PRICE= (long) (PRICE*0.1);
                        }
                        else if(UNIT.equals("Per Quintal")){

                        }
                        if(RUNIT.equals("Per KG")){
                            RPRICE= (long) (RPRICE*0.01);
                        }
                        else if(RUNIT.equals("Per Ton")){
                            RPRICE= (long) (RPRICE*0.1);
                        }
                        else if(RUNIT.equals("Per Quintal")){

                        }
                        DatabaseReference mref= FirebaseDatabase.getInstance().getReference();
                        final String finalType = type;
                        final long finalPRICE = PRICE;
                        final long finalRPRICE = RPRICE;
                        mref.child("basePrice").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                long p=dataSnapshot.child(finalType).getValue(Long.class);
                                if(p<=finalPRICE||p<= finalRPRICE)
                                {
                                    listener.addcitytoDB(ORIGIN,DEST, finalPRICE, finalType,finalRPRICE);
                                }
                                else{
                                   // Toast.makeText(getContext(), "Invalid Price", Toast.LENGTH_SHORT).show();
                                    Snackbar snackbar = Snackbar.make(Partner.l, "Invalid!!! Price Exceeds Base Price ", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                      //  listener.addcitytoDB(ORIGIN,DEST,PRICE,type);
                    }
                });

        origin = view.findViewById(R.id.origin);
        dest = view.findViewById(R.id.dest);
        price = view.findViewById(R.id.price);
        unit = view.findViewById(R.id.unit);
        rg=view.findViewById(R.id.rg);
        lout=view.findViewById(R.id.layout);
        returnprice=view.findViewById(R.id.rprice);
        runit=view.findViewById(R.id.runit);
        ArrayList<String> l=new ArrayList<>();
        l.add("Per Quintal");
        l.add("Per KG");
        l.add("Per Ton");
        ArrayAdapter a= new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, l);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit.setAdapter(a);
        runit.setAdapter(a);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface DialogListener {
        void addcitytoDB(String origin,String dest,long price,String type,long rprice);
    }
}
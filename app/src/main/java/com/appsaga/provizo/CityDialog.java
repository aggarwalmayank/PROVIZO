package com.appsaga.provizo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import static android.R.layout.simple_spinner_item;

public class CityDialog extends DialogFragment {
    private EditText origin,dest,price;
    private Spinner unit;
    private DialogListener listener;
    private RadioGroup rg;


    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
                        String ORIGIN= origin.getText().toString();
                        String DEST= dest.getText().toString();
                        String PRICE = price.getText().toString();
                        String UNIT= unit.getSelectedItem().toString();

                        int selectedId = rg.getCheckedRadioButtonId();
                        RadioButton rb = (RadioButton) view.findViewById(selectedId);
                        String type=rb.getText().toString();
                        type=type.replaceAll("\\s+","");
                        if(UNIT.equals("Per KG")){
                            PRICE=String.valueOf((Double.parseDouble(PRICE))*100);
                        }
                        else if(UNIT.equals("Per Ton")){
                            PRICE=String.valueOf((Double.parseDouble(PRICE))*0.1);
                        }
                        else if(UNIT.equals("Per Quintal")){

                        }
                        listener.addcitytoDB(ORIGIN,DEST,PRICE,type);
                    }
                });

        origin = view.findViewById(R.id.origin);
        dest = view.findViewById(R.id.dest);
        price = view.findViewById(R.id.price);
        unit = view.findViewById(R.id.unit);
        rg=view.findViewById(R.id.rg);
        ArrayList<String> l=new ArrayList<>();
        l.add("Per Quintal");
        l.add("Per KG");
        l.add("Per Ton");
        ArrayAdapter a= new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, l);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit.setAdapter(a);
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
        void addcitytoDB(String origin,String dest,String price,String type);
    }
}
package com.appsaga.provizo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import static android.R.layout.simple_spinner_item;

public class AdminAddPartnerDialog extends DialogFragment {
    private EditText address,name,id,experience,owner,origin,dest,price;
    private DialogListener listener;
    private CheckBox partload,fulltruckload,open,close;
    RadioGroup rg;
    RadioButton rb1;


    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.adminaddpartnerdialog, null);

        builder.setView(view)
                .setTitle("Add Partner")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ID= id.getText().toString();
                        String NAME= name.getText().toString();
                        String EXP = experience.getText().toString();
                        String Add= address.getText().toString();
                        String OWNER= owner.getText().toString();
                        String ORIGIN= origin.getText().toString();
                        String DEST= dest.getText().toString();
                        int selectedId = rg.getCheckedRadioButtonId();
                        RadioButton rb = (RadioButton) view.findViewById(selectedId);
                        String type=rb.getText().toString();
                        long PRICE= Long.parseLong(price.getText().toString());
                        Boolean Part=false,fullload=false,Open=false,Close=false;
                        if(partload.isChecked())
                            Part=true;
                        if(fulltruckload.isChecked())
                            fullload=true;

                        if(open.isChecked())
                            Open =true;
                        if(close.isChecked())
                            Close=true;
                        listener.addpartner(ID,NAME,EXP,Add,OWNER,Part,fullload,ORIGIN,DEST,PRICE,type,Close,Open);
                    }
                });

        id= view.findViewById(R.id.partnerid);
        name= view.findViewById(R.id.companyname);
        experience=view.findViewById(R.id.Experience);
        address= view.findViewById(R.id.address);
        owner= view.findViewById(R.id.owner);
        partload=view.findViewById(R.id.cb1);
        origin= view.findViewById(R.id.origin);
        dest= view.findViewById(R.id.dest);
        price=view.findViewById(R.id.price);
        rg=view.findViewById(R.id.rg);
        rb1=view.findViewById(R.id.rb1);
        rb1.setChecked(true);
        fulltruckload=view.findViewById(R.id.cb2);
        open=view.findViewById(R.id.cb3);
        close=view.findViewById(R.id.cb4);
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
        void addpartner(String id,String name,String exp,String add,String owner,Boolean part,Boolean full,String origin,String dest,long price,String type
        ,Boolean Close,Boolean Open);
    }
}
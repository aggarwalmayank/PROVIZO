package com.appsaga.provizo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileDialog extends DialogFragment {
    private TextView name,gender,dob,number,email;
    private DialogListener listener;
    FirebaseUser user;
    DatabaseReference mref;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.profiledialog, null);
        user= FirebaseAuth.getInstance().getCurrentUser();
        mref= FirebaseDatabase.getInstance().getReference();
        String emailid=user.getEmail().trim();
        builder.setView(view);
        name=view.findViewById(R.id.name);
        gender=view.findViewById(R.id.gender);
        dob=view.findViewById(R.id.dob);
        number=view.findViewById(R.id.number);
        email=view.findViewById(R.id.email);
        email.setText("Email: "+user.getEmail());
        mref.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                number.setText("Phone: "+dataSnapshot.child("Phone Number").getValue().toString());
                dob.setText("Date of Birth: "+dataSnapshot.child("Date of Birth").getValue().toString());
                name.setText("Name: "+dataSnapshot.child("Name").getValue().toString());
                gender.setText("Gender: "+dataSnapshot.child("Gender").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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
        void applyTexts();
    }
}
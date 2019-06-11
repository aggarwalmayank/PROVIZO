package com.appsaga.provizo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Payment extends AppCompatActivity {
    ImageView gpay;

    final int UPI_PAYMENT = 0;
    String amount,note,orderid,currentuser,company,emailandname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        amount=getIntent().getStringExtra("amount");
        orderid=getIntent().getStringExtra("Order ID");
        currentuser=getIntent().getStringExtra("Current User");
        company=getIntent().getStringExtra("company");
        note="Order ID: "+orderid+" Customer ID: "+currentuser+" for Company: "+company;
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(currentuser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emailandname=dataSnapshot.child("Email").getValue()+"   "+dataSnapshot.child("Name").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(this, amount, Toast.LENGTH_SHORT).show();
        TextView tv = findViewById(R.id.appname);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/copperplatebold.ttf");
        tv.setTypeface(typeface);

        gpay = findViewById(R.id.gpay);
        gpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Payment.this,completed.class);

                Toast.makeText(Payment.this, currentuser+orderid, Toast.LENGTH_SHORT).show();
                i.putExtra("Order ID",orderid);
                i.putExtra("Current User",currentuser);
                i.putExtra("company",company);
                i.putExtra("amount",amount);
                startActivity(i);
                // payUsingUpi(amount);
            }
        });
    }

    void payUsingUpi(String amount) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "99mayankagg@okhdfcbank")
                .appendQueryParameter("pn", emailandname)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", "1")
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(Payment.this, "No UPI app found, please install one to continue",  Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if ((Boolean) isConnectionAvailable(Payment.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Intent i=new Intent(Payment.this,completed.class);
                i.putExtra("Order ID",orderid);
                i.putExtra("Current User",currentuser);
                i.putExtra("company",company);
                i.putExtra("amount",amount);
                startActivity(i);
                Toast.makeText(Payment.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(Payment.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Payment.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Payment.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Payment context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}
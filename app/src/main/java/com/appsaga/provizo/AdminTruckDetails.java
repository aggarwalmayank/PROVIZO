package com.appsaga.provizo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class AdminTruckDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_truck_details);
        AdminTruckHelper ath= (AdminTruckHelper) getIntent().getSerializableExtra("ath");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.go_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView orderid=findViewById(R.id.appsignup);
        TextView user = findViewById(R.id.order_id);
        TextView mode = findViewById(R.id.mode);
        TextView risk = findViewById(R.id.risk);
        TextView comp = findViewById(R.id.truck_comp);
        TextView pickup = findViewById(R.id.pick_loc);
        TextView drop = findViewById(R.id.drop_loc);
        TextView pickDate = findViewById(R.id.pick_date);
        TextView amount = findViewById(R.id.amount);
        TextView consigneeName = findViewById(R.id.consignee_name);
        TextView consigneeAddress = findViewById(R.id.consignee_address);
        TextView consigneePhone = findViewById(R.id.consignee_phone);
        TextView consigneeGST = findViewById(R.id.consignee_gst);
        TextView consignorName = findViewById(R.id.consignor_name);
        TextView consignorAddress = findViewById(R.id.consignor_address);
        TextView consignorPhone = findViewById(R.id.consignor_phone);
        TextView consignorGST = findViewById(R.id.consignor_gst);
        TextView serviceType = findViewById(R.id.service_type);
        TextView description = findViewById(R.id.desc);
        TextView weight = findViewById(R.id.weight);
        TextView truckType = findViewById(R.id.truck_type);

        HashMap<String,String> consignee=ath.getConsignee();
        HashMap<String,String> consignor=ath.getConsignor();
        HashMap<String,String> service=ath.getServiceTruckDetails();

        orderid.setText(ath.getKey());
        user.setText("user id "+ath.getUser());
        mode.setText("Delivery mode: "+ath.getDeliveryMode());
        risk.setText("Goods Risk: "+ath.getGoodsRisk());
        comp.setText("Comapny Name: "+ath.getTruckCompany());
        pickup.setText("PickUp Loc: "+ath.getPickUpLocation());
        drop.setText("Drop Loc: "+ath.getDropLocation());
        pickDate.setText("PickUp Date: "+ath.getPickUpDate());
        amount.setText("Amountt: "+ath.getAmount());
        consigneeName.setText("Name: "+consignee.get("ConsigneeName"));
        consigneeAddress.setText("Address: "+consignee.get("Address"));
        consigneeGST.setText("GST: "+consignee.get("GST"));
        consigneePhone.setText("Phone: "+consignee.get("PhoneNumber"));
        consignorName.setText("Name: "+consignor.get("ConsignorName"));
        consignorGST.setText("GST: "+consignor.get("GST"));
        consignorPhone.setText("Phone: "+consignor.get("PhoneNumber"));
        consignorAddress.setText("Address: "+consignor.get("Address"));
        serviceType.setText("Service Type: "+service.get("ServiceType"));
        description.setText("Description: "+service.get("MaterialDescription"));
        weight.setText("Weight: "+service.get("Weight"));
        truckType.setText("Truck Type: "+service.get("TruckType"));

    }
}

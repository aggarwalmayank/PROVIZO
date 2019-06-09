package com.appsaga.provizo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ServiceAdapter extends ArrayAdapter<Services> {

    public ServiceAdapter(Context context, ArrayList<Services> services) {
        super(context, 0,services);
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        View serviceView = convertView;

        if(serviceView==null)
        {
            serviceView = LayoutInflater.from(getContext()).inflate(
                    R.layout.service_view, parent, false);
        }

        Services services = getItem(position);

        TextView compName = serviceView.findViewById(R.id.comp_name);
        TextView price = serviceView.findViewById(R.id.price);

        compName.setText(services.getCompanyName());
        price.setText("Price\n"+services.getPrice());
        return serviceView;
    }
}

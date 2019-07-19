package com.appsaga.provizo;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class DeleteCityAdapter extends ArrayAdapter<locMap> {

    public DeleteCityAdapter(Context context, ArrayList<locMap> locMaps) {
        super(context, 0,locMaps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View deleteCityView = convertView;

        if(deleteCityView==null)
        {
            deleteCityView = LayoutInflater.from(getContext()).inflate(R.layout.delete_city_view,parent,false);
        }

        TextView source = deleteCityView.findViewById(R.id.source);
        TextView dest = deleteCityView.findViewById(R.id.dest);
        TextView price = deleteCityView.findViewById(R.id.price);
        final CheckBox checkBox = deleteCityView.findViewById(R.id.delete_checkbox);

        locMap currentLocMap = getItem(position);

        source.setText(currentLocMap.getSource());
        dest.setText(currentLocMap.getDest());
        price.setText("₹ "+currentLocMap.getPrice());

        return deleteCityView;
    }
}

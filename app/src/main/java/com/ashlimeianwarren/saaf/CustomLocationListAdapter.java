package com.ashlimeianwarren.saaf;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashlimeianwarren.saaf.Beans.WhatsThis.Data;

/**
 * Created by Ash on 01/04/2015.
 */
public class CustomLocationListAdapter extends ArrayAdapter
{

    public CustomLocationListAdapter(Context context, Object[] objects)
    {
        super(context,R.layout.custom_location_list, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_location_list, parent, false);
        ImageView imageView = (ImageView) customView.findViewById(R.id.customLocation_pinIcon);
        TextView textView = (TextView) customView.findViewById(R.id.customLocation_locationName);
        Data d = (Data) getItem(position);

        textView.setText(d.getDataName());
        imageView.setImageResource(R.drawable.pinicon);

        return customView;
    }
}

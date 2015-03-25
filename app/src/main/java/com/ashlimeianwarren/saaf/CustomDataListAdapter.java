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
 * Created by Liam on 23/03/2015.
 */
public class CustomDataListAdapter extends ArrayAdapter
{
    public CustomDataListAdapter(Context context, Object[] objects)
    {
        super(context, R.layout.custom_folder_list, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Log.d("Custom Data Adapter", "Loaded Into Adapter");
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_places_list, parent, false);
        ImageView imageView = (ImageView) customView.findViewById(R.id.customFolder_folderIcon);
        TextView textView = (TextView) customView.findViewById(R.id.customFolder_folderName);
        Data d = (Data) getItem(position);
        Log.d("Custom Data Adapter", "Setting Text");
        Log.d("Found Data", d.toString());
        textView.setText(d.getDataName());
        imageView.setImageResource(R.drawable.foldericon);

        return customView;
    }
}

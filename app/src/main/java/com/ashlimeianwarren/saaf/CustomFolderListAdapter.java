package com.ashlimeianwarren.saaf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ash on 06/03/2015.
 */
public class CustomFolderListAdapter extends ArrayAdapter
{


    public CustomFolderListAdapter(Context context, Object[] objects)
    {
        super(context,R.layout.custom_folder_list, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_folder_list,parent,false);
        ImageView imageView = (ImageView)customView.findViewById(R.id.customeFolder_folderIcon);
        TextView textView = (TextView)customView.findViewById(R.id.customFolder_folderName);
        String folderName = getItem(position).toString();

        textView.setText(folderName);
        imageView.setImageResource(R.drawable.foldericon);

        return customView;
    }
}

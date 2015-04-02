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
import com.ashlimeianwarren.saaf.Beans.WheresMyCar.PointOfInterest;

/**
 * A class representing a list of locations.
 */
public class CustomLocationListAdapter extends ArrayAdapter
{

    /**
     * Constructor for this List Adapter
     * @param context The Context from which this list was created.
     * @param objects The objects to be stored in this list.
     */
    public CustomLocationListAdapter(Context context, Object[] objects)
    {
        super(context, R.layout.custom_location_list, objects);

    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return            A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_location_list, parent, false);
        ImageView imageView = (ImageView) customView.findViewById(R.id.customLocation_pinIcon);
        TextView textView = (TextView) customView.findViewById(R.id.customLocation_locationName);
        PointOfInterest p = (PointOfInterest) getItem(position);

        textView.setText(p.getInfo());
        imageView.setImageResource(R.drawable.pinicon);

        return customView;
    }
}

package com.ashlimeianwarren.saaf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.MediaNote;
import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.Note;
import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.Subject;
import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.TextNote;

/**
 * Created by Ash on 13/03/2015.
 */
public class CustomNoteListAdapter extends ArrayAdapter
{
    public CustomNoteListAdapter(Context context, Object[] objects)
    {
        super(context, R.layout.custom_note_list, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_note_list, parent, false);
        ImageView imageView = (ImageView) customView.findViewById(R.id.customNote_noteIcon);
        TextView textView = (TextView) customView.findViewById(R.id.customNote_noteName);
        Note n = (Note) getItem(position);
        switch (n.getType())
        {
            case "Text":
                TextNote tNote = (TextNote) n;
                textView.setText(tNote.getTextName());
                imageView.setImageResource(R.drawable.fileicon);
                break;
            case "Audio":
                MediaNote mNote = (MediaNote) n;
                textView.setText("Audio Note");
                imageView.setImageResource(R.drawable.audioicon);
                break;
            case "Image":
                MediaNote iNote = (MediaNote) n;
                textView.setText("Image Note");
                imageView.setImageResource(R.drawable.imageicon);
                break;
            default:


        }


        return customView;
    }

}

package com.camlab.simplechatalexslo;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyArrayAdapter <T> extends ArrayAdapter<String> {
    Context mContext;

    public MyArrayAdapter(Context context, int textViewResourceId) {
        super (context, textViewResourceId);

        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView text = (TextView) view.findViewById(android.R.id.text1);
        String msq = text.getText().toString();
        if(validateHashtag(msq)){
            text.setTextColor(Color.RED);
        }
        return view;
    }
    private boolean validateHashtag (String _msg)
    {
        return _msg.charAt(4) == '#';
    }
}

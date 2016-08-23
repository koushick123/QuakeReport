package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Koushick on 22-08-2016.
 */

public class EarthQuakeAdapter extends ArrayAdapter {


    public EarthQuakeAdapter(Context context, ArrayList<EarthQuake> quakes) {
        super(context, 0, quakes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        if(listItem == null)
        {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item,parent,false);
        }

        EarthQuake earthQuake = (EarthQuake) getItem(position);

        TextView text1 = (TextView)listItem.findViewById(R.id.text1);
        TextView text2 = (TextView)listItem.findViewById(R.id.text2);
        TextView text3 = (TextView)listItem.findViewById(R.id.text3);

        GradientDrawable gradientDrawable = (GradientDrawable)text1.getBackground();

        if(earthQuake.getMagnitude() >= 10.0)
        {
            gradientDrawable.setColor(ContextCompat.getColor(getContext(),R.color.magnitude10plus));
        }
        else if (earthQuake.getMagnitude() >= 9.0 && earthQuake.getMagnitude() < 10.0)
        {
            gradientDrawable.setColor(ContextCompat.getColor(getContext(),R.color.magnitude9));
        }
        else if (earthQuake.getMagnitude() >= 8.0 && earthQuake.getMagnitude() < 9.0)
        {
            gradientDrawable.setColor(ContextCompat.getColor(getContext(),R.color.magnitude8));
        }
        else if (earthQuake.getMagnitude() >= 7.0 && earthQuake.getMagnitude() < 8.0)
        {
            gradientDrawable.setColor(ContextCompat.getColor(getContext(),R.color.magnitude7));
        }
        else if (earthQuake.getMagnitude() >= 6.0 && earthQuake.getMagnitude() < 7.0)
        {
            gradientDrawable.setColor(ContextCompat.getColor(getContext(),R.color.magnitude6));
        }
        else if (earthQuake.getMagnitude() >= 5.0 && earthQuake.getMagnitude() < 6.0)
        {
            gradientDrawable.setColor(ContextCompat.getColor(getContext(),R.color.magnitude5));
        }
        else if (earthQuake.getMagnitude() >= 4.0 && earthQuake.getMagnitude() < 5.0)
        {
            gradientDrawable.setColor(ContextCompat.getColor(getContext(),R.color.magnitude4));
        }
        else if (earthQuake.getMagnitude() >= 3.0 && earthQuake.getMagnitude() < 4.0)
        {
            gradientDrawable.setColor(ContextCompat.getColor(getContext(),R.color.magnitude3));
        }
        else if (earthQuake.getMagnitude() >= 2.0 && earthQuake.getMagnitude() < 3.0)
        {
            gradientDrawable.setColor(ContextCompat.getColor(getContext(),R.color.magnitude2));
        }
        else if (earthQuake.getMagnitude() >= 1.0 && earthQuake.getMagnitude() < 2.0)
        {
            gradientDrawable.setColor(ContextCompat.getColor(getContext(),R.color.magnitude1));
        }
        text1.setText(formatMag(earthQuake.getMagnitude()));
        text2.setText(earthQuake.getLocation());
        formatTime(new Date(earthQuake.getDate_time()));
        text3.setText(formatDate(new Date(earthQuake.getDate_time()))+" "+formatTime(new Date(earthQuake.getDate_time())));
        return listItem;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatMag(double mag)
    {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(mag);
    }
}

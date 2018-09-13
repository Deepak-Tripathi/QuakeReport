package com.example.deepak.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake> {
    public EarthQuakeAdapter(Activity context, ArrayList<EarthQuake> Words) {

        super(context, 0, Words);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        EarthQuake currentQuake = getItem(position);

        // DISPLAYING MAGNITUDE AND FORMATTING IT

        double magnitude = currentQuake.mGetMagnitude();

        String formatMag = decimalFormat(magnitude);

        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);

        magnitudeView.setText(formatMag);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.

        GradientDrawable magnitudeCircle =(GradientDrawable) magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentQuake.mGetMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        // DISPLAYING LOCATION IN TWO TEXT VIEWS

        String OriginalLocation = currentQuake.mGetLocation();

        String Location_offset, Primary_location;

        String seperator = "of";

        if (OriginalLocation.contains(seperator)) {
            String Part[] = OriginalLocation.split(seperator);

            Location_offset = Part[0] + seperator;

            Primary_location = Part[1];
        } else {
            Location_offset = getContext().getString(R.string.near_the);

            Primary_location = OriginalLocation;
        }

        TextView offset = (TextView) listItemView.findViewById(R.id.location_offset);

        offset.setText(Location_offset);


        TextView primary = (TextView) listItemView.findViewById(R.id.primary_location);

        primary.setText(Primary_location);

        // DISPLAYIND DATE AND TIME IN TWO TEXT VIEWS

        long timeInMillisec = currentQuake.mGetDate();

        Date dateObject = new Date(timeInMillisec);

        TextView date = (TextView) listItemView.findViewById(R.id.date);

        String DateToDisplay = dateFormat(dateObject);

        date.setText(DateToDisplay);

        TextView time = (TextView) listItemView.findViewById(R.id.time);

        String TimeToDisplay = TimeFormat(dateObject);

        time.setText(TimeToDisplay);


        return listItemView;
    }

    // METHOD FOR DATE FORMAT

    public String dateFormat(Date dateObject) {
        SimpleDateFormat DateFor = new SimpleDateFormat("LLL dd ,yyyy");

        String dayTodisplay = DateFor.format(dateObject);

        return dayTodisplay;

    }

    // METHOD FOR TIME FORMAT

    public String TimeFormat(Date dateObject) {
        SimpleDateFormat TimeFor = new SimpleDateFormat("h:MM a");

        String timeTodisplay = TimeFor.format(dateObject);

        return timeTodisplay;
    }

    // METHOD FOR DOUBLE FORMAT

    public  String decimalFormat(double mag)
    {
        DecimalFormat decFor = new DecimalFormat("0.0");

        String displayMag = decFor.format(mag);

        return displayMag;
    }

    public int getMagnitudeColor(double magnitude)
    {
        int MagnitudeResourceID=0;

        int magnitudeFloor = (int ) Math.floor(magnitude);

        switch (magnitudeFloor)
        {
            case 0:
            case 1: MagnitudeResourceID=R.color.magnitude1;
                break;
            case 2: MagnitudeResourceID=R.color.magnitude2;
                break;
            case 3: MagnitudeResourceID=R.color.magnitude3;
                break;
            case 4: MagnitudeResourceID=R.color.magnitude4;
                break;
            case 5: MagnitudeResourceID=R.color.magnitude5;
                break;
            case 6: MagnitudeResourceID=R.color.magnitude6;
                break;
            case 7: MagnitudeResourceID=R.color.magnitude7;
                break;
            case 8: MagnitudeResourceID=R.color.magnitude8;
                break;
            case 9: MagnitudeResourceID=R.color.magnitude9;
                break;
            default: MagnitudeResourceID=R.color.magnitude10plus;
                break;

        }

        //convert the color resource ID into a color integer value

        return ContextCompat.getColor(getContext(), MagnitudeResourceID);
    }



}

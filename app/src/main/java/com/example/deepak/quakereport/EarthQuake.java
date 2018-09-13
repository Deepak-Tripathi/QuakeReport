package com.example.deepak.quakereport;

public class EarthQuake {
    private double mMagnitude;
    private String mLocation;
    private long mDate;
    private String mURL;

    EarthQuake(double mag, String loc, long date, String Url) {
        mMagnitude = mag;
        mLocation = loc;
        mDate = date;
        mURL = Url;
    }

    public double mGetMagnitude() {
        return mMagnitude;
    }

    public String mGetLocation() {
        return mLocation;
    }

    public long mGetDate() {
        return mDate;
    }

    public String mGetUrl() {
        return mURL;
    }


}

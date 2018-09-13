package com.example.deepak.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.support.v4.app.LoaderManager;

import java.util.ArrayList;

public class EarthQuakeLoader extends AsyncTaskLoader <ArrayList<EarthQuake>>{

    String murl =null;

    public EarthQuakeLoader(Context context , String url)
    {
        super(context);

        murl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public ArrayList<EarthQuake> loadInBackground()
    {
      if(murl == null)
      {
          return null;
      }

        ArrayList<EarthQuake> result = QueryUtils.FetchEarthQuakeData(murl);
        return result;
    }

}

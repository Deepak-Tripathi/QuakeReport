package com.example.deepak.quakereport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.LoaderManager.LoaderCallbacks;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<EarthQuake>> {

    private EarthQuakeAdapter mAdapter;

    private TextView emptyListText;

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=200";

    private static final int EARTHQUAKE_LOADER_ID = 1;

    private ProgressBar spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView earthquakeslistview = (ListView) findViewById(R.id.list);

        mAdapter = new EarthQuakeAdapter(this, new ArrayList<EarthQuake>());

        earthquakeslistview.setAdapter(mAdapter);

        emptyListText = (TextView) findViewById(R.id.EmptyList);

        earthquakeslistview.setEmptyView(emptyListText);

        earthquakeslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                EarthQuake earth = mAdapter.getItem(i);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(earth.mGetUrl()));
                Intent browserChooserIntent = Intent.createChooser(browserIntent, "Choose browser of your choice");
                startActivity(browserChooserIntent);
            }
        });


        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {

            emptyListText.setText("No Internet Connection");

            spin = (ProgressBar) findViewById(R.id.Progress);

            spin.setVisibility(View.GONE);


        }
    }


    @Override
    public Loader<ArrayList<EarthQuake>> onCreateLoader(int i, @Nullable Bundle bundle) {

        return new EarthQuakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<EarthQuake>> loader, ArrayList<EarthQuake> earthQuakes) {

        emptyListText.setText("No EarthQuake Found");

        spin = (ProgressBar) findViewById(R.id.Progress);

        spin.setVisibility(View.GONE);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthQuakes != null && !earthQuakes.isEmpty()) {
            mAdapter.addAll(earthQuakes);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<EarthQuake>> loader) {
        mAdapter.clear();
    }
}

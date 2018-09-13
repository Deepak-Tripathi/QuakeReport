package com.example.deepak.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

// Helper methods related to requesting and receiving earthquake data from USGS.

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    // Private Constructor

    private QueryUtils() {

    }

    public static ArrayList<EarthQuake> FetchEarthQuakeData(String requestUrl) {

        URL url = CreateUrl(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem while making HttpRequest");
        }

        ArrayList<EarthQuake> earthQuakes = extractEarthquakes(jsonResponse);

        return earthQuakes;
    }

    private static URL CreateUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, " Problem while creating URL");
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String JsonResponse = "";

        if (url == null) {
            return JsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();

                JsonResponse = ReadFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {

            Log.e(LOG_TAG, "Error in making Http Request ");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return JsonResponse;
    }

    private static String ReadFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }


    // returning array list

    private static ArrayList<EarthQuake> extractEarthquakes(String earthQuakeJson) {

        if (TextUtils.isEmpty(earthQuakeJson)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<EarthQuake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            JSONObject root = new JSONObject(earthQuakeJson);

            JSONArray featturesArray = root.getJSONArray("features");

            for (int i = 0; i < featturesArray.length(); i++) {
                JSONObject positionI = featturesArray.getJSONObject(i);

                JSONObject PropertiesI = positionI.getJSONObject("properties");

                double mag = PropertiesI.getDouble("mag");
                String place = PropertiesI.getString("place");
                long time = PropertiesI.getLong("time");
                String url = PropertiesI.getString("url");

                earthquakes.add(new EarthQuake(mag, place, time, url));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }


}

package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Koushick on 27-08-2016.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<EarthQuake>> {

    String url;
    public EarthquakeLoader(Context context) {
        super(context);
    }

    public EarthquakeLoader(Context context,String url)
    {
        super(context);
        this.url=url;
    }

    @Override
    protected void onStartLoading() {
        Log.d(""+this.getClass(),"onStartLoading");
        super.onStartLoading();
    }

    @Override
    public List<EarthQuake> loadInBackground() {
        Log.d(""+this.getClass(),"loadInBackground");
        if (this.url != null) {
            try {
                URL usgs_url = new URL(this.url);
                HttpURLConnection httpURLConnection;
                InputStream inputStream;
                try {
                    httpURLConnection = (HttpURLConnection) usgs_url.openConnection();
                    httpURLConnection.setReadTimeout(10000);
                    httpURLConnection.setConnectTimeout(15000);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode() == 200) {
                        inputStream = httpURLConnection.getInputStream();
                        String jsonResp = readFromStream(inputStream);
                        return QueryUtils.extractEarthquakes(jsonResp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
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
}

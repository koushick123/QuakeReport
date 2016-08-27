/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=2&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        new EarthQuakeRequest().execute(USGS_URL);
        /*ListView earthquakeListView = (ListView)findViewById(R.id.list);
        ArrayList<EarthQuake> earthQuakes = new ArrayList<EarthQuake>();
        earthQuakes.add(new EarthQuake(6.2,"Loc1",1231213,"Url"));
        final EarthQuakeAdapter adapter = new EarthQuakeAdapter(getBaseContext(),earthQuakes);
        earthquakeListView.setAdapter(adapter);*/
    }

    private class EarthQuakeRequest extends AsyncTask<String, Void, ArrayList<EarthQuake>>
    {
        @Override
        protected void onPostExecute(ArrayList<EarthQuake> earthQuakes) {
            UpdateUI(earthQuakes);
        }

        @Override
        protected ArrayList<EarthQuake> doInBackground(String... url) {

            if(url[0] != null)
            {
                try {
                URL usgs_url = new URL(url[0]);
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
                }catch(MalformedURLException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        private void UpdateUI(ArrayList<EarthQuake> earthQuakes)
        {
            ListView earthquakeListView = (ListView)findViewById(R.id.list);
            Log.d("EarthQuakes == ",""+earthQuakes.toString());
            final EarthQuakeAdapter adapter = new EarthQuakeAdapter(getBaseContext(),earthQuakes);
            earthquakeListView.setAdapter(adapter);

            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(((EarthQuake)adapter.getItem(position)).getUrl()));
                    startActivity(browserIntent);
                }
            });
        }
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

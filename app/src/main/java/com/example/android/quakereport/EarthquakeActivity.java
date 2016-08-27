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

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=2&limit=10";
    ListView earthquakeListView;
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        getLoaderManager().initLoader(1,null,this).forceLoad();
        spinner = (ProgressBar)findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int i, Bundle bundle) {
        Log.d(LOG_TAG,"initLoader");
        return new EarthquakeLoader(getApplicationContext(),USGS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<EarthQuake>> loader, List<EarthQuake> earthQuakes) {
        Log.d(LOG_TAG,"onLoadFinished");
        if(earthQuakes != null) {
            UpdateUI(new ArrayList<EarthQuake>(earthQuakes));
        }
        else{
            UpdateUI(new ArrayList<EarthQuake>());
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.d(LOG_TAG,"onLoaderReset");
        UpdateUI(new ArrayList<EarthQuake>());
    }

    private void UpdateUI(ArrayList<EarthQuake> earthQuakes)
    {
        earthquakeListView = (ListView)findViewById(R.id.list);
        Log.d("EarthQuakes == ",""+earthQuakes.toString());
        if(earthQuakes != null && earthQuakes.size() > 0) {
            spinner.setVisibility(View.INVISIBLE);
            final EarthQuakeAdapter adapter = new EarthQuakeAdapter(getBaseContext(), earthQuakes);
            earthquakeListView.setAdapter(adapter);

            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(((EarthQuake) adapter.getItem(position)).getUrl()));
                    startActivity(browserIntent);
                }
            });
        }
        else if(earthQuakes == null || earthQuakes.size() == 0)
        {
            TextView textView = (TextView)findViewById(R.id.placeholderText);
            textView.setText(getResources().getString(R.string.emptyData));
            spinner.setVisibility(View.INVISIBLE);
            earthquakeListView.setEmptyView(textView);
        }
    }
}

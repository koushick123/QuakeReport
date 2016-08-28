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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=2&limit=10";
    ListView earthquakeListView;
    ProgressBar spinner;
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    String EMPTY_TEXT = "", NO_INT_DATA = "No Internet Data", NO_DATA_FOUND = "No Data Found";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(getBroadcastRece(),new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        setContentView(R.layout.earthquake_activity);
        spinner = (ProgressBar) findViewById(R.id.spinner);
        getInternetConnection();
        if (networkInfo != null && networkInfo.isConnected())
        {
            getLoaderManager().initLoader(1, null, getEarthQuakeActObj()).forceLoad();
            spinner.setVisibility(View.VISIBLE);
        }
        else
        {
            earthquakeListView = (ListView)findViewById(R.id.list);
            setEmptyListView(NO_INT_DATA);
        }
    }

    private EarthquakeActivity getEarthQuakeActObj()
    {
        return EarthquakeActivity.this;
    }

    private BroadcastReceiver broadcastRece = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                for (String key: extras.keySet())
                {
                    if(key.equalsIgnoreCase("networkInfo"))
                    {
                        NetworkInfo networkInfo = (NetworkInfo) extras.get(key);
                        Log.d(LOG_TAG, "" + networkInfo.getState());
                        if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                            if (earthquakeListView == null) {
                                earthquakeListView = (ListView) findViewById(R.id.list);
                            }
                            setEmptyListView(EMPTY_TEXT);
                            getLoaderManager().initLoader(1, null, getEarthQuakeActObj()).forceLoad();
                            spinner.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    };

    private BroadcastReceiver getBroadcastRece()
    {
        return this.broadcastRece;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG,"onResume");
        getInternetConnection();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if (earthquakeListView == null || (earthquakeListView.getEmptyView() != null && !((TextView)earthquakeListView.getEmptyView()).getText().toString().equalsIgnoreCase(EMPTY_TEXT)))
            {
                Log.d(LOG_TAG,"Resume getting data");
                if(earthquakeListView == null)
                {
                    earthquakeListView = (ListView) findViewById(R.id.list);
                }
                setEmptyListView(EMPTY_TEXT);
                getLoaderManager().initLoader(1, null, getEarthQuakeActObj()).forceLoad();
                spinner.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        deRegisterConnectionReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        deRegisterConnectionReceiver();
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
        Log.d("EarthQuakes == ",""+earthQuakes);
        if(earthQuakes != null && earthQuakes.size() > 0)
        {
            setEmptyListView(EMPTY_TEXT);
            final EarthQuakeAdapter adapter = new EarthQuakeAdapter(getBaseContext(), earthQuakes);
            earthquakeListView.setAdapter(adapter);

            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
                {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(((EarthQuake) adapter.getItem(position)).getUrl()));
                    startActivity(browserIntent);
                }
            });
        }
        else if(earthQuakes == null || earthQuakes.size() == 0)
        {
            setEmptyListView(NO_DATA_FOUND);
        }
    }

    private void getInternetConnection()
    {
        connMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
    }

    private void setEmptyListView(String textSel)
    {
        if(textSel.equalsIgnoreCase(EMPTY_TEXT))
        {
            TextView textView = (TextView) findViewById(R.id.placeholderText);
            textView.setText(getResources().getString(R.string.blank));
            earthquakeListView.setEmptyView(textView);
        }
        else if(textSel.equalsIgnoreCase(NO_INT_DATA))
        {
            TextView textView = (TextView)findViewById(R.id.placeholderText);
            textView.setText(getResources().getString(R.string.noInternet));
            earthquakeListView.setEmptyView(textView);
        }
        else if(textSel.equalsIgnoreCase(NO_DATA_FOUND))
        {
            TextView textView = (TextView)findViewById(R.id.placeholderText);
            textView.setText(getResources().getString(R.string.emptyData));
            earthquakeListView.setEmptyView(textView);
        }
        spinner.setVisibility(View.INVISIBLE);
    }

    private void deRegisterConnectionReceiver()
    {
        if(getBroadcastRece() != null) {
            try {
                unregisterReceiver(getBroadcastRece());
                this.broadcastRece = null;
            }
            catch (IllegalArgumentException illegal)
            {
                Log.e(LOG_TAG,illegal.getMessage());
            }
        }
    }
}

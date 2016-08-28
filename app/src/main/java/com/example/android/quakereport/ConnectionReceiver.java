package com.example.android.quakereport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static android.R.attr.tag;

/**
 * Created by Koushick on 28-08-2016.
 */

public class ConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(""+ConnectionReceiver.class.getName(), "action: " + intent.getAction());
        Log.d(""+ConnectionReceiver.class.getName(), "component: " + intent.getComponent());
        Bundle extras = intent.getExtras();
        if (extras != null) {
            for (String key: extras.keySet()) {
                Log.d(""+ConnectionReceiver.class.getName(), "key [" + key + "]: " +
                        extras.get(key));
            }
        }
        else {
            Log.d(""+ConnectionReceiver.class.getName(), "no extras");
        }

    }
}

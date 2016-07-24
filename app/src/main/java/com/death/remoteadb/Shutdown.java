package com.death.remoteadb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class Shutdown extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences settings = context.getSharedPreferences("wireless", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("mState", false);
        editor.commit();

        if (Utility.prefsOnBoot(context)) {

            if (!Utility.hasRootPermission()) {
                Toast.makeText(context, "No root", Toast.LENGTH_LONG).show();
                return;
            }

            if (!Utility.checkWifiState(context)) {
                MainActivity.wifiState = false;
                Utility.saveWiFiState(context, MainActivity.wifiState);

                if (Utility.prefsWiFiOn(context)) {
                    Utility.enableWiFi(context, true);
                } else {
                    Toast.makeText(context, "No wifi", Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                MainActivity.wifiState = true;
                Utility.saveWiFiState(context, MainActivity.wifiState);
            }

            try {
                Utility.adbStart(context);
            } catch (Exception e) {
                Log.e("call adbStart() ERROR", e.getMessage());
            }

        }
    }
}
package com.death.remoteadb;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String PORT = "5555";
    public static final boolean USB_DEBUG = false;
    public static boolean mState = false;
    public static boolean wifiState;
    private Button iv_button;
    private TextView tv1,tv2;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.tv1 = (TextView) findViewById(R.id.tv1);
        this.tv2 = (TextView) findViewById(R.id.tv2);
        this.imageView = (ImageView)  findViewById(R.id.imageView);
        this.iv_button = (Button) findViewById(R.id.button3);

        if (Utility.manager == null) {
            Utility.manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        if (!Utility.hasRootPermission()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This app requires rooted device").setCancelable(true)
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    });
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.create();
            builder.setTitle("NO ROOT");
            builder.show();
        }

        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        if (!wifi.isWifiEnabled()){
            Utility.WiFidialog(this);
        }
        if (!Utility.checkWifiState(this)) {
            wifiState = false;
            Utility.saveWiFiState(this, wifiState);
            if (Utility.prefsWiFiOn(this)) {
                Utility.enableWiFi(this, true);
            } else {
                Utility.WiFidialog(this);
            }
        } else {
            wifiState = true;
            Utility.saveWiFiState(this, wifiState);
        }
        this.iv_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Utility.prefsHaptic(MainActivity.this))
                    vib.vibrate(45);
                try {
                    if (!mState) {
                        Utility.adbStart(MainActivity.this);
                    } else {
                        Utility.adbStop(MainActivity.this);
                    }
                    updateState();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = getSharedPreferences("wireless", 0);
        mState = settings.getBoolean("mState", false);
        wifiState = settings.getBoolean("wifiState", false);
        updateState();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("LOG","onPause()");
    }
    @Override
    protected void onDestroy() {
        try {
            Utility.adbStop(this);
        } catch (Exception e) {
        }
        try {
            Utility.manager.cancelAll();
        } catch (Exception e) {
        }
        try {
            if (Utility.prefsWiFiOff(this) && !wifiState && Utility.checkWifiState(this)) {
                Utility.enableWiFi(this, false);
            }
        } catch (Exception e) {
        }
        super.onDestroy();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void updateState() {
        if (mState) {
            tv1.setText("Remote ADB is On");
            try {
                tv2.setText("adb connect " + Utility.getWifiIp(this)+":"+Utility.prefsAutoConPort(this));
                iv_button.setText("Turn ADB off");
            } catch (Exception e) {
                tv2.setText("adb connect ?");
                iv_button.setText("Turn ADB off");
            }
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.bug_on);
        } else {
            tv1.setText("Remote ADB is Off");
            tv2.setText("---------------");
            iv_button.setText("Start ADB");
            imageView.setImageResource(R.drawable.bug_off);
        }
    }

}
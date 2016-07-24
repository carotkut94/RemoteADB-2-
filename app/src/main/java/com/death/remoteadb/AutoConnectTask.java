package com.death.remoteadb;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;

class AutoConnectTask extends AsyncTask<Void, Void, Void> {

    private String url;

    public AutoConnectTask(String u) {
        this.url = u;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URI url = new URI(this.url);
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet method = new HttpGet(url);
            httpClient.execute(method);
        } catch (Exception e) {
            Log.e("ERROR doInBackground()", e.getMessage());
        }
        return null;
    }

}
package com.kuhar.kos.tvspored;

import android.os.AsyncTask;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.VolumeProviderCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private static OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView text = (TextView) findViewById(R.id.res);
        client = new OkHttpClient();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                API a = new API();
                try {
                    return a.GET(client, "http://api.rtvslo.si/spored/listProvys");
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                text.setText(s);
            }
        }.execute();

        /*new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void[] params) {
                String response = "";
                try {
                    response = API.GET(client, "http://api.rtvslo.si/spored/listProvys");
                    //Parse the response string here
                    Log.d("Response", response);
                    return response;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                TextView text = (TextView) findViewById(R.id.res);
                text.setText(s);
            }
        }.execute();*/


    }
}

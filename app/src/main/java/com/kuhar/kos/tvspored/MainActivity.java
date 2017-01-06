package com.kuhar.kos.tvspored;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.VolumeProviderCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private static OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("TV spored");

        client = new OkHttpClient();

        new AsyncTask<Void, Void, ChannelData>() {
            @Override
            protected ChannelData doInBackground(Void... params) {
                XMLParse xmlParse = new XMLParse();
                ChannelData cd = new ChannelData();
                cd.channelNames = xmlParse.getChannelNames();
                cd.channelLinks = xmlParse.getChannelLinks();
                cd.currentShow  = xmlParse.getCurrentShow();
                return cd;
            }

            @Override
            protected void onPostExecute(final ChannelData channel) {
                ArrayList<MainItem> items = new ArrayList<MainItem>();
                for (int i = 0; i < channel.channelNames.size(); i++){
                    items.add(new MainItem(channel.channelNames.get(i),
                            channel.currentShow.get(i).title,
                            channel.currentShow.get(i).startTime,
                            channel.currentShow.get(i).endTime));
                }
                CustomAdapter adapter = new CustomAdapter(MainActivity.this, items);
                ListView listView = (ListView) findViewById(R.id.channelList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getBaseContext(), ChannelActivity.class);
                        intent.putExtra("channelClicked", channel.channelLinks.get((int)id));
                        intent.putExtra("channelName", channel.channelNames.get((int)id));
                        startActivity(intent);
                    }
                });
            }
        }.execute();
    }
}

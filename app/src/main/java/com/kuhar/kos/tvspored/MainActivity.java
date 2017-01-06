package com.kuhar.kos.tvspored;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

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

            private ProgressDialog mDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mDialog = new ProgressDialog(MainActivity.this);
                mDialog.setMessage("Nalaganje");
                mDialog.show();
                mDialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(final ChannelData channel) {
                mDialog.dismiss();

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

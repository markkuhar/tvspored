package com.kuhar.kos.tvspored;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private static DatabaseConnector db;
    private static OkHttpClient client;
    private static Cursor res;
    private static ArrayList<MainItem> favorites_all;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_showFavorites: {
                startFavorites();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseConnector(this);
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

                favorites_all = new ArrayList<MainItem>();
                for(int i = 0; i < channel.channelNames.size(); i++){
                    String ime = channel.channelNames.get(i);
                    res = db.getData(ime);
                    System.out.println("IMENA " + channel.channelNames.get(i));
                    while (res.moveToNext()) {
                        int var = Integer.parseInt(res.getString(0));

                        if (var == 1) {
                            favorites_all.add(new MainItem(channel.channelNames.get(i),
                                channel.currentShow.get(i).title,
                                channel.currentShow.get(i).startTime));
                        }
                    }
                    res.close();

                }

                ArrayList<MainItem> items = new ArrayList<MainItem>();
                for (int i = 0; i < channel.channelNames.size(); i++){
                    String startTime = channel.currentShow.get(i).startTime;
                    if (startTime.contains(":")){
                        startTime = startTime.substring(0, startTime.lastIndexOf(':'));
                    }
                    items.add(new MainItem(channel.channelNames.get(i),
                            channel.currentShow.get(i).title,
                            startTime));

                }
                //favorites.addAll(items);
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
    public void startFavorites(){
        Intent intent = new Intent(getBaseContext(), favorites.class);
        String listSerializedToJson = new Gson().toJson(favorites_all);
        intent.putExtra("fav", listSerializedToJson);
        startActivity(intent);

    }

    public static ArrayList<MainItem> getFavorites(){
        return favorites_all;
    }
}

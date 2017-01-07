package com.kuhar.kos.tvspored;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class favorites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        String listSerializedToJson = getIntent().getExtras().getString("fav");
        //System.out.println("lallaa " + listSerializedToJson);
        MainItem[] m = new Gson().fromJson(listSerializedToJson, MainItem[].class);
        for(int i = 0; i < m.length; i++){
            System.out.println(m[i].getDescription());
        }
        ArrayList<MainItem> priljubljeni = new ArrayList<MainItem>(Arrays.asList(m));
        CustomAdapter adapter = new CustomAdapter(favorites.this, priljubljeni);
        ListView listView = (ListView) findViewById(R.id.channelContentListview);
        listView.setAdapter(adapter);


        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("INTENT_NAME"));


    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String change = intent.getStringExtra("spremenjeno");
            //RefreshFavorites rf = new RefreshFavorites(favorites.this);
            //rf.refreshFavorites();
        }
    };

    public void bla(View v){
        RefreshFavorites rf = new RefreshFavorites(favorites.this);
        rf.refreshFavorites();
    }
/*
    public void refreshFavorites(View v){

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

                mDialog = new ProgressDialog(favorites.this);
                mDialog.setMessage("Nalaganje");
                mDialog.show();
                mDialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(final ChannelData channel) {
                mDialog.dismiss();

                ArrayList<MainItem> favorites_all = new ArrayList<MainItem>();
                DatabaseConnector db = new DatabaseConnector(favorites.this);
                Cursor res;
                for(int i = 0; i < channel.channelNames.size(); i++){
                    String ime = channel.channelNames.get(i);
                    res = db.getData(ime);
                    //System.out.println("IMENA " + channel.channelNames.get(i));
                    while (res.moveToNext()) {
                        int var = Integer.parseInt(res.getString(0));
                        //System.out.println("TO " + channel.channelNames.get(i) + " " + var);

                        if (var == 1) {
//                            System.out.println("BRISEM " + channel.channelNames.get(i) + " " + var);
                            favorites_all.add(new MainItem(channel.channelNames.get(i), channel.currentShow.get(i).title, channel.currentShow.get(i).startTime, channel.currentShow.get(i).endTime));
                            //channel.channelNames.remove(i);
                            //channel.currentShow.remove(i);
                            //channel.channelLinks.remove(i);
                        }
                    }
                    res.close();

                }
                CustomAdapter adapter = new CustomAdapter(favorites.this, favorites_all);
                ListView listView = (ListView) findViewById(R.id.channelContentListview);
                listView.setAdapter(adapter);


            }
        }.execute();
    }
    */
}

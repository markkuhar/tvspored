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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class favorites extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Priljubljeni");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

    }
    @Override
    public void onResume(){
        super.onResume();
        RefreshFavorites rf = new RefreshFavorites(favorites.this);
        rf.refreshFavorites();
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
}

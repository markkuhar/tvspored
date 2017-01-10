package com.kuhar.kos.tvspored;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by domen on 07/01/2017.
 */

public class RefreshFavorites {
    favorites act;

    public RefreshFavorites(favorites act){
        this.act = act;
    }
    public void refreshFavorites(){

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

                mDialog = new ProgressDialog(act);
                mDialog.setMessage("Nalaganje");
                mDialog.show();
                mDialog.setCancelable(false);
            }


            @Override
            protected void onPostExecute(final ChannelData channel) {
                mDialog.dismiss();
                final ArrayList<MainItem> favorites_all = new ArrayList<MainItem>();
                final ArrayList<String> favorites_names = new ArrayList<String>();
                final ArrayList<String> favorites_links = new ArrayList<String>();
                DatabaseConnector db = new DatabaseConnector(act);
                Cursor res;
                for(int i = 0; i < channel.channelNames.size(); i++){
                    String ime = channel.channelNames.get(i);
                    res = db.getData(ime);
                    while (res.moveToNext()) {
                        int var = Integer.parseInt(res.getString(0));
                        if (var == 1) {
                            String channelName = channel.channelNames.get(i);
                            String title = channel.currentShow.get(i).title;
                            String startTime = ((ProgrammeData) channel.currentShow.get(i)).startTime;
                            if (startTime.contains(":")){
                                startTime = startTime.substring(0, startTime.lastIndexOf(':'));
                            }
                            favorites_all.add(new MainItem(channelName,
                                    title,
                                    startTime,
                                    channel.currentShow.get(i).title));
                            favorites_names.add(channelName);
                            favorites_links.add(channel.channelLinks.get(i));
                        }
                    }
                    res.close();

                }
                CustomAdapter adapter = new CustomAdapter(act, favorites_all);
                ListView listView = (ListView) act.findViewById(R.id.channelContentListview);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(act, ChannelActivity.class);
                        intent.putExtra("channelName", favorites_names.get((int)id));
                        intent.putExtra("channelClicked", favorites_links.get((int)id));
                        act.startActivity(intent);
                    }
                });


            }
        }.execute();
    }
}

package com.kuhar.kos.tvspored;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
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
            protected void onPostExecute(final ChannelData channel) {
                ArrayList<MainItem> favorites_all = new ArrayList<MainItem>();
                DatabaseConnector db = new DatabaseConnector(act);
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
                            favorites_all.add(new MainItem(channel.channelNames.get(i), channel.currentShow.get(i).title, channel.currentShow.get(i).startTime));
                            //channel.channelNames.remove(i);
                            //channel.currentShow.remove(i);
                            //channel.channelLinks.remove(i);
                        }
                    }
                    res.close();

                }
                CustomAdapter adapter = new CustomAdapter(act, favorites_all);
                ListView listView = (ListView) act.findViewById(R.id.channelContentListview);
                listView.setAdapter(adapter);


            }
        }.execute();
    }
}

package com.kuhar.kos.tvspored;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

/**
 * Created by domen on 06/01/2017.
 */

public class ChangeClicked implements CompoundButton.OnCheckedChangeListener {
    String ime;
    DatabaseConnector db;
    CheckBox cb;
    ChannelActivity ca;
    private boolean sprememba;
    public ChangeClicked(String ime, DatabaseConnector db, CheckBox cb, ChannelActivity ca){
        this.ime = ime;
        this.db = db;
        this.cb = cb;
        this.ca = ca;

    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Cursor res = db.getData(this.ime);
        while (res.moveToNext()) {
            int val = Integer.parseInt(res.getString(0));
            if (val == 1) {
                cb.setChecked(false);
                if(db.updateData(this.ime,0)){
                    System.out.println();
                }
            }
            else {
                cb.setChecked(true);
                if(db.updateData(this.ime,1)){
                    //Toast.makeText(this.act, "Program je bil dodan med priljubljene",Toast.LENGTH_SHORT).show();
                    System.out.println();
                }

            }

        }
        //Intent intent = new Intent("INTENT_NAME").putExtra("spremenjeno", "true");
        //LocalBroadcastManager.getInstance(ca).sendBroadcast(intent);
    }
}

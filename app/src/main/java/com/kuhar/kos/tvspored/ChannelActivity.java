package com.kuhar.kos.tvspored;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class ChannelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        String ItemLink = getIntent().getStringExtra("channelClicked");

        setTitle(getIntent().getStringExtra("channelName"));

        new AsyncTask<String, Void, ChannelData>() {

            @Override
            protected ChannelData doInBackground(String... ItemLink) {
                XMLParse xml = new XMLParse();
                ChannelData cd = new ChannelData();
                cd.channelNames = xml.getChannelContent(ItemLink[0]); // to je treba narest s slovarjem
                cd.channelLinks = xml.getChannelMeta(ItemLink[0]);    // ZELO narobe
                return cd;
            }

            @Override
            protected void onPostExecute(final ChannelData cd) {
                super.onPostExecute(cd);
                ArrayAdapter adapter = new ArrayAdapter<String>(ChannelActivity.this,
                        R.layout.listview_row, cd.channelNames);
                ListView listView = (ListView) findViewById(R.id.channelContentListview);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChannelActivity.this);
                        alertDialogBuilder.setMessage(cd.channelLinks.get((int)id));

                        alertDialogBuilder.setNegativeButton("Zapri",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });
            }
        }.execute(ItemLink);
    }
}

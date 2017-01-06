package com.kuhar.kos.tvspored;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

public class ChannelActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_channel);

        String ItemLink = getIntent().getStringExtra("channelClicked");

        setTitle(getIntent().getStringExtra("channelName"));

        new AsyncTask<String, Void, ArrayList>() {

            @Override
            protected ArrayList doInBackground(String... ItemLink) {
                XMLParse xml = new XMLParse();
                ArrayList seznamOddaj = xml.getSeznamOddaj(ItemLink[0]);
                return seznamOddaj;
            }

            @Override
            protected void onPostExecute(final ArrayList seznamOddaj) {
                super.onPostExecute(seznamOddaj);

                Collections.sort(seznamOddaj);
                final ArrayList programmeData = new ArrayList();
                System.out.println(seznamOddaj.size());
                for (int i = 0; i < seznamOddaj.size() - 1; i++) {
                    System.out.println(((ProgrammeData) seznamOddaj.get(i)).getStartTime());
                    String desc = ((ProgrammeData) seznamOddaj.get(i)).description;
                    desc = trimString(desc, 10);
                    programmeData.add(new MainItem(((ProgrammeData) seznamOddaj.get(i)).title,
                            desc,
                            ((ProgrammeData) seznamOddaj.get(i)).getStartTime(),
                            ((ProgrammeData) seznamOddaj.get(i)).endTime));
                }

                CustomAdapter adapter = new CustomAdapter(ChannelActivity.this, programmeData);
                ListView listView = (ListView) findViewById(R.id.channelContentListview);
                listView.setAdapter(adapter);
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                                   int pos, long id) {
                        TextView tv = (TextView) view.findViewById(R.id.value);
                        tv.setText(((ProgrammeData) seznamOddaj.get((int) id)).description);
                        return true;
                    }
                });
            }
        }.execute(ItemLink);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public static String trimString(String string, int length) {
        if (string == null || string.trim().isEmpty() || !string.contains(" ")) {
            return "Ni opisa";
        }
        int countSpaces = string.length() - string.replace(" ", "").length();
        if (length > countSpaces) {
            return string;
        }

        return string.substring(0, nthIndexOf(string, ' ', length)) + " ...";
    }

    public static int nthIndexOf(String text, char needle, int n) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == needle) {
                n--;
                if (n == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Channel Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

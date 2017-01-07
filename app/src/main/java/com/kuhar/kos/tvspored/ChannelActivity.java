package com.kuhar.kos.tvspored;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
import android.support.v4.database.DatabaseUtilsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    DatabaseConnector db;
    private static final String TAG = "Error log";
    private static final MainActivity main = new MainActivity();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.channel_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.action_refresh: {
                String ItemLink = getIntent().getStringExtra("channelClicked");
                refresh(ItemLink);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseConnector(this);
        setContentView(R.layout.activity_channel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        setTitle(getIntent().getStringExtra("channelName"));
        String ItemLink = getIntent().getStringExtra("channelClicked");

        //this.deleteDatabase("Spored.db");
        refresh(ItemLink);
        String ime = getIntent().getStringExtra("channelName");
        Cursor res = db.getData(ime);
        CheckBox cb = (CheckBox)findViewById(R.id.favorite);
        if(res.getCount() == 0){
            if (db.insertData(ime, 0)) {
                System.out.println("Dodano!");
            }
        }
        else {
            while (res.moveToNext()){
                int val = Integer.parseInt(res.getString(0));
                System.out.println("Vrednost " + val);
                if(val == 1){
                    cb.setChecked(true);
                }
                else {
                    cb.setChecked(false);
                }
            }
        }

        cb.setOnCheckedChangeListener(new ChangeClicked(ime,db,cb,ChannelActivity.this));

        //isFavorite();
        System.out.println(cb.isChecked());
    }


    public void refresh(String ItemLink){
        /*
        Cursor res = db.getAllData();
        StringBuffer sb = new StringBuffer();
        while (res.moveToNext()){
            sb.append("Ime programa " + res.getString(0) + " Favorites " + res.getString(1) + "\n");
        }
        System.out.println(sb);
        */

        new AsyncTask<String, Void, ArrayList>() {

            @Override
            protected ArrayList doInBackground(String... ItemLink) {
                XMLParse xml = new XMLParse();
                ArrayList seznamOddaj = xml.getSeznamOddaj(ItemLink[0]);
                return seznamOddaj;
            }

            private ProgressDialog mDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mDialog = new ProgressDialog(ChannelActivity.this);
                mDialog.setMessage("Nalaganje");
                mDialog.show();
                mDialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(final ArrayList seznamOddaj) {
                super.onPostExecute(seznamOddaj);
                mDialog.dismiss();

                Collections.sort(seznamOddaj);
                final ArrayList programmeData = new ArrayList();
                for (int i = 0; i < seznamOddaj.size() - 1; i++) {
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
}

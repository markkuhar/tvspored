package com.kuhar.kos.tvspored;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChannelActivity extends AppCompatActivity implements ChannelFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        setTitle(getIntent().getStringExtra("channelName"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(1); //defaut tab je "danes"

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.channel_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.action_refresh: {
                String ItemLink = getIntent().getStringExtra("channelClicked");
                refresh(ItemLink);
            }
            case R.id.action_favorites: {
                item.setChecked(!item.isChecked());
                Drawable drawable = item.getIcon();
                if (item.isChecked()) {


                    /* Sm pridš če KLIKNŠ */


                    /* Tole je za pobarvat zvezdico */
                    drawable.setColorFilter(getResources().getColor(R.color.rumena), PorterDuff.Mode.SRC_ATOP);
                    /* Do sm */
                } else{


                    /* Sm pridš če ODKLIKAŠ */


                    /* Tole je za pobarvat zvezdico */
                    drawable.setColorFilter(getResources().getColor(R.color.bela), PorterDuff.Mode.SRC_ATOP);
                    /* Do sm */
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        String ItemLink = getIntent().getStringExtra("channelClicked");

        @Override
        public Fragment getItem(int position) {

            String newDate = "";
            String rawLink = ItemLink.substring(0, ItemLink.lastIndexOf('/') + 1);

            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(now);
            c.add(Calendar.DATE, position - 1);
            newDate = sdf.format(c.getTime());
            return ChannelFragment.newInstance(rawLink + newDate);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            String newDate;

            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.");
            Calendar c = Calendar.getInstance();
            c.setTime(now);
            switch (position) {
                case 0:
                    return "Včeraj";
                case 1:
                    return "Danes";
                case 2:
                    return "Jutri";
                case 3:
                    c.add(Calendar.DATE, 2);
                    newDate = sdf.format(c.getTime());
                    return newDate;
                case 4:
                    c.add(Calendar.DATE, 3);
                    newDate = sdf.format(c.getTime());
                    return newDate;
            }
            return null;
        }
    }

    public void refresh(String ItemLink) {
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
            }
        }.execute(ItemLink);
    }
}

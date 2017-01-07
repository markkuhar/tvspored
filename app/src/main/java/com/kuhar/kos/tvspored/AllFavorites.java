package com.kuhar.kos.tvspored;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class AllFavorites extends AppCompatActivity {
    DatabaseConnector db;
    ArrayList<String> allItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseConnector(this);
        Cursor res = db.getAllFavorites();
        StringBuffer sb = new StringBuffer();
        while (res.moveToNext()){
            //System.out.println("To je not " + res.getString(0));
            allItems.add(res.getString(0));
        }
        for(int i = 0; i < allItems.size(); i++){
            System.out.println(allItems.get(i));
        }
    }

}

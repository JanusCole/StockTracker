package com.example.janus.stocktracker;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.content.Intent;
import android.view.MenuItem;

import com.example.janus.stocktracker.util.BottomNavigationMap;

public class BaseActivity extends AppCompatActivity {

    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate (Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public void setContentView (@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new OnNavigationItemSelectedListener () {
                    @Override
                    public boolean onNavigationItemSelected (@NonNull MenuItem item) {
                        Intent intent = new Intent(getApplicationContext(), BottomNavigationMap.NAVIGATION_MAP.get(item.getItemId()));
                        startActivity(intent);
                        return false;
                    }
                });
    }
}
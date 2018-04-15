package com.example.janus.stocktracker.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.portfolio.PortfolioActivity;
import com.example.janus.stocktracker.splashscreen.SplashScreen;
import com.example.janus.stocktracker.stocksearch.StockSearchActivity;

import java.util.HashMap;

// This is a utility class to replace the switch statements commonly used for Bottom Menu Navigation
// with a HashMap.

public class BottomNavigationMap {

    public static final HashMap<Integer, Class> NAVIGATION_MAP = new HashMap<>();

    static {
        NAVIGATION_MAP.put(R.id.action_search_ticker, StockSearchActivity.class);
        NAVIGATION_MAP.put(R.id.action_splash_screen, SplashScreen.class);
        NAVIGATION_MAP.put(R.id.action_portfolio, PortfolioActivity.class);
    }
}

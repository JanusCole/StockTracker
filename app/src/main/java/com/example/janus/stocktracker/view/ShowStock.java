package com.example.janus.stocktracker.view;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

// This interface is used to facilitate comuncation between fragments and the Main Activity. This interface tells the Main
// Activity to do a search for a specific stock ticker and display the results.

public interface ShowStock {

    void showStock(String stockTicker);

}

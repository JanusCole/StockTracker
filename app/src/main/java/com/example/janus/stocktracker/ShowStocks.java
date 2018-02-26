package com.example.janus.stocktracker;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public interface ShowStocks {

// Ths is Google's recommended method for facilitating communcation between Fragments
// and Activities. https://developer.android.com/training/basics/fragments/communicating.html
    void showStocks(List<String> stockTickerArray, Fragment destinationFragment);

}

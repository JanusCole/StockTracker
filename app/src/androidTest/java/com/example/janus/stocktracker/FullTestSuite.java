package com.example.janus.stocktracker;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ActivityMainTest.class,
        DataBaseTest.class,
        PortfolioTest.class,
        RetrofitTest.class,
        SearchOneStockTest.class,
        SearchMultipleStockTest.class})
public class FullTestSuite {}

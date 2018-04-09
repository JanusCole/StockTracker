package com.example.janus.stocktracker;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({SplashScreenActivityTest.class,
        DataBaseUnitTest.class})
public class FullTestSuite {}

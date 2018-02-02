package com.example.janus.stocktracker;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

// ********************************************************************************************************************************************************
// *                                                                       ATTENTION                                                                      *
// ********************************************************************************************************************************************************
// This code is taken almost directly from Google CodeLabs. You can find the original code here:
// https://github.com/googlecodelabs/android-testing/blob/master/app/src/main/java/com/example/android/testing/notes/util/EspressoIdlingResource.java
// ********************************************************************************************************************************************************

public class EspressoIdlingResource {

    private static final String RESOURCE = "Stock Quote Search";

    private static SimpleIdlingResource mCountingIdlingResource =
            new SimpleIdlingResource(RESOURCE);

    public static void increment() {
        mCountingIdlingResource.increment();
    }

    public static void decrement() {
        mCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }
}
package com.example.janus.stocktracker;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.janus.stocktracker.data.database.PortfolioDBContract;
import com.example.janus.stocktracker.data.database.PortfolioDBOpenHelper;
import com.example.janus.stocktracker.splashscreen.SplashScreen;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.intent.Checks.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;


@RunWith(AndroidJUnit4.class)
public class SplashScreenActivityTest {

    @Rule
    public ActivityTestRule<SplashScreen> mActivityRule = new ActivityTestRule<>(
            SplashScreen.class);

    @Before
    public void setUp() throws Exception {
        deletePortfolio();
    }

    // Test Screen Startup

    @Test
    public void testMainActivityExists() throws Exception {
        checkNotNull(mActivityRule.getActivity());

    }

    @Test
    public void testSplashScreenAtStartup() throws Exception {
        onView(withId(R.id.splashScreenFragment)).check(matches((isDisplayed())));

    }

    @Test
      public void testEmptyPortfolio() throws Exception {

          onView(withId(R.id.action_portfolio)).perform(click());

          onView(withText("Your portfolio is empty"))
                  .inRoot(isDialog())
                  .check(matches(isDisplayed()));

          onView(withId(R.id.okButton_AlertDialog)).perform(click());


      }

    public void deletePortfolio () {

        PortfolioDBOpenHelper portfolioDBOpenHelper = new PortfolioDBOpenHelper(mActivityRule.getActivity());

        portfolioDBOpenHelper.getWritableDatabase().execSQL("DELETE FROM " + PortfolioDBContract.PortfolioEntry.TABLE_NAME);

    }

    @After
    public void tearDown() throws Exception {
        deletePortfolio();
    }
}
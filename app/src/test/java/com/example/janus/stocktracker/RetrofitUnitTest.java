package com.example.janus.stocktracker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

public class RetrofitUnitTest {

    String retroFiteResponse = "{\"symbol\":\"IBM\",\"companyName\":\"International Business Machines Corporation\",\"primaryExchange\":\"New York Stock Exchange\",\"sector\":\"Technology\",\"calculationPrice\":\"close\",\"open\":166.12,\"openTime\":1516977000714,\"close\":167.34,\"closeTime\":1517000510671,\"high\":167.414,\"low\":165.79,\"latestPrice\":167.34,\"latestSource\":\"Close\",\"latestTime\":\"January 26, 2018\",\"latestUpdate\":1517000510671,\"latestVolume\":3751199,\"iexRealtimePrice\":null,\"iexRealtimeSize\":null,\"iexLastUpdated\":null,\"delayedPrice\":167.29,\"delayedPriceTime\":1517003863274,\"previousClose\":165.47,\"change\":1.87,\"changePercent\":0.0113,\"iexMarketPercent\":null,\"iexVolume\":null,\"avgTotalVolume\":6165192,\"iexBidPrice\":null,\"iexBidSize\":null,\"iexAskPrice\":null,\"iexAskSize\":null,\"marketCap\":154921929195,\"peRatio\":12.1,\"week52High\":182.79,\"week52Low\":139.13,\"ytdChange\":0.0848622366288493}";

    MockWebServer mockWebServer;


    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
    }

    @Test
    public void testRetrofitWithNullResponse() throws Exception {

        MockWebServer mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse().setBody(""));

        RetrofitStockQuote retrofitStockQuote = new RetrofitStockQuote(mockWebServer.url("").toString());
        StockQuote stockQuote = retrofitStockQuote.getRetrofitStockQuote("IBM");

        assertEquals(null, stockQuote);

    }

    @Test
    public void testRetrofitWithBadResponse() throws Exception {

        MockWebServer mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse().setBody("{symbol:IBM"));

        RetrofitStockQuote retrofitStockQuote = new RetrofitStockQuote(mockWebServer.url("").toString());
        StockQuote stockQuote = retrofitStockQuote.getRetrofitStockQuote("IBM");

        assertEquals(null, stockQuote);

    }

    @Test
    public void testRetrofitWithGoodResponse() throws Exception {

        MockWebServer mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse().setBody(retroFiteResponse));

        RetrofitStockQuote retrofitStockQuote = new RetrofitStockQuote(mockWebServer.url("").toString());
        StockQuote stockQuote = retrofitStockQuote.getRetrofitStockQuote("IBM");

        assertFalse(stockQuote == null);
        assertEquals("IBM", stockQuote.getSymbol());
        assertEquals("International Business Machines Corporation", stockQuote.getCompanyName());
        assertEquals("Technology", stockQuote.getSector());
        assertEquals(166.12, stockQuote.getOpen(), 0.0);
        assertEquals(167.34, stockQuote.getClose(), 0.0);
        assertEquals(167.34, stockQuote.getLatestPrice(), 0.0);
        assertEquals(3751199, stockQuote.getLatestVolume());

    }

    @After
    public void tearDown() throws Exception {
    }

}
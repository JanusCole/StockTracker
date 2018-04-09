package com.example.janus.stocktracker.data.stockquotes.stockquotes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.data.stockquotes.StockQuotesAPI;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StockQuotesAPIUnitTest {

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

        StockQuotesAPI retrofitStockQuote = new StockQuotesAPI();
        retrofitStockQuote.setBASE_URL(mockWebServer.url("").toString());

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(retrofitStockQuote.getBASE_URL())
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitBuilder.build();
        StockQuotesAPI.StockQuoteInterface stockQuoteClient = retrofit.create(StockQuotesAPI.StockQuoteInterface.class);

        Call<StockQuote> stockQuoteCall = stockQuoteClient.getStockQuote("IBM");

        Response<StockQuote> stockQuote = null;

        try {
            stockQuote = stockQuoteCall.execute();

        } catch (Exception e) {
        }

        assertEquals(null, stockQuote);

    }

    @Test
    public void testRetrofitWithBadResponse() throws Exception {

        MockWebServer mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse().setBody("{symbol:IBM"));

        StockQuotesAPI retrofitStockQuote = new StockQuotesAPI();
        retrofitStockQuote.setBASE_URL(mockWebServer.url("").toString());

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(retrofitStockQuote.getBASE_URL())
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitBuilder.build();
        StockQuotesAPI.StockQuoteInterface stockQuoteClient = retrofit.create(StockQuotesAPI.StockQuoteInterface.class);

        Call<StockQuote> stockQuoteCall = stockQuoteClient.getStockQuote("IBM");

        Response<StockQuote> stockQuote = null;

        try {
            stockQuote = stockQuoteCall.execute();

        } catch (Exception e) {
        }

        assertEquals(null, stockQuote);

    }

    @Test
    public void testRetrofitWithGoodResponse() throws Exception {

        MockWebServer mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse().setBody(retroFiteResponse));

        StockQuotesAPI retrofitStockQuote = new StockQuotesAPI();
        retrofitStockQuote.setBASE_URL(mockWebServer.url("").toString());

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(retrofitStockQuote.getBASE_URL())
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitBuilder.build();
        StockQuotesAPI.StockQuoteInterface stockQuoteClient = retrofit.create(StockQuotesAPI.StockQuoteInterface.class);

        Call<StockQuote> stockQuoteCall = stockQuoteClient.getStockQuote("IBM");

        Response<StockQuote> stockQuote = null;

        try {
            stockQuote = stockQuoteCall.execute();

        } catch (Exception e) {
        }

        assertFalse(stockQuote == null);
        assertEquals("IBM", stockQuote.body().getSymbol());
        assertEquals("International Business Machines Corporation", stockQuote.body().getCompanyName());
        assertEquals("Technology", stockQuote.body().getSector());
        assertEquals(166.12, stockQuote.body().getOpen(), 0.0);
        assertEquals(167.34, stockQuote.body().getClose(), 0.0);
        assertEquals(167.34, stockQuote.body().getLatestPrice(), 0.0);
        assertEquals(3751199, stockQuote.body().getLatestVolume());

    }


    @After
    public void tearDown() throws Exception {
    }

}
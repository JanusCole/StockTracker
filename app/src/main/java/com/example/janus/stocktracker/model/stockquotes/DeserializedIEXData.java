package com.example.janus.stocktracker.model.stockquotes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeserializedIEXData {

// This is the destination class for the IEX stock quote API.The data will then be used to create a
// more generic Parelable StockQuote object which can be passed in a List as part of a bundle to a fragment

        @SerializedName("symbol")
        @Expose
        private String symbol;
        @SerializedName("companyName")
        @Expose
        private String companyName;
        @SerializedName("primaryExchange")
        @Expose
        private String primaryExchange;
        @SerializedName("sector")
        @Expose
        private String sector;
        @SerializedName("calculationPrice")
        @Expose
        private String calculationPrice;
        @SerializedName("open")
        @Expose
        private Double open;
        @SerializedName("close")
        @Expose
        private Double close;
        @SerializedName("high")
        @Expose
        private Double high;
        @SerializedName("low")
        @Expose
        private Double low;
        @SerializedName("latestPrice")
        @Expose
        private Double latestPrice;
        @SerializedName("latestSource")
        @Expose
        private String latestSource;
        @SerializedName("latestTime")
        @Expose
        private String latestTime;
        @SerializedName("latestVolume")
        @Expose
        private Long latestVolume;
        @SerializedName("iexRealtimePrice")
        @Expose
        private Double iexRealtimePrice;
        @SerializedName("iexRealtimeSize")
        @Expose
        private Long iexRealtimeSize;
        @SerializedName("iexLastUpdated")
        @Expose
        private Long iexLastUpdated;
        @SerializedName("delayedPrice")
        @Expose
        private Double delayedPrice;
        @SerializedName("previousClose")
        @Expose
        private Double previousClose;
        @SerializedName("change")
        @Expose
        private Double change;
        @SerializedName("changePercent")
        @Expose
        private Double changePercent;
        @SerializedName("iexMarketPercent")
        @Expose
        private Double iexMarketPercent;
        @SerializedName("iexVolume")
        @Expose
        private Long iexVolume;
        @SerializedName("avgTotalVolume")
        @Expose
        private Long avgTotalVolume;
        @SerializedName("iexBidPrice")
        @Expose
        private Double iexBidPrice;
        @SerializedName("iexBidSize")
        @Expose
        private Long iexBidSize;
        @SerializedName("iexAskPrice")
        @Expose
        private Double iexAskPrice;
        @SerializedName("iexAskSize")
        @Expose
        private Long iexAskSize;
        @SerializedName("marketCap")
        @Expose
        private Double marketCap;
        @SerializedName("peRatio")
        @Expose
        private Double peRatio;
        @SerializedName("week52High")
        @Expose
        private Double week52High;
        @SerializedName("week52Low")
        @Expose
        private Double week52Low;
        @SerializedName("ytdChange")
        @Expose
        private Double ytdChange;


        public String getSymbol() {
            return symbol;
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getSector() {
            return sector;
        }

        public Double getOpen() {
            return open;
        }

        public void setOpen(Double open) {
            this.open = open;
        }

        public Double getClose() {
            return close;
        }

        public Double getLatestPrice() {
            return latestPrice;
        }

        public Long getLatestVolume() {
            return latestVolume;
        }


}


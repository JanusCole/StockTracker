package com.example.janus.stocktracker.model.stockquotes;

        import android.os.Parcel;
        import android.os.Parcelable;

        import java.io.Serializable;

public class StockQuote implements Parcelable, Serializable {

// This is a Parcelable class of StockQuote data that can be passed to a fragment in a bundle.

    private String symbol;

    private String companyName;

    private String sector;

    private double open;

    private double close;

    private double latestPrice;

    private long latestVolume;

    public StockQuote(String symbol, String companyName, String sector, double open, double close, double latestPrice, long latestVolume) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.sector = sector;
        this.open = open;
        this.close = close;
        this.latestPrice = latestPrice;
        this.latestVolume = latestVolume;
    }

    // Getters
    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSector() {
        return sector;
    }

    public double getOpen() {
        return open;
    }

    public double getClose() {
        return close;
    }

    public double getLatestPrice() {
        return latestPrice;
    }

    public long getLatestVolume() {
        return latestVolume;
    }

// Parcelable Implementation
// This allows the object to be passed in a List as part of a bundle to a fragment

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public StockQuote createFromParcel(Parcel source) {
            return new StockQuote(source);
        }

        @Override
        public StockQuote[] newArray(int size) {
            return new StockQuote[size];
        }
    };

    public StockQuote(Parcel source) {

        this.latestVolume = source.readLong();

        this.open = source.readDouble();
        this.close = source.readDouble();
        this.latestPrice = source.readDouble();

        this.symbol = source.readString();
        this.companyName = source.readString();
        this.sector = source.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(this.latestVolume);

        dest.writeDouble(this.open);
        dest.writeDouble(this.close);
        dest.writeDouble(this.latestPrice);

        dest.writeString(this.symbol);
        dest.writeString(this.companyName);
        dest.writeString(this.sector);

    }

    @Override
    public int describeContents() {
        return 0;
    }
}


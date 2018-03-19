package com.example.janus.stocktracker.model.stockquotes;

        import android.os.Parcel;
        import android.os.Parcelable;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class StockQuote implements Parcelable {

// This is the destination class for the IEX stock quote API.
// Implementing Parelable allows the object to be passed in a List as part of a bundle to a fragment

    @SerializedName("symbol")
    @Expose
    private String symbol;

    @SerializedName("companyName")
    @Expose
    private String companyName;

    @SerializedName("sector")
    @Expose
    private String sector;

    @SerializedName("open")
    @Expose
    private double open;

    @SerializedName("close")
    @Expose
    private double close;

    @SerializedName("latestPrice")
    @Expose
    private double latestPrice;

    @SerializedName("latestVolume")
    @Expose
    private long latestVolume;

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


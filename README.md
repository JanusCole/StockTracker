StockTracker for Android
========================

![stocktrackersplash](https://user-images.githubusercontent.com/30201754/35749012-17863d74-081e-11e8-8f3c-85878e5b2984.png)

Research Individual Stocks

![stocktrackeronestock](https://user-images.githubusercontent.com/30201754/35749027-27c10a3e-081e-11e8-8597-3e1ec19549fa.png)

Track Your Portfolio

![stocktrackerportfolio](https://user-images.githubusercontent.com/30201754/35749048-3d6280f2-081e-11e8-87bd-e75ced2bab2b.png)

Search Stocks By Ticker Symbol

![stocktrackersearch](https://user-images.githubusercontent.com/30201754/35749062-4b8482f2-081e-11e8-95a3-f9142ec03023.png)

## Important!!

In order to run the code, you'll need to get an api key from [Alpha Vantage](https://www.alphavantage.co/) and store it in an environment variable on your system called ALPHA_VANTAGE_APIKEY. Otherwise, gradle will not sync and you will get the following error:

<span style="color:red">
Parameter specified as non-null is null: method com.android.build.gradle.internal.dsl.BuildType.buildConfigField, parameter value
</span>
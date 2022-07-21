<img src="JanusColeLogo.png"> 

## StockTracker for Android

Originally coded in 2017, this is a showcase app to demonstrate Android frameworks and design patterns from the mid-2010's.

In this repo you'll find:
*   A multi-activity architecture employing the **[Model-View-Presenter](https://www.raywenderlich.com/7026-getting-started-with-mvp-model-view-presenter-on-android)** design pattern.
*   Access to a REST api, using Square's **[Retrofit2](https://square.github.io/retrofit/)** library.
*   Persistence using an **[SQLite](https://developer.android.com/training/data-storage/sqlite)** database.
*   Web and database calls using **[Thread Executors](https://developer.android.com/reference/java/util/concurrent/Executor)** for asynchronous operations.

### Screen Shots

<tr>
<td>
<img src="https://user-images.githubusercontent.com/30201754/35749012-17863d74-081e-11e8-8f3c-85878e5b2984.png" width="100">
</td>
<td>
<img src="https://user-images.githubusercontent.com/30201754/35749062-4b8482f2-081e-11e8-95a3-f9142ec03023.png" width="100">
</td>
<td>
<img src="https://user-images.githubusercontent.com/30201754/35749027-27c10a3e-081e-11e8-8597-3e1ec19549fa.png" width="100">
</td>
<td>
<img src="https://user-images.githubusercontent.com/30201754/35749048-3d6280f2-081e-11e8-87bd-e75ced2bab2b.png" width="100">
</td>
</tr>

### Running the app in Android Studio

In order to run the code, you'll need to get an api key from [Alpha Vantage](https://www.alphavantage.co/) and store it in an environment variable on your system called ALPHA_VANTAGE_APIKEY. Otherwise, gradle will not sync and you will get the following error:

<span style="color:#F00">
Parameter specified as non-null is null: method com.android.build.gradle.internal.dsl.BuildType.buildConfigField, parameter value
</span>
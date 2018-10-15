package ma.mhy.sqliteeditorroot;

import android.app.Application;


import ma.mhy.sqliteeditorroot.util.Crashlytics;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new Crashlytics(this));
    }
}

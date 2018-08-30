package top.vchao.live.myapp;

import android.app.Application;

import org.litepal.LitePal;

public class MyApp extends Application {

    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        instance = this;
    }
}

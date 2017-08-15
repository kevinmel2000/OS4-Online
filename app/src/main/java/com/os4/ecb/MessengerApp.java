package com.os4.ecb;

import android.app.Application;


public class MessengerApp extends Application {

    private Integer menuItemId = null;

    @Override
    public void onCreate() {
        super.onCreate();/*
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
    }

    public Integer getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Integer menuItemId) {
        this.menuItemId = menuItemId;
    }
}

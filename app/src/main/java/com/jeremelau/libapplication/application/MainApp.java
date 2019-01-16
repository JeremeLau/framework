package com.jeremelau.libapplication.application;

import android.app.Application;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public class MainApp extends Application {
    private static MainApp instance;
    public static synchronized MainApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
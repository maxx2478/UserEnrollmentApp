package com.manohar.myapplication;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

public class MasterClass extends Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        instance = this;
    }

    public static Context getContext(){
        return instance.getApplicationContext();
    }

}

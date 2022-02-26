package com.dd.idea.pokemoninfo;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import io.realm.Realm;

public class ApplicationInstance extends MultiDexApplication {

    private static ApplicationInstance mInstance;

    public static Context getContext() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        mInstance = this;
        super.onCreate();
        Realm.init(this);
        Bootstrap.start(this);
    }

}

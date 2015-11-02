package com.unibratec.livia.polishcollection;

import android.app.Application;

import com.squareup.otto.Bus;

/**
 * Created by Livia on 02/11/2015.
 */
public class PolishApp extends Application {

    Bus mBus;

    @Override
    public void onCreate() {
        super.onCreate();

        mBus = new Bus();
    }

    public Bus getBus(){
        return mBus;
    }
}

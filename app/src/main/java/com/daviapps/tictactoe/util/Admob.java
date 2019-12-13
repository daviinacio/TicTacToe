package com.daviapps.tictactoe.util;

import android.content.Context;

import com.daviapps.tictactoe.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

// Created by daviinacio on 12/12/2019.
public abstract class Admob {
    public static void request(Context context, AdView adView, AdListener listener){
        MobileAds.initialize(context, context.getString(R.string.admob_app_id));

        adView.setAdListener(listener);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("A4DDF5EC5E81FE0D117CA05BA164E8B8") // LG K10
                .addTestDevice("B32DF5960E16B6E638F0861FB8E63372") // LG G6
                .build();

        adView.loadAd(adRequest);
    }
}

package com.lib.googleservice;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class GoogleServiceManager {
    //下载googleService
    public static void downloadGpService(Context context){
        int states=GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (states == ConnectionResult.SUCCESS){

        }else {
            GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable((Activity) context);
        }
    }
}

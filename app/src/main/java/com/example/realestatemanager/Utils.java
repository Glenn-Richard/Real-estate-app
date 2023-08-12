package com.example.realestatemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.909);
    }

    public static String getTodayDate(){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(new Date());
    }

    public static Boolean isInternetAvailable(Context context){
        WifiManager wifi = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }
}

package com.developer.reqwy.myapplication.utils;

import android.content.Context;
import android.content.res.Configuration;


public class OrientationUtils {
    public static String getScreenOrientation(Context context){
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            return "P";
        else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return "A";
        else
            return "";
    }
}

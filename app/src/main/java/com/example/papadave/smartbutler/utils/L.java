package com.example.papadave.smartbutler.utils;

import android.util.Log;

/**
 * Created by papadave on 2017/9/21.
 */
public class L {
    public static final boolean DEBUG = true;
    
    public static final String TAG = "Smartbulter";
    
    public static void d(String text){
        if (DEBUG){
            Log.d(TAG, "d: "+text);
        }
    }
    public static void i(String text){
        if (DEBUG){
            Log.i(TAG, "i: "+text);
        }
    }

    public static void w(String text){
        if (DEBUG){
            Log.w(TAG, "w: "+text );
        }
    }
    public static void e(String text){
        if (DEBUG){
            Log.e(TAG, "e: "+text );
        }
    }
}




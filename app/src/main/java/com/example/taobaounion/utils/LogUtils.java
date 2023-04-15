package com.example.taobaounion.utils;

import android.util.Log;

public class LogUtils {
    public static int currentLevel = 4;
    public static final int DEBUG_LEVEL = 4;
    public static final int INFO_LEVEL = 3;
    public static final int WARNING_LEVEL = 2;
    public static final int ERROR_LEVEL = 1;

    public static void d(Object object,String log){
        if(currentLevel>=DEBUG_LEVEL){
            Log.d(object.getClass().getSimpleName(), log);
        }
    }
    public static void i(Object object,String log){
        if(currentLevel>=INFO_LEVEL){
            Log.i(object.getClass().getSimpleName(), log);
        }
    }
    public static void w(Object object,String log){
        if(currentLevel>=WARNING_LEVEL){
            Log.w(object.getClass().getSimpleName(), log);
        }
    }
    public static void e(Object object,String log){
        if(currentLevel>=ERROR_LEVEL){
            Log.e(object.getClass().getSimpleName(), log);
        }
    }

}

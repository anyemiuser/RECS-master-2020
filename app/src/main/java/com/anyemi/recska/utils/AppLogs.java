/**
 @Module Name/Class		:	AppLogs
 @Author Name			:	Rohit Puri
 @Date					:	Feb 12, 2016
 @Purpose				:	This class is used to print logs.
 */
package com.anyemi.recska.utils;

import android.util.Log;

public class AppLogs {

    public static void log(String param1, String param2) {
        Log.e(param1, " :: " + param2);
    }

}

package com.anyemi.recska.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.anyemi.recska.model.ServicesResponseModel;
import com.instamojo.android.Instamojo;
import com.paytm.pgsdk.PaytmPGActivity;

public class AppData extends Application {
    public final static boolean IS_DEBUGGING_ON = false;
    public Typeface font = null;
    public Typeface fontbold = null;

    public final static String packName = "com.mswipetech.wisepad.sdk";
    public static final String SERVER_NAME = "Mswipe";
    public static final int PhoneNoLength = 10;
    public static final String Currency_Code = "Rs.";
    public static final String smsCode = "+91";
    public static final String mTipRequired = "false";
    public static SharedPreferences appSharedPreferences;

    public static boolean isIsDebuggingOn() {
        return IS_DEBUGGING_ON;
    }

    @Override
    public void onCreate() {

        Instamojo.getInstance().initialize(this, Instamojo.Environment.TEST);

        // TODO Auto-generated method stub
        super.onCreate();
        font = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTSTDLTCN.OTF");
        fontbold = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeueLTStd-MdCn.otf");

        appSharedPreferences = this.getSharedPreferences("prefrences", MODE_PRIVATE);


    }

    public ServicesResponseModel getServicesResponseModel() {
        return servicesResponseModel;
    }

    public void setServicesResponseModel(ServicesResponseModel servicesResponseModel) {
        this.servicesResponseModel = servicesResponseModel;
    }

    ServicesResponseModel servicesResponseModel;
}
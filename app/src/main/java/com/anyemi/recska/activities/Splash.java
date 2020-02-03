package com.anyemi.recska.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.anyemi.recska.LoginActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.utils.SharedPreferenceUtil;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class Splash extends AppCompatActivity {


    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }








    private void openLogin(final String logintype) {
        if (logintype.equals(Constants.LOGIN_TYPE_APEPDCL)) {
            SharedPreferenceUtil.setBaseUrl(getApplicationContext(), "https://api.anyemi.com/powerbill/");
        } else if (logintype.equals(Constants.LOGIN_TYPE_CDMA)) {
            SharedPreferenceUtil.setBaseUrl(getApplicationContext(), "https://api.anyemi.com/cdma/");
        } else if (logintype.equals(Constants.LOGIN_TYPE_GVMC)) {
            SharedPreferenceUtil.setBaseUrl(getApplicationContext(), "https://api.anyemi.com/gvmc/");
        } else if (logintype.equals(Constants.LOGIN_TYPE_RECS)) {
            SharedPreferenceUtil.setBaseUrl(getApplicationContext(), "https://api.anyemi.com/recs/");
        }


        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }
}


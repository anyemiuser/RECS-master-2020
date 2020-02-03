package com.anyemi.recska.hdfc_pg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.anyemi.recska.R;
//20201 03567

public class MerchantCheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_checkout);

        TextView postParamsTextView = (TextView) findViewById(R.id.text_view_post_params);

        //post data received by this activity contains all params posted to webview in transaction request.
        String postData = getIntent().getStringExtra("postData");
        postParamsTextView.setText("Merchant's post data : " + postData);

    }
}
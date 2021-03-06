package com.anyemi.recska.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anyemi.recska.NavigationActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.CollectionsModel;
import com.anyemi.recska.model.PaymentRequestModel;
import com.anyemi.recska.utils.Globals;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by SuryaTejaChalla on 07-02-2018.
 */

public class ScanAndPayActivity extends AppCompatActivity implements View.OnClickListener {

    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;

    String data;
    PaymentRequestModel paymentRequestModel;

    LinearLayout ll_paytm, ll_bharth;

    TextView tv_amount;
    EditText et_phone_num;
    Dialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_and_pay);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(ScanAndPayActivity.this, R.color.colorPrimaryDark));
        }
        createActionBar();
        initView2();

        try {
            Bundle parametros = getIntent().getExtras();
            data = parametros.getString(Constants.PAYMENT_REQUEST_MODEL);
            Gson gson = new Gson();
            paymentRequestModel = gson.fromJson(data, PaymentRequestModel.class);
            //  tv_amount.setText(String.valueOf(paymentRequestModel.getTotal_amount()));
            String resultStr = String.valueOf(paymentRequestModel.getTotal_amount());
            DecimalFormat df = new DecimalFormat("#,##,###.####", new DecimalFormatSymbols(Locale.US));
            resultStr = df.format(Double.valueOf(resultStr));
            tv_amount.setText("Rs." + resultStr + " /-");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView2() {

        tv_amount = findViewById(R.id.tv_amount);

        ll_paytm = findViewById(R.id.ll_paytm);
        ll_bharth = findViewById(R.id.ll_bharth);


        ll_bharth.setOnClickListener(this);
        ll_paytm.setOnClickListener(this);

    }


    public void getSelectedPayment(String payment_mode) {

        if (payment_mode.equals(Constants.PAYMENT_MODE_PAYTM_QR)) {
            paymentRequestModel.setPayment_type(Constants.PAYMENT_MODE_PAYTM_QR);
            Intent eWalletIntent = new Intent(getApplicationContext(), PaymentActivity.class);
            eWalletIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
            startActivity(eWalletIntent);

        } else if (payment_mode.equals(Constants.PAYMENT_MODE_BHARATH_QR)) {
            paymentRequestModel.setPayment_type(Constants.PAYMENT_MODE_BHARATH_QR);
            Intent eWalletIntent = new Intent(getApplicationContext(), PaymentActivity.class);
            eWalletIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
            startActivity(eWalletIntent);
        }

    }


    private void createActionBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_action_bar_with_home_button, null);

        aTitle = mCustomView.findViewById(R.id.title_text);
        rl_new_mails = mCustomView.findViewById(R.id.rl_new_mails);
        notification_count = mCustomView.findViewById(R.id.text_count);
        iv_add_new = mCustomView.findViewById(R.id.iv_add_new);

        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        aTitle.setTextColor(getResources().getColor(R.color.white));
        aTitle.setText("Choose payment Method");

    }


    private void openTransactionDialog() {

        dialog = new Dialog(ScanAndPayActivity.this);
        dialog.setTitle("Transaction Id");
        dialog.setContentView(R.layout.dialog_sms);
        et_phone_num = dialog.findViewById(R.id.et_phone_num);
        et_phone_num.setHint("Enter Transaction Id");
        Button btn_send_sms = dialog.findViewById(R.id.btn_send_sms);
        btn_send_sms.setText("Submit");
        btn_send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_phone_num.getText().toString().equals("")) {
                    String txn_id = et_phone_num.getText().toString();
                    paymentRequestModel.setRr_number(txn_id);
                    submitPayment();
                    dialog.dismiss();
                } else {
                    Globals.showToast(getApplicationContext(), "No Input Value Found");
                }
            }
        });
        dialog.show();

    }


    private void submitPayment() {

        new BackgroundTask(ScanAndPayActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.submitPayment(ScanAndPayActivity.this, paymentRequestModel);
            }

            public void taskCompleted(Object data) {
                if (data != null || data.equals("")) {
                    if (data.toString().contains("SUCCESS")) {
                        Globals.showToast(getApplicationContext(), "Payment Success");
                        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("FRAGMENT","COLLECTION");
                        startActivity(intent);
                    }
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }




    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("Wallet txn id:"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equalsIgnoreCase("PAYTM QR")) {
                final String message = intent.getStringExtra("id");
                et_phone_num.setText(message);
                dialog.dismiss();
            }
        }
    };


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_bharth:
                getSelectedPayment(Constants.PAYMENT_MODE_BHARATH_QR);
                break;
            case R.id.ll_paytm:
                getSelectedPayment(Constants.PAYMENT_MODE_PAYTM_QR);
                break;
        }
    }
}


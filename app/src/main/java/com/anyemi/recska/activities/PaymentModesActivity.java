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
import com.anyemi.recska.instamojo.InstamojoActivity;
import com.anyemi.recska.model.PaymentRequestModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.anyemi.recska.utils.Utils;
import com.google.gson.Gson;

/**
 * Created by SuryaTejaChalla on 24-01-2018.
 */

public class PaymentModesActivity extends AppCompatActivity implements View.OnClickListener {


    //Action Bar
    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;

    //Payment Details
    String paymentData;
    PaymentRequestModel paymentRequestModel;

    //View

    LinearLayout ll_cash, ll_cheque, ll_others, ll_credit_debit, ll_aadhar_pay, ll_scan_pay,
            ll_bhim, ll_internet_banking, ll_wallet, ll_instamojo;
    TextView tv_amount;
    EditText et_phone_num;
    Dialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_modes2);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(PaymentModesActivity.this, R.color.colorPrimaryDark));
        }

        createActionBar();
        initView();

        try {
            Bundle parametros = getIntent().getExtras();
            paymentData = parametros.getString(Constants.PAYMENT_REQUEST_MODEL);
            Gson gson = new Gson();
            paymentRequestModel = gson.fromJson(paymentData, PaymentRequestModel.class);

            String resultStr = String.valueOf(paymentRequestModel.getTotal_amount());
            resultStr = Utils.parseAmount(resultStr);
            tv_amount.setText("Rs." + resultStr + " /-");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {

        tv_amount = findViewById(R.id.tv_amount);

        ll_cash = findViewById(R.id.ll_cash);
        ll_cheque = findViewById(R.id.ll_cheque);
        ll_credit_debit = findViewById(R.id.ll_credit_debit);
        ll_aadhar_pay = findViewById(R.id.ll_aadhar_pay);
        ll_scan_pay = findViewById(R.id.ll_scan_pay);
        ll_bhim = findViewById(R.id.ll_bhim);
        ll_internet_banking = findViewById(R.id.ll_internet_banking);
        ll_wallet = findViewById(R.id.ll_wallet);
        ll_others = findViewById(R.id.ll_others);
        ll_instamojo = findViewById(R.id.ll_instamojo);

        String l_type = SharedPreferenceUtil.getLoginType(getApplicationContext());

        if (l_type.equals(Constants.LOGIN_TYPE_CUSTOMER)) {
//        if (l_type.equals("")) {
            ll_cash.setVisibility(View.GONE);
            ll_cheque.setVisibility(View.GONE);
            ll_credit_debit.setVisibility(View.GONE);
            ll_aadhar_pay.setVisibility(View.GONE);
        } else {
            ll_cash.setVisibility(View.VISIBLE);
            ll_cheque.setVisibility(View.VISIBLE);
            ll_credit_debit.setVisibility(View.VISIBLE);
            ll_aadhar_pay.setVisibility(View.VISIBLE);
        }

        ll_instamojo.setOnClickListener(this);
        ll_cash.setOnClickListener(this);
        ll_cheque.setOnClickListener(this);
        ll_credit_debit.setOnClickListener(this);
        ll_aadhar_pay.setOnClickListener(this);
        ll_scan_pay.setOnClickListener(this);
        ll_bhim.setOnClickListener(this);
        ll_internet_banking.setOnClickListener(this);
        ll_wallet.setOnClickListener(this);
        ll_others.setOnClickListener(this);

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

            if (intent.getAction().equalsIgnoreCase("Wallet txn id:")) {
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

            case R.id.ll_others:
                // Globals.showToast(getApplicationContext(),"SBI UPI is under process.We will update once process done");
                getSelectedPayment(Constants.PAYMENT_MODE_PAYTM_SBI_UPI);
                break;
            case R.id.ll_cash:
                getSelectedPayment(Constants.PAYMENT_MODE_CASH);
                break;
            case R.id.ll_cheque:
                getSelectedPayment(Constants.PAYMENT_MODE_CHEQUE);
                break;
            case R.id.ll_credit_debit:
                getSelectedPayment(Constants.PAYMENT_MODE_CREDIT_CARD);
                break;
            case R.id.ll_scan_pay:
                getSelectedPayment(Constants.PAYMENT_MODE_PAYTM_QR);
                break;
            case R.id.ll_bhim:
                getSelectedPayment(Constants.PAYMENT_MODE_BHIM);
                break;
            case R.id.ll_internet_banking:
                getSelectedPayment(Constants.PAYMENT_MODE_NET_BANKING);
                break;
            case R.id.ll_aadhar_pay:
                getSelectedPayment(Constants.PAYMENT_MODE_AADHAR);
                break;
            case R.id.ll_wallet:
                getSelectedPayment(Constants.PAYMENT_MODE_PAYTM_PG);
                break;

            case R.id.ll_instamojo:
                getSelectedPayment(Constants.PAYMENT_MODE_INSTAMOJO);
                break;
        }
    }

    public void getSelectedPayment(String payment_mode) {

        Intent paymentIntent;

        if (payment_mode.equals(Constants.PAYMENT_MODE_PAYTM_SBI_UPI)) {
            paymentRequestModel.setPayment_type(Constants.PAYMENT_MODE_PAYTM_SBI_UPI);
            paymentIntent = new Intent(getApplicationContext(), SbiPayPaymentActivity.class);
            paymentIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
            startActivity(paymentIntent);
        } else if (payment_mode.equals(Constants.PAYMENT_MODE_CASH)) {
            paymentRequestModel.setPayment_type(Constants.PAYMENT_MODE_CASH);
            paymentIntent = new Intent(getApplicationContext(), CompleateTransactionActivity.class);
            paymentIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
            startActivity(paymentIntent);
        } else if (payment_mode.equals(Constants.PAYMENT_MODE_CHEQUE)) {
//            paymentIntent = new Intent(getApplicationContext(), CheckActivity.class);
//            paymentIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
//            startActivity(paymentIntent);
        } else if (payment_mode.equals(Constants.PAYMENT_MODE_CREDIT_CARD)) {
//            paymentIntent = new Intent(getApplicationContext(), CompleateTransactionActivity.class);
//            paymentIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
//            startActivity(paymentIntent);

            //initMSwipe();

            Globals.showToast(getApplicationContext(),"Updated Soon");

        } else if (payment_mode.equals(Constants.PAYMENT_MODE_PAYTM_QR)) {
            paymentIntent = new Intent(getApplicationContext(), ScanAndPayActivity.class);
            paymentIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
            startActivity(paymentIntent);
        } else if (payment_mode.equals(Constants.PAYMENT_MODE_BHIM)) {
//            paymentIntent = new Intent(getApplicationContext(), BhimActivity.class);
//            paymentIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
//            startActivity(paymentIntent);
        } else if (payment_mode.equals(Constants.PAYMENT_MODE_NET_BANKING)) {
            paymentRequestModel.setPayment_type(Constants.PAYMENT_MODE_NET_BANKING);
            paymentIntent = new Intent(getApplicationContext(), CompleateTransactionActivity.class);
            paymentIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
            startActivity(paymentIntent);
        } else if (payment_mode.equals(Constants.PAYMENT_MODE_AADHAR)) {
            paymentRequestModel.setPayment_type(Constants.PAYMENT_MODE_AADHAR);
            paymentIntent = new Intent(getApplicationContext(), CompleateTransactionActivity.class);
            paymentIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
            startActivity(paymentIntent);
        } else if (payment_mode.equals(Constants.PAYMENT_MODE_PAYTM_PG)) {
            paymentIntent = new Intent(getApplicationContext(), PaymentWalletActivity.class);
            paymentIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
            startActivity(paymentIntent);
        } else if (payment_mode.equals(Constants.PAYMENT_MODE_INSTAMOJO)) {
            paymentRequestModel.setPayment_type(payment_mode);
            paymentIntent = new Intent(getApplicationContext(), InstamojoActivity.class);
            paymentIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
            startActivity(paymentIntent);
        }
    }


    private void submitPayment() {

        new BackgroundTask(PaymentModesActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.submitPayment(PaymentModesActivity.this, paymentRequestModel);
            }

            public void taskCompleted(Object data) {
                if (data != null || data.equals("")) {
                    if (data.toString().contains("SUCCESS")) {
                        Globals.showToast(getApplicationContext(), "Payment Success");
                        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("FRAGMENT", "COLLECTION");
                        startActivity(intent);
                    }
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }


}
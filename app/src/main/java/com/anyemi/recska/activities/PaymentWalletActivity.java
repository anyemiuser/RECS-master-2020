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

import com.anyemi.recska.R;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.model.CollectionsModel;
import com.anyemi.recska.model.PaymentRequestModel;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by SuryaTejaChalla on 06-02-2018.
 */

public class PaymentWalletActivity extends AppCompatActivity implements View.OnClickListener {

    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;

    String data;
    CollectionsModel.CollectionsBean item_details;
    PaymentRequestModel paymentRequestModel;

    LinearLayout ll_paytm, ll_free_charge, ll_phone_pe, ll_airtel_money, ll_jio_money, ll_m_pesa;

    TextView tv_amount, tpt_cash, tpt_debit_card, tpt_credit_card, tpt_net_banking,
            tpt_bhim_upi, tpt_wallets, tpt_other_payment;
    ImageView iv_qr_code;
    Button proceed;
    EditText et_phone_num;
    Dialog dialog;

    String Selection;
    String r_bank_name, r_total_amount, r_payment_type, r_emi_ids, r_user_id;

    String r_check_date, r_rrn_number, r_mobile_number, r_upi_id, r_assessment_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_wallets);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(PaymentWalletActivity.this, R.color.colorPrimaryDark));
        }
        createActionBar();
        //initView();
        initView2();

        try {
            Bundle parametros = getIntent().getExtras();
            data = parametros.getString(Constants.PAYMENT_REQUEST_MODEL);
            Gson gson = new Gson();
            paymentRequestModel = gson.fromJson(data, PaymentRequestModel.class);
            tv_amount.setText(String.valueOf(paymentRequestModel.getTotal_amount()));

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
        ll_free_charge = findViewById(R.id.ll_free_charge);
        ll_phone_pe = findViewById(R.id.ll_phone_pe);
        ll_airtel_money = findViewById(R.id.ll_airtel_money);
        ll_jio_money = findViewById(R.id.ll_jio_money);
        ll_m_pesa = findViewById(R.id.ll_m_pesa);
//        ll_internet_banking = (LinearLayout) findViewById(R.id.ll_internet_banking);
//        ll_wallet = (LinearLayout) findViewById(R.id.ll_wallet);


        ll_paytm.setOnClickListener(this);
        ll_free_charge.setOnClickListener(this);
        ll_phone_pe.setOnClickListener(this);
        ll_airtel_money.setOnClickListener(this);
        ll_jio_money.setOnClickListener(this);
        ll_m_pesa.setOnClickListener(this);
//        ll_internet_banking.setOnClickListener(this);
//        ll_wallet.setOnClickListener(this);

    }


    public void getSelectedPayment(String payment_mode) {
        paymentRequestModel.setPayment_type(payment_mode);
        Intent eWalletIntent = new Intent(getApplicationContext(), CompleateTransactionActivity.class);
        eWalletIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
        startActivity(eWalletIntent);
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
                // Globals.showToast(getApplicationContext(),message);
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
            case R.id.ll_paytm:
                getSelectedPayment(Constants.PAYMENT_MODE_PAYTM_PG);
                break;
            case R.id.ll_free_charge:
                getSelectedPayment(Constants.PAYMENT_MODE_FREE_CHARGE);
                break;
            case R.id.ll_phone_pe:
                getSelectedPayment(Constants.PAYMENT_MODE_PHONE_PE);
                break;
            case R.id.ll_airtel_money:
                getSelectedPayment(Constants.PAYMENT_MODE_FREE_AIRTEL);
                break;
            case R.id.ll_jio_money:
                getSelectedPayment(Constants.PAYMENT_MODE_JIO);
                break;
            case R.id.ll_m_pesa:
                getSelectedPayment(Constants.PAYMENT_MODE_FREE_M_PESA);
                break;
        }
    }
}






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
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.anyemi.recska.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by SuryaTejaChalla on 24-02-2018.
 * <p>
 * TO show Avaiable payments
 */

public class CustomerPaymentModesActivity extends AppCompatActivity implements View.OnClickListener {

    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;

    String data;

    CollectionsModel.CollectionsBean item_details;
    PaymentRequestModel paymentRequestModel;

    LinearLayout ll_cash, ll_cheque, ll_others, ll_credit_debit, ll_aadhar_pay,
            ll_scan_pay, ll_bhim, ll_internet_banking, ll_wallet;

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
        setContentView(R.layout.activity_customer_payment_modes);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(CustomerPaymentModesActivity.this, R.color.colorPrimaryDark));
        }
        createActionBar();
        //initView();
        initView2();

        try {
            Bundle parametros = getIntent().getExtras();
            data = parametros.getString(Constants.PAYMENT_REQUEST_MODEL);
            Gson gson = new Gson();
            paymentRequestModel = gson.fromJson(data, PaymentRequestModel.class);

            String resultStr = String.valueOf(paymentRequestModel.getTotal_amount());
            DecimalFormat df = new DecimalFormat("#,##,###.####", new DecimalFormatSymbols(Locale.US));
            // resultStr = df.format(Double.valueOf(resultStr));
            resultStr = Utils.parseAmount(resultStr);
            tv_amount.setText("Rs." + resultStr + " /-");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView2() {

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

        String l_type = SharedPreferenceUtil.getLoginType(getApplicationContext());

        if (l_type.equals(Constants.LOGIN_TYPE_CUSTOMER)) {
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


    public void getSelectedPayment(String payment_mode) {

        if (payment_mode.equals("CASH")) {

            r_payment_type = "C";
            paymentRequestModel.setPayment_type(r_payment_type);
            Intent i = new Intent(getApplicationContext(), CompleateTransactionActivity.class);
            i.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
            startActivity(i);

            //          submitPayment();

        } else if (payment_mode.equals("AADHAR")) {

            r_payment_type = "AADHAR";
            paymentRequestModel.setPayment_type(r_payment_type);
            Intent i = new Intent(getApplicationContext(), CompleateTransactionActivity.class);
            i.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
            startActivity(i);

            //          submitPayment();

        } else if (payment_mode.equals("SBI UPI")) {

            r_payment_type = "UPI";
            paymentRequestModel.setPayment_type(r_payment_type);
            Intent eWalletIntent = new Intent(getApplicationContext(), SbiPayPaymentActivity.class);
            eWalletIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
            startActivity(eWalletIntent);

        } else if (payment_mode.equals("PAYTM")) {

            r_payment_type = "paytm";
            paymentRequestModel.setPayment_type(r_payment_type);
            Intent eWalletIntent = new Intent(getApplicationContext(), PaymentActivity.class);
            eWalletIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
            startActivity(eWalletIntent);

        } else if (payment_mode.equals("Bharat QR")) {
            r_payment_type = "BQ";
            paymentRequestModel.setPayment_type(r_payment_type);
            Intent eWalletIntent = new Intent(getApplicationContext(), PaymentActivity.class);
            eWalletIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
            startActivity(eWalletIntent);
        } else if (payment_mode.equals("credit")) {
//            Intent i = new Intent(getApplicationContext(), EzeTapPaymentActivity.class);
//            i.putExtra("amt", String.valueOf(paymentRequestModel.getTotal_amount()));
//            //i.putExtra("amt", "20");
//            i.putExtra("assessment_num", paymentRequestModel.getAssessment_id());
//            startActivity(i);
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

    private void initView() {

        tv_amount = findViewById(R.id.tv_amount);
        tpt_cash = findViewById(R.id.tv_cash);
        tpt_debit_card = findViewById(R.id.tv_debit_card);
        tpt_credit_card = findViewById(R.id.tv_credit_card);
        tpt_net_banking = findViewById(R.id.tv_net_banking);
        tpt_bhim_upi = findViewById(R.id.tv_bhim_upi);
        tpt_wallets = findViewById(R.id.tv_wallets);
        tpt_other_payment = findViewById(R.id.tv_other_payment);
        proceed = findViewById(R.id.btn_procced);


        tpt_cash.setOnClickListener(this);
        tpt_debit_card.setOnClickListener(this);
        tpt_credit_card.setOnClickListener(this);
        tpt_net_banking.setOnClickListener(this);
        tpt_bhim_upi.setOnClickListener(this);
        tpt_wallets.setOnClickListener(this);
        tpt_other_payment.setOnClickListener(this);
        proceed.setOnClickListener(this);

    }


    private void openTransactionDialog() {

        dialog = new Dialog(CustomerPaymentModesActivity.this);
        dialog.setTitle("Transaction Id");
        dialog.setContentView(R.layout.selection_dialog);
        et_phone_num = dialog.findViewById(R.id.et_phone_num);
        et_phone_num.setHint("Enter Transaction Id");
        Button btn_send_sms = dialog.findViewById(R.id.btn_send_sms);
        btn_send_sms.setText("Submit");
        btn_send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_phone_num.getText().toString().equals("")) {
                    String txn_id = et_phone_num.getText().toString();
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

        new BackgroundTask(CustomerPaymentModesActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.submitPayment(CustomerPaymentModesActivity.this, paymentRequestModel);
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


    private PaymentRequestModel finalPaymentrequest(final String txn_id) {

        try {
            //  paymentRequestModel = new PaymentRequestModel();
            // String user_id=SharedPreferenceUtil.getUserId(getApplicationContext());;

//            paymentRequestModel.setUser_id(Integer.parseInt(SharedPreferenceUtil.getUserId(getApplicationContext())));
//            paymentRequestModel.setEmi_ids(re);
//            paymentRequestModel.setPayment_type("SBIUPI");
//            paymentRequestModel.setTotal_amount(Integer.parseInt(amount));
//            paymentRequestModel.setBankname("");
//            paymentRequestModel.setCheckdate("");
            paymentRequestModel.setRr_number(txn_id);
            paymentRequestModel.setTrsno(txn_id);
            // paymentRequestModel.setMobile_number("");
            //paymentRequestModel.setUpi_id(et_trns_id.getText().toString());
            // paymentRequestModel.setAssessment_id(assessment_num);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return paymentRequestModel;
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
//            case R.id.tv_cash:
//                getSelectedPayment("Cash");
//                break;
//            case R.id.tv_debit_card:
//                getSelectedPayment("");
//                break;
//            case R.id.tv_credit_card:
//                getSelectedPayment("");
//                break;
//            case R.id.tv_net_banking:
//                getSelectedPayment("");
//                break;
//            case R.id.tv_bhim_upi:
//                getSelectedPayment("SBI UPI");
//                break;
//            case R.id.tv_wallets:
//                getSelectedPayment("PAYTM");
//                break;
            case R.id.ll_others:
                getSelectedPayment("SBI UPI");
                break;
//            case R.id.btn_procced:
//                break;

            case R.id.ll_cash:
                getSelectedPayment("CASH");
                break;

            case R.id.ll_cheque:
//                getSelectedPayment("");

//                Intent checkIntent = new Intent(getApplicationContext(), CheckActivity.class);
//                checkIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
//                startActivity(checkIntent);
                break;

            case R.id.ll_credit_debit:
                Intent cardIntent = new Intent(getApplicationContext(), CompleateTransactionActivity.class);
                cardIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
                startActivity(cardIntent);

                //openTransactionDialog();
                // getSelectedPayment("credit");
                break;
            case R.id.ll_scan_pay:

                Intent ScanIntent = new Intent(getApplicationContext(), ScanAndPayActivity.class);
                ScanIntent.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
                startActivity(ScanIntent);


                //  getSelectedPayment("Bharat QR");
                break;

            case R.id.ll_bhim:

//                Intent bhim = new Intent(getApplicationContext(), BhimActivity.class);
//                bhim.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
//                startActivity(bhim);
                break;

            case R.id.ll_internet_banking:
                paymentRequestModel.setPayment_type("NB");
                Intent nb = new Intent(getApplicationContext(), CompleateTransactionActivity.class);
                nb.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
                startActivity(nb);

                //   getSelectedPayment("");
                break;

            case R.id.ll_aadhar_pay:
                getSelectedPayment("AADHAR");
                break;

            case R.id.ll_wallet:
                Intent i = new Intent(getApplicationContext(), PaymentWalletActivity.class);
                i.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
                startActivity(i);
//

//                Intent i = new Intent(getApplicationContext(), CompleateTransactionActivity.class);
//                paymentRequestModel.setPayment_type("pg");
//                i.putExtra(Constants.PAYMENT_REQUEST_MODEL, new Gson().toJson(paymentRequestModel));
//                startActivity(i);


                // getSelectedPayment("PAYTM");
                break;
        }

    }

}

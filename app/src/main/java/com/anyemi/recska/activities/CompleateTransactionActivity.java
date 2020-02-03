package com.anyemi.recska.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anyemi.recska.NavigationActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.CheckSumModel;
import com.anyemi.recska.model.PaymentRequestModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.PrintLog;
import com.anyemi.recska.utils.Utils;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by SuryaTejaChalla on 07-02-2018.
 * To submit Transction
 */

public class CompleateTransactionActivity extends AppCompatActivity implements TextWatcher {

    //ACTION BAR UI COMPONENTS

    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;

    // VARIBLES

    String data;
    PaymentRequestModel paymentRequestModel;
    TextView tv_amount;

    //UI COMPONENTS

    Button proceed;
    Dialog dialog;

    EditText et_trns_id, et_phone_num, et_aadhar;
    TextInputLayout til_trns_id, til_phone_id, til_aadhar;


    //Payment Gate Way

    String orderId;
    String hash;
    Gson gson;
    Dialog infoDialog;

    //   boolean Security=false;

//    MSWIPE


    boolean isPreAuth = false;
    boolean isSaleWithCash = false;
    private String CARDSALE_DIALOG_MSG = "MswipeWisepad Cardsale";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_transaction);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(CompleateTransactionActivity.this, R.color.colorPrimaryDark));
        }


        try {
            Bundle parametros = getIntent().getExtras();
            data = parametros.getString(Constants.PAYMENT_REQUEST_MODEL);
            gson = new Gson();
            paymentRequestModel = gson.fromJson(data, PaymentRequestModel.class);
            paymentRequestModel.setLast_paid_date(Utils.getCurrentDateTime());

            // Payment type Credit card or Debit Card

            if (paymentRequestModel.getPayment_type().equals(Constants.PAYMENT_MODE_DEBIT_CARD) ||
                    paymentRequestModel.getPayment_type().equals(Constants.PAYMENT_MODE_CREDIT_CARD)) {
//                mWisePadController = WisePadController.sharedInstance(this, this);

                isPreAuth = getIntent().getBooleanExtra("isPreAuth", false);
                isSaleWithCash = getIntent().getBooleanExtra("isSaleWithCash", false);


                if (isPreAuth)
                    CARDSALE_DIALOG_MSG = " Pre Auth";
                else if (isSaleWithCash)
                    CARDSALE_DIALOG_MSG = " Sale Cash";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        createActionBar();
        initView2();
        if (paymentRequestModel.getPayment_type().equals(Constants.PAYMENT_MODE_PAYTM_PG)) {
            til_trns_id.setVisibility(View.GONE);

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
        aTitle.setText("Complete payment ");

    }

    private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
//        orderId = "ORDER" + (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
        generateHashKey();
    }


    private void generateHashKey() {

        new BackgroundTask(CompleateTransactionActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.generateHash(CompleateTransactionActivity.this, paymentRequestModel());
            }

            public void taskCompleted(Object data) {
                if (data != null || !data.equals("")) {
//                    hash = data.toString();
//                    PrintLog.print("HASH KEY", hash);
//                    onStartTransaction();

                    CheckSumModel checkSumModel = new CheckSumModel();
                    checkSumModel = gson.fromJson(data.toString(), CheckSumModel.class);
                    if (checkSumModel.getStatus().equals("success")) {
                        hash = checkSumModel.getCheckSum();
                        PrintLog.print("HASH KEY", hash);
                        onStartTransaction();
                    } else {
                        Globals.showToast(getApplicationContext(), "Unable to generate hash");
                    }
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }


    private String paymentRequestModel() {
        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("MID", "ANYEMI64501677609833");
            jsonObject.put("ORDER_ID", orderId);
            jsonObject.put("CUST_ID", paymentRequestModel.getAssessment_id().replace(" ", ""));
            jsonObject.put("INDUSTRY_TYPE_ID", "Retail92");
            jsonObject.put("CHANNEL_ID", "WAP");
            jsonObject.put("TXN_AMOUNT", paymentRequestModel.getTotal_amount());
            // jsonObject.put("TXN_AMOUNT", "1");
            jsonObject.put("WEBSITE", "ANYEMIWAP");
            jsonObject.put("EMAIL", "");
            jsonObject.put("MOBILE_NO", et_phone_num.getText().toString());
            jsonObject.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);

            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }


    public void onStartTransaction() {
        //  PaytmPGService Service = PaytmPGService.getStagingService();
        PaytmPGService Service = PaytmPGService.getProductionService();

        //Kindly create complete Map and checksum on your server side and then put it here in paramMap.

        Map<String, String> paramMap = new HashMap<String, String>();

        paramMap.put("MID", "ANYEMI64501677609833");
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("CUST_ID", paymentRequestModel.getAssessment_id().replace(" ", ""));
        paramMap.put("INDUSTRY_TYPE_ID", "Retail92");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", paymentRequestModel.getTotal_amount());
        // paramMap.put("TXN_AMOUNT", "1");
        paramMap.put("WEBSITE", "ANYEMIWAP");
        paramMap.put("EMAIL", "");
        paramMap.put("MOBILE_NO", et_phone_num.getText().toString());
        paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
        paramMap.put("CHECKSUMHASH", hash);


        PaytmOrder Order = new PaytmOrder((HashMap<String, String>) paramMap);


        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {

                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction : " + inResponse);
                        String status = inResponse.getString("STATUS");
                        Log.d("Status", status);
                        if (status.equals("TXN_SUCCESS")) {
                            paymentRequestModel.setRr_number(inResponse.getString("TXNID"));
                            paymentRequestModel.setPayment_type("Paytm");

                            Globals.showToast(getApplicationContext(), "Money Collected");
                            submitPayment();
                        } else {
                            openInfoDialog(inResponse.getString("RESPMSG"), "Payment Failed");
                        }
                    }

                    @Override
                    public void networkNotAvailable() {
                        // If network is not
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                });
    }


    private void openInfoDialog(String respmsg, final String s) {

        if (infoDialog == null) {
            infoDialog = new Dialog(CompleateTransactionActivity.this);
        }

        infoDialog.setContentView(R.layout.dialog_info);
        final TextView tv_info = infoDialog.findViewById(R.id.tv_info);
        final Button btn_send_sms = infoDialog.findViewById(R.id.btn_send_sms);
        final ProgressBar prgs_load = infoDialog.findViewById(R.id.prgs_load);
        infoDialog.setTitle(s);
        btn_send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tv_info.setText(respmsg);
        infoDialog.setCancelable(false);
        infoDialog.show();
    }

    private void initView2() {

        tv_amount = findViewById(R.id.tv_amount);
        proceed = findViewById(R.id.btn_pay);
        et_trns_id = findViewById(R.id.et_trns_id);
        et_phone_num = findViewById(R.id.et_phone_num);
        et_aadhar = findViewById(R.id.et_aadhar);
        til_trns_id = findViewById(R.id.til_trns_id);
        tv_amount = findViewById(R.id.tv_amount);
        et_trns_id.addTextChangedListener(this);
        et_aadhar.addTextChangedListener(this);
        et_phone_num.addTextChangedListener(this);
        til_phone_id = findViewById(R.id.til_phone_id);
        til_aadhar = findViewById(R.id.til_aadhar);
        proceed.setText("Proceed Payment");

        if (paymentRequestModel.getTrsno() != null && !paymentRequestModel.getTrsno().equals("")) {
            et_trns_id.setText(paymentRequestModel.getTrsno());
            et_trns_id.setEnabled(false);
        }

        String resultStr = Utils.parseAmount(String.valueOf(paymentRequestModel.getTotal_amount()));
        tv_amount.setText("Rs." + resultStr + " /-");

        if (paymentRequestModel.getMobile_number() != null) {
            et_phone_num.setText(paymentRequestModel.getMobile_number());
        }

        if (paymentRequestModel.getPayment_type().equals(Constants.PAYMENT_MODE_CASH)) {
            til_trns_id.setVisibility(View.GONE);
            et_trns_id.setVisibility(View.GONE);
        } else {
            til_trns_id.setVisibility(View.VISIBLE);
            et_trns_id.setVisibility(View.VISIBLE);
        }

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!et_aadhar.getText().toString().equals("") && et_aadhar.getText().toString().length() != 12) {
                    Globals.showToast(getApplicationContext(), " Please enter valid Aadhar Number");
                }


                if (paymentRequestModel.getPayment_type().equals(Constants.PAYMENT_MODE_CASH)) {
                    if (et_phone_num.getText().toString().equals("")) {
                        Globals.showToast(getApplicationContext(), " Please enter Mobile Number");
                    } else if (et_phone_num.getText().toString().length() == 10) {
                        paymentRequestModel.setMobile_number(et_phone_num.getText().toString());
                        submitPayment();
                    } else {
                        Globals.showToast(getApplicationContext(), "Please enter valid mobile number");
                    }
                } else if (paymentRequestModel.getPayment_type().equals(Constants.PAYMENT_MODE_PAYTM_PG)) {

                    if (et_phone_num.getText().toString().equals("")) {
                        Globals.showToast(getApplicationContext(), " Please enter Mobile Number");
                    } else if (et_phone_num.getText().toString().length() == 10) {
                        paymentRequestModel.setMobile_number(et_phone_num.getText().toString());
                        initOrderId();
                    } else {
                        Globals.showToast(getApplicationContext(), "Please enter valid mobile number");
                    }


                } else if (performValidation()) {
                    paymentRequestModel.setRr_number(et_trns_id.getText().toString());
                    submitPayment();
                }
            }
        });

    }


    private void submitPayment() {

        new BackgroundTask(CompleateTransactionActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.submitPayment(CompleateTransactionActivity.this, paymentRequestModel);
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


    @Override
    public void onResume() {
        Random r = new Random(System.currentTimeMillis());
        orderId = "ORDER" + (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
        orderId = orderId + System.currentTimeMillis();
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


    private boolean performValidation() {
        boolean isValid = false;
        if (et_trns_id.getText().toString().equals("")) {
            til_trns_id.setError("Please enter Transaction id");
            et_trns_id.requestFocus();
        } else if (et_phone_num.getText().toString().equals("")) {
            til_phone_id.setError("Enter your Mobile Number");
            et_phone_num.requestFocus();
        } else if (et_phone_num.getText().toString().length() != 10) {
            til_phone_id.setError("Invalid Mobile Number");
            et_phone_num.requestFocus();
        } else {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (et_trns_id.hasFocus() && et_trns_id.getText().toString().length() > 0) {
            til_trns_id.setErrorEnabled(false);
            til_trns_id.setError(null);
        } else if (et_phone_num.hasFocus() && et_phone_num.getText().toString().length() > 0) {
            til_phone_id.setErrorEnabled(false);
            til_phone_id.setError(null);
        }

        if (et_trns_id.hasFocus() && et_trns_id.getText().toString().length() > 255) {
            Globals.showToast(getApplicationContext(), "More than 255 character are not allowed");
        }


    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


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


}
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

import com.anyemi.recska.R;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.CheckSumModel;
import com.anyemi.recska.model.PaymentIdModel;
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
import java.util.Random;

public class PaytmPgActivity extends AppCompatActivity implements TextWatcher {

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

    EditText et_phone_num;
    TextInputLayout til_phone_id;


    //Payment Gate Way

    String orderId, payment_id;
    String hash;
    Gson gson = new Gson();
    Dialog infoDialog;

    //   boolean Security=false;

//    MSWIPE


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_pg);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(PaytmPgActivity.this, R.color.colorPrimaryDark));
        }


        try {


            paymentRequestModel = Globals.getPaymentRequestModes(getIntent().getExtras());

            if (paymentRequestModel == null) {
                Globals.showToast(getApplicationContext(), Constants.PAYMENT_REQ_ERROR);
                finish();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        createActionBar();
        initView();


    }


    private void createActionBar() {


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_action_bar_with_home_button, null);

        aTitle = (TextView) mCustomView.findViewById(R.id.title_text);
        rl_new_mails = (RelativeLayout) mCustomView.findViewById(R.id.rl_new_mails);
        notification_count = (TextView) mCustomView.findViewById(R.id.text_count);
        iv_add_new = (ImageView) mCustomView.findViewById(R.id.iv_add_new);

        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        aTitle.setTextColor(getResources().getColor(R.color.white));
        aTitle.setText("Complete payment ");

    }

    private void initOrderId() {
        
        //max 50 char
        Random r = new Random(System.currentTimeMillis());
        String rr = String.valueOf(System.currentTimeMillis());
        orderId = "ANYEMI64501677609833" + rr;
        //orderId = "ORDER" + (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
        initPaymentRequest();
    }


    private void initPaymentRequest() {

        new BackgroundTask(PaytmPgActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.postPayment(PaytmPgActivity.this, paymentRequestModel);
            }

            public void taskCompleted(Object data) {

                if (data != null || !data.equals("")) {
                    PaymentIdModel mResponsedata = null;
                    mResponsedata = gson.fromJson(data.toString(), PaymentIdModel.class);

                    if (mResponsedata.getStatus().equals("success")) {
                        payment_id = String.valueOf(mResponsedata.getPayment_id());
                        paymentRequestModel.setPayment_id(payment_id);
                        generateHashKey();
                    } else {
                        Globals.showToast(getApplicationContext(), "Unable Fetch Dat Please Try again later");
                    }
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }


    private void generateHashKey() {

        new BackgroundTask(PaytmPgActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.generateHash(PaytmPgActivity.this, paymentRequestModel());
            }

            public void taskCompleted(Object data) {
                if (data != null || !data.equals("")) {
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
            //jsonObject.put("MID", "ANYEMI64501677609833");  //Updated on 08/28/2018 as per sakshi instruction
            jsonObject.put("ORDER_ID", orderId);
          //  jsonObject.put("ORDER_ID", "01234586");
            jsonObject.put("CUST_ID", paymentRequestModel.getAssessment_id().replace(" ", ""));
            jsonObject.put("INDUSTRY_TYPE_ID", "Retail92");
            jsonObject.put("CHANNEL_ID", "WAP");
            jsonObject.put("TXN_AMOUNT", paymentRequestModel.getTotal_amount());
            // jsonObject.put("TXN_AMOUNT", "1");
            jsonObject.put("WEBSITE", "ANYEMIWAP");
            jsonObject.put("EMAIL", "");
            jsonObject.put("MOBILE_NO", et_phone_num.getText().toString());
            jsonObject.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
           // jsonObject.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + "01234586");


            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }


    public void onStartTransaction() {
        PaytmPGService Service = PaytmPGService.getProductionService();
        // PaytmPGService Service = PaytmPGService.getStagingService();


        HashMap<String, String> paramMap = new HashMap<String, String>();

//production

        paramMap.put("MID", "ANYEMI64501677609833");
        paramMap.put("ORDER_ID", orderId);
       // paramMap.put("ORDER_ID", "01234586");
        paramMap.put("CUST_ID", paymentRequestModel.getAssessment_id().replace(" ", ""));
        paramMap.put("INDUSTRY_TYPE_ID", "Retail92");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", paymentRequestModel.getTotal_amount());
        // paramMap.put("TXN_AMOUNT", "1");
        paramMap.put("WEBSITE", "ANYEMIWAP");
        paramMap.put("EMAIL", "");
        paramMap.put("MOBILE_NO", et_phone_num.getText().toString());
        paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
       // paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + "01234586");
        paramMap.put("CHECKSUMHASH", hash);


//Development

/*
        //   paramMap.put("MID", "ANYEMI14182027236332");
        paramMap.put("MID", "ANYEMI14182027236332");
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("CUST_ID", paymentRequestModel.getAssessment_id().replace(" ", ""));
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", paymentRequestModel.getTotal_amount());
        // paramMap.put("TXN_AMOUNT", "1");
        paramMap.put("WEBSITE", "APP_STAGING");
        paramMap.put("EMAIL", "");
        paramMap.put("MOBILE_NO", et_phone_num.getText().toString());
        paramMap.put("CALLBACK_URL", "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
        paramMap.put("CHECKSUMHASH", hash);
*/

        //  PaytmOrder Order = new PaytmOrder(paramMap);
        PaytmOrder Order = new PaytmOrder(paramMap);


        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {

                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction : " + inResponse);
                        String status = inResponse.getString("STATUS");
                        Log.d("Status", status);
                        if (status.equals("TXN_SUCCESS")) {

                            paymentRequestModel.setRr_number(inResponse.getString("TXNID"));
                            paymentRequestModel.setTrsno(inResponse.getString("TXNID"));

                            Globals.showToast(getApplicationContext(), "Payment success");
                            submitPayment();
                        } else {
                            openInfoDialog(inResponse.getString("RESPMSG"), "Payment Failed");
                        }
                    }

                    @Override
                    public void networkNotAvailable() {
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {

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
            infoDialog = new Dialog(PaytmPgActivity.this);
        }

        infoDialog.setContentView(R.layout.dialog_info);
        final TextView tv_info = (TextView) infoDialog.findViewById(R.id.tv_info);
        final Button btn_send_sms = (Button) infoDialog.findViewById(R.id.btn_send_sms);
        final ProgressBar prgs_load = (ProgressBar) infoDialog.findViewById(R.id.prgs_load);
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

    private void initView() {

        tv_amount = (TextView) findViewById(R.id.tv_amount);
        proceed = (Button) findViewById(R.id.btn_pay);

        et_phone_num = (EditText) findViewById(R.id.et_phone_num);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        et_phone_num.addTextChangedListener(this);
        til_phone_id = (TextInputLayout) findViewById(R.id.til_phone_id);
        proceed.setText("Proceed Payment");


        String resultStr = Utils.parseAmount(String.valueOf(paymentRequestModel.getTotal_amount()));
        tv_amount.setText("Rs." + resultStr + " /-");

        if (paymentRequestModel.getMobile_number() != null) {
            if (paymentRequestModel.getMobile_number().length() < 10) {
                et_phone_num.setText("");
            } else {
                et_phone_num.setText(paymentRequestModel.getMobile_number());
            }
        }


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (performValidation()) {
                    initOrderId();
                }
            }
        });

    }


    private void submitPayment() {

        paymentRequestModel.setMobile_number(et_phone_num.getText().toString());

        new BackgroundTask(PaytmPgActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.submitPayment(PaytmPgActivity.this, paymentRequestModel);
            }

            public void taskCompleted(Object data) {
                if (data != null || data.equals("")) {
                    if (data.toString().contains("SUCCESS")) {
                        Globals.ProceedNextScreen(getApplicationContext(), paymentRequestModel);
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

        if (et_phone_num.getText().toString().equals("")) {
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
        if (et_phone_num.hasFocus() && et_phone_num.getText().toString().length() > 0) {
            til_phone_id.setErrorEnabled(false);
            til_phone_id.setError(null);
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

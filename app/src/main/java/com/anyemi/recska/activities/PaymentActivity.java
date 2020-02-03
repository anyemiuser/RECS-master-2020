package com.anyemi.recska.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
 * Created by SuryaTejaChalla on 12-12-2017.
 */

public class PaymentActivity extends AppCompatActivity {
    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;

    String data;
    CollectionsModel.CollectionsBean item_details;
    PaymentRequestModel paymentRequestModel;


    TextView tv_amount;
    ImageView iv_qr_code;
    Button proceed;
    EditText et_phone_num;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(PaymentActivity.this, R.color.colorPrimaryDark));
        }

        createActionBar();
        initView();
        createDelay();

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

            if (paymentRequestModel.getPayment_type().equals(Constants.PAYMENT_MODE_PAYTM_QR)) {
               // iv_qr_code.setImageDrawable(getResources().getDrawable(R.drawable.anyemi_paytm));
            } else if (paymentRequestModel.getPayment_type().equals(Constants.PAYMENT_MODE_BHARATH_QR)) {
                iv_qr_code.setImageDrawable(getResources().getDrawable(R.drawable.bharath_qr));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {


        tv_amount = findViewById(R.id.tv_amount);
        iv_qr_code = findViewById(R.id.iv_qr_code);
        proceed = findViewById(R.id.btn_done_scan);
        proceed.setEnabled(false);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // openTransactionDialog();
                compleatePayment();


            }
        });


    }

    private void compleatePayment() {

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
        aTitle.setText("Complete payment ");

    }


//    private void openTransactionDialog() {
//
//        dialog = new Dialog(PaymentActivity.this);
//        dialog.setTitle("Transaction Id");
//        dialog.setContentView(R.layout.dialog_sms);
//        et_phone_num = (EditText) dialog.findViewById(R.id.et_phone_num);
//        et_phone_num.setHint("Enter Transaction Id");
//        Button btn_send_sms = (Button) dialog.findViewById(R.id.btn_send_sms);
//        btn_send_sms.setText("Submit");
//        btn_send_sms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!et_phone_num.getText().toString().equals("")) {
//                    String txn_id = et_phone_num.getText().toString();
//                    submitPayment(txn_id);
//                    dialog.dismiss();
//                } else {
//                    Globals.showToast(getApplicationContext(), "No Input Value Found");
//                }
//            }
//        });
//        dialog.show();
//
//    }


//    private void submitPayment(final String txn_id) {
//
//        new BackgroundTask(PaymentActivity.this, new BackgroundThread() {
//            @Override
//            public Object runTask() {
//                return HomeServices.submitPayment(PaymentActivity.this, finalPaymentrequest(txn_id));
//            }
//
//            public void taskCompleted(Object data) {
//                if (data != null || data.equals("")) {
//                    if (data.toString().contains("SUCCESS")) {
//                        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        intent.putExtra("FRAGMENT", "COLLECTION");
//                        startActivity(intent);
//                    }
//                }
//            }
//        }, getString(R.string.loading_txt)).execute();
//    }


//    private PaymentRequestModel finalPaymentrequest(final String txn_id) {
//
//        try {
//            //  paymentRequestModel = new PaymentRequestModel();
//            // String user_id=SharedPreferenceUtil.getUserId(getApplicationContext());;
//
////            paymentRequestModel.setUser_id(Integer.parseInt(SharedPreferenceUtil.getUserId(getApplicationContext())));
////            paymentRequestModel.setEmi_ids(re);
////            paymentRequestModel.setPayment_type("SBIUPI");
////            paymentRequestModel.setTotal_amount(Integer.parseInt(amount));
////            paymentRequestModel.setBankname("");
////            paymentRequestModel.setCheckdate("");
//            paymentRequestModel.setRr_number(txn_id);
//            paymentRequestModel.setTrsno(txn_id);
//            // paymentRequestModel.setMobile_number("");
//            //paymentRequestModel.setUpi_id(et_trns_id.getText().toString());
//            // paymentRequestModel.setAssessment_id(assessment_num);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return paymentRequestModel;
//    }


    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("PAYTM QR"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equalsIgnoreCase("PAYTM QR")) {
                final String message = intent.getStringExtra("id");
                // Globals.showToast(getApplicationContext(),message);
//                et_phone_num.setText(message);
//                dialog.dismiss();
                paymentRequestModel.setRr_number(message);
                paymentRequestModel.setTrsno(message);

               compleatePayment();
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

    private void createDelay() {
        CountDownTimer countDownTimer = new CountDownTimer(1 * 60 * 1000, 2 * 1000) {

            public void onTick(long millisUntilFinished) {
                proceed.setText("Reading TID " + millisUntilFinished / 1000);
                System.out.print(millisUntilFinished / 1000);
            }

            public void onFinish() {
                proceed.setEnabled(true);
                proceed.setText("Continue Manually");
            }

        }.start();

    }
}






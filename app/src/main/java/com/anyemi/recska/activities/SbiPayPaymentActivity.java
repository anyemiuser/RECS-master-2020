package com.anyemi.recska.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anyemi.recska.NavigationActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.CheackValidVpaModel;
import com.anyemi.recska.model.CheckValidResponseModel;
import com.anyemi.recska.model.PaymentChooserModel;
import com.anyemi.recska.model.PaymentRequestModel;
import com.anyemi.recska.model.SbiCheckPaymentStatus;
import com.anyemi.recska.model.SbiPayInitResaponseModel;
import com.anyemi.recska.model.VpaListModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.anyemi.recska.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by SuryaTejaChalla on 21-12-2017.
 */

public class SbiPayPaymentActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    //ACTION BAR UI COMPONENTS

    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;

    //UI COMPONENTS

    Button btn_pay;
    EditText et_sbi_id, et_phone_num;
    TextInputLayout til_sbi_id, til_phone_id;
    TextView tv_amount;


    String amount = "", ids = "", assessment_num = "", payment_id = "", data;
    boolean mButtonClicked, mTransDone;

    PaymentRequestModel paymentRequestModel;
    JSONObject jsonObject;
    Long startTime;
    Dialog chooserDialog;
    Gson gson = new Gson();
    CountDownTimer countDownTimer;
    Dialog infoDialog;
    VpaListModel vpaListModel;
    ArrayList<VpaListModel.UpilistBean> SCollections = new ArrayList<>();
    UpiListAdapter mAdapter;
    ListView lv_my_account;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbi_pay);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(SbiPayPaymentActivity.this, R.color.colorPrimaryDark));
        }


        createActionBar();
        mButtonClicked = false;

        /*
        //Getting Parameters from intent
         */
        try {
            Bundle parametros = getIntent().getExtras();
            data = parametros.getString(Constants.PAYMENT_REQUEST_MODEL);
            paymentRequestModel = gson.fromJson(data, PaymentRequestModel.class);
            amount = String.valueOf(paymentRequestModel.getTotal_amount());
            ids = paymentRequestModel.getEmi_ids();
            assessment_num = paymentRequestModel.getAssessment_id();
        } catch (Exception e) {
            e.printStackTrace();
        }

        initializeView();
        getUpiData();
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
        aTitle.setText("Complete payment");

    }


    private void initializeView() {

        btn_pay = findViewById(R.id.btn_pay);
        et_sbi_id = findViewById(R.id.et_sbi_id);
        et_phone_num = findViewById(R.id.et_phone_num);
        til_sbi_id = findViewById(R.id.til_sbi_id);
        tv_amount = findViewById(R.id.tv_amount);
        et_sbi_id.addTextChangedListener(this);
        et_phone_num.addTextChangedListener(this);
        til_phone_id = findViewById(R.id.til_phone_id);
        tv_amount.setText("Rs." + amount + " /-");
        String resultStr = Utils.parseAmount(paymentRequestModel.getTotal_amount());
        tv_amount.setText("Rs." + resultStr + " /-");
        btn_pay.setText("Proceed Payment");
        btn_pay.setOnClickListener(this);

        lv_my_account = findViewById(R.id.lv_collection);
        mAdapter = new UpiListAdapter(getApplicationContext(), SCollections);
        lv_my_account.setAdapter(mAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:

                if (performValidation() && !mButtonClicked) {

                    mButtonClicked = true;
                    mTransDone = false;
                    //paymentRequestModel = new PaymentRequestModel();
                    paymentRequestModel.setUser_id(Integer.parseInt(SharedPreferenceUtil.getUserId(getApplicationContext())));
                    paymentRequestModel.setEmi_ids(ids);
                    paymentRequestModel.setPayment_type(Constants.PAYMENT_MODE_PAYTM_SBI_UPI);
                    paymentRequestModel.setTotal_amount(amount);
                    //  paymentRequestModel.setTotal_amount("1");
                    paymentRequestModel.setBankname("");
                    paymentRequestModel.setCheckdate("");
                    paymentRequestModel.setRr_number("");
                    paymentRequestModel.setMobile_number(et_phone_num.getText().toString());
                    paymentRequestModel.setUpi_id(et_sbi_id.getText().toString());
                    paymentRequestModel.setAssessment_id(assessment_num);

                    btn_pay.setEnabled(false);

                    checkValidVpa();

                }
                break;
        }
    }


    private boolean checkTwice(String s) {
        boolean isValid = false;
        String ss[] = s.split("@");
        if (ss.length == 2) {
            String prefix = ss[0];
            String post = ss[1];

            if (prefix.equals("") || prefix.length() == 0) {
                isValid = false;
            } else if (post.equals("") || post.length() == 0) {
                isValid = false;
            }else{
                isValid=true;
            }
        }


        return isValid;
    }


    /*
   // Validations
    */

    private boolean performValidation() {
        boolean isValid = false;
        String number = et_phone_num.getText().toString();
        if (number.startsWith("9")) {
            Log.e("NUMBER", number = number.substring(0, 1));
        }

        if (et_sbi_id.getText().toString().equals("") || et_sbi_id.getText().toString().contains(" ")) {
            til_sbi_id.setError("Please enter a valid VPA");
            et_sbi_id.requestFocus();
        } else if (!et_sbi_id.getText().toString().contains("@")) {
            til_sbi_id.setError("Please enter a valid VPA");
            et_sbi_id.requestFocus();
        } else if (!Utils.isValidVpa(et_sbi_id.getText().toString())) {
            til_sbi_id.setError("Please enter a valid VPA");
            et_sbi_id.requestFocus();
        } else if (!checkTwice(et_sbi_id.getText().toString())) {
            til_sbi_id.setError("Please enter a valid VPA");
            et_sbi_id.requestFocus();
        } else if (et_sbi_id.getText().toString().length() > 255) {
            til_sbi_id.setError("More than 255 character are not allowed");
            et_sbi_id.requestFocus();
        } else if (et_phone_num.getText().toString().equals("")) {
            til_phone_id.setError("Enter your Mobile Number");
            et_phone_num.requestFocus();
        } else if (et_phone_num.getText().toString().length() != 10) {
            til_phone_id.setError("Invalid Mobile Number");
            et_phone_num.requestFocus();
        } else if (number.startsWith("1")
                || number.startsWith("2")
                || number.startsWith("3")
                || number.startsWith("4")
                || number.startsWith("5")) {
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

        if (et_sbi_id.hasFocus() && et_sbi_id.getText().toString().length() > 0) {
            til_sbi_id.setErrorEnabled(false);
            til_sbi_id.setError(null);
        } else if (et_phone_num.hasFocus() && et_phone_num.getText().toString().length() > 0) {
            til_phone_id.setErrorEnabled(false);
            til_phone_id.setError(null);
        }

        if (et_sbi_id.hasFocus() && et_sbi_id.getText().toString().length() > 255) {
            String s = et_sbi_id.getText().toString();
            s = s.substring(0, 255);
            et_sbi_id.setText(s);
            et_sbi_id.setSelection(s.length());
            Globals.showToast(getApplicationContext(), "More than 255 character are not allowed");
        }


    }

    @Override
    public void afterTextChanged(Editable editable) {

    }



    private void openPaymentDialog() {


        if (chooserDialog == null) {
            chooserDialog = new Dialog(SbiPayPaymentActivity.this);
        }

        chooserDialog.setCanceledOnTouchOutside(false);

        chooserDialog.setContentView(R.layout.fragment_collection_layout);
        final ListView lv_my_account = chooserDialog.findViewById(R.id.lv_collection);


        String UPI = getUPIString("anyemihpcl@sbi", "Merchant", "",
                "", "",
                "Testing", "1", "INR", "");


        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(UPI));


        PackageManager pm = getPackageManager();
        List<ResolveInfo> launchables = pm.queryIntentActivities(intent, 0);

        PaymentChooserModel paymentChooserModel;

        ArrayList<PaymentChooserModel> ary = new ArrayList<>();

        for (int test = 0; test < launchables.size(); test++) {
            paymentChooserModel = new PaymentChooserModel();


            ResolveInfo ri = launchables.get(test);
            String packageName = ri.activityInfo.packageName;


            try {

                Drawable ico = getPackageManager().getApplicationIcon(packageName);
                String app_name = (String) pm.getApplicationLabel(pm.getApplicationInfo(packageName
                        , PackageManager.GET_META_DATA));
                paymentChooserModel.setApp_name(app_name);
                paymentChooserModel.setApp_icon(ico);
                paymentChooserModel.setApp_package_name(packageName);
                ary.add(paymentChooserModel);
                Log.e("ico-->", "" + ico);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


//                        Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
//                        startActivity(launchIntent);


        }

        ListingAdapter mAdapter;

        mAdapter = new ListingAdapter(getApplicationContext(), ary);
        lv_my_account.setAdapter(mAdapter);

        chooserDialog.setTitle("PayWith");
        chooserDialog.show();


    }


    private String getUPIString(String payeeAddress, String payeeName, String payeeMCC, String trxnID, String trxnRefId,
                                String trxnNote, String payeeAmount, String currencyCode, String refUrl) {
        String UPI = "upi://pay?pa=" + payeeAddress + "&pn=" + payeeName
                + "&mc=" + payeeMCC + "&tid=" + trxnID + "&tr=" + trxnRefId
                + "&tn=" + trxnNote + "&am=" + payeeAmount + "&cu=" + currencyCode
                + "&refUrl=" + refUrl;
        return UPI.replace(" ", "+");
    }



    private void getUpiData() {
        new BackgroundTask(SbiPayPaymentActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.vpaList(SbiPayPaymentActivity.this, upiRequest());
            }

            public void taskCompleted(Object data) {
                if (data != null || data.equals("")) {
                    if (data != null || data.equals("")) {
                        try {
                            SCollections.clear();
                            Gson gson = new Gson();
                            vpaListModel = new VpaListModel();
                            vpaListModel = gson.fromJson(data.toString(), VpaListModel.class);

                            if(vpaListModel.getUpilist()!=null) {

                                SCollections.addAll(vpaListModel.getUpilist());
                            }

                            mAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }

    private String upiRequest() {

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("user_id", SharedPreferenceUtil.getUserId(getApplicationContext()));
            System.out.println(requestObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject.toString();
    }


    public class UpiListAdapter extends BaseAdapter {

        private Context c;
        private LayoutInflater mInflater;
        ViewHolder _listView;
        ArrayList<VpaListModel.UpilistBean> tenant_matches_listings;

        public UpiListAdapter(Context c, ArrayList<VpaListModel.UpilistBean> mListings) {
            this.tenant_matches_listings = mListings;
            this.c = c;
            mInflater = LayoutInflater.from(c);
        }


        @Override
        public int getCount() {
            //return tenant_matches_listings.size();
            return tenant_matches_listings.size();
        }

        @Override
        public Object getItem(int position) {
            return tenant_matches_listings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // final MyAccountListingsResponse2 studentList = tenant_matches_listings.get(position);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.lv_upi_list, null, false);
                _listView = new ViewHolder();

                _listView.tv_c_name = convertView.findViewById(R.id.tv_c_name);
                _listView.textViewOptions = convertView.findViewById(R.id.textViewOptions);
                _listView.iv_item = (ImageView) convertView.findViewById(R.id.iv_item);

                convertView.setTag(_listView);
            } else {
                _listView = (ViewHolder) convertView.getTag();
            }

            try {
                String url = tenant_matches_listings.get(position).getIcon();
                Glide.with(getApplicationContext() )
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        // .centerCrop()
                        .error(R.drawable.any_emi_launcher)
                        .into(_listView.iv_item);
            } catch (Exception e) {
            }


            try {
                _listView.tv_c_name.setText(tenant_matches_listings.get(position).getUpi_id());
                _listView.textViewOptions.setVisibility(View.GONE);

                _listView.tv_c_name.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onClick(View view) {
                        et_sbi_id.setText(tenant_matches_listings.get(position).getUpi_id());


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder {

            TextView tv_c_name, textViewOptions;
            ImageView iv_item;

        }
    }


    public class ListingAdapter extends BaseAdapter {

        private Context c;
        private LayoutInflater mInflater;
        ViewHolder _listView;
        ArrayList<PaymentChooserModel> tenant_matches_listings;

        public ListingAdapter(Context c, ArrayList<PaymentChooserModel> mListings) {
            this.tenant_matches_listings = mListings;
            this.c = c;
            mInflater = LayoutInflater.from(c);
        }


        @Override
        public int getCount() {
            //return tenant_matches_listings.size();
            return tenant_matches_listings.size();
        }

        @Override
        public Object getItem(int position) {
            return tenant_matches_listings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // final MyAccountListingsResponse2 studentList = tenant_matches_listings.get(position);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.lv_chooser, null, false);
                _listView = new ListingAdapter.ViewHolder();

                _listView.tv_c_name = convertView.findViewById(R.id.tv_c_name);
                _listView.iv_icon = convertView.findViewById(R.id.iv_icon);

                convertView.setTag(_listView);
            } else {
                _listView = (ViewHolder) convertView.getTag();
            }

            try {
                _listView.tv_c_name.setText(tenant_matches_listings.get(position).getApp_name());
                _listView.iv_icon.setBackground(tenant_matches_listings.get(position).getApp_icon());

                _listView.tv_c_name.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onClick(View view) {
                        Intent launchIntent =getPackageManager().getLaunchIntentForPackage(tenant_matches_listings.get(position).getApp_package_name());
                        startActivity(launchIntent);
                        chooserDialog.dismiss();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_c_name, tv_s_number, tv_s_amount, tv_payment_date, tv_service_type;

        }
    }

/*
//  Check valid vpa or not
*/


    private void checkValidVpa() {
        new BackgroundTask(SbiPayPaymentActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.checkValidVpa(SbiPayPaymentActivity.this, prepareCheckValidRequest());
            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {

                    try {

                        CheckValidResponseModel checkValidResponseModel = new CheckValidResponseModel();
                        checkValidResponseModel = gson.fromJson(data.toString(), CheckValidResponseModel.class);

                        if (checkValidResponseModel.getStatus().equals("VE")) {  // VPA VALID, VN INVALID
                            initPaymentRequest();
                        } else {
                            openInfoDialog(checkValidResponseModel.getStatusDesc());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }

//    {
//        "requestinfo": {
//        "pgMerchantid": "SBIOOOOOOOOO",
//                "pspRefNo": "450824145"
//    },
//        "payeeType": {
//        "virtualAddress": "test@hbh"
//    },
//        "vAReqType": "T"
//    }

    private CheackValidVpaModel prepareCheckValidRequest() {

        CheackValidVpaModel vpaModel = new CheackValidVpaModel();
        vpaModel.setPayment_type("SBIUPI");
        vpaModel.setUpi_id(et_sbi_id.getText().toString());
        return vpaModel;

    }


    /*
    // Init Payment Request
     */

    private void initPaymentRequest() {
        new BackgroundTask(SbiPayPaymentActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.postPayment(SbiPayPaymentActivity.this, paymentRequestModel);
            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {
                    SbiPayInitResaponseModel mResponsedata = null;
                    mResponsedata = gson.fromJson(data.toString(), SbiPayInitResaponseModel.class);
                    if (mResponsedata.getApiResp().getStatus().equals("S")) {
                        openInfoDialog("Transaction has been initiated and please approve it from the SBI PSP app");
                        String pspRefNo = mResponsedata.getApiResp().getPspRefNo();
                        float service_list_id = mResponsedata.getService_list_id();
                        String custRefNo = mResponsedata.getApiResp().getCustRefNo();
                        payment_id = String.valueOf(mResponsedata.getPayment_id());
                        try {
                            jsonObject = new JSONObject();
                            jsonObject.put("pspRefNo", pspRefNo);
                            jsonObject.put("service_list_id", service_list_id);
                            jsonObject.put("custRefNo", custRefNo);
                            paymentRequestModel.setPayment_id(payment_id);
                            startTime = Utils.getCurrentTimeStamp();
                            checkPaymentStatus(jsonObject.toString());
                            openPaymentDialog();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (mResponsedata.getApiResp().getStatus().equals("I")) {
                        mTransDone = true;
                        openInfoDialog(mResponsedata.getApiResp().getStatusDesc());
                    } else if (mResponsedata.getApiResp().getStatus().equals("F")) {
                        mTransDone = true;
                        openInfoDialog(mResponsedata.getApiResp().getStatusDesc());
                    }
                } else {
                    Globals.showToast(getApplicationContext(), "Unable Fetch Dat Please Try again later");
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }

    /*
    // Payment Request Status
     */

    private void checkPaymentStatus(final String request) {
        new BackgroundTask(SbiPayPaymentActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.postPaymentTwo(SbiPayPaymentActivity.this, request);
            }

            public void taskCompleted(Object data) {
                if (data != null || data.equals("")) {
                    try {
                        SbiCheckPaymentStatus mResponsedata = null;
                        mResponsedata = gson.fromJson(data.toString(), SbiCheckPaymentStatus.class);

                        //Create delay to check payment status

                        if (mResponsedata.getApiResp().getStatus().equals("P")) {
                            try {
                                if (!mTransDone) {
                                    createDelay(jsonObject.toString());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else if (mResponsedata.getApiResp().getStatus().equals("S")) {
                            mTransDone = true;
                            countDownTimer.cancel();
                            submitPaymentDetails();
                            openInfoDialog("Payment Success");
                        } else if (mResponsedata.getApiResp().getStatus().equals("R")) {
                            mTransDone = true;
                            // openInfoDialog("Payment Rejected By Customer");
                            openInfoDialog(mResponsedata.getApiResp().getStatusDesc());
                        } else if (mResponsedata.getApiResp().getStatus().equals("F")) {
                            mTransDone = true;
                            openInfoDialog(mResponsedata.getApiResp().getStatusDesc() + " : " + mResponsedata.getApiResp().getResponseMsg());
                        } else if (mResponsedata.getApiResp().getStatus().equals("T")) {
                            mTransDone = true;
                            openInfoDialog(mResponsedata.getApiResp().getStatusDesc() + " : " + mResponsedata.getApiResp().getResponseMsg());
                        }
                    } catch (Exception e) {
                        mTransDone = true;
                        e.printStackTrace();
                        Globals.showToast(getApplicationContext(), "Payment Failed");
                        countDownTimer.cancel();
                        btn_pay.setEnabled(true);
                        finish();
                    }

                } else {
                    mTransDone = true;
                    Globals.showToast(getApplicationContext(), "Unable Fetch Dat Please Try again later");
                    btn_pay.setEnabled(true);
                    countDownTimer.cancel();
                    finish();
                }
            }
        }).execute();
    }

    private void createDelay(String s) {
        CountDownTimer countDownTimer = new CountDownTimer(2 * 1000, 2 * 1000) {

            public void onTick(long millisUntilFinished) {
                System.out.print(millisUntilFinished / 1000);
            }

            public void onFinish() {
                checkPaymentStatus(jsonObject.toString());
            }

        }.start();

    }

    //Submit Details to server after succesful trasaction


    private void submitPaymentDetails() {
        new BackgroundTask(SbiPayPaymentActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.submitPayment(SbiPayPaymentActivity.this, finalPaymentrequest());
            }

            public void taskCompleted(Object data) {
                if (data != null || data.equals("")) {
                    if (data.toString().contains("SUCCESS")) {
                        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("FRAGMENT", "COLLECTION");
                        startActivity(intent);
                    }
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }

    private PaymentRequestModel finalPaymentrequest() {

        try {
            paymentRequestModel.setUser_id(Integer.parseInt(SharedPreferenceUtil.getUserId(getApplicationContext())));
            paymentRequestModel.setEmi_ids(ids);
            paymentRequestModel.setPayment_type(Constants.PAYMENT_MODE_PAYTM_SBI_UPI);
            paymentRequestModel.setTotal_amount(amount);
            paymentRequestModel.setBankname("");
            paymentRequestModel.setCheckdate("");
            paymentRequestModel.setRr_number(jsonObject.getString("pspRefNo"));
            paymentRequestModel.setTrsno(jsonObject.getString("custRefNo"));
            paymentRequestModel.setMobile_number(et_phone_num.getText().toString());
            paymentRequestModel.setUpi_id(et_sbi_id.getText().toString());
            paymentRequestModel.setAssessment_id(assessment_num);
            paymentRequestModel.setPayment_id(payment_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentRequestModel;
    }


    private void openInfoDialog(final String s) {

        if (infoDialog == null) {
            infoDialog = new Dialog(SbiPayPaymentActivity.this);
        }

        infoDialog.setContentView(R.layout.dialog_info);
        final TextView tv_info = infoDialog.findViewById(R.id.tv_info);
        final Button btn_send_sms = infoDialog.findViewById(R.id.btn_send_sms);
        final ProgressBar prgs_load = infoDialog.findViewById(R.id.prgs_load);
        if (s.equals("Payment Success")) {
            // Globals.showToast(getApplicationContext(),"hvhvjhvjh potiiii peonnnn");
           // submitPaymentDetails();
        }
        btn_send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                mButtonClicked = false;
                btn_pay.setEnabled(true);
                mTransDone = true;
                infoDialog.dismiss();
//                if (s.equals("Payment Success")) {
//                   // Globals.showToast(getApplicationContext(),"hvhvjhvjh potiiii peonnnn");
//                   // submitPaymentDetails();
//                }
            }
        });

        if (s.equals("Payment Success")) {
            infoDialog.setCancelable(false);
            btn_send_sms.setText("Print Receipt");
        }


        if (s.equals("Transaction has been initiated and please approve it from the SBI PSP app")) {

            infoDialog.setCancelable(false);
            prgs_load.setVisibility(View.VISIBLE);
            btn_send_sms.setText("Cancel Request");
            btn_send_sms.setVisibility(View.GONE);

            countDownTimer = new CountDownTimer(600000, 1000) {
                public void onTick(long millisUntilFinished) {
                    tv_info.setText(s + "\n  \n Time Remaining to complete Transaction is " + String.format("%d : %d ",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }

                public void onFinish() {
                    mTransDone = true;
                    mButtonClicked = false;
                    btn_pay.setEnabled(true);
                    prgs_load.setVisibility(View.GONE);
                    btn_send_sms.setVisibility(View.VISIBLE);
                    btn_send_sms.setText("Retry");
                    //infoDialog.dismiss();
                    tv_info.setText("Transaction Expired");
                }
            }.start();
        }
        tv_info.setText(s);
        infoDialog.show();
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
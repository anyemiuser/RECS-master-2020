package com.anyemi.recska.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anyemi.recska.LoginActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.RegisterModel;
import com.anyemi.recska.model.VerifyOtpModel;
import com.anyemi.recska.popups.SelectionDialog;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by SuryaTejaChalla on 12-02-2018.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    //ACTION BAR UI COMPONENTS

    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;

    //UI COMPONENTS

    Button btn_pay,btn_logi;
    EditText et_phone_num, et_name, et_user_name, et_password, et_email, et_conform_password, et_otp;
    TextInputLayout til_name, til_email, til_user_name, til_phone_id, til_conform_password, til_password, til_otp;
    TextView tv_u_id, tv_resend_otp;
    Dialog dialog;

    JSONObject jsonObject;
    Gson gson = new Gson();
    CountDownTimer countDownTimer;
    String mOtp;

    Dialog chooserDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Window window = this.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(RegisterActivity.this, R.color.colorPrimaryDark));
        }
        createActionBar();
        initializeView();
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
        aTitle.setText("User Registration");

    }


    private void initializeView() {

        btn_pay = findViewById(R.id.btn_pay);
        btn_logi = (Button) findViewById(R.id.btn_logi);
        tv_u_id = findViewById(R.id.tv_u_id);
        tv_resend_otp = findViewById(R.id.tv_resend_otp);

        et_name = findViewById(R.id.et_name);
        et_user_name = findViewById(R.id.et_user_name);
        et_password = findViewById(R.id.et_password);
        et_email = findViewById(R.id.et_email);
        et_conform_password = findViewById(R.id.et_conform_password);
        et_phone_num = findViewById(R.id.et_phone_num);
        et_otp = findViewById(R.id.et_otp);

        til_name = findViewById(R.id.til_name);
        til_email = findViewById(R.id.til_email);
        til_user_name = findViewById(R.id.til_user_name);
        til_name = findViewById(R.id.til_name);
        til_password = findViewById(R.id.til_password);
        til_conform_password = findViewById(R.id.til_conform_password);
        til_phone_id = findViewById(R.id.til_phone_id);
        til_otp = findViewById(R.id.til_otp);


        btn_logi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_name.addTextChangedListener(this);
        et_user_name.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_conform_password.addTextChangedListener(this);
        et_phone_num.addTextChangedListener(this);
        et_email.addTextChangedListener(this);

        btn_pay.setText("Register");
        btn_pay.setOnClickListener(this);
        tv_resend_otp.setOnClickListener(this);
        //et_user_name.setEnabled(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                if (performValidation() && btn_pay.getText().toString().equals("Register")) {
                    register();
                } else if (btn_pay.getText().toString().equals("Submit Otp")) {
                    openTransactionDialog(mOtp);
                }
                break;

            case R.id.tv_resend_otp:
                resendOtp();
        }
    }

    private void resendOtp() {
        new BackgroundTask(RegisterActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.reSendOtp(RegisterActivity.this, prepareResendModel());
            }

            public void taskCompleted(Object data) {
                if (data != null || data.equals("")) {
                    try {
                        Gson gson = new Gson();
                        RegisterModel registerModel = new RegisterModel();
                        registerModel = gson.fromJson(data.toString(), RegisterModel.class);
                        if (registerModel.getStatus().equals("sucess")) {
                            mOtp = String.valueOf(registerModel.getOtp());
                            btn_pay.setText("Submit Otp");
                            til_otp.setVisibility(View.VISIBLE);
                            tv_resend_otp.setVisibility(View.VISIBLE);
                            tv_resend_otp.setEnabled(false);
                            displayTimer();
                            createDelay(jsonObject.toString());
                        } else {
                            Globals.showToast(getApplicationContext(), "Unable Resend Otp Please Try again later");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Globals.showToast(getApplicationContext(), "Unable Fetch Data Please Try again later");
                }

            }
        },

                getString(R.string.loading_txt)).

                execute();

    }

    private void displayTimer() {

        countDownTimer = new CountDownTimer(1 * 60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                tv_resend_otp.setText("Resend Otp in " + String.format("%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                tv_resend_otp.setText("Resend Otp");
                tv_resend_otp.setEnabled(true);
            }
        }.start();
    }

    private void register() {
        new BackgroundTask(RegisterActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.register(RegisterActivity.this, prepareRegisterRequest());
            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {

                    Gson gson = new Gson();
                    RegisterModel registerModel = new RegisterModel();
                    registerModel = gson.fromJson(data.toString(), RegisterModel.class);
                    if (registerModel.getStatus().equals("sucess")) {
                        mOtp = String.valueOf(registerModel.getOtp());
                        btn_pay.setText("Submit Otp");
                        til_otp.setVisibility(View.VISIBLE);
                        tv_resend_otp.setVisibility(View.VISIBLE);
                        tv_resend_otp.setEnabled(false);
                        displayTimer();
                        createDelay(jsonObject.toString());

                    } else {
                        Globals.showToast(getApplicationContext(), "User Already exists");
                    }

                } else {
                    Globals.showToast(getApplicationContext(), "Unable Fetch Dat Please Try again later");
                }
            }
        }, getString(R.string.loading_txt)).execute();

    }


    private String prepareRegisterRequest() {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("name", et_name.getText().toString());
            jsonObject.put("user_name", et_phone_num.getText().toString());
            jsonObject.put("password", et_password.getText().toString());
            jsonObject.put("mobile_number", et_phone_num.getText().toString());
            jsonObject.put("email", et_email.getText().toString());
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    private String prepareResendModel() {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("mobile_number", et_phone_num.getText().toString());
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void createDelay(String s) {
        CountDownTimer countDownTimer = new CountDownTimer(1 * 60 * 1000, 2 * 1000) {

            public void onTick(long millisUntilFinished) {
                System.out.print(millisUntilFinished / 1000);
            }

            public void onFinish() {
                tv_resend_otp.setVisibility(View.VISIBLE);
                tv_resend_otp.setText("Resend Otp");
            }

        }.start();

    }


    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("OTP"));
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

            if (intent.getAction().equalsIgnoreCase("OTP")) {
                final String message = intent.getStringExtra("id");
                et_otp.setText(message);
                verifyOtp(et_otp.getText().toString());
                // dialog.dismiss();
            }
        }
    };


    private void openTransactionDialog(final String otp) {

        if (et_otp.getText().toString().equals("")) {
            til_otp.setError("Please enter Otp");
            et_otp.requestFocus();
        } else if (et_otp.getText().toString().equals(otp)) {
            verifyOtp(otp);
        } else {
            Globals.showToast(getApplicationContext(), "Please enter correct Otp");
        }

    }


    private void verifyOtp(final String otp) {
        new BackgroundTask(RegisterActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.verifyOtp(RegisterActivity.this, otpRequest(String.valueOf(otp)));
            }

            public void taskCompleted(Object data) {


                if (data != null || data.equals("")) {

                    VerifyOtpModel verifyOtpModel = new VerifyOtpModel();
                    verifyOtpModel = new Gson().fromJson(data.toString(), VerifyOtpModel.class);

                    // openChooserDialog(verifyOtpModel);
                    if (verifyOtpModel.getStatus().equals("success")) {
                        Globals.showToast(getApplicationContext(), verifyOtpModel.getMsg());
                        if (verifyOtpModel.getUser_vpas().size() > 0) {
                            Globals.showToast(getApplicationContext(), verifyOtpModel.getMsg());
                            SelectionDialog registerloginPopup = new SelectionDialog(RegisterActivity.this);
                            registerloginPopup.showDialog(verifyOtpModel, verifyOtpModel.getUser_vpas());
                        } else {
                            Globals.showToast(getApplicationContext(), verifyOtpModel.getMsg());
                            finish();
                        }
                    } else {
                        Globals.showToast(getApplicationContext(), verifyOtpModel.getMsg());
                    }

                } else {
                    Globals.showToast(getApplicationContext(), "Unable Fetch Data Please Try again later");
                }
            }
        }, getString(R.string.loading_txt)).execute();

    }





    private String otpRequest(String s) {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("otp", s);
            //jsonObject.put("otp", "3566");
            jsonObject.put("mobile_number", et_phone_num.getText().toString());
          //  jsonObject.put("mobile_number", "8008621100");
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public void sumit(final String req) {
        new BackgroundTask(RegisterActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.addMultiVpa(RegisterActivity.this, req);
            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {

                    finish();
                    Globals.showToast(getApplicationContext(), "Saved Sucessfully");
                } else {
                    Globals.showToast(getApplicationContext(), "Error Occured Please Try again later");

                }
            }
        }, getString(R.string.loading_txt)).execute();

    }

    private void openChooserDialog(VerifyOtpModel verifyOtpModel) {

        if (chooserDialog == null) {
            chooserDialog = new Dialog(RegisterActivity.this);
        }

        chooserDialog.setCanceledOnTouchOutside(false);

        chooserDialog.setContentView(R.layout.fragment_collection_layout);
        final ListView lv_my_account = (ListView) chooserDialog.findViewById(R.id.lv_collection);


        ListingAdapter mAdapter;

        mAdapter = new ListingAdapter(getApplicationContext(), verifyOtpModel.getUser_vpas());
        lv_my_account.setAdapter(mAdapter);

        chooserDialog.setTitle("Select Vpa To Save");
        chooserDialog.show();


    }


    public class ListingAdapter extends BaseAdapter {

        private Context c;
        private LayoutInflater mInflater;
        ViewHolder _listView;
        ArrayList<VerifyOtpModel.UserVpasBean> tenant_matches_listings;

        public ListingAdapter(Context c, ArrayList<VerifyOtpModel.UserVpasBean> mListings) {
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

                _listView.tv_c_name = (TextView) convertView.findViewById(R.id.tv_c_name);
                _listView.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);

                convertView.setTag(_listView);
            } else {
                _listView = (ViewHolder) convertView.getTag();
            }

            try {
                _listView.tv_c_name.setText(tenant_matches_listings.get(position).getVpa());
//                _listView.iv_icon.setBackground(tenant_matches_listings.get(position).getApp_icon());

                _listView.tv_c_name.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onClick(View view) {
//                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(tenant_matches_listings.get(position).getApp_package_name());
//                        startActivity(launchIntent);
//                        chooserDialog.dismiss();

//                        submitPayment();


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
   // Validations
    */

    private boolean performValidation() {
        boolean isValid = false;

        if (et_name.getText().toString().equals("")) {
            til_name.setError("Please enter Name");
            et_name.requestFocus();
        } else if (et_email.getText().toString().length() > 0 && !Utils.isEmailValid(et_email.getText().toString())) {
            til_email.setError("Enter valid Email Id");
            et_email.requestFocus();
        } else if (et_phone_num.getText().toString().equals("")) {
            til_phone_id.setError("Enter your Mobile Number");
            et_phone_num.requestFocus();
        } else if (et_phone_num.getText().toString().length() != 10) {
            til_phone_id.setError("Invalid Mobile Number");
            et_phone_num.requestFocus();
        } else if (tv_u_id.getText().toString().equals("")) {
            til_user_name.setError("Please enter UserName");
            et_user_name.requestFocus();
        } else if (et_password.getText().toString().equals("")) {
            til_password.setError("Please enter Password");
            et_password.requestFocus();
        } else if (et_password.getText().toString().length() <= 5) {
            til_password.setError("Please choose a password with at least 6 characters.");
            et_password.requestFocus();
        } else if (et_conform_password.getText().toString().equals("")) {
            til_conform_password.setError("Please Reenter Password");
            et_conform_password.requestFocus();
        } else if (!et_conform_password.getText().toString().equals(et_password.getText().toString())) {
            til_conform_password.setError("Password and Confirm password doesn't Match");
            et_conform_password.requestFocus();
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

        if (et_name.hasFocus() && et_name.getText().toString().length() > 0) {
            til_name.setErrorEnabled(false);
            til_name.setError(null);
        } else if (et_email.hasFocus() && et_email.getText().toString().length() > 0) {
            til_email.setErrorEnabled(false);
            til_email.setError(null);
        } else if (et_phone_num.hasFocus() && et_phone_num.getText().toString().length() > 0) {
            til_phone_id.setErrorEnabled(false);
            til_phone_id.setError(null);
            tv_u_id.setText(String.valueOf(charSequence));

        } else if (et_user_name.hasFocus() && et_user_name.getText().toString().length() > 0) {
            til_user_name.setErrorEnabled(false);
            til_user_name.setError(null);
        } else if (et_password.hasFocus() && et_password.getText().toString().length() > 0) {
            til_password.setErrorEnabled(false);
            til_password.setError(null);
        } else if (et_conform_password.hasFocus() && et_conform_password.getText().toString().length() > 0) {
            til_conform_password.setErrorEnabled(false);
            til_conform_password.setError(null);
        } else if (et_otp.hasFocus() && et_otp.getText().toString().length() > 0) {
            til_otp.setErrorEnabled(false);
            til_otp.setError(null);
        }

        if (et_phone_num.getText().toString().length() <= 0) {
            tv_u_id.setText("");
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



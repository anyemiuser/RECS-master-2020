package com.anyemi.recska.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.anyemi.recska.R;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.AddVpaModel;
import com.anyemi.recska.model.CheackValidVpaModel;
import com.anyemi.recska.model.CheckValidResponseModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

public class AddUPIActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    //ACTION BAR UI COMPONENTS

    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;

    //UI COMPONENTS

    Button btn_pay;
    EditText et_sbi_id;
    TextInputLayout til_sbi_id;
    TextView tv_name;
    Gson gson = new Gson();


    // VARIBLES


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_upi_activity);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(AddUPIActivity.this, R.color.colorPrimaryDark));
        }

        createActionBar();

        /*
        //Getting Parameters from intent
         */

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
        aTitle.setText("Add New UPI");

    }


    private void initializeView() {

        btn_pay = findViewById(R.id.btn_pay);
        tv_name = findViewById(R.id.tv_name);
        et_sbi_id = findViewById(R.id.et_sbi_id);
        til_sbi_id = findViewById(R.id.til_sbi_id);
        btn_pay.setText("Check UPI ID");
        btn_pay.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                if (performValidation()) {
                    if (btn_pay.getText().toString().equals("Check UPI ID")) {
                        checkValidVpa();
                    } else {
                        addVpa();
                    }

                }
                break;


        }
    }


    private boolean performValidation() {
        boolean isValid = false;

        if (et_sbi_id.getText().toString().equals("") || et_sbi_id.getText().toString().contains(" ")) {
            til_sbi_id.setError("Please enter a valid VPA");
            et_sbi_id.requestFocus();
        } else if (!et_sbi_id.getText().toString().contains("@")) {
            til_sbi_id.setError("Please enter a valid VPA");
            et_sbi_id.requestFocus();
        } else if (et_sbi_id.getText().toString().length() > 255) {
            til_sbi_id.setError("More than 255 character are not allowed");
            et_sbi_id.requestFocus();
        } else {
            isValid = true;
        }
        return isValid;
    }


    private void checkValidVpa() {
        new BackgroundTask(AddUPIActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.checkValidVpa(AddUPIActivity.this, prepareCheckValidRequest());
            }

            public void taskCompleted(Object data) {
                if (data != null || data.equals("")) {
                    try {
                        CheckValidResponseModel checkValidResponseModel = new CheckValidResponseModel();
                        checkValidResponseModel = gson.fromJson(data.toString(), CheckValidResponseModel.class);

                        if (checkValidResponseModel.getStatus().equals("VE")) {  // VE > VPA VALID, VN > VPA INVALID
                            String name = checkValidResponseModel.getPayeeType().getName();
                            tv_name.setText(name);
                            btn_pay.setText("Add Upi Id");
                        } else {
                            Globals.showToast(getApplicationContext(), checkValidResponseModel.getStatusDesc());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }


    private CheackValidVpaModel prepareCheckValidRequest() {
        CheackValidVpaModel vpaModel = new CheackValidVpaModel();
        vpaModel.setPayment_type(Constants.PAYMENT_MODE_PAYTM_SBI_UPI);
        vpaModel.setPayment_type("SBIUPI");
        vpaModel.setUpi_id(et_sbi_id.getText().toString());
        return vpaModel;

    }

    private void addVpa() {
        new BackgroundTask(AddUPIActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.addVpa(AddUPIActivity.this, prepareAddVpaRequest());
            }

            public void taskCompleted(Object data) {
                if (data != null || data.equals("")) {
                    try {
                        AddVpaModel addVpaModel = new AddVpaModel();
                        addVpaModel = gson.fromJson(data.toString(), AddVpaModel.class);

                        Globals.showToast(getApplicationContext(), addVpaModel.getMsg());
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }

    private String prepareAddVpaRequest() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("upi_id", et_sbi_id.getText().toString());
            jsonObject.put("user_id", SharedPreferenceUtil.getUserId(getApplicationContext()));
            jsonObject.put("primary", "0");
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


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



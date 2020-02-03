package com.anyemi.recska.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.anyemi.recska.NavigationActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.BanksModel;
import com.anyemi.recska.model.BranchModel;
import com.anyemi.recska.model.PaymentRequestModel;
import com.anyemi.recska.utils.Globals;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by SuryaTejaChalla on 07-02-2018.
 * To do Check transactions
 */

public class CheckActivity extends AppCompatActivity implements TextWatcher {

    //ACTION BAR UI COMPONENTS

    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;

    //PAYMENT DETAILS

    String paymentData;
    PaymentRequestModel paymentRequestModel;

    //UI COMPONENTS

    Button proceed;
    TextView tv_amount;
    TextView et_date;

    DatePickerDialog datePickerDialog;
    Dialog dialog;
    Calendar calendar = Calendar.getInstance();

    EditText et_cheque_num, et_phone_num;

    Spinner spnr_bank, spnr_branch;


    TextInputLayout til_cheque_num, til_phone_id, til_date;

    //DATA VARIBLES

    ArrayList<String> bankNames = new ArrayList<>();
    ArrayList<String> bankIds = new ArrayList<>();
    ArrayList<String> branchNames = new ArrayList<>();
    ArrayList<String> branchIds = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(CheckActivity.this, R.color.colorPrimaryDark));
        }

        try {
            Bundle parametros = getIntent().getExtras();
            paymentData = parametros.getString(Constants.PAYMENT_REQUEST_MODEL);
            Gson gson = new Gson();
            paymentRequestModel = gson.fromJson(paymentData, PaymentRequestModel.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        createActionBar();
        initView2();
        getBanks();
    }

    private void initView2() {

        tv_amount = findViewById(R.id.tv_amount);
        proceed = findViewById(R.id.btn_pay);

        et_cheque_num = findViewById(R.id.et_cheque_num);
        et_phone_num = findViewById(R.id.et_phone_num);
        et_date = findViewById(R.id.et_date);

        til_cheque_num = findViewById(R.id.til_cheque_num);
        til_phone_id = findViewById(R.id.til_phone_id);
        til_date = findViewById(R.id.til_date);

        spnr_bank = findViewById(R.id.spnr_bank);
        spnr_branch = findViewById(R.id.spnr_branch);


        et_cheque_num.addTextChangedListener(this);
        et_phone_num.addTextChangedListener(this);

        proceed.setText("Proceed Payment");
        String resultStr = String.valueOf(paymentRequestModel.getTotal_amount());
        DecimalFormat df = new DecimalFormat("#,##,###.####", new DecimalFormatSymbols(Locale.US));
        resultStr = df.format(Double.valueOf(resultStr));
        tv_amount.setText("Rs." + resultStr + " /-");


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (performValidation()) {
                    paymentRequestModel.setPayment_type(Constants.PAYMENT_MODE_CHEQUE);
                    paymentRequestModel.setCheckno(Integer.parseInt(et_cheque_num.getText().toString()));
                    paymentRequestModel.setMobile_number(et_phone_num.getText().toString());
                    paymentRequestModel.setBankname(spnr_bank.getSelectedItem().toString());
                    paymentRequestModel.setBranch(spnr_branch.getSelectedItem().toString());

                    //paymentRequestModel.setBankname(bankIds.get(spnr_bank.getSelectedItemPosition()));
                    //paymentRequestModel.setBranch(branchIds.get(spnr_branch.getSelectedItemPosition()));
                    paymentRequestModel.setCheckdate(et_date.getText().toString());
                    submitPayment();
                }
            }
        });
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialog = new DatePickerDialog(CheckActivity.this, listener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();

            }
        });


        bankNames.addAll(Arrays.asList(getResources().getStringArray(R.array.bank_names)));
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, bankNames);
        spnr_bank.setAdapter(regionAdapter);


        branchNames.addAll(Arrays.asList(getResources().getStringArray(R.array.branch_names)));
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, branchNames);
        spnr_branch.setAdapter(stateAdapter);


        spnr_bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position != 0) {
                    spnr_branch.setSelection(0);
                    getBranches(bankIds.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

            et_date.setText("" + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
        }
    };


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


    private void submitPayment() {

        new BackgroundTask(CheckActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.submitPayment(CheckActivity.this, paymentRequestModel);
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
        // LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("Wallet txn id:"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //   LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            if (intent.getAction().equalsIgnoreCase("Wallet txn id:")) {
//                final String message = intent.getStringExtra("id");
//                // Globals.showToast(getApplicationContext(),message);
//                et_phone_num.setText(message);
//                dialog.dismiss();
//
//            }
//        }
//    };


    private boolean performValidation() {
        boolean isValid = false;

        if (et_cheque_num.getText().toString().equals("")) {
            til_cheque_num.setError("Please enter Cheque Number");
            et_cheque_num.requestFocus();
        } else if (et_cheque_num.getText().toString().length() != 6) {
            til_cheque_num.setError("Cheque Number should be 6 digit");
            et_cheque_num.requestFocus();
        } else if (et_phone_num.getText().toString().equals("")) {
            til_phone_id.setError("Enter your Mobile Number");
            et_phone_num.requestFocus();
        } else if (et_phone_num.getText().toString().length() != 10) {
            til_phone_id.setError("Invalid Mobile Number");
            et_phone_num.requestFocus();
        } else if (spnr_bank.getSelectedItemPosition() == 0) {
            Globals.showToast(getApplicationContext(), "Please select Bank");
        } else if (spnr_branch.getSelectedItemPosition() == 0) {
            Globals.showToast(getApplicationContext(), "Please select BranchModel");
        } else if (et_date.getText().toString().equals("")) {
            Globals.showToast(getApplicationContext(), "Cheack Date should not be empty");
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

        if (et_cheque_num.hasFocus() && et_cheque_num.getText().toString().length() > 0) {
            til_cheque_num.setErrorEnabled(false);
            til_cheque_num.setError(null);
        } else if (et_phone_num.hasFocus() && et_phone_num.getText().toString().length() > 0) {
            til_phone_id.setErrorEnabled(false);
            til_phone_id.setError(null);
        }


    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


//Service Calls


    private void getBranches(final String regionId) {
        new BackgroundTask(getApplicationContext(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.getBranches(getApplicationContext(), buildLoginRequest(regionId));
            }

            public void taskCompleted(Object data) {
                if (!data.toString().equals("")) {

                    Type listType = new TypeToken<List<BranchModel>>() {}.getType();
                    List<BranchModel> states = new Gson().fromJson(data.toString(), listType);
                    branchNames.clear();
                    branchIds.clear();
                    try {
                        branchNames.add("Select BranchModel");
                        branchIds.add("0");
                        for (int i = 0; i < states.size(); i++) {
                            branchNames.add(states.get(i).getBranch_name());
                            branchIds.add(states.get(i).getBranch_id());
                        }
                    } catch (Exception e) {
                        branchNames.addAll(Arrays.asList(getResources().getStringArray(R.array.branch_names)));
                    }
//                    stateList.addAll(Arrays.asList(getResources().getStringArray(R.array.states_array)));
                    ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, branchNames);
                    spnr_branch.setAdapter(stateAdapter);
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }


    public String buildLoginRequest(String id) {
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("bank_id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject.toString();
    }

    private void getBanks() {
        new BackgroundTask(getApplicationContext(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.getBanks(getApplicationContext());
            }

            public void taskCompleted(Object data) {
                if (!data.toString().equals("")) {
                    Type listType = new TypeToken<List<BanksModel>>() {
                    }.getType();
                    List<BanksModel> cities = new Gson().fromJson(data.toString(), listType);
                    bankNames.clear();
                    bankIds.clear();
                    try {
                        bankNames.add("Select Bank");
                        bankIds.add("0");
                        for (int i = 0; i < cities.size(); i++) {
                            bankNames.add(cities.get(i).getName());
                            bankIds.add(cities.get(i).getId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, bankNames);
                    spnr_bank.setAdapter(stateAdapter);
                }
            }
        }
                , getString(R.string.loading_txt)).execute();
    }


//Back Button

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
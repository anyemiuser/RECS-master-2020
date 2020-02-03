package com.anyemi.recska.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agsindia.mpos_integration.models.LogUtils;
import com.anyemi.recska.NavigationActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.connection.HttpClient1;
import com.anyemi.recska.model.PaymentChooserModel;
import com.anyemi.recska.model.PaymentRequestModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.anyemi.recska.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by SuryaTejaChalla on 07-02-2018.
 */

public class CreditAndDebitCardActivity extends AppCompatActivity implements View.OnClickListener {


    Intent intent = null;

    Gson gson = new Gson();

    Double AMOUNT_WITHOUT_CHARGES;


    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;

    String data;

    PaymentRequestModel paymentRequestModel;

    LinearLayout ll_debit, ll_credit, ll_bank_charges;

    CheckBox chbk_debit, chbk_credit;

    TextView tv_amount, tv_bank_charges, tv_gst_charges;

    Button proceed;
    EditText et_phone_num;
    Dialog dialog;


    String selection_amount;


    Dialog chooserDialog;
    private String sessionkey;
    private String BlueTooth_Address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_credit_debit);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(CreditAndDebitCardActivity.this, R.color.colorPrimaryDark));
        }
        createActionBar();
        //initView();s
        initView2();

        try {

            paymentRequestModel = Globals.getPaymentRequestModes(getIntent().getExtras());

            if (paymentRequestModel == null) {
                Globals.showToast(getApplicationContext(), Constants.PAYMENT_REQ_ERROR);
                finish();
            }


            selection_amount = paymentRequestModel.getTotal_amount();
            String resultStr = Utils.parseAmount(paymentRequestModel.getTotal_amount());
            AMOUNT_WITHOUT_CHARGES = Double.parseDouble(paymentRequestModel.getTotal_amount());
            tv_amount.setText("Rs." + resultStr + " /-");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView2() {

        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_bank_charges = (TextView) findViewById(R.id.tv_bank_charges);
        tv_gst_charges = (TextView) findViewById(R.id.tv_gst_charges);

        ll_debit = (LinearLayout) findViewById(R.id.ll_debit);
        ll_credit = (LinearLayout) findViewById(R.id.ll_credit);
        //  ll_bank_charges = (LinearLayout) findViewById(R.id.ll_bank_charges);
        chbk_credit = (CheckBox) findViewById(R.id.chbk_credit);
        chbk_debit = (CheckBox) findViewById(R.id.chbk_debit);
        proceed = (Button) findViewById(R.id.btn_procced);


        proceed.setOnClickListener(this);
        chbk_credit.setOnClickListener(this);
        chbk_debit.setOnClickListener(this);
        ll_credit.setOnClickListener(this);
        ll_debit.setOnClickListener(this);

    }


    public void getSelectedPayment(String payment_mode) {

        if (payment_mode.equals(Constants.PAYMENT_MODE_CREDIT_CARD)) {

            paymentRequestModel.setTotal_amount(calculateCharges(Constants.PAYMENT_MODE_CREDIT_CARD));
            paymentRequestModel.setPayment_type(Constants.PAYMENT_MODE_CREDIT_CARD);
            chbk_credit.setChecked(true);
            chbk_debit.setChecked(false);

        } else if (payment_mode.equals(Constants.PAYMENT_MODE_DEBIT_CARD)) {
            paymentRequestModel.setPayment_type(Constants.PAYMENT_MODE_DEBIT_CARD);
            paymentRequestModel.setTotal_amount(calculateCharges(Constants.PAYMENT_MODE_DEBIT_CARD));
            chbk_credit.setChecked(false);
            chbk_debit.setChecked(true);
        }

//        if (SharedPreferenceUtil.getPrintHeader(getApplicationContext()).equals(Constants.PRINT_HEADER_GVMC)) {
//            paymentRequestModel.setTotal_amount(Utils.parseTwoDigitAmount(String.valueOf(AMOUNT_WITHOUT_CHARGES)));
//            tv_amount.setText(Utils.parseAmount(String.valueOf(AMOUNT_WITHOUT_CHARGES)));
//            tv_amount.setText("Rs." + Utils.parseAmount(String.valueOf(AMOUNT_WITHOUT_CHARGES)) + " /-");
//        }
    }


    private String calculateCharges(String payment_mode) {

        if (payment_mode.equals(Constants.PAYMENT_MODE_CREDIT_CARD)) {

            Double due_amount = AMOUNT_WITHOUT_CHARGES;

            Double card_charges = Double.parseDouble(paymentRequestModel.getCredit_service_tax_());
            Double gst_on_card_charges = Double.parseDouble(paymentRequestModel.getGst_credit());

            card_charges = (card_charges * due_amount) / 100;
            gst_on_card_charges = (card_charges * gst_on_card_charges) / 100;

            Double final_amount = 0.0;

            if (gst_on_card_charges > 0) {
                final_amount = card_charges + due_amount + gst_on_card_charges;
            } else {
                final_amount = Double.valueOf(due_amount);
            }
            String bank_charges = Utils.parseTwoDigitAmount(String.valueOf(card_charges + gst_on_card_charges));

            paymentRequestModel.setBankCharges(bank_charges);

            String resultStr = Utils.parseTwoDigitAmount(String.valueOf(Math.round(final_amount)));

            tv_bank_charges.setText("Rs." + Utils.parseAmount(String.valueOf(card_charges)) + " /-");
            tv_gst_charges.setText("Rs." + Utils.parseAmount(String.valueOf(gst_on_card_charges)) + " /-");
            tv_amount.setText("Rs." + Utils.parseAmount(String.valueOf(final_amount)) + " /-");

            return resultStr;

        } else {
            Double due_amount = AMOUNT_WITHOUT_CHARGES;


            Double card_charges = Double.parseDouble(paymentRequestModel.getDebit_service_tax_());
            Double gst_on_card_charges = Double.parseDouble(paymentRequestModel.getGst_debit());

            card_charges = (card_charges * due_amount) / 100;
            gst_on_card_charges = (card_charges * gst_on_card_charges) / 100;

            Double final_amount = 0.0;

            if (gst_on_card_charges > 0) {
                final_amount = card_charges + due_amount + gst_on_card_charges;
            } else {
                final_amount = Double.valueOf(due_amount);
            }
            String bank_charges = Utils.parseTwoDigitAmount(String.valueOf(card_charges + gst_on_card_charges));

            paymentRequestModel.setBankCharges(bank_charges);

            String resultStr = Utils.parseTwoDigitAmount(String.valueOf(Math.round(final_amount)));

            tv_bank_charges.setText("Rs." + Utils.parseAmount(String.valueOf(card_charges)) + " /-");
            tv_gst_charges.setText("Rs." + Utils.parseAmount(String.valueOf(gst_on_card_charges)) + " /-");
            tv_amount.setText("Rs." + Utils.parseAmount(String.valueOf(final_amount)) + " /-");

            return resultStr;
        }
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
        aTitle.setText("Choose payment Method");

    }


    private void submitPayment() {

        new BackgroundTask(CreditAndDebitCardActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.submitPayment(CreditAndDebitCardActivity.this, paymentRequestModel);
            }

            public void taskCompleted(Object data) {
                if (data != null || data.equals("")) {
                    if (data.toString().contains("SUCCESS")) {
                        if (data.toString().contains("SUCCESS")) {
                            Globals.showToast(getApplicationContext(), "Payment Successful");
                            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("FRAGMENT", "COLLECTION");
                            startActivity(intent);
                        }
                    }
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_credit:
                getSelectedPayment(Constants.PAYMENT_MODE_CREDIT_CARD);
                break;

            case R.id.ll_debit:
                getSelectedPayment(Constants.PAYMENT_MODE_DEBIT_CARD);
                break;

            case R.id.chbk_credit:
                getSelectedPayment(Constants.PAYMENT_MODE_CREDIT_CARD);
                break;

            case R.id.chbk_debit:
                getSelectedPayment(Constants.PAYMENT_MODE_DEBIT_CARD);
                break;

            case R.id.btn_procced:
                if (chbk_debit.isChecked() || chbk_credit.isChecked()) {
                    openChooserDialog();
                } else {
                    Globals.showToast(getApplicationContext(), "Please Select Payment Mode");
                }

                break;

        }

    }


    private void openChooserDialog() {

        if (chooserDialog == null) {
            chooserDialog = new Dialog(CreditAndDebitCardActivity.this);
        }

        chooserDialog.setCanceledOnTouchOutside(false);

        chooserDialog.setContentView(R.layout.fragment_collection_layout);
        final ListView lv_my_account = (ListView) chooserDialog.findViewById(R.id.lv_collection);


        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();


        ArrayList<PaymentChooserModel> s = new ArrayList<PaymentChooserModel>();
        for (BluetoothDevice bt : pairedDevices) {
            PaymentChooserModel paymentChooserModel = new PaymentChooserModel();
            paymentChooserModel.setApp_name(bt.getName());
            paymentChooserModel.setApp_package_name(bt.getAddress());
            s.add(paymentChooserModel);
        }


        ListingAdapter mAdapter;

        mAdapter = new ListingAdapter(getApplicationContext(), s);
        lv_my_account.setAdapter(mAdapter);

        chooserDialog.setTitle("Choose Device");
        chooserDialog.show();

        if (s.size() == 0) {
            Globals.showToast(getApplicationContext(), "No BlueTooth Devices Avaiable");
            chooserDialog.dismiss();
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

                _listView.tv_c_name = (TextView) convertView.findViewById(R.id.tv_c_name);
                _listView.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);

                convertView.setTag(_listView);
            } else {
                _listView = (ViewHolder) convertView.getTag();
            }

            try {
                _listView.tv_c_name.setText(tenant_matches_listings.get(position).getApp_name());
//                _listView.iv_icon.setBackground(tenant_matches_listings.get(position).getApp_icon());

                _listView.tv_c_name.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onClick(View view) {
//                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(tenant_matches_listings.get(position).getApp_package_name());
//                        startActivity(launchIntent);
//                        chooserDialog.dismiss();

//                        submitPayment();
                        BlueTooth_Address = tenant_matches_listings.get(position).getApp_package_name();


                        new GetSessionKey().execute();


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


    private class GetSessionKey extends AsyncTask<Void, Void, String> {
        //        String card_tran_type="mPOS_"+tran_card_type;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CreditAndDebitCardActivity.this, AlertDialog.THEME_TRADITIONAL);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
//            dialog.setMessage("Getting session key");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {

                HttpClient1 hp = new HttpClient1();
                String terminal_id = SharedPreferenceUtil.getMPOSID(getApplicationContext());
                String merchant_id = "130000000018397";
//
//                LogUtils.d("Session key Request", terminal_id + "|" + merchant_id
//                        + "|" + "ezswype_mPOS_SWIPE"
//                        + "|" + getResources().getString(R.string.version));
                sessionkey = hp.getDataFromUrl(terminal_id + "|" + merchant_id + "|" +
                                "ezswype_mPOS_SWIPE"
                                + "|" + getResources().getString(R.string.version),
                        "getSessionKey", CreditAndDebitCardActivity.this);

//                LogUtils.d("Session Key Response: ", "> " + sessionkey);


                return sessionkey;
            } catch (Exception e) {
                e.printStackTrace();
                return "No Session Key";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            LogUtils.d("justbilling", "Result:" + sessionkey); //

            dialog.dismiss();

            getSessionKey(BlueTooth_Address);
        }
    }


    private void getSessionKey(String bt_name) {

        String terminal_id = SharedPreferenceUtil.getMPOSID(getApplicationContext());
        String device_name = SharedPreferenceUtil.getMPOSUID(getApplicationContext());

        Intent intent = new Intent(CreditAndDebitCardActivity.this
                , com.agsindia.mpos_integration.baseActivity.BaseActivity.class
        );
        intent.putExtra("serviceType", "sale");
        intent.putExtra("merchantId", "130000000018397");
        intent.putExtra("terminalId", terminal_id);
        intent.putExtra("className", "com.agsindia.mpos_integration.ActivitySwipe");
        intent.putExtra("bt_address", bt_name);
        intent.putExtra("bt_name", device_name);
        intent.putExtra("amount", paymentRequestModel.getTotal_amount());
        intent.putExtra("sessionkey", sessionkey);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {


            switch (requestCode) {


                case 1:
                    String strData = "";
                    String callFrom = "sale";
                    try {
                        //Toast.makeText(InstamojoActivity.this, "Output is:" + data.getStringExtra("Result"), Toast.LENGTH_SHORT).show();
                        try {
                            boolean isPin = data.getBooleanExtra("IS_PIN_ENTERED", false);


                            if (callFrom.equalsIgnoreCase("TransactionSummary") ||
                                    callFrom.equalsIgnoreCase("settlement") ||
                                    callFrom.equalsIgnoreCase("LastTransaction") ||
                                    callFrom.equalsIgnoreCase("anyReceipt")) {
                                strData = "Result  :" + data.getStringExtra("Result");
                            } else {
                                strData = "Result  :" + data.getStringExtra("Result") + "\n" +
                                        "Masked Pan  :" + (data.getStringExtra("Masked_Pan") != null ? data.getStringExtra("Masked_Pan") : "") + "\n" +
                                        "CardHolderName  :" + (data.getStringExtra("Card_Holder_Name") != null ? data.getStringExtra("Card_Holder_Name") : "") + "\n" +
                                        "RRN  :" + (data.getStringExtra("RRN") != null ? data.getStringExtra("RRN") : "") + "\n" +
                                        "AuthCode  :" + (data.getStringExtra("Auth_Code") != null ? data.getStringExtra("Auth_Code") : "") + "\n" +
                                        "BatchNumber  :" + (data.getStringExtra("Batch_Number") != null ? data.getStringExtra("Batch_Number") : "") + "\n" +
                                        "Stan  :" + (data.getStringExtra("Stan") != null ? data.getStringExtra("Stan") : "") + "\n" +
                                        "Card Type  :" + (data.getStringExtra("Card_Type") != null ? data.getStringExtra("Card_Type") : "") + "\n" +
                                        "IS_SUCCESS:" + (data.getStringExtra("IS_SUCCESS") != null ? data.getStringExtra("IS_SUCCESS") : "") + "\n" +
                                        "IS_PIN_ENTERED:" + (isPin == true ? "TRUE" : "FALSE") + "\n" +
                                        "Sub_Type = " + (data.getStringExtra("Sub_Type") != null ? data.getStringExtra("Sub_Type") : "");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (data.getStringExtra("Result") != null)
//                        showConfirmDialog(strData);

//                    Result:
//                    Approved or completed successfully
//                    Masked Pan  :459115 XXXXXX9199
//                    CardHolderName:
//                    MEDIBOINA SUDHAKAR
//                    RRN:
//                    001510597009
//                    AuthCode:
//                    786595
//                    BatchNumber:
//                    000002
//                    Stan:
//                    567645
//                    Card Type  :VISA
//                    IS_SUCCESS:
//                    TRUE
//                    IS_PIN_ENTERED:
//                    TRUE
//                            Sub_Type = VISA |
                            Globals.showToast(getApplicationContext(), data.getStringExtra("Result"));
                        String rrn = data.getStringExtra("RRN");

                        if (rrn != null & !rrn.equals("")) {
                            if (data.getStringExtra("IS_SUCCESS").equalsIgnoreCase("TRUE")) {


                                paymentRequestModel.setRr_number(data.getStringExtra("RRN"));
                                submitPayment();
                            } else {
                                Globals.showToast(getApplicationContext(), data.getStringExtra("Result"));
                            }

                        } else {
                            Globals.showToast(getApplicationContext(), "Invalid Reference Number");
                        }

                        LogUtils.d("response of sale is ", "" + data.getStringExtra("Result"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

package com.anyemi.recska.activities;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AEP.paystarr.AepPaymentInterface;
import com.AEP.paystarr.activities.payment.InitPaymentActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.LoginModel;
import com.anyemi.recska.model.PaymentRequestModel;
import com.anyemi.recska.model.ServicesResponseModel;
import com.anyemi.recska.utils.AppData;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

import static com.anyemi.recska.utils.Globals.ProceedNextScreen;


public class PaymentModeActivityNew extends AppCompatActivity implements AepPaymentInterface {

    //Action Bar
    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;
    boolean is_valid = true;
    boolean transDone = true;
    //    GridView gv_services;
    ListView gv_services;
    ArrayList<ServicesResponseModel.FinancerBean.PaymentModeBean> ary_data = new ArrayList<>();
    PaymentModesAdapter mAdapter;
    AppData appData;
    ServicesResponseModel servicesResponseModel;

    String paymentData;
    PaymentRequestModel paymentRequestModel;
    TextView tv_amount;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_modes);

        appData = (AppData) getApplication();

        servicesResponseModel = new ServicesResponseModel();
        servicesResponseModel = appData.getServicesResponseModel();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(PaymentModeActivityNew.this, R.color.colorPrimaryDark));
        }

        paymentRequestModel = Globals.getPaymentRequestModes(getIntent().getExtras());
        //  paymentRequestModel.setAepPaymentInterface(this);

        if (paymentRequestModel == null) {
            Globals.showToast(getApplicationContext(), Constants.PAYMENT_REQ_ERROR);
            finish();
        }

        createActionBar();
        initView();

        //  InitPaymentActivity


    }


    private void initView() {

        //    finish();

        LoginModel mResponsedata = new LoginModel();
        mResponsedata = new Gson().fromJson(SharedPreferenceUtil.getUserData(getApplicationContext()), LoginModel.class);

        Random r = new Random(System.currentTimeMillis());
        String orderId = "AEP" + (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);


        paymentRequestModel.setUser_id(Integer.parseInt(mResponsedata.getId()));
        paymentRequestModel.setMID_ID(mResponsedata.getAEP_MID());
        //  paymentRequestModel.setORDER_ID(orderId);
        paymentRequestModel.setFIN_ID(SharedPreferenceUtil.getFIN_ID(getApplicationContext()));
        paymentRequestModel.setFIN_ID(SharedPreferenceUtil.getFIN_ID(getApplicationContext()));

     //   paymentRequestModel.setTotal_amount("1");
//        paymentRequestModel.setActualDueAmount("1");

        PaymentRequestModel aprm = paymentRequestModel;

        aprm.setUser_id(Integer.parseInt(mResponsedata.getId()));
        aprm.setMID_ID(mResponsedata.getAEP_MID());
        //   aprm.setMID_ID("AnyEMI");
        // aprm.setORDER_ID(orderId);
        aprm.setTotal_amount(paymentRequestModel.getTotal_amount());
        aprm.setActualDueAmount(paymentRequestModel.getTotal_amount());
        aprm.setMobile_number(mResponsedata.getUser_phone_number());


        if (is_valid){
            InitPaymentActivity.getInstance().initialize(PaymentModeActivityNew.this,
                    this, new Gson().toJson(paymentRequestModel));
         }else{
            finish();
        }
        is_valid = false;


    }



    private void createActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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


    @Override
    public void onResume() {
        super.onResume();

        if (!is_valid) {
            finish();
        }
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
    public void success(String s) {
        finish();

        PaymentRequestModel prm = new Gson().fromJson(s, PaymentRequestModel.class);

        paymentRequestModel.setTrsno(prm.getTrsno());
        paymentRequestModel.setPayment_type(prm.getPayment_type());
        paymentRequestModel.setMobile_number(prm.getMobile_number());
  //      paymentRequestModel.setRemarks(new Gson().toJson(s));
        // Globals.showToast(getApplicationContext(),s);
        if(transDone) {
            transDone=false;
            submitPayment();
        }
     //   finish();

    }

    @Override
    public void failure(String s) {
        Globals.showToast(getApplicationContext(), "Payment Failed");
        finish();
    }

    @Override
    public void error(String s) {
        Globals.showToast(getApplicationContext(), "Error While Processing Payment");
        finish();
    }


    private void submitPayment() {
        new BackgroundTask(PaymentModeActivityNew.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.submitPayment(PaymentModeActivityNew.this, paymentRequestModel);
            }

            public void taskCompleted(Object data) {
                if (data != null || data.equals("")) {
                    if (data.toString().contains("SUCCESS")) {

                        ProceedNextScreen(getApplicationContext(), paymentRequestModel);
                    }
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }


    public class PaymentModesAdapter extends BaseAdapter {
        private Context c;
        private LayoutInflater mInflater;
        ViewHolder _listView;
        ArrayList<ServicesResponseModel.FinancerBean.PaymentModeBean> tenant_matches_listings;

        public PaymentModesAdapter(Context c, ArrayList<ServicesResponseModel.FinancerBean.PaymentModeBean> mListings) {
            this.tenant_matches_listings = mListings;
            this.c = c;
            mInflater = LayoutInflater.from(c);
        }


        @Override
        public int getCount() {
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
                convertView = mInflater.inflate(R.layout.lv_payment_tile, null, false);
                _listView = new ViewHolder();
                _listView.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
                _listView.iv_item = (ImageView) convertView.findViewById(R.id.iv_item);


                convertView.setTag(_listView);
            } else {
                _listView = (ViewHolder) convertView.getTag();
            }

            try {

                _listView.tv_shop_name.setText(tenant_matches_listings.get(position).getPayment_mode());

                try {
                    String url = tenant_matches_listings.get(position).getPaymentmode_icon().toString();
                    Glide.with(getApplicationContext())
                            .load(url)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            // .centerCrop()
                            .error(R.drawable.any_emi_launcher)
                            .into(_listView.iv_item);
                } catch (Exception e) {
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder {
            TextView tv_shop_name;
            ImageView iv_item;
        }
    }

}
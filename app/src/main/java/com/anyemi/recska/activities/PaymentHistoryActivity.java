package com.anyemi.recska.activities;

import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.anyemi.recska.R;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.CollectionsModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.anyemi.recska.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentHistoryActivity extends AppCompatActivity {

    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;




    ViewGroup header;

    ListView lv_my_account;
    Button btn_search;
    Spinner spnr_sort_by;
    EditText et_search;
    LinearLayout linearLayout;
    ListingAdapter mAdapter;
    ArrayList<CollectionsModel.CollectionsBean> mCollections = new ArrayList<>();
    ArrayList<CollectionsModel.CollectionsBean> SCollections = new ArrayList<>();
    Object rdata;
    String service_number = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        createActionBar();

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(PaymentHistoryActivity.this, R.color.colorPrimaryDark));
        }

        // this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Intent mIntent = getIntent();
        service_number = mIntent.getStringExtra(Constants.SERVICE_NUMBER);
        initializeComponents();
      //  initHeaderView();

        attemptLogin(SharedPreferenceUtil.getUserId(getApplicationContext())+"&loan_number="+service_number+"");

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
        aTitle.setText("Payment Details");

    }



    private void initializeComponents() {
        lv_my_account = findViewById(R.id.lv_collection);
        Button btn_search = findViewById(R.id.btn_search);


        mAdapter = new ListingAdapter(getApplicationContext(), SCollections);
        lv_my_account.setAdapter(mAdapter);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rdata != null) {
                    parseData(rdata);
                }
            }
        });
    }

    private void initHeaderView() {

        LayoutInflater inflater = getLayoutInflater();
        header = (ViewGroup) inflater.inflate(R.layout.header_search_slip, lv_my_account, false);
        lv_my_account.addHeaderView(header, null, false);

        et_search = header.findViewById(R.id.ett_search);
        btn_search = header.findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_search.getText().toString().equals("")) {
                    Globals.showToast(getApplicationContext(), "Assesment number should not be empty");
                } else {
                    Utils.hideKeyboard(PaymentHistoryActivity.this);
                    if (btn_search.getText().toString().equals("Search")) {
                        if (et_search.getText().toString().length() == 10) {
                            String s = et_search.getText().toString();
                            String start = s.substring(0, 5);
                            String end = s.substring(5);
                            String finals = start + " " + end;
                            et_search.setText(finals);
                        }
                        openSearchDialog(et_search.getText().toString());
                        btn_search.setText("Clear");
                        //et_search.setVisibility(View.GONE);
                    } else {
                        if (rdata != null) {
                            btn_search.setText("Search");
                            et_search.setText("");
                            et_search.setVisibility(View.VISIBLE);
                            parseData(rdata);
                        } else {
                            Globals.showToast(getApplicationContext(), "No Data Found");
                        }
                    }


                }

            }
        });
    }


    public class ListingAdapter extends BaseAdapter {

        private Context c;
        private LayoutInflater mInflater;
        ViewHolder _listView;
        ArrayList<CollectionsModel.CollectionsBean> tenant_matches_listings;

        public ListingAdapter(Context c, ArrayList<CollectionsModel.CollectionsBean> mListings) {
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
                convertView = mInflater.inflate(R.layout.lv_collections, null, false);
                _listView = new ViewHolder();
                _listView.ll_item = convertView.findViewById(R.id.ll_item);
                _listView.tv_c_name = convertView.findViewById(R.id.tv_c_name);
                _listView.tv_s_amount = convertView.findViewById(R.id.tv_s_amount);
                _listView.tv_s_number = convertView.findViewById(R.id.tv_s_number);
                _listView.tv_payment_date = convertView.findViewById(R.id.tv_payment_date);
                _listView.iv_payment_type = convertView.findViewById(R.id.iv_payment_type);
                _listView.viewDetails = convertView.findViewById(R.id.btn_view);

                convertView.setTag(_listView);
            } else {
                _listView = (ViewHolder) convertView.getTag();
            }

            try {


                try {
                    String url = tenant_matches_listings.get(position).getIcon();
                    Glide.with(getApplicationContext())
                            .load(url)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            // .centerCrop()
                            .error(R.drawable.any_emi_launcher)
                            .into(_listView.iv_payment_type);
                } catch (Exception e) {
                }


//            if (position == 0) {
//                _listView.ll_search.setVisibility(View.VISIBLE);
//            } else {
//                _listView.ll_search.setVisibility(View.GONE);
//            }

                _listView.tv_payment_date.setText(Utils.parseShowDate(tenant_matches_listings.get(position).getDate_time()));

                _listView.tv_c_name.setText(tenant_matches_listings.get(position).getCustomer_name());
                _listView.tv_s_number.setText("Service No " + tenant_matches_listings.get(position).getLoan_number());
                // _listView.tv_s_amount.setText("Amount  " + tenant_matches_listings.get(position).getPaid_amount());

                Double amount;
                Double bankCharges;
                Double total_amount;

                if (!tenant_matches_listings.get(position).getTotal_amount().equals("")) {
                    amount = Double.parseDouble(tenant_matches_listings.get(position).getTotal_amount());
                } else {
                    amount = 0.0;
                }

                if (!tenant_matches_listings.get(position).getBank_charges().equals("")) {
                    bankCharges = Double.parseDouble(tenant_matches_listings.get(position).getBank_charges());
                } else {
                    bankCharges = 0.0;
                }


                total_amount = amount + bankCharges;
                String resultStr = Utils.parseAmount(String.valueOf(total_amount));
                _listView.tv_s_amount.setText(getResources().getString(R.string.Rs) +" " +resultStr + " /-");

                _listView.ll_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Gson gson = new Gson();
                        Intent i = new Intent(getApplicationContext(), CollectionsDetailsActivity.class);
                        i.putExtra(Constants.PAYMENTS_DATA, gson.toJson(tenant_matches_listings.get(position)));
                        startActivity(i);
                    }
                });


                _listView.viewDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Gson gson = new Gson();
                        Intent i = new Intent(getApplicationContext(), CollectionsDetailsActivity.class);
                        i.putExtra(Constants.PAYMENTS_DATA, gson.toJson(tenant_matches_listings.get(position)));
                        startActivity(i);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder {

            TextView tv_c_name, tv_s_number, tv_s_amount,tv_payment_date;
            LinearLayout ll_item;
            Button viewDetails;
            ImageView iv_payment_type;
        }
    }


    private void attemptLogin(final String id) {
        new BackgroundTask(getApplicationContext(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.getCollections(getApplicationContext(),prepareRequest());
            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {
                    rdata = data;
                    parseData(rdata);

                } else {
                    Globals.showToast(getApplicationContext(), "No Data Found");
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }

    private String prepareRequest() {

        JSONObject requestObject = new JSONObject();
        try {

          //  int index="";


            requestObject.put("fdate", "");
            requestObject.put("tdate",  "");
            requestObject.put("user_id",SharedPreferenceUtil.getUserId(getApplicationContext()));
            requestObject.put("P_MODES",   "");
            requestObject.put("loan_number",  service_number);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject.toString();
    }



    private void parseData(Object data) {
        mCollections.clear();
        SCollections.clear();
        Gson gson = new Gson();
        CollectionsModel mResponsedata = new CollectionsModel();
        mResponsedata = gson.fromJson(data.toString(), CollectionsModel.class);
        try {
            mCollections.addAll(mResponsedata.getCollections());
            SCollections.addAll(mResponsedata.getCollections());
            mAdapter.notifyDataSetChanged();
        }catch (Exception e ){
            e.printStackTrace();
            //Globals.showToast(getActivity(),mResponsedata.getMsg());
            Globals.showToast(getApplicationContext(),"Sorry No data found");
        }
    }


    private void openSearchDialog(String query) {


        if (!query.equals("")) {
            SCollections.clear();
            for (int i = 0; i < mCollections.size(); i++) {
                if (mCollections.get(i).getLoan_number().contains(query)) {
                    SCollections.add(mCollections.get(i));
                }
            }
            Globals.showToast(getApplicationContext(), SCollections.size() + " Results Found");
            mAdapter.notifyDataSetChanged();
        } else {
            Globals.showToast(getApplicationContext(), "No Input Value Found");
        }
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

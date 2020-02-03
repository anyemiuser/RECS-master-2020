package com.anyemi.recska.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
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
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anyemi.recska.LoginActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.AddVpaModel;
import com.anyemi.recska.model.PaymentRequestModel;
import com.anyemi.recska.model.VpaListModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RemoveUpiIdActivity extends AppCompatActivity {

    //ACTION BAR UI COMPONENTS

    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;

    //UI COMPONENTS


    PaymentRequestModel paymentRequestModel;
    JSONObject jsonObject;
    Long startTime;
    String service_list_id;
    String payment_id = "";
    Gson gson = new Gson();
    CountDownTimer countDownTimer;
    Dialog infoDialog;
    LinearLayout email_login_form;

    Button btn_pay;
    TextView tv_name;
    EditText et_amount;
    TextInputLayout til_amount;

    ListView lv_my_account;
    ListingAdapter mAdapter;

    String data;

    VpaListModel vpaListModel;
    ArrayList<VpaListModel.UpilistBean> SCollections = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_upi);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(RemoveUpiIdActivity.this, R.color.colorPrimaryDark));
        }

        createActionBar();


        initializeView();


        String user_id = SharedPreferenceUtil.getUserId(getApplicationContext());
        if (user_id.equals("")) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        } else {
            getUpiData();
        }


    }


    private void getUpiData() {
        new BackgroundTask(RemoveUpiIdActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.vpaList(RemoveUpiIdActivity.this, upiRequest());
            }

            public void taskCompleted(Object data) {
                if (data != null || data.equals("")) {
                    if (data != null || data.equals("")) {
                        try {
                            SCollections.clear();
                            Gson gson = new Gson();
                            vpaListModel = new VpaListModel();
                            vpaListModel = gson.fromJson(data.toString(), VpaListModel.class);

                            if (vpaListModel.getUpilist() != null) {

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
        aTitle.setText("Remove UPI id");

    }


    private void initializeView() {


        lv_my_account = findViewById(R.id.lv_collection);
        email_login_form = findViewById(R.id.email_login_form);
        email_login_form.setVisibility(View.GONE);
        mAdapter = new ListingAdapter(getApplicationContext(), SCollections);
        lv_my_account.setAdapter(mAdapter);

    }


    public class ListingAdapter extends BaseAdapter {

        private Context c;
        private LayoutInflater mInflater;
        ViewHolder _listView;
        ArrayList<VpaListModel.UpilistBean> tenant_matches_listings;

        public ListingAdapter(Context c, ArrayList<VpaListModel.UpilistBean> mListings) {
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

                _listView.tv_c_name.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onClick(View view) {

                        //creating a popup menu
                        PopupMenu popup = new PopupMenu(RemoveUpiIdActivity.this
                                , view);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.menu_xml_name);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.menu1:


                                        final AlertDialog.Builder builder =
                                                new AlertDialog.Builder(RemoveUpiIdActivity.this);

                                        final String message = "Are you sure to Remove Upi Id";

                                        builder.setMessage(message)
                                                .setPositiveButton("OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface d, int id) {
                                                                removeUpi(tenant_matches_listings.get(position).getUpi_id());
                                                            }
                                                        })
                                                .setNegativeButton("Cancel",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface d, int id) {
                                                                d.cancel();
                                                            }
                                                        });
                                        builder.create().show();

                                        break;

                                }
                                return false;
                            }
                        });
                        //displaying the popup
                        popup.show();


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder {

            TextView tv_c_name;
            ImageView iv_item;

        }
    }


    private void removeUpi(final String upi) {
        new BackgroundTask(RemoveUpiIdActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.removeUPI(RemoveUpiIdActivity.this, prepareAddVpaRequest(upi));
            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {
                    try {
                        AddVpaModel addVpaModel = new AddVpaModel();
                        addVpaModel = gson.fromJson(data.toString(), AddVpaModel.class);

                        Globals.showToast(getApplicationContext(), addVpaModel.getMsg());
                        getUpiData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        }, getString(R.string.loading_txt)).execute();
    }

    private String prepareAddVpaRequest(String upi) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("upi_id", upi);
            jsonObject.put("user_id", SharedPreferenceUtil.getUserId(getApplicationContext()));
            jsonObject.put("primary", "0");

            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
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
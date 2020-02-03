package com.anyemi.recska.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.anyemi.recska.LoginActivity;
import com.anyemi.recska.NavigationActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.activities.RegisterActivity;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.CollectionsModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.PageIndicator;
import com.anyemi.recska.utils.SharedPreferenceUtil;

import java.util.ArrayList;

public class HomePageFragement extends Fragment implements View.OnClickListener {

    private View rootView;
    LinearLayout ll_reg, ll_bill_pay, ll_customer_care, ll_consumption, ll_payment_history, ll_supply_position;
    LinearLayout ll_connect, ll_my_usage, ll_pending,ll_feed_back;


    // UI References
    private ViewPager mViewPager;
    TextView tv_reports;
    PageIndicator mIndicator;
    ArrayList<String> products = new ArrayList<>();
    boolean stopSliding = false;
    private Handler handler;
    private Runnable animateViewPager;

    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;


    ArrayList<CollectionsModel.CollectionsBean> mCollections = new ArrayList<>();
    ArrayList<CollectionsModel.CollectionsBean> SCollections = new ArrayList<>();
    Object rdata;
    home_fragment.ListingAdapter mAdapter;
    ListView lv_my_account;

    public HomePageFragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        findViewById(rootView);
        ((NavigationActivity) getActivity()).showAdd();



        return rootView;
    }


    private void findViewById(View view) {
        ll_reg = view.findViewById(R.id.ll_reg);
        ll_bill_pay = view.findViewById(R.id.ll_bill_pay);
        ll_customer_care = view.findViewById(R.id.ll_customer_care);
        ll_consumption = view.findViewById(R.id.ll_consumption);
        ll_payment_history = view.findViewById(R.id.ll_payment_history);
        ll_supply_position = view.findViewById(R.id.ll_supply_position);
        ll_connect = view.findViewById(R.id.ll_connect);
        ll_my_usage = view.findViewById(R.id.ll_my_usage);
        ll_feed_back = view.findViewById(R.id.ll_feed_back);
        ll_pending = view.findViewById(R.id.ll_pending);
        tv_reports = view.findViewById(R.id.tv_reports);

        if (!SharedPreferenceUtil.getLoginType(getActivity()).equals(Constants.LOGIN_TYPE_CUSTOMER)) {

            tv_reports.setText("Reports");
        }else{
            tv_reports.setText("Consumption");
        }

        ll_reg.setOnClickListener(this);
        ll_bill_pay.setOnClickListener(this);
        ll_customer_care.setOnClickListener(this);
        ll_consumption.setOnClickListener(this);
        ll_payment_history.setOnClickListener(this);
        ll_supply_position.setOnClickListener(this);
        ll_connect.setOnClickListener(this);
        ll_my_usage.setOnClickListener(this);
        ll_pending.setOnClickListener(this);
        ll_feed_back.setOnClickListener(this);

    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
    }






    @Override
    public void onClick(View view) {



        switch (view.getId()) {
            case R.id.ll_reg:
                Intent i = new Intent(getActivity(), RegisterActivity.class);
                startActivity(i);
                break;


            case R.id.ll_bill_pay:
                ((NavigationActivity) getActivity()).changeFragment( R.id.nav_search);
                break;
            case R.id.ll_customer_care:
                ((NavigationActivity) getActivity()).changeFragment( R.id.nav_contact_us);
                break;
            case R.id.ll_consumption:

                if (!SharedPreferenceUtil.getLoginType(getActivity()).equals(Constants.LOGIN_TYPE_CUSTOMER)) {
                    ((NavigationActivity) getActivity()).changeFragment( R.id.ll_consumption);
                }else{
                    Globals.showToast(getActivity(),"We Will Update soon");
                }

                break;
            case R.id.ll_payment_history:
                ((NavigationActivity) getActivity()).changeFragment( R.id.nav_collections);
                break;
            case R.id.ll_supply_position:
                Globals.showToast(getActivity(),"We Will Update soon");
                break;

            case R.id.ll_connect:
                Globals.showToast(getActivity(),"We Will Update soon");
                break;

            case R.id.ll_my_usage:

                Globals.showToast(getActivity(),"We Will Update soon");

                break;

                case R.id.ll_pending:

                ((NavigationActivity) getActivity()).changeFragment( R.id.ll_my_usage);

                break;


            case R.id.ll_feed_back:
                ((NavigationActivity) getActivity()).changeFragment( R.id.nav_feed_back);
                break;

        }

    }
}


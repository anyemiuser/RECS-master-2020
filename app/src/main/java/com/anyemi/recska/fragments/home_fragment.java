package com.anyemi.recska.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.anyemi.recska.R;
import com.anyemi.recska.activities.CollectionsDetailsActivity;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.CollectionsModel;
import com.anyemi.recska.utils.CirclePageIndicator;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.ImageSlideAdapter;
import com.anyemi.recska.utils.PageIndicator;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.anyemi.recska.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;


/**
 * Created by SuryaTejaChalla on 25-08-2017.
 */

public class home_fragment extends Fragment {

    private View rootView;


    // UI References
    private ViewPager mViewPager;
    TextView imgNameTxt;
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
    ListingAdapter mAdapter;
    ListView lv_my_account;

    public home_fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_home, container, false);

        findViewById(rootView);
//        app = (AppData) getActivity().getApplication();
//        products = app.getImages();
        getCollections(SharedPreferenceUtil.getUserId(getActivity()));

        mIndicator.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {

                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_UP:
                        // calls when touch release on ViewPager
                        if (products != null && products.size() != 0) {
                            stopSliding = false;
                            runnable(products.size());
                            handler.postDelayed(animateViewPager,
                                    ANIM_VIEWPAGER_DELAY_USER_VIEW);
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // calls when ViewPager touch
                        if (handler != null && stopSliding == false) {
                            stopSliding = true;
                            handler.removeCallbacks(animateViewPager);
                        }
                        break;
                }
                return false;
            }
        });


        return rootView;
    }




    private void findViewById(View view) {
        mViewPager = view.findViewById(R.id.view_pager);
        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        imgNameTxt = view.findViewById(R.id.img_name);

        lv_my_account = rootView.findViewById(R.id.lv_collection);
        // Button btn_search = (Button) rootView.findViewById(R.id.btn_search);


        mAdapter = new ListingAdapter(getActivity(), SCollections);
        lv_my_account.setAdapter(mAdapter);

        // btn_search.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (rdata != null) {
//                            parseData(rdata);
//                        }
//                    }
//                });

    }

    public void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {
                    if (mViewPager.getCurrentItem() == size - 1) {
                        mViewPager.setCurrentItem(0);
                    } else {
                        mViewPager.setCurrentItem(
                                mViewPager.getCurrentItem() + 1, true);
                    }
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                }
            }
        };
    }


    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (products != null) {
                    imgNameTxt.setText("" + (products.get(state)));
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        if (products == null) {
            addImages();

            mViewPager.setAdapter(new ImageSlideAdapter(getActivity(), products, home_fragment.this));

            mIndicator.setViewPager(mViewPager);
            // imgNameTxt.setText("" + ((Product) products.get(mViewPager.getCurrentItem())).getName());
            imgNameTxt.setText("" + "test");
            runnable(products.size());
            //Re-run callback
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        } else {
            addImages(); // sendRequest();
            mViewPager.setAdapter(new ImageSlideAdapter(getActivity(), products, home_fragment.this));

            mIndicator.setViewPager(mViewPager);
            // imgNameTxt.setText("" + ((Product) products.get(mViewPager.getCurrentItem())).getName());
            imgNameTxt.setText("" + "test");
            runnable(products.size());
            //Re-run callback
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        }


        super.onResume();
        //((SideMenuActivity) getActivity()).refreshNotfication();
    }

    private void addImages() {
        products.clear();
        products.add("http://recsakp.com/images/banner-1.jpg");
        products.add("http://recs.ctms.info/Accslide/5.jpg");
        products.add("http://recs.ctms.info/Accslide/6.jpg");

    }


    private void getCollections(final String id) {
        new BackgroundTask(getActivity(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.getCollections(getActivity(), id);
            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {
                    rdata = data;
                    parseData(rdata);

                } else {
                    Globals.showToast(getContext(), "No Data Found");
                }
            }
        }, getString(R.string.loading_txt)).execute();
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
        } catch (Exception e) {
            e.printStackTrace();
            //Globals.showToast(getActivity(),mResponsedata.getMsg());
            Globals.showToast(getActivity(), "Sorry No data found");
        }
    }


    public class ListingAdapter extends BaseAdapter {

        private Context c;
        private LayoutInflater mInflater;
        ListingAdapter.ViewHolder _listView;
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
                convertView = mInflater.inflate(R.layout.lv_power_bill_collections, null, false);
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


                if (tenant_matches_listings.get(position).getPayment_type().equals(Constants.PAYMENT_MODE_AADHAR)) {
                    _listView.iv_payment_type.setBackground(getResources().getDrawable(R.drawable.aadharhdpi));
                } else if (tenant_matches_listings.get(position).getPayment_type().equals(Constants.PAYMENT_MODE_BHARATH_QR)) {
                    _listView.iv_payment_type.setBackground(getResources().getDrawable(R.drawable.bharath_qr));
                } else if (tenant_matches_listings.get(position).getPayment_type().equals(Constants.PAYMENT_MODE_BHIM)) {
                    _listView.iv_payment_type.setBackground(getResources().getDrawable(R.drawable.bhim));
                } else if (tenant_matches_listings.get(position).getPayment_type().equals(Constants.PAYMENT_MODE_CASH)) {
                    _listView.iv_payment_type.setBackground(getResources().getDrawable(R.drawable.moneyhdpi));
                } else if (tenant_matches_listings.get(position).getPayment_type().equals(Constants.PAYMENT_MODE_CREDIT_CARD)) {
                    _listView.iv_payment_type.setBackground(getResources().getDrawable(R.drawable.cardhdpi));
                } else if (tenant_matches_listings.get(position).getPayment_type().equals(Constants.PAYMENT_MODE_CHEQUE)) {
                    _listView.iv_payment_type.setBackground(getResources().getDrawable(R.drawable.chequehdpi));
                } else if (tenant_matches_listings.get(position).getPayment_type().equals(Constants.PAYMENT_MODE_DEBIT_CARD)) {
                    _listView.iv_payment_type.setBackground(getResources().getDrawable(R.drawable.cardhdpi));
                } else if (tenant_matches_listings.get(position).getPayment_type().equals(Constants.PAYMENT_MODE_PAYTM_SBI_UPI)) {
                    _listView.iv_payment_type.setBackground(getResources().getDrawable(R.drawable.sbi_upi));
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
                _listView.tv_s_amount.setText(getResources().getString(R.string.Rs) + " " + resultStr + " /-");

                _listView.ll_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Gson gson = new Gson();
                        Intent i = new Intent(getActivity(), CollectionsDetailsActivity.class);
                        i.putExtra(Constants.PAYMENTS_DATA, gson.toJson(tenant_matches_listings.get(position)));
                        startActivity(i);
                    }
                });


                _listView.viewDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Gson gson = new Gson();
                        Intent i = new Intent(getActivity(), CollectionsDetailsActivity.class);
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

            TextView tv_c_name, tv_s_number, tv_s_amount, tv_payment_date;
            LinearLayout ll_item;
            Button viewDetails;
            ImageView iv_payment_type;
        }
    }
}

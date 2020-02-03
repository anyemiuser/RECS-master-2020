package com.anyemi.recska.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.anyemi.recska.R;
import com.anyemi.recska.activities.PaymentHistoryActivity;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.CollectionsModel;
import com.anyemi.recska.model.PendingTransactionModel;
import com.anyemi.recska.model.ReportsModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.anyemi.recska.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ReportsFragmnet extends Fragment {

    private View rootView;
    ViewGroup header;

    ListView lv_my_account;
    Button btn_search;

    Button btn_to_date,btn_from_date;
    LinearLayout linearLayout;
    ListingAdapter mAdapter;
    ArrayList<ReportsModel.PaymentWiseTranscitionsDetailsBean> mReports = new ArrayList<>();

    Object rdata;


    public ReportsFragmnet() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_collection_layout, container, false);
        setHasOptionsMenu(false);
        initializeComponents();
        initHeaderView();


        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        //((SideMenuActivity) getActivity()).refreshNotfication();
    }


    private void initializeComponents() {
        lv_my_account = rootView.findViewById(R.id.lv_collection);
        Button btn_search = rootView.findViewById(R.id.btn_search);


        mAdapter = new ListingAdapter(getActivity(), mReports);
        lv_my_account.setAdapter(mAdapter);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getReports();
            }
        });
    }

    private void initHeaderView() {

        LayoutInflater inflater = getLayoutInflater();
        header = (ViewGroup) inflater.inflate(R.layout.header_date, lv_my_account, false);
        lv_my_account.addHeaderView(header, null, false);

        btn_from_date = header.findViewById(R.id.btn_from_date);
        btn_to_date = header.findViewById(R.id.btn_to_date);
        btn_search = header.findViewById(R.id.btn_search);



        btn_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        int s = monthOfYear + 1;
                     //   String a = dayOfMonth + "/" + s + "/" + year;

                        String a = year + "/" + s + "/" + dayOfMonth ;
                        btn_from_date.setText(a);
                    }
                };

//

                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =
                        new DatePickerDialog(getActivity(), dpd, mYear, mMonth, mDay);
                dialog.show();


            }
        });

        btn_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        int s = monthOfYear + 1;
                        //String a = dayOfMonth + "/" + s + "/" + year;
                        String a = year + "/" + s + "/" + dayOfMonth ;
                        btn_to_date.setText(a);
                    }
                };

//

                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =
                        new DatePickerDialog(getActivity(), dpd, mYear, mMonth, mDay);
                dialog.show();
            }
        }); btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_from_date.getText().toString().equals("")) {
                    Globals.showToast(getActivity(), "From Date should not be empty");
                }else if (btn_to_date.getText().toString().equals("")) {
                    Globals.showToast(getActivity(), "To Date should not be empty");
                } else {
                    getReports();
                }

            }
        });
    }





    public class ListingAdapter extends BaseAdapter {

        private Context c;
        private LayoutInflater mInflater;
        ViewHolder _listView;
        ArrayList<ReportsModel.PaymentWiseTranscitionsDetailsBean> tenant_matches_listings;

        public ListingAdapter(Context c, ArrayList<ReportsModel.PaymentWiseTranscitionsDetailsBean> mListings) {
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
                } else if (tenant_matches_listings.get(position).getPayment_type().equals(Constants.PAYMENT_MODE_PAYTM_PG)) {
                    _listView.iv_payment_type.setBackground(getResources().getDrawable(R.drawable.paytmhdpi));
                } else {
                    _listView.iv_payment_type.setBackground(getResources().getDrawable(R.drawable.any_emi_launcher));
                }

                _listView.tv_payment_date.setVisibility(View.GONE);



                _listView.tv_c_name.setText("Total Amount");
                _listView.tv_s_number.setText("Total Number of Transactions " + tenant_matches_listings.get(position).getTotalTranscitions());

             try {
                 String resultStr = Utils.parseAmount(String.valueOf(tenant_matches_listings.get(position).getTotalamount()));
                 _listView.tv_s_amount.setText(getResources().getString(R.string.Rs) + " " + resultStr + " /-");
             }catch (Exception e){

             }

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


    private void getReports() {
        new BackgroundTask(getActivity(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.getReports(getActivity(), prepareRequest());
            }



            public void taskCompleted(Object data) {



                if (data != null || data.equals("")) {
                    rdata = data;
                    parseData(rdata);

                } else {
                    Globals.showToast(getContext(), "No Data Found");
                    mReports.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }

    private String prepareRequest() {

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("fdate", btn_from_date.getText().toString());
            requestObject.put("tdate",  btn_to_date.getText().toString());
            requestObject.put("user_id",SharedPreferenceUtil.getUserId(getActivity()) );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject.toString();
    }




    private void parseData(Object data) {

        mReports.clear();
        Gson gson = new Gson();
        ReportsModel mResponsedata = new ReportsModel();
        mResponsedata = gson.fromJson(data.toString(), ReportsModel.class);
        try {
            mReports.addAll(mResponsedata.getPaymentWiseTranscitionsDetails());

            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            mReports.clear();
            mAdapter.notifyDataSetChanged();
            //Globals.showToast(getActivity(),mResponsedata.getMsg());
            Globals.showToast(getActivity(), "Sorry No data found");
        }
    }





}


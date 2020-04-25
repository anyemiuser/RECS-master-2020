package com.anyemi.recska.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.anyemi.recska.R;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.bluetoothPrinter.BluetoothPrinterMain;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.ReportsModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.anyemi.recska.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ReportsFragmnet extends Fragment {

    private View rootView;
    ViewGroup header,footer;

    ListView lv_my_account;
    Button btn_search,btnPrint;
Spinner spnr_p_modes;
    ArrayList<String> paymmentModesNames = new ArrayList<>();
    ArrayList<String> paymmentModesIds = new ArrayList<>();

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
        footer = (ViewGroup) inflater.inflate(R.layout.footer_print, lv_my_account, false);

        lv_my_account.addHeaderView(header, null, false);
        lv_my_account.addFooterView(footer);

        btn_from_date = header.findViewById(R.id.btn_from_date);
        btn_to_date = header.findViewById(R.id.btn_to_date);
        btn_search = header.findViewById(R.id.btn_search);
        spnr_p_modes = header.findViewById(R.id.spnr_p_modes);
        btnPrint=footer.findViewById(R.id.btn_print);

        paymmentModesNames.addAll(Arrays.asList(getResources().getStringArray(R.array.payment_names)));
        paymmentModesIds.addAll(Arrays.asList(getResources().getStringArray(R.array.payment_ids)));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, paymmentModesNames);
        spnr_p_modes.setAdapter(adapter);

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent i = new Intent(getActivity(), BluetoothPrinterMain.class);
                i.putExtra("reports_data",rdata.toString());
                startActivity(i);
            }
        });

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
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
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
                convertView = mInflater.inflate(R.layout.lv_reports, null, false);
                _listView = new ViewHolder();
                _listView.ll_item = convertView.findViewById(R.id.ll_item);
                _listView.tv_c_name = convertView.findViewById(R.id.tv_c_name);
                _listView.tv_a_name = convertView.findViewById(R.id.tv_a_name);
                _listView.tv_a_amount = convertView.findViewById(R.id.tv_a_amount);
                _listView.tv_ad_name = convertView.findViewById(R.id.tv_ad_name);
                _listView.tv_ad_amount = convertView.findViewById(R.id.tv_ad_amount);
                _listView.tv_t_amount = convertView.findViewById(R.id.tv_t_amount);
                _listView.tv_t_name = convertView.findViewById(R.id.tv_t_name);
                _listView.tv_reconnection_fee = convertView.findViewById(R.id.tv_reconnection_fee);
                _listView.tv_reconnection_fee_name = convertView.findViewById(R.id.tv_reconnection_fee_name);

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


                int index =paymmentModesIds.indexOf(tenant_matches_listings.get(position).getPayment_type());


                _listView.tv_payment_date.setText(paymmentModesNames.get(index));

              //  _listView.tv_payment_date.setVisibility(View.GONE);
              //  _listView.tv_payment_date.setVisibility();



                _listView.tv_c_name.setText("Bill Amount");
                _listView.tv_reconnection_fee.setText(tenant_matches_listings.get(position).getReconnection_fee());
                _listView.tv_ad_name.setText("Adjustment Amount");
                _listView.tv_reconnection_fee_name.setText("Reconnection fees");
                _listView.tv_a_name.setText("Arears Amount");
                _listView.tv_t_name.setText("Total Amount");

                _listView.tv_s_number.setText("Total Number of Transactions " +
                        tenant_matches_listings.get(position).getTotalTranscitions());

             try {
                 String billAmount = Utils.parseAmount(String.valueOf(tenant_matches_listings.get(position).getBillamount()));
                 String arear = Utils.parseAmount(String.valueOf(tenant_matches_listings.get(position).getArrearsamount()));
                 String admount = Utils.parseAmount(String.valueOf(tenant_matches_listings.get(position).getAdjustmentamount()));
                 String total = Utils.parseAmount(String.valueOf(tenant_matches_listings.get(position).getTotalamount()));

                 _listView.tv_s_amount.setText(getResources().getString(R.string.Rs)+"  "+ billAmount);
                 _listView.tv_ad_amount.setText(getResources().getString(R.string.Rs)+"  "+ admount);
                 _listView.tv_a_amount.setText(getResources().getString(R.string.Rs)+"  "+ arear);
                 _listView.tv_t_amount.setText(getResources().getString(R.string.Rs)+"  "+ total);

             }catch (Exception e){

             }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder {

            TextView tv_c_name,tv_reconnection_fee_name,tv_reconnection_fee, tv_s_number, tv_s_amount, tv_payment_date,tv_a_name,tv_a_amount,tv_ad_name,tv_ad_amount,tv_t_name,tv_t_amount;
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
                    btnPrint.setVisibility(View.VISIBLE);

                } else {
                    Globals.showToast(getContext(), "No Data Found");
                    mReports.clear();
                    btnPrint.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }

    private String prepareRequest() {

        JSONObject requestObject = new JSONObject();
        try {

            int index=spnr_p_modes.getSelectedItemPosition();


            requestObject.put("fdate", btn_from_date.getText().toString());
            requestObject.put("tdate",  btn_to_date.getText().toString());
            requestObject.put("user_id",SharedPreferenceUtil.getUserId(getActivity()) );
            requestObject.put("P_MODES",   paymmentModesIds.get(index));

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


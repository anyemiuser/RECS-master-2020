package com.anyemi.recska.fragments;

/**
 * Created by SuryaTejaChalla on 23-09-2017.
 */


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
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.anyemi.recska.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


/**
 * Created by Anuprog on 11/23/2016.
 * shows all the paid bills history
 */

public class CollectionsFragment extends Fragment {

    private View rootView;
    ViewGroup header;

    ListView lv_my_account;
    Button btn_search;
    Button get_data;
    Button from_date;
    Button to_date;
    Spinner spnr_sort_by;
    Spinner spnr_p_model;
    EditText et_search;
    LinearLayout linearLayout;
    ListingAdapter mAdapter;

    ArrayList<String> paymmentModesNames = new ArrayList<>();
    ArrayList<String> paymmentModesIds = new ArrayList<>();


    ArrayList<CollectionsModel.CollectionsBean> mCollections = new ArrayList<>();
    ArrayList<CollectionsModel.CollectionsBean> SCollections = new ArrayList<>();
    ArrayList<PendingTransactionModel.TransactionsBean> pending_Collections = new ArrayList<>();
    Object rdata;
    String query = "";


    public CollectionsFragment() {
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


        mAdapter = new ListingAdapter(getActivity(), SCollections);
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
        get_data=header.findViewById(R.id.btn1_search);
        from_date=header.findViewById(R.id.btn_from_date);
        to_date=header.findViewById(R.id.btn_to_date);
        spnr_p_model=header.findViewById(R.id.spnr_p_modes);
        paymmentModesNames.addAll(Arrays.asList(getResources().getStringArray(R.array.payment_names)));
        paymmentModesIds.addAll(Arrays.asList(getResources().getStringArray(R.array.payment_ids)));



        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        int s = monthOfYear + 1;
                        //   String a = dayOfMonth + "/" + s + "/" + year;

                        String a = year + "/" + s + "/" + dayOfMonth ;
                        from_date.setText(a);
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

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        int s = monthOfYear + 1;
                        //String a = dayOfMonth + "/" + s + "/" + year;
                        String a = year + "/" + s + "/" + dayOfMonth ;
                        to_date.setText(a);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, paymmentModesNames);
        spnr_p_model.setAdapter(adapter);


        get_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from_date.getText().toString().equals("From Date")) {
                    Globals.showToast(getActivity(), "Please Select From Date");
                }else if (to_date.getText().toString().equals("To Date")) {
                    Globals.showToast(getActivity(), "Please Select To Date");
                } else {
                    attemptLogin(SharedPreferenceUtil.getUserId(getActivity()));                }


            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_search.getText().toString().equals("")) {
                    Globals.showToast(getActivity(), "Assesment number should not be empty");
                } else {
                    Utils.hideKeyboard(getActivity());
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
                            Globals.showToast(getActivity(), "No Data Found");
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
                    Glide.with(getActivity())
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
                _listView.tv_s_amount.setText(getResources().getString(R.string.Rs) + " " + resultStr + " /-");

                _listView.ll_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Gson gson = new Gson();
//                        Intent i = new Intent(getActivity(), CollectionsDetailsActivity.class);
//                        i.putExtra(Constants.PAYMENTS_DATA, gson.toJson(tenant_matches_listings.get(position)));
//                        startActivity(i);


                        Intent i = new Intent(getActivity(), PaymentHistoryActivity.class);
                        i.putExtra(Constants.SERVICE_NUMBER, tenant_matches_listings.get(position).getLoan_number());
                        startActivity(i);


                    }
                });


                _listView.viewDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Gson gson = new Gson();
//                        Intent i = new Intent(getActivity(), CollectionsDetailsActivity.class);
//                        i.putExtra(Constants.PAYMENTS_DATA, gson.toJson(tenant_matches_listings.get(position)));
//                        startActivity(i);


                        Intent i = new Intent(getActivity(), PaymentHistoryActivity.class);
                        i.putExtra(Constants.SERVICE_NUMBER, tenant_matches_listings.get(position).getLoan_number());
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


    private void attemptLogin(final String id) {
        new BackgroundTask(getActivity(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.getCollections(getActivity(), prepareRequest());
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
    private String prepareRequest() {

        JSONObject requestObject = new JSONObject();
        try {

            int index=spnr_p_model.getSelectedItemPosition();


            requestObject.put("fdate", from_date.getText().toString());
            requestObject.put("tdate",  to_date.getText().toString());
            requestObject.put("user_id",SharedPreferenceUtil.getUserId(getActivity()) );
            requestObject.put("P_MODES",   paymmentModesIds.get(index));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject.toString();
    }


    private void getPendingTransactions(final String id) {
        new BackgroundTask(getActivity(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.getPendingTransaction(getActivity(), id);
            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {
                    rdata = data;
                    parsePendingData(rdata);

                } else {
                    Globals.showToast(getContext(), "No Data Found");
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }

    private void parsePendingData(Object data) {

        Gson gson = new Gson();
        PendingTransactionModel mResponsedata = new PendingTransactionModel();
        mResponsedata = gson.fromJson(data.toString(), PendingTransactionModel.class);

        if (mResponsedata.getStatus() != null && mResponsedata.getStatus().equals("Failed")) {
            Globals.showToast(getActivity(), mResponsedata.getMsg());
        } else {
            pending_Collections.clear();
            try {
                pending_Collections.addAll(mResponsedata.getTransactions());

                for(int i=0; i<pending_Collections.size(); i++){

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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


    private void openSearchDialog(String query) {


        if (!query.equals("")) {
            SCollections.clear();
            for (int i = 0; i < mCollections.size(); i++) {
                if (mCollections.get(i).getLoan_number().contains(query)) {
                    SCollections.add(mCollections.get(i));
                }
            }
            Globals.showToast(getContext(), SCollections.size() + " Results Found");
            mAdapter.notifyDataSetChanged();
        } else {
            Globals.showToast(getContext(), "No Input Value Found");
        }
    }


    //
//        final Dialog dialog = new Dialog(getActivity());
//        dialog.setTitle("Search");
//        dialog.setContentView(R.layout.dialog_sms);
//        final EditText et_phone_num = (EditText) dialog.findViewById(R.id.et_phone_num);
//        et_phone_num.setHint("Enter Assessment Number");
//        Button btn_send_sms = (Button) dialog.findViewById(R.id.btn_send_sms);
//        btn_send_sms.setText("Search");
//        btn_send_sms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!et_phone_num.getText().toString().equals("")) {
//                    query = et_phone_num.getText().toString();
//                    SCollections.clear();
//                    for (int i = 0; i< mCollections.size(); i++) {
//                        if (mCollections.get(i).getLoan_number().contains(et_phone_num.getText().toString())) {
//                            SCollections.add(mCollections.get(i));
//                        }
//                    }
//
//
//                    dialog.dismiss();
//                    mAdapter.notifyDataSetChanged();
//                } else {
//                    Globals.showToast(getContext(), "No Input Value Found");
//                }
//            }
//        });
//        dialog.show();
//
//    }


}


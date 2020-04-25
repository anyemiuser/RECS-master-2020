package com.anyemi.recska.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.anyemi.recska.R;
import com.anyemi.recska.activities.CollectionsDetailsActivity;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.CollectionsModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by SuryaTejaChalla on 12-02-2018.
 */

public class RefundFragment  extends Fragment {

    private View rootView;
    ViewGroup header;

   // ListView lv_my_account;
    Button btn_search;
    Spinner spnr_sort_by;
    EditText et_search;
    LinearLayout linearLayout;
    ListingAdapter mAdapter;
    ArrayList<CollectionsModel.CollectionsBean> mCollections = new ArrayList<>();
    ArrayList<CollectionsModel.CollectionsBean> SCollections = new ArrayList<>();
    Object rdata;
    String query = "";

   TextView tv_info;

    public RefundFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_refund, container, false);
        setHasOptionsMenu(false);
        initializeComponents();
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


       // lv_my_account = (ListView) rootView.findViewById(R.id.lv_collection);
       // Button btn_search = (Button) rootView.findViewById(R.id.btn_search);


       // mAdapter = new ListingAdapter(getActivity(), SCollections);
       // lv_my_account.setAdapter(mAdapter);

//        btn_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (rdata != null) {
//                    parseData(rdata);
//                }
//            }
//        });
    }

    private void initHeaderView() {

        LayoutInflater inflater = getLayoutInflater();
        //header = (ViewGroup) inflater.inflate(R.layout.header_search_slip, lv_my_account, false);
        //lv_my_account.addHeaderView(header, null, false);

        et_search = header.findViewById(R.id.ett_search);
        btn_search = header.findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_search.getText().toString().equals("")) {
                    Globals.showToast(getActivity(), "Assesment number should not be empty");
                } else {
                    Utils.hideKeyboard(getActivity());
                    if (btn_search.getText().toString().equals("Search")) {
                        if(et_search.getText().toString().length()==10){
                            String s=et_search.getText().toString();
                            String start=s.substring(0,5);
                            String end=s.substring(5);
                            String finals=start+" "+end;
                            et_search.setText(finals);
                        }
                        openSearchDialog(et_search.getText().toString());
                        btn_search.setText("Clear Search");
                        et_search.setVisibility(View.GONE);
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // final MyAccountListingsResponse2 studentList = tenant_matches_listings.get(position);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.lv_reports, null, false);
                _listView = new ListingAdapter.ViewHolder();
                _listView.ll_item = convertView.findViewById(R.id.ll_item);
                _listView.tv_c_name = convertView.findViewById(R.id.tv_c_name);
                _listView.tv_s_amount = convertView.findViewById(R.id.tv_s_amount);
                _listView.tv_s_number = convertView.findViewById(R.id.tv_s_number);
                _listView.viewDetails = convertView.findViewById(R.id.btn_view);

                convertView.setTag(_listView);
            } else {
                _listView = (ListingAdapter.ViewHolder) convertView.getTag();
            }

//            if (position == 0) {
//                _listView.ll_search.setVisibility(View.VISIBLE);
//            } else {
//                _listView.ll_search.setVisibility(View.GONE);
//            }

            _listView.tv_c_name.setText(tenant_matches_listings.get(position).getCustomer_name());
            _listView.tv_s_number.setText("Service No " + tenant_matches_listings.get(position).getLoan_number());
            // _listView.tv_s_amount.setText("Amount  " + tenant_matches_listings.get(position).getPaid_amount());
            Double amount=Double.parseDouble(tenant_matches_listings.get(position).getTotal_amount());
            Double bankCharges=Double.parseDouble(tenant_matches_listings.get(position).getBank_charges());
            Double total_amount=amount+bankCharges;
            //_listView.tv_s_amount.setText("Amount  " + tenant_matches_listings.get(position).getTotal_amount());
            _listView.tv_s_amount.setText("Amount  " + total_amount);

            _listView.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson = new Gson();
                    Intent i = new Intent(getActivity(), CollectionsDetailsActivity.class);
                    i.putExtra("data", gson.toJson(tenant_matches_listings.get(position)));
                    startActivity(i);
                }
            });

//            if (query.equals("")) {
//                convertView.setVisibility(View.VISIBLE);
//            } else if (mCollections.get(position).getLoan_number().contains(query)) {
//                convertView.setVisibility(View.VISIBLE);
//               // populate(convertView);
//
//            } else {
//                //populate(convertView);
//                convertView.setVisibility(View.GONE);
//               // convertView = new Space(getActivity());
//            }


            _listView.viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson = new Gson();
                    Intent i = new Intent(getActivity(), CollectionsDetailsActivity.class);
                    i.putExtra("data", gson.toJson(tenant_matches_listings.get(position)));
                    startActivity(i);
                }
            });
            return convertView;
        }

        class ViewHolder {

            TextView tv_c_name, tv_s_number,tv_s_amount;
            LinearLayout ll_item;
            Button viewDetails;
        }
    }


    private void attemptLogin(final String id) {
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
                    Globals.showToast(getContext(), "Unable Fetch Dat Please Try again later");
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
        mCollections.addAll(mResponsedata.getCollections());
        SCollections.addAll(mResponsedata.getCollections());
        mAdapter.notifyDataSetChanged();
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



}


package com.anyemi.recska.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.anyemi.recska.LoginActivity;
import com.anyemi.recska.NavigationActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.PaymentRequestModel;
import com.anyemi.recska.model.PendingTransactionModel;
import com.anyemi.recska.model.SbiCheckPaymentStatus;
import com.anyemi.recska.model.SbiPayPendingModel;
import com.anyemi.recska.model.StatusModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.anyemi.recska.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

public class PendingTransactionsFragment extends Fragment {


    private View rootView;
    ViewGroup header;

    boolean rental_aval = true;
    protected int pageId = 0;
    String typeId = "1";
    ListView lv_my_account;
    Button btn_search;
    Spinner spnr_sort_by;
    EditText et_search;
    LinearLayout linearLayout;
    ListingAdapter mAdapter;
    String records;
    ArrayList<PendingTransactionModel.TransactionsBean> mCollections = new ArrayList<>();
    ArrayList<PendingTransactionModel.TransactionsBean> SCollections = new ArrayList<>();
    ArrayList<PendingTransactionModel.TransactionsBean> ary_payments = new ArrayList<>();
    Object rdata;
    String query = "";

    PaymentRequestModel P_Model;
    PaymentRequestModel paymentRequestModel =new PaymentRequestModel();

    Gson gson = new Gson();
    Dialog infoDialog;
    JSONObject jsonObject;

    public PendingTransactionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_collection_layout, container, false);
        setHasOptionsMenu(false);

        mCollections.clear();
        SCollections.clear();
        initializeComponents();
        //initHeaderView();

        String user_id = SharedPreferenceUtil.getUserId(getActivity());

        if (user_id.equals("")) {
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
        } else {
            typeId = "1";
            user_id = user_id + "&start=" + pageId + "&type=" + typeId;
            getPendingTransactions(user_id);
        }
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


        lv_my_account.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && (lv_my_account.getLastVisiblePosition() -
                        lv_my_account.getHeaderViewsCount() -
                        lv_my_account.getFooterViewsCount()) >= (SCollections.size() - 1)) {

                 /*   if(Integer.parseInt(records)==SCollections.size()){
                        rental_aval=false;
                    }

                    */

                    if (rental_aval == true) {
                        pageId += 20;
                        String user_id = SharedPreferenceUtil.getUserId(getActivity());
                        user_id = user_id + "&start=" + pageId;
                        //  getPendingTransactions(user_id);

                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount - visibleItemCount != firstVisibleItem) {
                    //   float_btn_up.setVisibility(View.VISIBLE);
                }
                if (firstVisibleItem == 0) {
                    //    float_btn_up.setVisibility(View.GONE);
                }
            }
        });

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
        //  lv_my_account.addHeaderView(header, null, false);

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
        ArrayList<PendingTransactionModel.TransactionsBean> tenant_matches_listings;

        public ListingAdapter(Context c, ArrayList<PendingTransactionModel.TransactionsBean> mListings) {
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
                _listView.tv_service_type = convertView.findViewById(R.id.tv_service_type);
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

                //  _listView.tv_c_name.setVisibility(View.GONE);

                String response = tenant_matches_listings.get(position).getPayment_response();

                if (response != null) {

                    final Gson gson = new Gson();

                    SbiPayPendingModel sbiPayInitResaponseModel = new SbiPayPendingModel();

                    sbiPayInitResaponseModel = gson.fromJson(response, SbiPayPendingModel.class);

                    _listView.tv_s_amount.setText(sbiPayInitResaponseModel.getApiResp().getAmount());
                    _listView.tv_payment_date.setText(sbiPayInitResaponseModel.getApiResp().getTxnAuthDate());
                    _listView.tv_c_name.setText(sbiPayInitResaponseModel.getApiResp().getPayerVPA());
                    _listView.tv_s_number.setText(sbiPayInitResaponseModel.getApiResp().getUpiTransRefNo() + "");


                    if (sbiPayInitResaponseModel.getApiResp().getStatus().equals("S")) {

                        _listView.tv_service_type.setTextColor(getResources().getColor(R.color.green));
                        _listView.tv_s_amount.setTextColor(getResources().getColor(R.color.green));
                        _listView.tv_service_type.setText("Sucess");

                        _listView.tv_s_amount.setText(sbiPayInitResaponseModel.getApiResp().getAmount());
                    } else if (sbiPayInitResaponseModel.getApiResp().getStatus().equals("F")) {
                        _listView.tv_service_type.setTextColor(getResources().getColor(R.color.red_oval_color));
                        _listView.tv_s_amount.setTextColor(getResources().getColor(R.color.red_oval_color));
                        _listView.tv_service_type.setText("Failure");

                    } else {
                        _listView.tv_service_type.setTextColor(getResources().getColor(R.color.red_oval_color));
                        _listView.tv_s_amount.setTextColor(getResources().getColor(R.color.red_oval_color));
                        _listView.tv_service_type.setText(sbiPayInitResaponseModel.getApiResp().getStatusDesc());
                    }


                    final SbiPayPendingModel finalSbiPayInitResaponseModel = sbiPayInitResaponseModel;
                    final SbiPayPendingModel finalSbiPayInitResaponseModel1 = sbiPayInitResaponseModel;
                    _listView.ll_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            String request_model = tenant_matches_listings.get(position).getPayment_request();

                            P_Model = gson.fromJson(request_model, PaymentRequestModel.class);


                            // if (!finalSbiPayInitResaponseModel.getApiResp().getStatus().equals("S")) {
                                 //   openInfoDialog("Transaction has been initiated and please approve it from the SBI PSP app");
                                 String pspRefNo = finalSbiPayInitResaponseModel.getApiResp().getPspRefNo();
                                 String service_list_id = String.valueOf(tenant_matches_listings.get(position).getId());
                                 String custRefNo = finalSbiPayInitResaponseModel.getApiResp().getCustRefNo();
                                 String payment_id = String.valueOf(tenant_matches_listings.get(position).getPayment_id());
                                 P_Model.setPayment_id(payment_id);
                                 //   P_Model.setPayment_through("payment_id");
                                 try {

                                     jsonObject = new JSONObject();
                                     jsonObject.put("pspRefNo", pspRefNo);
                                     jsonObject.put("service_list_id", service_list_id);
                                     jsonObject.put("custRefNo", custRefNo);
                                     //  jsonObject.put("payment_id", payment_id);
                                     //  startTime = Utils.getCurrentTimeStamp();

                                     String status=finalSbiPayInitResaponseModel1.getApiResp().getStatus();
                                     if (status.equals("S") || status.equals("P") ){
                                         checkPaymentStatus(jsonObject.toString());
                                    }else{
                                         openInfoDialog(finalSbiPayInitResaponseModel1.getApiResp().getStatusDesc());
                                     }



                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }
                            // }


                                /*else if (mResponsedata.getApiResp().getStatus().equals("I")) {
                                    mTransDone = true;
                                    openInfoDialog(mResponsedata.getApiResp().getStatusDesc());
                                } else if (mResponsedata.getApiResp().getStatus().equals("F")) {
                                    mTransDone = true;
                                    openInfoDialog(mResponsedata.getApiResp().getStatusDesc());
                                }

                            */

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder {

            TextView tv_c_name, tv_s_number, tv_s_amount, tv_payment_date, tv_service_type;
            LinearLayout ll_item;
            Button viewDetails;
            ImageView iv_payment_type;
        }
    }


    private void checkPaymentStatus(final String request) {
        new BackgroundTask(getActivity(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.postPaymentTwo(getActivity(), request);
            }

            public void taskCompleted(Object data) {
                if (data != null || data.equals("")) {
                    try {
                        SbiCheckPaymentStatus mResponsedata = null;
                        mResponsedata = gson.fromJson(data.toString(), SbiCheckPaymentStatus.class);

                        //Create delay to check payment status

                        if (mResponsedata.getApiResp().getStatus().equals("P")) {
                            openInfoDialog("Request not approved yet");
                        } else if (mResponsedata.getApiResp().getStatus().equals("S")) {
                            openInfoDialog("Payment Success");
                        } else if (mResponsedata.getApiResp().getStatus().equals("R")) {
                            openInfoDialog(mResponsedata.getApiResp().getStatusDesc());
                        } else if (mResponsedata.getApiResp().getStatus().equals("F")) {
                            openInfoDialog(mResponsedata.getApiResp().getStatusDesc() + " : " + mResponsedata.getApiResp().getResponseMsg());
                        } else if (mResponsedata.getApiResp().getStatus().equals("T")) {
                            openInfoDialog(mResponsedata.getApiResp().getStatusDesc() + " : " + mResponsedata.getApiResp().getResponseMsg());
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                        Globals.showToast(getActivity(), "Payment Failed");

                    }

                } else {

                    Globals.showToast(getActivity(), "Unable Fetch Dat Please Try again later");
                }
            }
        } ,getString(R.string.loading_txt)).execute();
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
                    parseData(rdata);

                } else {
                    Globals.showToast(getContext(), "No Data Found");
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }

    private void parseData(Object data) {
        //mCollections.clear();
        // SCollections.clear();

        PendingTransactionModel mResponsedata = new PendingTransactionModel();
        mResponsedata = gson.fromJson(data.toString(), PendingTransactionModel.class);

        if (mResponsedata.getStatus() != null && mResponsedata.getStatus().equals("Failed")) {
              Globals.showToast(getActivity(),mResponsedata.getMsg());
        } else {
            mCollections.clear();
            SCollections.clear();

            try {
                mCollections.addAll(mResponsedata.getTransactions());
                SCollections.addAll(mResponsedata.getTransactions());
                //  ary_payments.addAll((ArrayList<PendingTransactionModel.TransactionsBean>) mResponsedata.getTransactions());
                //    records = String.valueOf(mResponsedata.getTotal_records());
                mAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                //Globals.showToast(getActivity(),mResponsedata.getMsg());
                Globals.showToast(getActivity(), "Sorry No data found");
            }
        }
    }


    private void openSearchDialog(String query) {


        if (!query.equals("")) {
            SCollections.clear();
            for (int i = 0; i < mCollections.size(); i++) {
              /*  if (mCollections.get(i).getTxn_id().contains(query)) {
                    SCollections.add(mCollections.get(i));
                }*/
            }
            Globals.showToast(getContext(), SCollections.size() + " Results Found");
            mAdapter.notifyDataSetChanged();
        } else {
            Globals.showToast(getContext(), "No Input Value Found");
        }
    }


    private void openInfoDialog(final String s) {

        if (infoDialog == null) {
            infoDialog = new Dialog(getActivity());
        }

        infoDialog.setContentView(R.layout.dialog_info);
        final TextView tv_info = infoDialog.findViewById(R.id.tv_info);
        final Button btn_send_sms = infoDialog.findViewById(R.id.btn_send_sms);
        final ProgressBar prgs_load = infoDialog.findViewById(R.id.prgs_load);
        btn_send_sms.setText("Dismiss");
        if (s.equals("Payment Success")) {
            // Globals.showToast(getApplicationContext(),"hvhvjhvjh potiiii peonnnn");
            submitPaymentDetails();
        }
        btn_send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                infoDialog.dismiss();
                if (!s.equals("Payment Success")) {
                    getPendingTransactions(SharedPreferenceUtil.getUserId(getActivity()));
                }
            }
        });

        if (s.equals("Payment Success")) {
            infoDialog.setCancelable(false);
            btn_send_sms.setText("Print Receipt");
        }


        if (s.equals("Transaction has been initiated and please approve it from the SBI PSP app")) {

            infoDialog.setCancelable(false);
            prgs_load.setVisibility(View.VISIBLE);
            btn_send_sms.setText("Cancel Request");
            btn_send_sms.setVisibility(View.GONE);

        }
        tv_info.setText(s);
        infoDialog.show();
    }


    private void submitPaymentDetails() {
        new BackgroundTask(getActivity(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.submitPayment(getActivity(), finalPaymentrequest());
            }

            public void taskCompleted(Object data) {

                StatusModel statusModel= new StatusModel();
                statusModel=gson.fromJson(data.toString(),StatusModel.class);
                if(statusModel.getStatus().equalsIgnoreCase("Success")){
                        Intent intent = new Intent(getActivity(), NavigationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("FRAGMENT", "COLLECTION");
                        startActivity(intent);
                }else{
                  Globals.showToast(getActivity(),statusModel.getMsg());
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }


    private PaymentRequestModel finalPaymentrequest() {

        try {

            paymentRequestModel.setTotal_amount(Utils.parseTwoDigitAmount(P_Model.getTotal_amount()));

            P_Model.setRr_number(jsonObject.getString("pspRefNo"));
            P_Model.setRr_number(jsonObject.getString("custRefNo"));
            P_Model.setTrsno(jsonObject.getString("custRefNo"));
            P_Model.setServiceCharge("0");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return P_Model;
    }


}


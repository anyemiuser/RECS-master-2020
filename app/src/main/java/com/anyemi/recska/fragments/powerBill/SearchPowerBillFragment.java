package com.anyemi.recska.fragments.powerBill;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.anyemi.recska.R;
import com.anyemi.recska.activities.PaymentModeActivityNew;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.DemandModel;
import com.anyemi.recska.model.DemandModelNew;
import com.anyemi.recska.model.PaymentRequestModel;
import com.anyemi.recska.model.RegisterModel;
import com.anyemi.recska.popups.SelectionDialog;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.anyemi.recska.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by SuryaTejaChalla on 06-04-2018.
 */

public class SearchPowerBillFragment extends Fragment {

    private View rootView;

    Spinner spnr_area_name;
    Spinner spnr_tax_type;
    EditText et_search;
    Button btn_search;
    String page, area_id, tax_type;

    ArrayList<String> mAreaArray = new ArrayList<>();
    ArrayList<String> mtaxArray = new ArrayList<>();

    ListView lv_my_account;
    LinearLayout ll_search, ll_details;
    ListingAdapter mAdapter;
    //ArrayList<DemandModel.EmiBean> emis = new ArrayList<>();
    ArrayList<DemandModelNew.EmiBean> emis = new ArrayList<>();
    ArrayList<DemandModelNew.TaxArrayBean> taxArrayBean = new ArrayList<>();
    ArrayList<DemandModel.LoanDetailsBean> loanDetailsBeans = new ArrayList<>();
    Object rdata;
    String query = "";
    TextView tv_c_name, tv_phone_num, tv_aadhar, tv_assment_num, tv_address, tv_tax_type;

    Bundle r_bundle;
    // DemandModel mResponsedata;

    DemandModelNew mResponsedata;
    Button btn_print_on_device;
    ArrayList<String> mSelectedEmis = new ArrayList<>();
    ArrayList<String> paymentMethods = new ArrayList<>();
    ArrayList<String> aryTaxNames = new ArrayList<>();
    ArrayList<String> aryTaxIds = new ArrayList<>();
    SelectionDialog selctionDialog;
    String mUserId;
    Dialog mBottomSheetDialog;
    double final_amount;
    String id_string = "";
    ViewGroup header;
    PaymentRequestModel paymentRequestModel;


    String r_bank_name, r_total_amount, r_payment_type, r_emi_ids,
            r_user_id, credit_service_tax, debit_service_tax_, gst_debit, gst_credit,
            user_charges, due_amount;

    String r_check_date, r_rrn_number, r_mobile_number, r_upi_id, r_assessment_id;

    Dialog dialog;
    EditText et_number;
    TextView tv_header;
    Button btn_pay;

    public SearchPowerBillFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_demand_slip_generation, container, false);
        setHasOptionsMenu(false);
        mUserId = SharedPreferenceUtil.getUserId(getActivity());
        initView();
        initHeaderView();


        aryTaxNames.clear();
        aryTaxIds.clear();

        aryTaxNames.addAll(Arrays.asList(getResources().getStringArray(R.array.tax_type_array)));
        aryTaxIds.addAll(Arrays.asList(getResources().getStringArray(R.array.tax_id_array)));

        paymentMethods.clear();
        paymentMethods.addAll(Arrays.asList(getResources().getStringArray(R.array.payment_names)));

        return rootView;
    }

    private void initHeaderView() {

        LayoutInflater inflater = getLayoutInflater();
        header = (ViewGroup) inflater.inflate(R.layout.header_deamand_slip, lv_my_account, false);
        lv_my_account.addHeaderView(header, null, false);


        tv_c_name = header.findViewById(R.id.tv_c_name);
        tv_phone_num = header.findViewById(R.id.tv_phone_num);
        tv_aadhar = header.findViewById(R.id.tv_aadhar);
        tv_assment_num = header.findViewById(R.id.tv_assment_num);
        tv_address = header.findViewById(R.id.tv_address);
        tv_tax_type = header.findViewById(R.id.tv_tax_type);
        ll_details = header.findViewById(R.id.ll_details);


        spnr_area_name = header.findViewById(R.id.spnr_area_name);
        spnr_tax_type = header.findViewById(R.id.spnr_tax_type);
        et_search = header.findViewById(R.id.et_search);
        btn_search = header.findViewById(R.id.btn_search);


        mAreaArray.clear();
        mAreaArray.addAll(Arrays.asList(getResources().getStringArray(R.array.area_array)));
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, mAreaArray);
        spnr_area_name.setAdapter(regionAdapter);
        mtaxArray.clear();
        mtaxArray.addAll(Arrays.asList(getResources().getStringArray(R.array.tax_type_array)));
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, mtaxArray);
        spnr_tax_type.setAdapter(stateAdapter);
        spnr_area_name.setVisibility(View.GONE);
        spnr_tax_type.setVisibility(View.GONE);
        spnr_area_name.setSelection(1);

        spnr_area_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                spnr_tax_type.setSelection(0);
//                if (position != 0) {
//                    area_id = getResources().getStringArray(R.array.area_id_array)[position];
//                }

                spnr_tax_type.setSelection(1);
                if (position != 0) {
                    area_id = getResources().getStringArray(R.array.area_id_array)[position];
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spnr_tax_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position != 0) {
                    tax_type = getResources().getStringArray(R.array.tax_id_array)[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_search.getText().toString().equals("")) {
                    Globals.showToast(getActivity(), "Assessment number should not be empty");
                }

//                else if (spnr_area_name.getSelectedItemPosition() == 0) {
//                    Globals.showToast(getActivity(), "Please select area");
//                } else if (spnr_tax_type.getSelectedItemPosition() == 0) {
//
//                    Globals.showToast(getActivity(), "Please select Tax type");
//                }


                else {
                    Utils.hideKeyboard(getActivity());
                    if (et_search.getText().toString().length() == 10) {
                        String s = et_search.getText().toString();
                        String start = s.substring(0, 5);
                        String end = s.substring(5);
                        String finals = start + " " + end;
                        et_search.setText(finals);
                    }

                    getDeatails();

                }
            }
        });


    }

    private void initView() {

        ll_search = rootView.findViewById(R.id.ll_search);
        lv_my_account = rootView.findViewById(R.id.lv_collection);
        btn_print_on_device = rootView.findViewById(R.id.btn_print_on_device);
        btn_print_on_device.setVisibility(View.GONE);


        selctionDialog = new SelectionDialog(getActivity());


        mAdapter = new ListingAdapter(getActivity(), emis);
        lv_my_account.setAdapter(mAdapter);


//        lv_my_account.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//
//              _li
//
//                if (mSelectedEmis.contains(emis.get(position).getId())) {
//                    mSelectedEmis.remove(emis.get(position).getId());
//                } else {
//                    mSelectedEmis.add(emis.get(position).getId());
//                }
//                mAdapter.notifyDataSetChanged();
//
//            }
//        });


        btn_print_on_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedEmis.size() > 0) {
                    // openPaymentOptionDialog();

                    openPaymentModesActivity();
                } else {
                    Globals.showToast(getActivity(), "Please click on Due");
                }

            }


        });


    }

    private void openPaymentModesActivity() {


        final_amount = 0;
        id_string = "";
        double usercharges = 0;
        double emiamount = 0;


        for (int position = 0; position < emis.size(); position++) {

            if (mSelectedEmis.contains(emis.get(position).getId())) {

                try {
                    for (int i = 0; i < taxArrayBean.size(); i++) {
                        Double maxLimit = Double.parseDouble(taxArrayBean.get(i).getTo());
                        emiamount = Double.parseDouble(emis.get(position).getEmi_amount());
                        if (maxLimit > emiamount) {
                            due_amount = String.valueOf(emiamount);
                            usercharges = Double.parseDouble(taxArrayBean.get(i).getTransaction_amount());
                            usercharges = usercharges + ((emiamount * Double.parseDouble(taxArrayBean.get(i).getExtra_tax())) / 100);
                            user_charges = String.valueOf(usercharges);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                double totalamount = Double.valueOf(emis.get(position).getEmi_amount())
//                        + Double.valueOf(emis.get(position).getOver_due()) + usercharges;

                double totalamount = emiamount + usercharges;

                final_amount = final_amount + totalamount;

                try {
                    for (int i = 0; i < taxArrayBean.size(); i++) {
                        Double maxLimit = Double.parseDouble(taxArrayBean.get(i).getTo());
                        if (maxLimit > final_amount) {
                            gst_credit = taxArrayBean.get(i).getGst_credit();
                            gst_debit = taxArrayBean.get(i).getGst_debit();
                            credit_service_tax = taxArrayBean.get(i).getCredit_service_tax_();
                            debit_service_tax_ = taxArrayBean.get(i).getDebit_service_tax_();
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }


        for (int i = 0; i < mSelectedEmis.size(); i++) {
            if (i == 0 || mSelectedEmis.size() == i - 1) {
                id_string = id_string + mSelectedEmis.get(i);
            } else {
                id_string = id_string + "&" + mSelectedEmis.get(i);
            }
        }

        r_bank_name = "";
        // r_total_amount = String.valueOf((int) final_amount);
        r_total_amount = String.valueOf(final_amount);
        r_payment_type = "";
        r_emi_ids = String.valueOf(id_string);
        r_user_id = SharedPreferenceUtil.getUserId(getActivity());
        r_check_date = "";
        r_rrn_number = "";
        if (mResponsedata.getLoan_details().getPhone() != null) {
            r_mobile_number = mResponsedata.getLoan_details().getPhone();
        } else {
            r_mobile_number = "";
        }

        r_upi_id = "";
        r_assessment_id = mResponsedata.getLoan_details().getLoan_number();

        if (final_amount != 0) {

            Intent i = new Intent(getActivity(), PaymentModeActivityNew.class);
            i.putExtra(Constants.PAYMENT_REQUEST_MODEL, preparePaymentRequest());
            startActivity(i);
        } else {
            Globals.showToast(getActivity(), "Unable to Process Amount");
        }
    }

    private void setData() {

        ll_details.setVisibility(View.VISIBLE);
        btn_print_on_device.setVisibility(View.VISIBLE);

        if (mResponsedata.getLoan_details().getCustomer_name() != null) {
            tv_c_name.setText(mResponsedata.getLoan_details().getCustomer_name());
        }

        if (mResponsedata.getLoan_details().getPhone() != null && !mResponsedata.getLoan_details().getPhone().equals("")) {
            tv_phone_num.setText(mResponsedata.getLoan_details().getPhone());
            tv_phone_num.setTextColor(getResources().getColor(R.color.Standard));
        } else {
            tv_phone_num.setText("Click to Update");
            tv_phone_num.setTextColor(getResources().getColor(R.color.red_oval_color));
        }

        if (mResponsedata.getLoan_details().getAdhaar() != null && !mResponsedata.getLoan_details().getAdhaar().equals("")) {
            tv_aadhar.setText(mResponsedata.getLoan_details().getAdhaar());
            tv_aadhar.setTextColor(getResources().getColor(R.color.Standard));
        } else {
            tv_aadhar.setText("Click to Update");
            tv_aadhar.setTextColor(getResources().getColor(R.color.red_oval_color));
        }


        if (mResponsedata.getLoan_details().getLoan_number() != null) {
            tv_assment_num.setText(mResponsedata.getLoan_details().getLoan_number());
        }

        if (mResponsedata.getLoan_details().getAddress() != null) {
            tv_address.setText(mResponsedata.getLoan_details().getAddress());
        }

//        if (mResponsedata.getLoan_details().getTax_type() != null) {
//            int taxtype = aryTaxIds.indexOf(mResponsedata.getLoan_details().getTax_type());
//            tv_tax_type.setText(aryTaxNames.get(taxtype));
//        }

        if (emis.size() > 0) {
            btn_print_on_device.setEnabled(true);
            btn_print_on_device.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btn_print_on_device.setTextColor(getResources().getColor(R.color.white));
            btn_print_on_device.setText("Make payment");
        } else {
            btn_print_on_device.setEnabled(false);
            btn_print_on_device.setBackgroundColor(getResources().getColor(R.color.gray));
            btn_print_on_device.setTextColor(getResources().getColor(R.color.red_oval_color));
            btn_print_on_device.setText("No Pending Dues");
        }


        tv_phone_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearchDialog("Phone Number");

            }
        });

        tv_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearchDialog("Aadhar");
            }
        });

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


    private String preparePaymentRequest() {

        paymentRequestModel = new PaymentRequestModel();
        paymentRequestModel.setUser_id(Integer.parseInt(r_user_id));
        paymentRequestModel.setEmi_ids(r_emi_ids);
        paymentRequestModel.setPayment_type(r_payment_type);
        paymentRequestModel.setBankname(r_bank_name);
        paymentRequestModel.setCheckdate(r_check_date);
        paymentRequestModel.setRr_number(r_rrn_number);
        paymentRequestModel.setMobile_number(r_mobile_number);
        paymentRequestModel.setUpi_id(r_upi_id);
        paymentRequestModel.setAssessment_id(r_assessment_id);
        paymentRequestModel.setGst_credit(gst_credit);
        paymentRequestModel.setGst_debit(gst_debit);
        paymentRequestModel.setCredit_service_tax_(credit_service_tax);
        paymentRequestModel.setDebit_service_tax_(debit_service_tax_);
        paymentRequestModel.setActualDueAmount(due_amount);
        paymentRequestModel.setServiceCharge(user_charges);
        paymentRequestModel.setTotal_amount(r_total_amount);

        return new Gson().toJson(paymentRequestModel);
    }

    public class ListingAdapter extends BaseAdapter {

        private Context c;
        private LayoutInflater mInflater;
        ViewHolder _listView;
        ArrayList<DemandModelNew.EmiBean> tenant_matches_listings;

        public ListingAdapter(Context c, ArrayList<DemandModelNew.EmiBean> emis) {
            this.tenant_matches_listings = emis;
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
                convertView = mInflater.inflate(R.layout.lv_emi_terms, null, false);


                _listView = new ViewHolder();

                _listView.ll_item = convertView.findViewById(R.id.ll_item);
                _listView.tv_due_date = convertView.findViewById(R.id.tv_due_date);
                _listView.tv_due = convertView.findViewById(R.id.tv_due);
                _listView.tv_fine = convertView.findViewById(R.id.tv_fine);
                _listView.tv_total = convertView.findViewById(R.id.tv_total);
                _listView.chbk_check_amount = convertView.findViewById(R.id.chbk_check_amount);
                convertView.setTag(_listView);
            } else {
                _listView = (ViewHolder) convertView.getTag();
            }

            String date = Utils.parseDate(tenant_matches_listings.get(position).getDue_date());

            _listView.tv_due_date.setText(date);

            // String amount_format=String.valueOf(tenant_matches_listings.get(position).getEmi_amount());
            String amount_format = Utils.parseAmount(String.valueOf(tenant_matches_listings.get(position).getEmi_amount()));
//            DecimalFormat df = new DecimalFormat("#,##,###.####", new DecimalFormatSymbols(Locale.US));
//            amount_format = df.format(Double.valueOf(amount_format));
            _listView.tv_due.setText("Rs." + amount_format + " /-");

            // _listView.tv_due.setText(tenant_matches_listings.get(position).getEmi_amount());

            double usercharges = 0;

            try {
                for (int i = 0; i < taxArrayBean.size(); i++) {
                    Double maxLimit = Double.parseDouble(taxArrayBean.get(i).getTo());
                    Double emiamount = Double.parseDouble(tenant_matches_listings.get(position).getEmi_amount());
                    if (maxLimit > emiamount) {
                        usercharges = Double.parseDouble(taxArrayBean.get(i).getTransaction_amount());
                        usercharges = Double.parseDouble(taxArrayBean.get(i).getTransaction_amount());
                        if (taxArrayBean.get(i).getExtra_tax() != null) {
                            usercharges = usercharges + ((emiamount * Double.parseDouble(taxArrayBean.get(i).getExtra_tax())) / 100);
                        }
                        _listView.tv_fine.setText(taxArrayBean.get(i).getTransaction_amount());
                        amount_format = String.valueOf(usercharges);
                        //DecimalFormat df = new DecimalFormat("#,##,###.####", new DecimalFormatSymbols(Locale.US));

                        //amount_format = df.format(Double.valueOf(amount_format));
                        amount_format = Utils.parseAmount(amount_format);
                        _listView.tv_fine.setText("Rs." + amount_format + " /-");
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {


                if (tenant_matches_listings.get(position).getEmi_amount() == null ||
                        tenant_matches_listings.get(position).getEmi_amount().equals("")) {
                    _listView.tv_total.setText("Rs." + "0" + " /-");
                } else {

                    double totalamount = Double.valueOf(tenant_matches_listings.get(position).getEmi_amount())
                            + Double.valueOf(tenant_matches_listings.get(position).getOver_due()) + usercharges;


                    amount_format = Utils.parseAmount(String.valueOf(totalamount));

                    _listView.tv_total.setText("Rs." + amount_format + " /-");

                }


                if (mSelectedEmis.contains(tenant_matches_listings.get(position).getId())) {
                    _listView.chbk_check_amount.setChecked(true);
                } else {
                    _listView.chbk_check_amount.setChecked(false);
                }


                _listView.ll_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        checkDues(tenant_matches_listings, position);
                        // notifyDataSetChanged();
                    }

                });

                _listView.chbk_check_amount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        checkDues(tenant_matches_listings, position);
                        //  notifyDataSetChanged();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            TextView tv_due_date, tv_due, tv_fine, tv_total;
            LinearLayout ll_item;
            CheckBox chbk_check_amount;
        }
    }


    private void checkDues(ArrayList<DemandModelNew.EmiBean> tenant_matches_listings, int position) {

        if (mSelectedEmis.size() == position) {
            if (mSelectedEmis.contains(tenant_matches_listings.get(position).getId())) {
                mSelectedEmis.remove(tenant_matches_listings.get(position).getId());
            } else {
                mSelectedEmis.add(tenant_matches_listings.get(position).getId());
            }
        } else if (mSelectedEmis.contains(tenant_matches_listings.get(position).getId())) {
            for (int i = 0; i < tenant_matches_listings.size(); i++) {
                if (i >= position) {
                    mSelectedEmis.remove(tenant_matches_listings.get(i).getId());
                }
            }
        } else {
            Globals.showToast(getActivity(), "Please check Previous dues");
        }

        mAdapter.notifyDataSetChanged();

    }

    private void getDeatails() {
        new BackgroundTask(getActivity(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.getAssessment(getActivity(), buildLoginRequest());
            }

            public void taskCompleted(Object data) {

                try {
                    if (data != null || data.equals("")) {

                        Gson gson = new Gson();
                        mResponsedata = new DemandModelNew();
                        mResponsedata = gson.fromJson(data.toString(), DemandModelNew.class);
//                        if(mResponsedata.getStatus().equals("Failed")){
//                            Globals.showToast(getActivity(), mResponsedata.getMsg());
//                        }else {
                        //mResponsedata.getLoan_details().getArea();
                        emis.clear();
                        taxArrayBean.clear();
                        mSelectedEmis.clear();
                        emis.addAll(mResponsedata.getEmi());
                        taxArrayBean.addAll(mResponsedata.getTax_array());
                        setData();
                        mAdapter.notifyDataSetChanged();
                        //  }
                    } else {
                        Globals.showToast(getActivity(), "Service Number not found");
                    }
                } catch (Exception e) {
                    Globals.showToast(getActivity(), "Service Number not found");
                    e.printStackTrace();
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }


    public String buildLoginRequest() {
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("assessmentNo", et_search.getText().toString());
            requestObject.put("service_area", area_id);
            requestObject.put("property_type", tax_type);
            requestObject.put("user_id", SharedPreferenceUtil.getUserId(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject.toString();
    }


    private void openSearchDialog(final String query) {

        dialog = new Dialog(getActivity());
        dialog.setTitle("Reset " + query);
        dialog.setContentView(R.layout.dialog_change_number);


        btn_pay = dialog.findViewById(R.id.btn_pay);


        tv_header = dialog.findViewById(R.id.tv_header);
        et_number = dialog.findViewById(R.id.et_number);
        int maxLength = 12;

        if (query.equals("Phone Number")) {
            maxLength = 10;
        } else {
            maxLength = 12;
        }

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        et_number.setFilters(FilterArray);

        tv_header.setText("Update " + query);


        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                if (query.equals("Phone Number")) {
                    if (et_number.getText().toString().equals("")) {
                        Globals.showToast(getActivity(), "Please enter Phone number");
                    } else if (et_number.getText().toString().length() == 10) {
                        updatePhoneNumber(et_number.getText().toString());
                    } else {
                        Globals.showToast(getActivity(), "Please enter valid Phone number");
                    }
                } else if (query.equals("Aadhar")) {
                    if (et_number.getText().toString().equals("")) {
                        Globals.showToast(getActivity(), "Please enter Aadhar number");
                    } else if (et_number.getText().toString().length() == 12) {
                        updateAadharNumber(et_number.getText().toString());
                    } else {
                        Globals.showToast(getActivity(), "Please enter valid Aadhar Number");
                    }
                }
            }
        });
        dialog.show();
    }


    private void updatePhoneNumber(final String s) {
        new BackgroundTask(getActivity(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.updatePhoneNumber(getActivity(), prepareRegisterRequest(s));
            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {
                    try {
                        Gson gson = new Gson();
                        RegisterModel registerModel = new RegisterModel();
                        registerModel = gson.fromJson(data.toString(), RegisterModel.class);
                        if (registerModel.getStatus().equals("sucess")) {
                            getDeatails();
                            Globals.showToast(getActivity(), registerModel.getMsg());
                        } else {
                            Globals.showToast(getActivity(), "Unable Submit FeedBack Please Try again later");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Globals.showToast(getActivity(), "Unable Fetch Dat Please Try again later");
                }
            }
        }, getString(R.string.loading_txt)).execute();

    }

    private String prepareRegisterRequest(String s) {
        try {
            JSONObject jsonObject;
            jsonObject = new JSONObject();
            jsonObject.put("mobile_number", s);
            jsonObject.put("service_number", mResponsedata.getLoan_details().getLoan_number());
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    private void updateAadharNumber(final String s) {
        new BackgroundTask(getActivity(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.updateAadharNumber(getActivity(), prepareRequest(s));
            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {
                    try {
                        Gson gson = new Gson();
                        RegisterModel registerModel = new RegisterModel();
                        registerModel = gson.fromJson(data.toString(), RegisterModel.class);
                        if (registerModel.getStatus().equals("sucess")) {
                            getDeatails();
                            Globals.showToast(getActivity(), registerModel.getMsg());
                        } else {
                            Globals.showToast(getActivity(), "Unable Update Please Try again later");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Globals.showToast(getActivity(), "Unable Fetch Dat Please Try again later");
                }
            }
        }, getString(R.string.loading_txt)).execute();

    }

    private String prepareRequest(String s) {
        try {
            JSONObject jsonObject;
            jsonObject = new JSONObject();
            jsonObject.put("aadhar_number", s);
            jsonObject.put("service_number", mResponsedata.getLoan_details().getLoan_number());
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}


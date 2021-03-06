package com.anyemi.recska.fragments;

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
import android.widget.AutoCompleteTextView;
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
import com.anyemi.recska.activities.UpdateBillModel;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.AreaModel;
import com.anyemi.recska.model.CollectionsModel;
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by SuryaTejaChalla on 12-12-2017.
 */

public class SearchBillFragment extends Fragment {

    private View rootView;

    Spinner spnr_area_name;
    Spinner spnr_tax_type, spnr_type, spnr_village;
    EditText et_search;
    Button btn_search;
    String page, area_id, tax_type;

    ArrayList<String> mAreaArray = new ArrayList<>();
    ArrayList<String> mtaxArray = new ArrayList<>();
    ArrayList<String> ero_names_array = new ArrayList<>();
    ArrayList<String> ero_ids_array = new ArrayList<>();

    ListView lv_my_account;
    LinearLayout ll_search, ll_details, ll_footer;
    ListingAdapter mAdapter;
    ArrayList<DemandModelNew.EmiBean> emis = new ArrayList<>();

    TextView tv_c_name, tv_phone_num, tv_aadhar, tv_assment_num, tv_address, tv_ero, tv_tax_type;
    AutoCompleteTextView act_search;


    DemandModelNew mResponsedata;
    Button btn_print_on_device;
    ArrayList<String> mSelectedEmis = new ArrayList<>();

    ArrayList<String> aryTaxNames = new ArrayList<>();
    ArrayList<String> aryTaxIds = new ArrayList<>();

    ArrayList<AreaModel.AREAMASTERBean> areas_master = new ArrayList<>();
    ArrayList<String> areas_names = new ArrayList<>();
    ArrayList<String> areas_service_codes = new ArrayList<>();
    SelectionDialog selctionDialog;
    String mUserId;
    double final_amount;
    String id_string = "";
    String ero_id = "0";
    ViewGroup header;

    EditText et_bill, et_arrear;
    ViewGroup footer;
    PaymentRequestModel paymentRequestModel;


    String r_bank_name, r_total_amount, r_payment_type, r_emi_ids,
            r_user_id, credit_service_tax, debit_service_tax_, gst_debit, gst_credit,
            user_charges, due_amount;

    String r_check_date, r_rrn_number, r_mobile_number, r_upi_id, r_assessment_id;

    Dialog dialog;
    EditText et_number;
    TextView tv_header;
    Button btn_pay;
    Boolean isAccepted=false;
    public SearchBillFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_demand_slip_generation, container, false);
        setHasOptionsMenu(false);
        mUserId = SharedPreferenceUtil.getUserId(getActivity());
        initView();
        r_user_id=SharedPreferenceUtil.getUserId(getActivity());
        initHeaderView();
        initData();


        aryTaxNames.clear();
        aryTaxIds.clear();

        aryTaxNames.addAll(Arrays.asList(getResources().getStringArray(R.array.tax_type_array)));
        aryTaxIds.addAll(Arrays.asList(getResources().getStringArray(R.array.tax_id_array)));


       attemptLogin(SharedPreferenceUtil.getUserId(getActivity()));

        return rootView;
    }


    private void initData() {
        Gson gson = new Gson();
        try {
            InputStream is = getResources().getAssets().open("area_master.json");
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                json = new String(data, StandardCharsets.UTF_8);
            }
            AreaModel areaModel = new AreaModel();
            areaModel = gson.fromJson(json, AreaModel.class);

            areas_master.clear();
            areas_master.addAll(areaModel.getAREA_MASTER());

            areas_names.clear();
            areas_service_codes.clear();

            for (int i = 0; i < areas_master.size(); i++) {

                if (areas_master.get(i).getEROCD().equals(ero_id)) {
                    areas_names.add(areas_master.get(i).getAREANAME());
                    areas_service_codes.add(areas_master.get(i).getAREACODE());
                }

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, areas_names);
            spnr_village.setAdapter(adapter);
          /*  Log.e("spnr_tax_type",SharedPreferenceUtil.getEro(getActivity()));
            if(!SharedPreferenceUtil.getEro(getActivity()).equals("")){
                spnr_type.setSelection(Integer.parseInt(SharedPreferenceUtil.getEro(getActivity())));
            }
            if(!SharedPreferenceUtil.getAREA(getActivity()).equals("")){
                spnr_village.setSelection(Integer.parseInt(SharedPreferenceUtil.getAREA(getActivity())));
            }
            if(!SharedPreferenceUtil.getServiceNo(getActivity()).equals("")){
                et_search.setText(SharedPreferenceUtil.getServiceNo(getActivity()));
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initHeaderView() {

        LayoutInflater inflater = getLayoutInflater();
        header = (ViewGroup) inflater.inflate(R.layout.header_deamand_slip, lv_my_account, false);
        lv_my_account.addHeaderView(header, null, false);

        footer = (ViewGroup) inflater.inflate(R.layout.footer_bill_pay, lv_my_account, false);
        lv_my_account.addFooterView(footer, null, false);

        ll_footer = footer.findViewById(R.id.ll_footer);

        et_bill = footer.findViewById(R.id.et_bill);
        et_arrear = footer.findViewById(R.id.et_arrear);
        ll_footer.setVisibility(View.GONE);



        tv_c_name = header.findViewById(R.id.tv_c_name);
        tv_phone_num = header.findViewById(R.id.tv_phone_num);
        tv_aadhar = header.findViewById(R.id.tv_aadhar);
        tv_assment_num = header.findViewById(R.id.tv_assment_num);
        tv_address = header.findViewById(R.id.tv_address);
        tv_ero = header.findViewById(R.id.tv_ero);
        act_search = header.findViewById(R.id.act_search);
        act_search.setVisibility(View.GONE);
        tv_tax_type = header.findViewById(R.id.tv_tax_type);
        ll_details = header.findViewById(R.id.ll_details);


        spnr_area_name = header.findViewById(R.id.spnr_area_name);
        spnr_tax_type = header.findViewById(R.id.spnr_tax_type);
        spnr_type = header.findViewById(R.id.spnr_type);
        spnr_village = header.findViewById(R.id.spnr_village);
        et_search = header.findViewById(R.id.et_search);
        btn_search = header.findViewById(R.id.btn_search);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, areas_names);
        act_search.setThreshold(2);
        act_search.setAdapter(adapter);


        act_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                int index = areas_names.indexOf(act_search.getText().toString());
                et_search.setText(areas_service_codes.get(index));
            }
        });


        //btn_search.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(10)});

        if (SharedPreferenceUtil.getPrintHeader(getContext()).equals(Constants.PRINT_HEADER_APEPDCL)) {
            et_search.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        } else {
            et_search.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        }

        mAreaArray.clear();
        mAreaArray.addAll(Arrays.asList(getResources().getStringArray(R.array.area_array)));
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, mAreaArray);
        spnr_area_name.setAdapter(regionAdapter);

        mtaxArray.clear();
        mtaxArray.addAll(Arrays.asList(getResources().getStringArray(R.array.tax_type_array)));
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, mtaxArray);
        spnr_tax_type.setAdapter(stateAdapter);

        ero_names_array.clear();
        ero_ids_array.clear();
        ero_names_array.addAll(Arrays.asList(getResources().getStringArray(R.array.ero_names)));
        ero_ids_array.addAll(Arrays.asList(getResources().getStringArray(R.array.ero_ids)));
        ArrayAdapter<String> feederAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, ero_names_array);
        spnr_type.setAdapter(feederAdapter);

        spnr_area_name.setVisibility(View.GONE);
        spnr_tax_type.setVisibility(View.GONE);
        spnr_area_name.setSelection(1);

        spnr_area_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                spnr_tax_type.setSelection(1);
                if (position != 0) {

                    area_id = getResources().getStringArray(R.array.area_id_array)[position];
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spnr_village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {


                et_search.setText(areas_service_codes.get(position));


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
                } else {
                    openSearchConditionDialog("");

                }
            }
        });


        spnr_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    ero_id = getResources().getStringArray(R.array.ero_ids)[position];
                }

                act_search.setText("");
                et_search.setText("");
                initData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

        btn_print_on_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (performValidations()) {
                   //  openPaymentModesActivity();
                   int USER_ID = Integer.parseInt(SharedPreferenceUtil.getUserId(getActivity()));
                   String MOBILE_NUMBER="";
                   String ASSESMENT_NUMBER="";
                   String SERVICE_CHARGE=mResponsedata.getEmi().get(0).getService_charge();

                    if (mResponsedata.getLoan_details().getPhone() != null) {
                        MOBILE_NUMBER = mResponsedata.getLoan_details().getPhone();
                    } else {
                        MOBILE_NUMBER = "";
                    }
                    ASSESMENT_NUMBER = mResponsedata.getLoan_details().getLoan_number();

                    paymentRequestModel = new PaymentRequestModel();
                    paymentRequestModel.setUser_id(USER_ID);
                    paymentRequestModel.setEmi_ids(mSelectedEmis.get(0));
                    paymentRequestModel.setPayment_type("");
                    paymentRequestModel.setBankname("");
                    paymentRequestModel.setCheckdate("");
                    paymentRequestModel.setRr_number("");
                    paymentRequestModel.setMobile_number(MOBILE_NUMBER);
                    paymentRequestModel.setUpi_id("");
                    paymentRequestModel.setAssessment_id(ASSESMENT_NUMBER);

                  /*  paymentRequestModel.setGst_credit(GST_CREDIT_CARD);
                    paymentRequestModel.setGst_debit(GST_DEBIT_CARD);

                    paymentRequestModel.setCredit_service_tax_(CREDIT_SERVICE_TAX);
                    paymentRequestModel.setDebit_service_tax_(DEBIT_SERVICE_TAX);


                   */

                    paymentRequestModel.setActualDueAmount(et_bill.getText().toString());
                    paymentRequestModel.setServiceCharge(SERVICE_CHARGE);
                    paymentRequestModel.setTotal_amount(mResponsedata.getEmi().get(0).getFinal_amount()+"");
                    paymentRequestModel.setPayment_through("Mobile");


                    String request = new Gson().toJson(paymentRequestModel);


                    if (mResponsedata.getEmi().get(0).getFinal_amount() != 0) {
                        upDateBillAmount(request);
                    } else {
                        Globals.showToast(getActivity(), "Unable to Process Amount");
                    }

                   // calculateAmount();
                }
            }

            private boolean performValidations() {
                boolean isValid = false;

                if (!SharedPreferenceUtil.getLoginType(getActivity()).equals(Constants.LOGIN_TYPE_CUSTOMER)) {
                    if (mSelectedEmis.size() == 0) {
                        Globals.showToast(getActivity(), "Please click on Due");
                    }


                    /*/else if (et_bill.getText().toString().equals("")) {
                        Globals.showToast(getActivity(), "Please Enter Bill Amount");
                    } else if (et_arrear.getText().toString().equals("")) {
                        Globals.showToast(getActivity(), "Please Enter Arreas Amount");
                    } */


                    else {
                        isValid = true;
                    }
                }else{
                    if (mSelectedEmis.size() == 0) {
                        Globals.showToast(getActivity(), "Please click on Due");
                    }  else {
                        isValid = true;
                    }


                }






                return isValid;
            }


        });
    }

    // method to calculate the total amount consisting of bills, service charge
//    private void calculateAmount() {
//
//        String EMI_ID;
//        int USER_ID;
//        String MOBILE_NUMBER;
//        String ASSESMENT_NUMBER;
//        String GST_CREDIT_CARD = "";
//        String GST_DEBIT_CARD = "";
//        String CREDIT_SERVICE_TAX = "";
//        String DEBIT_SERVICE_TAX = "";
//        String ACTUAL_DUE_AMOUNT = "";
//        String SERVICE_CHARGE = "";
//        String TOTAL_AMOUNT = "";
//
//        double User_Charge = 0;
//        double Emi_Amount = 0; //with Arrears etc
//        double Bill_Amount = 0; // without Arrears etc
//        double Final_Bill_Amount = 0;  // Amount to pay include service charge exclude bank charges
//        final_amount = 0;
//
//
//        USER_ID = Integer.parseInt(SharedPreferenceUtil.getUserId(getActivity()));
//
//        if (mResponsedata.getLoan_details().getPhone() != null) {
//            MOBILE_NUMBER = mResponsedata.getLoan_details().getPhone();
//        } else {
//            MOBILE_NUMBER = "";
//        }
//        ASSESMENT_NUMBER = mResponsedata.getLoan_details().getLoan_number();
//        SERVICE_CHARGE = Utils.parseTwoDigitAmount(String.valueOf(User_Charge));
//
//        for (int position = 0; position < emis.size(); position++) {
//
//            if (mSelectedEmis.contains(emis.get(position).getId())) {
//                try {
//                    for (int i = 0; i < taxArrayBean.size(); i++) {
//                        Double maxLimit = Double.parseDouble(taxArrayBean.get(i).getTo());
//                        Emi_Amount = Double.parseDouble(emis.get(position).getEmi_amount());  //2177
//                        Bill_Amount = Double.parseDouble(emis.get(position).getBillamt()); // without Arrears etc dummy
//
//                        if (maxLimit > Emi_Amount) {
//                            User_Charge = Double.parseDouble(taxArrayBean.get(i).getTransaction_amount());
//                            User_Charge = User_Charge + ((Emi_Amount * Double.parseDouble(taxArrayBean.get(i).getExtra_tax())) / 100);
//                            SERVICE_CHARGE = Utils.parseTwoDigitAmount(String.valueOf(User_Charge));
//                            break;
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (!SharedPreferenceUtil.getLoginType(getActivity()).equals(Constants.LOGIN_TYPE_CUSTOMER)) {
//                    ll_footer.setVisibility(View.VISIBLE);
//
//                    if(et_bill.getText().toString().equals("") && et_arrear.getText().toString().equals("")){
//
//                        Final_Bill_Amount = Emi_Amount + User_Charge;
//                    }else if(et_arrear.getText().toString().equals("")){
//
//                        Final_Bill_Amount = Integer.parseInt(et_bill.getText().toString()) +
//                                User_Charge
//                                +Double.parseDouble(emis.get(0).getReconnection_fee());
//                    }else if(et_bill.getText().toString().equals("")){
//
//                        Final_Bill_Amount = Double.parseDouble(emis.get(0).getBillamt())+
//                                +Double.parseDouble(emis.get(0).getReconnection_fee())+
//                        Integer.parseInt(et_arrear.getText().toString()) + User_Charge;
//                    }else {
//
//                        Final_Bill_Amount = Integer.parseInt(et_bill.getText().toString()) +
//                                Integer.parseInt(et_arrear.getText().toString()) + User_Charge
//                                + Double.parseDouble(emis.get(0).getReconnection_fee());
//                    }
//
//
//                }else{
//                    ll_footer.setVisibility(View.GONE);
//                    Final_Bill_Amount = Emi_Amount + User_Charge;
//
//                    et_bill.setText(Final_Bill_Amount+"");
//                    et_arrear.setText("0");
//
//
//                }
//
//
//
//
//
//
//
//
//                final_amount = final_amount + Final_Bill_Amount;
//
//                TOTAL_AMOUNT = Utils.parseTwoDigitAmount(String.valueOf(final_amount));
//                ACTUAL_DUE_AMOUNT = Utils.parseTwoDigitAmount(String.valueOf(Emi_Amount));
//                //ACTUAL_DUE_AMOUNT = Utils.parseTwoDigitAmount(String.valueOf(Bill_Amount));
//
//                /*
//                 ** Getting Tax Details
//                 */
//
//
//                try {
//                    for (int i = 0; i < taxArrayBean.size(); i++) {
//                        Double maxLimit = Double.parseDouble(taxArrayBean.get(i).getTo());
//                        if (maxLimit > final_amount) {
//                            GST_CREDIT_CARD = taxArrayBean.get(i).getGst_credit();
//                            GST_DEBIT_CARD = taxArrayBean.get(i).getGst_debit();
//                            CREDIT_SERVICE_TAX = taxArrayBean.get(i).getCredit_service_tax_();
//                            DEBIT_SERVICE_TAX = taxArrayBean.get(i).getDebit_service_tax_();
//                            break;
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }
//
//        /*
//         ** Prepare Selected Emi
//         */
//
//        id_string = "";
//        for (int i = 0; i < mSelectedEmis.size(); i++) {
//            if (i == 0 || mSelectedEmis.size() == i - 1) {
//                id_string = id_string + mSelectedEmis.get(i);
//            } else {
//                id_string = id_string + "&" + mSelectedEmis.get(i);
//            }
//        }
//
//        EMI_ID = id_string;
//
//
//        paymentRequestModel = new PaymentRequestModel();
//        paymentRequestModel.setUser_id(USER_ID);
//        paymentRequestModel.setEmi_ids(EMI_ID);
//        paymentRequestModel.setPayment_type("");
//        paymentRequestModel.setBankname("");
//        paymentRequestModel.setCheckdate("");
//        paymentRequestModel.setRr_number("");
//        paymentRequestModel.setMobile_number(MOBILE_NUMBER);
//        paymentRequestModel.setUpi_id("");
//        paymentRequestModel.setAssessment_id(ASSESMENT_NUMBER);
//
//        paymentRequestModel.setGst_credit(GST_CREDIT_CARD);
//        paymentRequestModel.setGst_debit(GST_DEBIT_CARD);
//
//        paymentRequestModel.setCredit_service_tax_(CREDIT_SERVICE_TAX);
//        paymentRequestModel.setDebit_service_tax_(DEBIT_SERVICE_TAX);
//
//        paymentRequestModel.setActualDueAmount(et_bill.getText().toString());
//        paymentRequestModel.setServiceCharge(SERVICE_CHARGE);
//        paymentRequestModel.setTotal_amount(TOTAL_AMOUNT);
//        paymentRequestModel.setPayment_through("Mobile");
//
//
//        String request = new Gson().toJson(paymentRequestModel);
//
//
//        if (final_amount != 0) {
//
//            upDateBillAmount(request);
//
//
////            Intent i = new Intent(getActivity(), PaymentModeActivityNew.class);
////            i.putExtra(Constants.PAYMENT_REQUEST_MODEL, request);
////            startActivity(i);
//        } else {
//            Globals.showToast(getActivity(), "Unable to Process Amount");
//        }
//    }
//
//    private void openPaymentModesActivity() {
//
//        /*
//         ** Getting Service Charge Details
//         */
//
//        final_amount = 0;
//        id_string = "";
//
//        double User_Charge = 0;
//        double Emi_Amount = 0; //with Arrears etc
//        double Bill_Amount = 0; // without Arrears etc
//        double Final_Bill_Amount = 0;  // Amount to pay include service charge exclude bank charges
//
//
//        for (int position = 0; position < emis.size(); position++) {
//            if (mSelectedEmis.contains(emis.get(position).getId())) {
//                try {
//                    for (int i = 0; i < taxArrayBean.size(); i++) {
//                        Double maxLimit = Double.parseDouble(taxArrayBean.get(i).getTo());
//                        Emi_Amount = Double.parseDouble(emis.get(position).getEmi_amount());  //2177
//                        Bill_Amount = Double.parseDouble(emis.get(position).getBillamt()); // without Arrears etc dummy
//
//                        if (maxLimit > Emi_Amount) {
//                            User_Charge = Double.parseDouble(taxArrayBean.get(i).getTransaction_amount());
//                            User_Charge = User_Charge + ((Emi_Amount * Double.parseDouble(taxArrayBean.get(i).getExtra_tax())) / 100);
//                            User_Charge = Math.round(User_Charge);
//                            user_charges = Utils.parseTwoDigitAmount(String.valueOf(Math.round(User_Charge)));
//                            System.out.println(user_charges);
//                            break;
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                Final_Bill_Amount = Emi_Amount + User_Charge;
//
//                final_amount = final_amount + Final_Bill_Amount;
//
//                /*
//                 ** Getting Tax Details
//                 */
//
//
//                try {
//                    for (int i = 0; i < taxArrayBean.size(); i++) {
//                        Double maxLimit = Double.parseDouble(taxArrayBean.get(i).getTo());
//                        if (maxLimit > final_amount) {
//                            gst_credit = taxArrayBean.get(i).getGst_credit();
//                            gst_debit = taxArrayBean.get(i).getGst_debit();
//                            credit_service_tax = taxArrayBean.get(i).getCredit_service_tax_();
//                            debit_service_tax_ = taxArrayBean.get(i).getDebit_service_tax_();
//                            break;
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }
//
//        /*
//         ** Prepare Selected Emi
//         */
//
//
//        for (int i = 0; i < mSelectedEmis.size(); i++) {
//            if (i == 0 || mSelectedEmis.size() == i - 1) {
//                id_string = id_string + mSelectedEmis.get(i);
//            } else {
//                id_string = id_string + "&" + mSelectedEmis.get(i);
//            }
//        }
//
//        r_bank_name = "";
//        r_total_amount = String.valueOf(final_amount);
//        due_amount = String.valueOf(Emi_Amount);
//        // due_amount=String.valueOf(Bill_Amount);
//        r_payment_type = "";
//        r_emi_ids = String.valueOf(id_string);
//        r_user_id = SharedPreferenceUtil.getUserId(getActivity());
//        r_check_date = "";
//        r_rrn_number = "";
//
//        if (mResponsedata.getLoan_details().getPhone() != null) {
//            r_mobile_number = mResponsedata.getLoan_details().getPhone();
//        } else {
//            r_mobile_number = "";
//        }
//
//        r_upi_id = "";
//        r_assessment_id = mResponsedata.getLoan_details().getLoan_number();
//
//        if (final_amount != 0) {
//            //upDateBillAmount();
//
//        } else {
//            Globals.showToast(getActivity(), "Unable to Process Amount");
//        }
//
//    }

    private void setData() {

        ll_details.setVisibility(View.VISIBLE);

        if (!SharedPreferenceUtil.getLoginType(getActivity()).equals(Constants.LOGIN_TYPE_CUSTOMER)) {
            ll_footer.setVisibility(View.VISIBLE);
        }else{
            ll_footer.setVisibility(View.GONE);


        }

        btn_print_on_device.setVisibility(View.VISIBLE);

        if (mResponsedata.getLoan_details().getCustomer_name() != null) {
            tv_c_name.setText(mResponsedata.getLoan_details().getCustomer_name());
        }

        if (mResponsedata.getLoan_details().getPhone() != null && !mResponsedata.getLoan_details().getPhone().equals("")) {
            String phn = mResponsedata.getLoan_details().getPhone();
            phn = phn.substring(phn.length() - 3);
            tv_phone_num.setText("*****" + phn);
            tv_phone_num.setTextColor(getResources().getColor(R.color.Standard));
        } else {
            tv_phone_num.setText("Click to Update");
            tv_phone_num.setTextColor(getResources().getColor(R.color.red_oval_color));
        }

        if (mResponsedata.getLoan_details().getAdhaar() != null && !mResponsedata.getLoan_details().getAdhaar().equals("")) {
            String phn = mResponsedata.getLoan_details().getAdhaar();
            phn = phn.substring(phn.length() - 3);
            tv_aadhar.setText("*****" + phn);
            //  tv_aadhar.setText(mResponsedata.getLoan_details().getAdhaar().toString());
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

        if (mResponsedata.getLoan_details().getRoi() != null) {
            tv_ero.setText(mResponsedata.getLoan_details().getRoi());
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
        paymentRequestModel.setPayment_through("Mobile");

        return new Gson().toJson(paymentRequestModel);
    }

    public class ListingAdapter extends BaseAdapter {

        private Context c;
        private LayoutInflater mInflater;
        ListingAdapter.ViewHolder _listView;
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
            final DemandModelNew.EmiBean data = tenant_matches_listings.get(position);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.lv_emi_terms, null, false);


                _listView = new ListingAdapter.ViewHolder();

                _listView.ll_item = convertView.findViewById(R.id.ll_item);
                _listView.tv_due_date = convertView.findViewById(R.id.tv_due_date);
                _listView.tv_due = convertView.findViewById(R.id.tv_due);
                _listView.tv_last_paid_date = convertView.findViewById(R.id.tv_last_paid_date);
                _listView.tv_last_paid_amount = convertView.findViewById(R.id.tv_last_paid_amount);
                _listView.tv_surcharge = convertView.findViewById(R.id.tv_surcharge);
                _listView.tv_fine = convertView.findViewById(R.id.tv_fine);
                _listView.tv_total = convertView.findViewById(R.id.tv_total);
                _listView.tv_rec = convertView.findViewById(R.id.tv_rec);
                _listView.tv_adj_amount = convertView.findViewById(R.id.tv_adj_amount);
                _listView.tv_arrears = convertView.findViewById(R.id.tv_arrears);
                _listView.chbk_check_amount = convertView.findViewById(R.id.chbk_check_amount);
                convertView.setTag(_listView);
            } else {
                _listView = (ListingAdapter.ViewHolder) convertView.getTag();
            }


            double User_Charges = 0;

            _listView.tv_fine.setText(getResources().getString(R.string.Rs) + " " + data.getService_charge() + " /-");
//
//            try {
//
//                for (int i = 0; i < taxArrayBean.size(); i++) {
//                    Double maxLimit = Double.parseDouble(taxArrayBean.get(i).getTo());
//                    Double emiamount = Double.parseDouble(tenant_matches_listings.get(position).getEmi_amount());
//
//                    if (maxLimit > emiamount) {
//                        User_Charges = Double.parseDouble(taxArrayBean.get(i).getTransaction_amount());
//
//                        if (taxArrayBean.get(i).getExtra_tax() != null) {
//                            User_Charges = User_Charges + ((emiamount * Double.parseDouble(taxArrayBean.get(i).getExtra_tax())) / 100);
//                        }
//                        String amount_format = Utils.parseAmount(String.valueOf(User_Charges));
//                        _listView.tv_fine.setText(getResources().getString(R.string.Rs) + " " + amount_format + " /-");
//                        break;
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            try {

                String bill_amount = Utils.parseAmount(String.valueOf(data.getBillamt()));
                String adj_amount = Utils.parseAmount(String.valueOf(data.getAdjamt()));
                String rec_amount = Utils.parseAmount(String.valueOf(data.getReconnection_fee()));
                String arrears_amount = Utils.parseAmount(String.valueOf(data.getNewarrears()));


                _listView.tv_due_date.setText(Utils.parseDateTime(data.getDue_date()));

                _listView.tv_due.setText(getResources().getString(R.string.Rs) + " " + bill_amount + " /-");

                if (data.getLast_paid_date() != null) {
                    _listView.tv_last_paid_date.setText(data.getLast_paid_date());
                }

                if (data.getLast_paid_amt() != null) {
                    _listView.tv_last_paid_amount.setText(getResources().getString(R.string.Rs) + " " + data.getLast_paid_amt() + " /-");
                }

                if (data.getSurcharge_fee() != null) {
                    _listView.tv_surcharge.setText(getResources().getString(R.string.Rs) + " " + data.getSurcharge_fee() + " /-");
                }

                _listView.tv_surcharge.setText(getResources().getString(R.string.Rs) +"  "+"0");

                _listView.tv_adj_amount.setText(getResources().getString(R.string.Rs) + " " + adj_amount + " /-");
                _listView.tv_arrears.setText(getResources().getString(R.string.Rs) + " " + arrears_amount + " /-");
                _listView.tv_rec.setText(getResources().getString(R.string.Rs) + " " + rec_amount + " /-");

                if (tenant_matches_listings.get(position).getEmi_amount() == null ||
                        tenant_matches_listings.get(position).getEmi_amount().equals("")) {
                    _listView.tv_total.setText(getResources().getString(R.string.Rs) + " " + "0" + " /-");
                } else {


                    _listView.tv_total.setText(getResources().getString(R.string.Rs) + " " + data.getFinal_amount() + " /-");

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
                    }

                });

                _listView.chbk_check_amount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkDues(tenant_matches_listings, position);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            TextView tv_due_date, tv_surcharge, tv_last_paid_amount, tv_last_paid_date, tv_fine, tv_total,
                    tv_rec, tv_adj_amount;
            LinearLayout ll_item;
            CheckBox chbk_check_amount;

            TextView tv_due, tv_arrears;
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

                        SharedPreferenceUtil.setServiceNo(getActivity(), et_search.getText().toString());
                        SharedPreferenceUtil.setEro(getActivity(), spnr_type.getSelectedItemPosition() + "");
                        SharedPreferenceUtil.setAREA(getActivity(), spnr_village.getSelectedItemPosition() + "");

                        Gson gson = new Gson();
                        mResponsedata = new DemandModelNew();
                        mResponsedata = gson.fromJson(data.toString(), DemandModelNew.class);
                        emis.clear();

                        mSelectedEmis.clear();
                        emis.addAll(mResponsedata.getEmi());

                        setData();

                        if (emis.size() > 0) {

                            if (emis.get(0).getEmi_amount().equals("0")) {
                                emis.clear();

                                btn_print_on_device.setEnabled(false);
                                btn_print_on_device.setBackgroundColor(getResources().getColor(R.color.gray));
                                btn_print_on_device.setTextColor(getResources().getColor(R.color.red_oval_color));
                                btn_print_on_device.setText("No Pending Dues");
                            }
                        }
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

    private void upDateBillAmount(final String req) {
        new BackgroundTask(getActivity(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.updateBill(getActivity(), buildUpdateRequest());
            }

            public void taskCompleted(Object data) {

                try {
                    if (data != null || data.equals("")) {
                        UpdateBillModel  Billupdate_model;
                        Gson gson = new Gson();
                        Billupdate_model = gson.fromJson(data.toString(), UpdateBillModel.class);

                        paymentRequestModel.setORDER_ID(Billupdate_model.getOrderid());
                        String request = new Gson().toJson(paymentRequestModel);



                        Intent i = new Intent(getActivity(), PaymentModeActivityNew.class);
                        i.putExtra(Constants.PAYMENT_REQUEST_MODEL, request);
                        startActivity(i);
                    } else {
                        Globals.showToast(getActivity(), "Unable To Process Request");
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
            requestObject.put("version_id", "0.1");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject.toString();
    }

    public String buildUpdateRequest() {
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("assessment_id", et_search.getText().toString());
            requestObject.put("emi_id", emis.get(0).getId());
            requestObject.put("change_billamt", et_bill.getText().toString());
            requestObject.put("newarrears", et_arrear.getText().toString());
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
    private void openSearchConditionDialog(final String query) {

        dialog = new Dialog(getActivity());
       // dialog.setTitle("Reset " + query);
        dialog.setContentView(R.layout.dialog_condition_bill);


       Button btn_ok = dialog.findViewById(R.id.btn_pay);


        final CheckBox checkbox = dialog.findViewById(R.id.checkbox);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkbox.isChecked()){
                    dialog.dismiss();
                    Utils.hideKeyboard(getActivity());
                    if (et_search.getText().toString().length() == 10) {
                        String s = et_search.getText().toString();
                        String start = s.substring(0, 5);
                        String end = s.substring(5);
                        String finals = start + " " + end;
                        et_search.setText(finals);
                    }

                    getDeatails();

                }else {
                    Globals.showToast(getActivity(),"Please Accept Terms and Conditions");
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

    private String prepareCollRequest() {
        try {
            JSONObject requestObject;
            requestObject = new JSONObject();
            requestObject.put("fdate", "");
            requestObject.put("tdate", "");
            requestObject.put("user_id", SharedPreferenceUtil.getUserId(getActivity()));
            requestObject.put("P_MODES", "");
            return requestObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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

    private void attemptLogin(final String id) {
        new BackgroundTask(getActivity(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.getCollections(getActivity(), prepareCollRequest());
            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {
                    Gson gson = new Gson();
                    CollectionsModel mResponsedata = new CollectionsModel();
                    mResponsedata = gson.fromJson(data.toString(), CollectionsModel.class);
                    if (mResponsedata.getCollections() != null) {
                        String text = mResponsedata.getCollections().get(0).getLoan_number();
                        et_search.setText(text);
                    }
                } else {
                    Globals.showToast(getContext(), "No Data Found");
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }


}


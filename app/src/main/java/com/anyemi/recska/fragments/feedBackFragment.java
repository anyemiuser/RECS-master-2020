package com.anyemi.recska.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.anyemi.recska.NavigationActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.activities.CollectionsDetailsActivity;
import com.anyemi.recska.activities.RegisterActivity;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.CollectionsModel;
import com.anyemi.recska.model.RegisterModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.anyemi.recska.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SuryaTejaChalla on 12-02-2018.
 */

public class feedBackFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private View rootView;
    ViewGroup header;

    Button btn_pay;
    EditText et_phone_num, et_name, et_user_name, et_password, et_conform_password;
    TextInputLayout til_name, til_user_name, til_phone_id, til_conform_password, til_password;

    // ListView lv_my_account;
    Button btn_search;
    Spinner spnr_sort_by;
    EditText et_search;
    LinearLayout linearLayout;
    RefundFragment.ListingAdapter mAdapter;
    ArrayList<CollectionsModel.CollectionsBean> mCollections = new ArrayList<>();
    ArrayList<CollectionsModel.CollectionsBean> SCollections = new ArrayList<>();
    Object rdata;
    String query = "";

    public feedBackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
        setHasOptionsMenu(false);
        initializeComponents();
        //  initHeaderView();

        // attemptLogin(SharedPreferenceUtil.getUserId(getActivity()));
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

        btn_pay = rootView.findViewById(R.id.btn_pay);

        et_name = rootView.findViewById(R.id.et_name);
        et_user_name = rootView.findViewById(R.id.et_user_name);
        et_password = rootView.findViewById(R.id.et_password);
        et_conform_password = rootView.findViewById(R.id.et_conform_password);
        et_phone_num = rootView.findViewById(R.id.et_phone_num);

        til_name = rootView.findViewById(R.id.til_name);
        til_user_name = rootView.findViewById(R.id.til_user_name);
        til_name = rootView.findViewById(R.id.til_name);
        til_password = rootView.findViewById(R.id.til_password);
        til_conform_password = rootView.findViewById(R.id.til_conform_password);
        til_phone_id = rootView.findViewById(R.id.til_phone_id);

        et_name.addTextChangedListener(this);
        et_user_name.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_conform_password.addTextChangedListener(this);
        et_phone_num.addTextChangedListener(this);

        btn_pay.setText("Submit");
        btn_pay.setOnClickListener(this);
    }



    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (et_name.hasFocus() && et_name.getText().toString().length() > 0) {
            til_name.setErrorEnabled(false);
            til_name.setError(null);
        } else if (et_phone_num.hasFocus() && et_phone_num.getText().toString().length() > 0) {
            til_phone_id.setErrorEnabled(false);
            til_phone_id.setError(null);
            //tv_u_id.setText(String.valueOf(charSequence));

        } else if (et_user_name.hasFocus() && et_user_name.getText().toString().length() > 0) {
            til_user_name.setErrorEnabled(false);
            til_user_name.setError(null);
        } else if (et_password.hasFocus() && et_password.getText().toString().length() > 0) {
            til_password.setErrorEnabled(false);
            til_password.setError(null);
        } else if (et_conform_password.hasFocus() && et_conform_password.getText().toString().length() > 0) {
            til_conform_password.setErrorEnabled(false);
            til_conform_password.setError(null);
        }


    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_pay:
                if (performValidation()) {
                    submitFeedBack();
                }
                break;
        }
    }

    private boolean performValidation() {
        boolean isValid = false;

        if (et_name.getText().toString().equals("")) {
            til_name.setError("Please enter Name");
            et_name.requestFocus();
        } else if (et_user_name.getText().toString().equals("")) {
            til_user_name.setError("Please enter Subject");
            et_user_name.requestFocus();
        }

        else if (et_password.getText().toString().length()>0 && !Utils.isEmailValid(et_password.getText().toString())) {
            til_password.setError("Please enter valid email");
            et_password.requestFocus();
        }

//        else if (et_conform_password.getText().toString().equals("")) {
//            til_conform_password.setError("Please Reenter Password");
//            et_conform_password.requestFocus();
//        }

//        else if (!et_conform_password.getText().toString().equals(et_password.getText().toString())) {
//            til_conform_password.setError("Password not Match");
//            et_conform_password.requestFocus();
//        }


        else if (et_phone_num.getText().toString().equals("")) {
            til_phone_id.setError("Enter your Mobile Number");
            et_phone_num.requestFocus();
        } else if (et_phone_num.getText().toString().length() != 10) {
            til_phone_id.setError("Invalid Mobile Number");
            et_phone_num.requestFocus();
        } else if (et_conform_password.getText().toString().equals("")) {
            til_conform_password.setError("Please Enter Message");
            et_conform_password.requestFocus();
        }else {
            isValid = true;
        }
        return isValid;
    }


    private void submitFeedBack() {
        new BackgroundTask(getActivity(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.submitFeedBack(getActivity(), prepareRegisterRequest());
            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {
                        try {
                            Gson gson = new Gson();
                            RegisterModel registerModel = new RegisterModel();
                            registerModel = gson.fromJson(data.toString(), RegisterModel.class);
                            if (registerModel.getStatus().equals("Success")) {
                                Globals.showToast(getActivity(), "FeedBack Submit Successfully");

                                Intent intent = new Intent(getActivity(), NavigationActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

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

    private String prepareRegisterRequest() {
        try {
            JSONObject jsonObject;
            jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreferenceUtil.getUserId(getActivity()));
            jsonObject.put("name", et_name.getText().toString());
            jsonObject.put("subject", et_user_name.getText().toString());
            jsonObject.put("email", et_password.getText().toString());
            jsonObject.put("mobile_number", et_phone_num.getText().toString());
            jsonObject.put("message", et_conform_password.getText().toString());
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}


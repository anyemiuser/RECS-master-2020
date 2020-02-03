package com.anyemi.recska.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.anyemi.recska.NavigationActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.activities.AddUPIActivity;
import com.anyemi.recska.activities.RemoveUpiIdActivity;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.RegisterModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.anyemi.recska.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class edit_profile extends Fragment implements View.OnClickListener, TextWatcher {
    private View rootView;

    Button btn_pay;

    EditText et_name, et_phone_num, et_user_id, et_email;

    TextInputLayout til_name, til_user_id, til_email, til_phone;
    TextView tv_change_password;
    TextView tv_add_upi;
    TextView tv_remove_upi;

    Dialog dialog;


    public edit_profile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
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
    }


    private void initializeComponents() {

        btn_pay = rootView.findViewById(R.id.btn_pay);
        tv_change_password = rootView.findViewById(R.id.tv_change_password);

        et_name = rootView.findViewById(R.id.et_name);
        et_user_id = rootView.findViewById(R.id.et_user_id);
        et_email = rootView.findViewById(R.id.et_email);
        et_phone_num = rootView.findViewById(R.id.et_phone_num);


        til_name = rootView.findViewById(R.id.til_name);
        til_user_id = rootView.findViewById(R.id.til_user_id);
        til_email = rootView.findViewById(R.id.til_email);
        til_phone = rootView.findViewById(R.id.til_phone);

        et_name.addTextChangedListener(this);
        et_user_id.addTextChangedListener(this);
        et_email.addTextChangedListener(this);
        et_phone_num.addTextChangedListener(this);


        btn_pay.setText("Update");
        btn_pay.setOnClickListener(this);

        tv_add_upi = rootView.findViewById(R.id.tv_add_upi);
        tv_remove_upi = rootView.findViewById(R.id.tv_remove_upi);
        tv_change_password.setOnClickListener(this);

        tv_add_upi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddUPIActivity.class);
                startActivity(i);
            }
        });

        tv_remove_upi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), RemoveUpiIdActivity.class);
                startActivity(i);
            }
        });
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
            til_phone.setErrorEnabled(false);
            til_phone.setError(null);
        } else if (et_user_id.hasFocus() && et_user_id.getText().toString().length() > 0) {
            til_user_id.setErrorEnabled(false);
            til_user_id.setError(null);
        } else if (et_email.hasFocus() && et_email.getText().toString().length() > 0) {
            til_email.setErrorEnabled(false);
            til_email.setError(null);
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
                    updateProfile();
                }
                break;

            case R.id.tv_change_password:
                openSearchDialog();
                break;
        }
    }

    private boolean performValidation() {
        boolean isValid = false;

        if (et_name.getText().toString().equals("")) {
            til_name.setError("Please enter Name");
            et_name.requestFocus();
        } else if (et_email.getText().toString().length()>0 && !Utils.isEmailValid(et_email.getText().toString())) {
            til_email.setError("Please enter valid email");
            til_email.requestFocus();
        }else{
            isValid=true;
        }

        return isValid;
    }


    private void updateProfile() {
        new BackgroundTask(getActivity(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.updateProfile(getActivity(), prepareRegisterRequest());
            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {
                    try {
                        Gson gson = new Gson();
                        RegisterModel registerModel = new RegisterModel();
                        registerModel = gson.fromJson(data.toString(), RegisterModel.class);
                        if (registerModel.getStatus().equals("Success")) {
                            Globals.showToast(getActivity(), "Details Updated Successfully");
                            SharedPreferenceUtil.setUserName(getActivity(), et_name.getText().toString());
                            SharedPreferenceUtil.setUserEmail(getActivity(), et_email.getText().toString());

                            Intent intent = new Intent(getActivity(), NavigationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Globals.showToast(getActivity(), "Unable Update Details Please Try again later");
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
            jsonObject.put("email", et_email.getText().toString());
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }




    private void openSearchDialog() {

        dialog = new Dialog(getActivity());
        dialog.setTitle("Reset Password");
        dialog.setContentView(R.layout.dialog_change_password);


        final Button btn_pay = dialog.findViewById(R.id.btn_pay);

        final TextView tv_u_id = dialog.findViewById(R.id.tv_u_id);
        final EditText et_old_password = dialog.findViewById(R.id.et_old_password);
        final  EditText et_password = dialog.findViewById(R.id.et_password);
        final EditText et_conform_password = dialog.findViewById(R.id.et_conform_password);


        tv_u_id.setText(SharedPreferenceUtil.getsetcheckbok(getActivity()));


//        TextInputLayout til_password = (TextInputLayout) findViewById(R.id.til_password);
//        TextInputLayout til_conform_password = (TextInputLayout) findViewById(R.id.til_conform_password);


        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_old_password.getText().toString().equals("")) {
                    Globals.showToast(getActivity(), "Please enter old password");
                } else if (et_password.getText().toString().equals("")) {
                    Globals.showToast(getActivity(), "Please enter new password");
                } else if (et_password.getText().toString().length() < 6) {
                    Globals.showToast(getActivity(), "Password should contains more than 5 charcters");
                } else if (et_conform_password.getText().toString().equals("")) {
                    Globals.showToast(getActivity(), "Please Re enter new password");
                } else if (!et_conform_password.getText().toString().equals(et_password.getText().toString())) {
                    Globals.showToast(getActivity(), "Password Mismatch");
                } else {
                    String uname, password,old_password;

                    uname = tv_u_id.getText().toString();
                    old_password = et_old_password.getText().toString();
                    password = et_password.getText().toString();

                    resetPassword(uname,old_password,password);
                }
            }
        });
        dialog.show();
    }

    private void resetPassword(final String uname, final String old_password, final String password) {
        new BackgroundTask(getActivity(), new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.resetPassword(getActivity(), reset(uname,old_password,password));

            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {
                    try {
                        Gson gson = new Gson();
                        RegisterModel registerModel = new RegisterModel();
                        registerModel = gson.fromJson(data.toString(), RegisterModel.class);
                        if (registerModel.getStatus().equals("Success")) {
                            Globals.showToast(getActivity(), registerModel.getMsg());
                            dialog.dismiss();


                            Intent intent = new Intent(getActivity(), NavigationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);


                        } else {
                            Globals.showToast(getActivity(), registerModel.getMsg());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Globals.showToast(getActivity(), "Unable to Process, Please Try again later");
                }

            }
        }, getString(R.string.loading_txt)).execute();
    }


    private String reset(String uname, String old_password, String password) {



        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("user_id", uname);
            requestObject.put("old_password", old_password);
            requestObject.put("new_password", password);
            System.out.println(requestObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject.toString();
    }

}



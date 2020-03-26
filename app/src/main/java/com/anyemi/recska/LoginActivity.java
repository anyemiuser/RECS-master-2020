package com.anyemi.recska;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.anyemi.recska.activities.RegisterActivity;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.RegisterModel;
import com.anyemi.recska.model.loginResponseModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.GpsTracker;
import com.anyemi.recska.utils.RunTimePermissions;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    TextView register, tv_forget_password;
    private Spinner login_type;
    ArrayList<String> mLoginArray = new ArrayList<>();
    GpsTracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        gps = new GpsTracker(LoginActivity.this);
        RunTimePermissions runtimePermission = new RunTimePermissions(LoginActivity.this);
        boolean allPermissions = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            allPermissions = runtimePermission.check_permissions();
        }
        if (allPermissions == true) {
           // getLocation();
        }
        initView();
    }


    private void initView() {
        register = findViewById(R.id.register);
        tv_forget_password = findViewById(R.id.tv_forget_password);
        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        login_type = findViewById(R.id.spnr_login_type);


        mLoginArray.clear();
        mLoginArray.addAll(Arrays.asList(getResources().getStringArray(R.array.ary_login_type)));
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLoginArray);
        login_type.setAdapter(regionAdapter);
        login_type.setSelection(4);


        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });


        tv_forget_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEmailView.getText().toString().equals("")) {
                    Globals.showToast(getApplicationContext(), "Please Enter Mobile Number");
                } else if (mEmailView.getText().toString().length() != 10) {
                    Globals.showToast(getApplicationContext(), "Invalid Mobile Number");
                } else {
                    forget_password();
                }

            }


        });

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    if (performValidations()) {
                        attemptLogin();
                    }
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (performValidations()) {
                    attemptLogin();
                }
            }
        });

        //  mLoginFormView = findViewById(R.id.login_form);
        // mProgressView = findViewById(R.id.login_progress);
    }

    private boolean performValidations() {
        Boolean isValid = false;
        if (login_type.getSelectedItemPosition() == 0) {
            Globals.showToast(getApplicationContext(), "Please Select Login type");
            isValid = false;
        } else if (mEmailView.getText().toString().equals("")) {
            Globals.showToast(getApplicationContext(), "Please enter username");
            isValid = false;
        } else if (mPasswordView.getText().toString().equals("")) {
            Globals.showToast(getApplicationContext(), "Please enter password");
        } else {
            isValid = true;
        }
        return isValid;
    }


    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }


    private void forget_password() {
        new BackgroundTask(LoginActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.password(LoginActivity.this, password());

            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {

                    if (data != null || data.equals("")) {
                        try {
                            Gson gson = new Gson();
                            RegisterModel registerModel = new RegisterModel();
                            registerModel = gson.fromJson(data.toString(), RegisterModel.class);
                            if (registerModel.getStatus().equals("Success")) {
                               // Globals.showToast(getApplicationContext(), "Password Sent to your registered mobile number to login");
                                Globals.showToast(getApplicationContext(), registerModel.getMsg());
                            } else {
                                //Globals.showToast(getApplicationContext(), "Unable Submit Request Please Try again later");
                                Globals.showToast(getApplicationContext(), registerModel.getMsg());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Globals.showToast(getApplicationContext(), "Unable Fetch Dat Please Try again later");
                    }

                } else {
                    Globals.showToast(getApplicationContext(), "Unable Fetch Data Please Try again later");
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    private void openSearchDialog() {

        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setTitle("Update App to Continue");
        dialog.setContentView(R.layout.dialog_update);

        TextView textView = dialog.findViewById(R.id.update_from_play_store);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                startActivity(i);
            }
        });


        dialog.show();
    }


    private void attemptLogin() {
        new BackgroundTask(LoginActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.Register(LoginActivity.this, login());
            }

            public void taskCompleted(Object data) {

                mEmailView.setText("");
                mPasswordView.setText("");

                if (data != null || data.equals("")) {

                    SharedPreferenceUtil.setLoginType(getApplicationContext(), login_type.getSelectedItem().toString());
                    Gson gson = new Gson();
                    loginResponseModel mResponsedata = new loginResponseModel();
                    mResponsedata = gson.fromJson(data.toString(), loginResponseModel.class);

                    if (mResponsedata.getVersion_id() != null && !mResponsedata.getVersion_id().equals(Constants.VERSION_ID)) {
                        openSearchDialog();
                    } else {
                        if (mResponsedata.getGroup_id() != null) {
                            SharedPreferenceUtil.setGroupId(getApplicationContext(),mResponsedata.getGroup_id());
                            SharedPreferenceUtil.setFIN_ID(getApplicationContext(),"1");

                             if (mResponsedata.getGroup_id().equals("14")) {
                                SharedPreferenceUtil.setLoginType(getApplicationContext(), Constants.LOGIN_TYPE_CUSTOMER);
                            } else if (mResponsedata.getGroup_id().equals("11")) {
                                SharedPreferenceUtil.setLoginType(getApplicationContext(), Constants.LOGIN_TYPE_COLLECTION_AGENT);
                            }
                            SharedPreferenceUtil.setUserData(getApplicationContext(), data.toString());
                            SharedPreferenceUtil.setUserId(getApplicationContext(), mResponsedata.getId());
                            SharedPreferenceUtil.setUserName(getApplicationContext(), mResponsedata.getName());
                            SharedPreferenceUtil.setUserEmail(getApplicationContext(), mResponsedata.getEmail());
                            SharedPreferenceUtil.setcheckbok(getApplicationContext(), mResponsedata.getUsername());
                            SharedPreferenceUtil.setMPOSID(getApplicationContext(), mResponsedata.getMpos_id());
                            SharedPreferenceUtil.setMPOSUID(getApplicationContext(), mResponsedata.getMpos_username());
                            SharedPreferenceUtil.setMPOSPWD(getApplicationContext(), mResponsedata.getMpos_password());

                            Intent i = new Intent(LoginActivity.this, NavigationActivity.class);
                            startActivity(i);
                        } else {
                            Globals.showToast(getApplicationContext(), mResponsedata.getMsg());
                        }
                    }
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }


    private String login() {
        String uname, password;
        uname = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("username", uname);
            requestObject.put("password", password);
            requestObject.put("version_id", Constants.VERSION_ID);
            System.out.println(requestObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject.toString();
    }

    private String password() {
        String uname;
        uname = mEmailView.getText().toString();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("mobile_number", uname);
            System.out.println(requestObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject.toString();
    }
}


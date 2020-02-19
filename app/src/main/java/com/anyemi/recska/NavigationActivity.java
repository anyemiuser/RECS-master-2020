package com.anyemi.recska;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.anyemi.recska.activities.AddUPIActivity;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.fragments.CollectionsFragment;
import com.anyemi.recska.fragments.HomePageFragement;
import com.anyemi.recska.fragments.PendingTransactionsFragment;
import com.anyemi.recska.fragments.RefundFragment;
import com.anyemi.recska.fragments.ReportsFragmnet;
import com.anyemi.recska.fragments.SearchBillFragment;
import com.anyemi.recska.fragments.edit_profile;
import com.anyemi.recska.fragments.feedBackFragment;
import com.anyemi.recska.model.RegisterModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;

    // WebView browser;
    LinearLayout linearLayout;
    Button btn_go;
    EditText editText;
    String page, area_id, tax_type;
    ProgressBar pb;
    Fragment fragment = null;

    Spinner spnr_area_name;
    Spinner spnr_tax_type;
    EditText et_search;
    Button btn_search;
    WebView browser;
    //TextView refund;

    boolean IsHomeFragment = true;

    ArrayList<String> mAreaArray = new ArrayList<>();
    ArrayList<String> mtaxArray = new ArrayList<>();


    //UI COMPONENTS

    Button btn_pay;
    EditText et_phone_num, et_name, et_user_name, et_password, et_old_password, et_conform_password, et_otp;
    TextInputLayout til_name, til_user_name, til_phone_id, til_conform_password, til_password, til_otp;
    TextView tv_u_id, tv_resend_otp;
    Dialog dialog;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        MobileAds.initialize(this, "ca-app-pub-1013900452874783~5405394873");

        createActionBar();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header_navigation, null);
        ((TextView) nav_header.findViewById(R.id.tv_u_name)).setText(SharedPreferenceUtil.getUserName(getApplicationContext()));
        ((TextView) nav_header.findViewById(R.id.tv_u_email)).setText(SharedPreferenceUtil.getUserEmail(getApplicationContext()));
        navigationView.addHeaderView(nav_header);

        Menu nav_Menu = navigationView.getMenu();

        if (SharedPreferenceUtil.getLoginType(getApplicationContext()).equals(Constants.LOGIN_TYPE_CUSTOMER)) {
            nav_Menu.findItem(R.id.nav_change_pwd).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.nav_change_pwd).setVisible(true);
        }

//        if (SharedPreferenceUtil.getLoginType(getApplicationContext())
//                .equals(Constants.LOGIN_TYPE_CUSTOMER)) {
//            nav_Menu.findItem(R.id.nav_add_vpa).setVisible(true);
//
//        }else{
//            nav_Menu.findItem(R.id.nav_add_vpa).setVisible(false);
//        }

        if(SharedPreferenceUtil.getUserId(getApplicationContext()).equals("")){
            nav_Menu.findItem(R.id.nav_add_vpa).setVisible(false);
            nav_Menu.findItem(R.id.nav_change_pwd).setVisible(false);
        }


        createWebView();

        //    fragment = new home_fragment();
        //   getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

        // refund = (TextView) findViewById(R.id.text_count);
        String frag_name = "";
        try {
            Bundle intent = getIntent().getExtras();
            frag_name = intent.getString("FRAGMENT", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (frag_name.equals("COLLECTION")) {
            aTitle.setText("Collections");
            fragment = new CollectionsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

        } else {

            //fragment = new SearchBillFragment();
            fragment = new HomePageFragement();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        initView();

        showAdd();

    }

    private void initView() {
        MobileAds.initialize(this,"ca-app-pub-1013900452874783~5405394873");

        mAdView = findViewById(R.id.adView);
    }



    public void showAdd() {




       if(mAdView!=null){
            mAdView.setVisibility(View.VISIBLE);

            Bundle extras = new Bundle();
            extras.putString("npa", "1");

            AdRequest adRequest = new AdRequest.Builder()
                   // .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .addTestDevice("DC5CA454F9517A3CFD2B8135AF5E8E2E")
                    .addTestDevice("93847E8DDF2F22E5C910D61C72E37EAF")
                    // .addTestDevice("1F474434B1229D7A3963E1B2DEB4E609")
                     //   .addTestDevice("93847E8DDF2F22E5C910D61C72E37EAF")
                    //  .addTestDevice("93847E8DDF2F22E5C910D61C72E37EAF")
                    .build();

            mAdView.loadAd(adRequest);

            showAdds(false);
        } else

        {
          showAdds(false);
        }

    }

    private void createActionBar() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_action_bar_with_home_button, null);

        aTitle = mCustomView.findViewById(R.id.title_text);
        rl_new_mails = mCustomView.findViewById(R.id.rl_new_mails);
        notification_count = mCustomView.findViewById(R.id.text_count);
        iv_add_new = mCustomView.findViewById(R.id.iv_add_new);

        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        aTitle.setTextColor(getResources().getColor(R.color.white));
        aTitle.setText("Home");
        //  createWebView();
    }


    private void createWebView() {


        linearLayout = findViewById(R.id.ll_web_view);
        browser = findViewById(R.id.webview);

        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        editText = findViewById(R.id.et_search);
        editText.setEnabled(false);
        btn_go = findViewById(R.id.btn_search);
        pb = findViewById(R.id.pb);

        btn_go.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                loadPage(editText.getText().toString());
            }
        });

        browser.getSettings().setJavaScriptEnabled(true);
        browser.setWebViewClient(new Callback());
        browser.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {

                pb.setProgress(progress);
                if (progress == 100)
                    pb.setVisibility(View.GONE);
            }
        });

        browser.setWebViewClient(new myWebClient());


        //
        //    browser.setWebViewClient(new WebViewClient():

        if (editText.getText().toString().equals("")) {
            editText.setText("https://recs.anyemi.com");
            browser.loadUrl("https://recs.anyemi.com");
        } else {
            browser.loadUrl(editText.getText().toString());
        }
    }

//    private void createWebView() {
//
//
//        linearLayout = (LinearLayout) findViewById(R.id.ll_web_view);
//        browser = (WebView) findViewById(R.id.webview);
//        editText = (EditText) findViewById(R.id.et_search);
//        editText.setEnabled(false);
//        btn_go = (Button) findViewById(R.id.btn_search);
//        pb = (ProgressBar) findViewById(R.id.pb);
//        btn_go.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadPage(editText.getText().toString());
//            }
//        });
//
//        browser.getSettings().setJavaScriptEnabled(true);
//
//        if (editText.getText().toString().equals("")) {
//            editText.setText("https://anyemi.com");
//            //   browser.loadUrl("https://anyemi.com");
//        } else {
////            browser.loadUrl(editText.getText().toString());
//        }
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            if (IsHomeFragment) {


                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(NavigationActivity.this);
                final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                final String message = "Are you sure to exit";

                builder.setMessage(message)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int id) {
                                        finish();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int id) {
                                        d.cancel();
                                    }
                                });
                builder.create().show();
            } else {

                fragment = new HomePageFragement();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                aTitle.setText("Home");
                linearLayout.setVisibility(View.GONE);
                IsHomeFragment = true;

            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        IsHomeFragment = false;

        int id = item.getItemId();

        changeFragment(id);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeFragment(int id) {

        IsHomeFragment = false;

        linearLayout.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);


        if (id == R.id.nav_collections) {

            fragment = new CollectionsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            aTitle.setText("Collections");
        } else if (id == R.id.nav_search) {
            fragment = new SearchBillFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            //  aTitle.setText("Pay ARECS Bills");
            aTitle.setText("Pay Bills");
            //openSearchDialog();
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_about_us) {
            linearLayout.setVisibility(View.VISIBLE);
            aTitle.setText("About Us");
            loadPage("https://recs.anyemi.com/index.php/aboutus");

//           Intent intent = new Intent(getApplicationContext(), MenuView.class);
//            startActivity(intent);
        }else if (id == R.id.nav_add_vpa) {
            // finish();
            Intent i = new Intent(getApplicationContext(), AddUPIActivity.class);
            i.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);


        } else if (id == R.id.nav_terms) {
            linearLayout.setVisibility(View.VISIBLE);
            aTitle.setText(getResources().getString(R.string.terms));
            loadPage("https://recs.anyemi.com/index.php/termsandconditions");

//           Intent intent = new Intent(getApplicationContext(), MenuView.class);
//            startActivity(intent);
        } else if (id == R.id.nav_contact_us) {
            linearLayout.setVisibility(View.VISIBLE);
            aTitle.setText("Contact Us");
            loadPage("https://recs.anyemi.com/index.php/contactus");
        } else if (id == R.id.nav_privacy) {
            aTitle.setText("Privacy Policy");
            linearLayout.setVisibility(View.VISIBLE);
            loadPage("https://recs.anyemi.com/index.php/privacypolicy");
        } else if (id == R.id.nav_refund) {
            fragment = new RefundFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            aTitle.setText(getResources().getString(R.string.refund));
        } else if (id == R.id.nav_feed_back) {
            fragment = new feedBackFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            aTitle.setText(getResources().getString(R.string.feedback));
        } else if (id == R.id.nav_home) {
            fragment = new HomePageFragement();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            aTitle.setText("ARECS");
            IsHomeFragment = false;
        } else if (id == R.id.nav_change_pwd) {

            fragment = new edit_profile();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            aTitle.setText("Edit Profile");
        } else if (id == R.id.ll_my_usage) {
            fragment = new PendingTransactionsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            aTitle.setText("ARECS");
            IsHomeFragment = false;

        }else if(id==R.id.ll_consumption){
            fragment = new ReportsFragmnet();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            aTitle.setText("ARECS");
            IsHomeFragment = false;
        }

    }

    private void openSearchDialog() {

        dialog = new Dialog(NavigationActivity.this);
        dialog.setTitle("Reset Password");
        dialog.setContentView(R.layout.dialog_change_password);


        btn_pay = dialog.findViewById(R.id.btn_pay);

        tv_u_id = dialog.findViewById(R.id.tv_u_id);
        et_old_password = dialog.findViewById(R.id.et_old_password);
        et_password = dialog.findViewById(R.id.et_password);
        et_conform_password = dialog.findViewById(R.id.et_conform_password);


        tv_u_id.setText(SharedPreferenceUtil.getsetcheckbok(getApplicationContext()));


        til_password = findViewById(R.id.til_password);
        til_conform_password = findViewById(R.id.til_conform_password);


        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_old_password.getText().toString().equals("")) {
                    Globals.showToast(getApplicationContext(), "Please enter old password");
                } else if (et_password.getText().toString().equals("")) {
                    Globals.showToast(getApplicationContext(), "Please enter new password");
                } else if (et_password.getText().toString().length() < 6) {
                    Globals.showToast(getApplicationContext(), "Password should contains more than 5 charcters");
                } else if (et_conform_password.getText().toString().equals("")) {
                    Globals.showToast(getApplicationContext(), "Please Re enter new password");
                } else if (!et_conform_password.getText().toString().equals(et_conform_password.getText().toString())) {
                    Globals.showToast(getApplicationContext(), "Password Mismatch");
                } else {
                    resetPassword();
                }
            }
        });
        dialog.show();
    }


   public void showAdds(boolean add){

        try {

            if (mAdView != null) {

                if (add) {
                    mAdView.setVisibility(View.GONE);
                } else {
                    mAdView.setVisibility(View.GONE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void getDeatails() {
        new BackgroundTask(NavigationActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.getAssessment(NavigationActivity.this, buildLoginRequest());
            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {
                    // linearLayout.setVisibility(View.GONE);
                    fragment = new SearchBillFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("data", data.toString());
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

                } else {
                    Globals.showToast(getApplicationContext(), "Unable Fetch Dat Please Try again later");
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
            requestObject.put("user_id", SharedPreferenceUtil.getUserId(getApplicationContext()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject.toString();
    }

    private void loadPage(String s) {
        browser.loadUrl(s);
        editText.setText(s);
    }


    void loadTime(String s) {
        browser.loadDataWithBaseURL("x-data://base", page,
                "text/html", "UTF-8",
                null);
    }

    private class Callback extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            loadTime(page);
            return (true);
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.action_search_device:
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search, menu);
//        return true;
//    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
// TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
// TODO Auto-generated method stub
//            if(url.contains("anyemi.print://anyemi.com")){
//                String s=url;
//                s=s.replaceAll("anyemi.print://anyemi.com/","");
//                Intent i=new Intent(getApplicationContext(), BluetoothPrinterMain.class);
//                i.putExtra("data",s);
//                startActivity(i);
//            }else {
//                view.loadUrl(url);
//            }



            view.loadUrl(url);
            return true;

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && browser.canGoBack()) {
            browser.goBack();
            fragment = new HomePageFragement();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            aTitle.setText("Home");
            IsHomeFragment = true;
            linearLayout.setVisibility(View.GONE);
            return true;

        }


        return super.onKeyDown(keyCode, event);
    }


    private void resetPassword() {
        new BackgroundTask(NavigationActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.resetPassword(NavigationActivity.this, reset());

            }

            public void taskCompleted(Object data) {

                if (data != null || data.equals("")) {
                    try {
                        Gson gson = new Gson();
                        RegisterModel registerModel = new RegisterModel();
                        registerModel = gson.fromJson(data.toString(), RegisterModel.class);
                        if (registerModel.getStatus().equals("Success")) {
                            Globals.showToast(getApplicationContext(), registerModel.getMsg());
                            dialog.dismiss();
                        } else {
                            Globals.showToast(getApplicationContext(), registerModel.getMsg());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Globals.showToast(getApplicationContext(), "Unable to Process, Please Try again later");
                }

            }
        }, getString(R.string.loading_txt)).execute();
    }


    private String reset() {
        String uname, password, old_password;

        uname = tv_u_id.getText().toString();
        old_password = et_old_password.getText().toString();
        password = et_password.getText().toString();


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

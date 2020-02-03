/**
 * Copyright 2014 NGX Technologies Pvt. Ltd http://ngxtech.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anyemi.recska.bluetoothPrinter;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anyemi.recska.R;
import com.anyemi.recska.activities.CollectionsDetailsActivity;
import com.anyemi.recska.fragments.PrintFragment;
import com.ngx.BluetoothPrinter;

import java.util.HashMap;
import java.util.Map;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class BluetoothPrinterMain extends AppCompatActivity {
    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;


    public static BluetoothPrinter mBtp = BluetoothPrinter.INSTANCE;

    private static FragmentManager fragMgr;
    private Fragment nm;
    private static final String cHomeStack = "home";
    private TextView tvStatus;

    private String mConnectedDeviceName = "";
    public static final String title_connecting = "connecting...";
    public static final String title_connected_to = "connected: ";
    public static final String title_not_connected = "not connected";

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothPrinter.STATE_CONNECTED:
                            tvStatus.setText(title_connected_to);
                            tvStatus.append(mConnectedDeviceName);
                            break;
                        case BluetoothPrinter.STATE_CONNECTING:
                            tvStatus.setText(title_connecting);
                            break;
                        case BluetoothPrinter.STATE_LISTEN:
                        case BluetoothPrinter.STATE_NONE:
                            tvStatus.setText(title_not_connected);
                            break;
                    }
                    break;
                case BluetoothPrinter.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(
                            BluetoothPrinter.DEVICE_NAME);
                    break;
                case BluetoothPrinter.MESSAGE_STATUS:
                    tvStatus.setText(msg.getData().getString(
                            BluetoothPrinter.STATUS_TEXT));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btp_main);

        createActionBar();
        tvStatus = findViewById(R.id.tvStatus);


        fragMgr = getSupportFragmentManager();


        // lockOrientation(this);

        //	mBtp.setDebugService(BuildConfig.DEBUG);

        try {
            mBtp.initService(this, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  nm = new AsciiFragment();
        nm = new PrintFragment();

        Bundle parametros = getIntent().getExtras();
//        String dataa = parametros.getString("doctype");
//        String data = parametros.getString("print_data");



        if (parametros != null) {
//            String Doctype = parametros.getString("doctype");
//            String DocNo = parametros.getString("docno");
//            Toast.makeText(getApplicationContext(), Doctype + DocNo, Toast.LENGTH_SHORT).show();
            nm.setArguments(parametros);
        }
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        startService(intent);//启动打印服务
        bindService(intent, connService, Context.BIND_AUTO_CREATE);
        fragMgr.beginTransaction().replace(R.id.container, nm)
                .addToBackStack(cHomeStack).commit();
    }


    private void createActionBar() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }



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
        aTitle.setTextColor(getResources().getColor(R.color.orange_color));
        aTitle.setText("");

    }
    public static IWoyouService woyouService;

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    public static ICallback callback = new ICallback.Stub() {

        @Override
        public void onRunResult(boolean success) throws RemoteException {
        }

        @Override
        public void onReturnString(final String value) throws RemoteException {
        }

        @Override
        public void onRaiseException(int code, final String msg)
                throws RemoteException {
        }

        @Override
        public void onPrintResult(int code, String msg) throws RemoteException {

        }
    };

    @Override
    public void onPause() {
        mBtp.onActivityPause();
        super.onPause();
    }



    @Override
    public void onResume() {
        mBtp.onActivityResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mBtp.onActivityDestroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBtp.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (fragMgr.getBackStackEntryCount()) {
                case 0:
                case 1:
                    finish();
                    break;
                case 2:
                    fragMgr.popBackStack();
                    break;
                default:
                    fragMgr.popBackStack();
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                finish();
                break;
            case R.id.action_clear_device:
                // deletes the last used printer, will avoid auto connect
                Builder d = new Builder(this);
                d.setTitle("NGX Bluetooth Printer");
                // d.setIcon(R.drawable.ic_launcher);
                d.setMessage("Are you sure you want to delete your preferred Bluetooth printer ?");
                d.setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mBtp.clearPreferredPrinter();
                                Toast.makeText(getApplicationContext(),
                                        "Preferred Bluetooth printer cleared",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                d.setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                d.show();
                return true;
            case R.id.action_connect_device:
                // show a dialog to select from the list of available printers
                mBtp.showDeviceList(this);
                return true;


            case R.id.action_unpair_device:
                Builder u = new Builder(this);
                u.setTitle("Bluetooth Printer unpair");
                // d.setIcon(R.drawable.ic_launcher);
                u.setMessage("Are you sure you want to unpair all Bluetooth printers ?");
                u.setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (mBtp.unPairBluetoothPrinters()) {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "All NGX Bluetooth printer(s) unpaired",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                u.setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                u.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bluetooth_printer_main, menu);
        return true;
    }

    @SuppressLint("InlinedApi")
//	private static void lockOrientation(Activity activity) {
//		Display display = ((WindowManager) activity
//				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//		int rotation = display.getRotation();
//		int tempOrientation = activity.getResources().getConfiguration().orientation;
//		switch (tempOrientation) {
//			case Configuration.ORIENTATION_LANDSCAPE:
//				if (rotation == Surface.ROTATION_0
//						|| rotation == Surface.ROTATION_90)
//					activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//				else
//					activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
//				break;
//			case Configuration.ORIENTATION_PORTRAIT:
//				if (rotation == Surface.ROTATION_0
//						|| rotation == Surface.ROTATION_270)
//					activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//				else
//					activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
//		}
//	}

    public static void changeFragment(int iContainerId, Fragment fragment,
                                      boolean addToBackStack) {
        FragmentTransaction t = fragMgr.beginTransaction();

        if (iContainerId == R.id.container) {
            t.hide(fragMgr.findFragmentById(R.id.container));
            t.add(iContainerId, fragment);

            if (addToBackStack) {
                t.addToBackStack(cHomeStack);
            }
        }
        // Commit the transaction
        t.commit();
    }


}



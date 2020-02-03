package com.anyemi.recska.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import java.util.ArrayList;


/**
 * Created by SuryaTejaChalla on 13-09-2017.
 */

public class RunTimePermissions extends AppCompatActivity {
    Activity mContext;

    ArrayList<String> permission = new ArrayList<>();

    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private SharedPreferences permissionStatus;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

    public RunTimePermissions(Activity context) {
        this.mContext = context;
        permissionStatus = context.getSharedPreferences("permissionStatus", MODE_PRIVATE);
        permission.clear();
        permission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permission.add(Manifest.permission.CAMERA);
        permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean check_permissions() {
        boolean granted = false;


        for (int i = 0; i < permission.size(); i++) {
            if (ActivityCompat.checkSelfPermission(mContext, permission.get(i)) != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                break;
            } else {
                granted = true;
            }
        }

        if (granted) {
            proceedAfterPermission();
            return true;
        } else {

            for (int ii = 0; ii < permission.size(); ii++) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, permission.get(ii))) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This Application needs Location,Message  and Phone permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            mContext.requestPermissions(permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
                    builder.show();
                    break;
                }else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This Application needs Camera, Storage and Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            // sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                            intent.setData(uri);
                            mContext.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            //Toast.makeText(getActivity(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                    break;
                } else {
                    mContext.requestPermissions(permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    break;
                }
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
    }
        return false;
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;

                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (
                    ActivityCompat.shouldShowRequestPermissionRationale(mContext, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(mContext, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(mContext, permissionsRequired[2])
                    ) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This needs Camera,Storage  and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // mBottomSheetDialog.dismiss();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            mContext.requestPermissions(permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // mBottomSheetDialog.dismiss();
                    }
                });
                builder.show();
            } else {
                mContext.finish();
            }
        }
    }

    private void proceedAfterPermission() {

        isLocationEnabled();
//        if (isLocationEnabled()) {
//            Intent StartMap = new Intent(mContext, LoactionService.class);
//            mContext.startService(StartMap);
//        }
    }


    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(mContext.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }


}

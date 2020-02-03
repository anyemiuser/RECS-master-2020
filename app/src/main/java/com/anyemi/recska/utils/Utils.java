/*
 * Utils.java
 */

package com.anyemi.recska.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;

import com.anyemi.recska.R;
import com.anyemi.recska.connection.Constants;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {



    public static String getCurrentMethodName() {
        StackTraceElement[] stackTraceElements = (new Throwable()).getStackTrace();
        return stackTraceElements[1].toString();
    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isValidVpa(String email) {
        boolean isValid = false;


        String[] parts = email.split("@");
        if(parts.length==2){
            String part1 = parts[1];
            if(!part1.equals("")){
                isValid= true;
            }

        }else{
            isValid=false;
        }



        return isValid;
    }

    /**
     * get Month
     *
     * @param substring integer month as string
     * @return month name
     */
    public static String getMonth(String substring) {
        int month = Integer.parseInt(substring);
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                break;
        }
        return null;
    }

    /**
     * Return date in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @param dateFormat   Date format
     * @return String representing date in specified format
     */
    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified
        // format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in
        // milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static boolean isNullOrWhitespace(final String string) {
        return string == null || string.equalsIgnoreCase("null") || string.trim().length() == 0;
    }

    /**
     * Check whether the Internet connection is present or not. <uses-permission
     * android:name="android.permission.ACCESS_NETWORK_STATE" />
     */
    public static boolean isNetworkAvailable(Activity _activity) {
        ConnectivityManager conMgr = (ConnectivityManager) _activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();
    }


//    /**
//     * Check whether the Internet connection is present or not. <uses-permission
//     * android:name="android.permission.ACCESS_NETWORK_STATE" />
//     */
//    public static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager conMgr = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        return conMgr.getActiveNetworkInfo() != null
//                && conMgr.getActiveNetworkInfo().isAvailable()
//                && conMgr.getActiveNetworkInfo().isConnected();
//    }
//

    /**
     * Show an alert dialog and navigate back to previous screen if goBack is true
     *
     * @param _activity - Instance of the activity
     * @param title     - Title of the alert
     * @param alertMsg  - Message of the alert
     * @param goBack    - Flag to move back on destroy of alert
     */
    public static void showAlert(final Activity _activity, String title, String alertMsg, final boolean goBack, DialogInterface.OnClickListener okClickListener) {

    }

    /**
     * Show an alert dialog and navigate back to previous screen if goBack is true
     *
     * @param _activity - Instance of the activity
     * @param title     - Title of the alert
     * @param alertMsg  - Message of the alert
     */
    public static void showExitDialog(final Activity _activity, String title, String alertMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
        builder.setMessage(alertMsg)
                .setTitle(title)
                .setCancelable(false)
                .setTitle(_activity.getResources().getString(R.string.app_name))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        _activity.finish();

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }


    public static boolean isWifiAvailable(Context context) {
        boolean isWifiAvailable = false;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            isWifiAvailable = true;
        }
        return isWifiAvailable;
    }

    public static boolean compareSyncTime(long aLastSyncTime, long aContactTimeStamp) {

        return aContactTimeStamp > aLastSyncTime;
    }

    public static boolean compateTwoDates(long aLastSyncTime, long aContactTimeStamp) {
        long diffInDays = (aLastSyncTime - aContactTimeStamp) / (24 * 60 * 60 * 1000);

        return diffInDays < 6570;
    }

    public static boolean isGPSEnabled(Context context) {
        final LocationManager manager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

//    public static String getCurrentDateTime() {
//        Calendar myCalendar = Calendar.getInstance();
//        SimpleDateFormat mySdf = new SimpleDateFormat("MMMM dd, HH:mm a");
//        String myCurDate = mySdf.format(myCalendar.getTime());
//        return myCurDate;
//    }

    public static String getCurrentDateTime() {
        Calendar myCalendar = Calendar.getInstance();
        SimpleDateFormat mySdf = new SimpleDateFormat("dd-MM-yyyy");
        String myCurDate = mySdf.format(myCalendar.getTime());
        return myCurDate;

    }

    public static Long getCurrentTimeStamp() {
        Calendar myCalendar = Calendar.getInstance();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(myCalendar.getTimeInMillis());
        return minutes;
    }


//    /**
//     * Save string value from SharedPreference for the given key
//     */
//    public static void saveStringInSP(Context context, String key, String value) {
//        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(key, value);
//        editor.commit();
//    }
//
//    /**
//     * Retrieve string value from SharedPreference for the given key
//     */
//    public static String getStringFromSP(Context context, String key) {
//        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
//        return preferences.getString(key, null);
//    }
//
//    /**
//     * Delete value from SharedPreference for the given key
//     */
//    public static void deleteFromSP(Context context, String key) {
//        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.remove(key);
//        editor.commit();
//    }


    public static long convertToLongDateFormat(String oldDate) {
        long longDate = 0;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(oldDate);

            longDate = date.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return longDate;
    }

    public static String convertLongToDateFormat(Long oldDate) {

        Date date = new Date(oldDate);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateText = df2.format(date);
        System.out.println(dateText);
        return dateText;
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }


    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {

            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                //Read byte from input stream

                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;

                //Write byte from output stream
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    public static void openHomeScreen(Context applicationContext) {
//        try {
//            String USER_TYPE = SharedPreferenceUtil.getUserType(applicationContext);
//            if (USER_TYPE != null && !USER_TYPE.equals("")) {
//                if (USER_TYPE.equals("LANDLORD")) {
//                    Intent landlord_home = new Intent(applicationContext, Lr_SideMenuActivity.class);
//                    landlord_home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    applicationContext.startActivity(landlord_home);
//
//                } else if (USER_TYPE.equals("TENANT")) {
//                    Intent tenant_home = new Intent(applicationContext, Le_SideMenuActivity.class);
//                    tenant_home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    applicationContext.startActivity(tenant_home);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


    public static boolean isValidPhoneNumber(String email) {
        boolean isValid = false;
        if (email.length() == 10) {
            isValid = true;
        }

        return isValid;
    }


    public static String parseDateTime(String s) {
        String sdt="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date convert_s_date = sdf.parse(s);
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
            sdt = sdf2.format(convert_s_date);
        } catch (Exception e) {
            e.printStackTrace();
           return parseDate(s);
        }
        return sdt;
    }

    public static String parseDate(String s) {
        String sdt="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date convert_s_date = sdf.parse(s);
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
             sdt = sdf2.format(convert_s_date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sdt;
    }

    public static String parseShowDate(String s) {
        String sdt="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date convert_s_date = sdf.parse(s);
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
            sdt = sdf2.format(convert_s_date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sdt;
    }


    public static String parseAmount(String resultStr) {
        try {
            DecimalFormat df = new DecimalFormat("#,##,###.##", new DecimalFormatSymbols(Locale.US));
            resultStr = df.format(Double.valueOf(resultStr));
        } catch (Exception e) {
            e.printStackTrace();
            resultStr="0";
        }
        return resultStr;
    }

    public static String parseTwoDigitAmount(String resultStr) {
        try {
            DecimalFormat df = new DecimalFormat("########.##", new DecimalFormatSymbols(Locale.US));
            resultStr = df.format(Double.valueOf(resultStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

}

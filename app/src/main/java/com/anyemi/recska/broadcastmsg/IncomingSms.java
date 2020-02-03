package com.anyemi.recska.broadcastmsg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by SuryaTejaChalla on 12-12-2017.
 */

public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();


                    String senderNum = phoneNumber;
                    String whole_msg = currentMessage.getDisplayMessageBody();
                    if (whole_msg.contains("Paytm")) {
                        String[] message = whole_msg.split("Wallet txn id ");
                        String id = message[1];
                        id = id.substring(0, id.length() - 1);
                        id = id.substring(0, 11);

                        Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + id);

                        Intent myIntent = new Intent("PAYTM QR");
                        myIntent.putExtra("id", id);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);

                    } else {
                        String[] message = whole_msg.split("Your OTP is ");
                        String id = message[1];

                        Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + id);

                        Intent myIntent = new Intent("OTP");
                        myIntent.putExtra("id", id);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);

                    }


                    // message = message.substring(0, message.length()-1);
                    // id = id.substring(0,11);

                    // Show Alert


                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }
}

package com.anyemi.recska.paytmpg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.anyemi.recska.NavigationActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.PaymentRequestModel;
import com.anyemi.recska.utils.Globals;
import com.google.gson.Gson;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This is the sample app which will make use of the PG SDK. This activity will
 * show the usage of Paytm PG SDK API's.
 **/

public class MerchantActivity extends Activity {

    PaymentRequestModel paymentRequestModel;
    Gson gson = new Gson();
    String orderId;
    String hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchantapp);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        try {
            Bundle parametros = getIntent().getExtras();
            String data = parametros.getString(Constants.PAYMENT_REQUEST_MODEL);
            paymentRequestModel = gson.fromJson(data, PaymentRequestModel.class);
//			amount = String.valueOf(paymentRequestModel.getTotal_amount());
//			ids = paymentRequestModel.getEmi_ids();
//			assessment_num = paymentRequestModel.getAssessment_id();


        } catch (Exception e) {
            e.printStackTrace();
        }

        initOrderId();
    }

    //This is to refresh the order id: Only for the Sample App's purpose.
    @Override
    protected void onStart() {
        super.onStart();
        //initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);
        generateHashKey();
//		EditText orderIdEditText = (EditText) findViewById(R.id.order_id);
//		orderIdEditText.setText(orderId);
    }

//	private void generateHashKey() {
//	}


    private void generateHashKey() {

        new BackgroundTask(MerchantActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.generateHash(MerchantActivity.this, paymentRequestModel());
            }

            public void taskCompleted(Object data) {
//				if (data != null || !data.equals("")) {
//					CheckSumModel checkSumModel= new CheckSumModel();
//					checkSumModel=gson.fromJson(data.toString(),CheckSumModel.class);
//					if(checkSumModel.getStatus().equals("success")) {
//						hash = checkSumModel.getCheckSum();
//					}else {
//						Globals.showToast(getApplicationContext(),"Unable to generate hash");
//					}

                //			}

                hash = data.toString();
            }
        }, getString(R.string.loading_txt)).execute();
    }

    private String paymentRequestModel() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount", paymentRequestModel.getActualDueAmount());
            jsonObject.put("orderId", orderId);
            return jsonObject.toString();

//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put("MID" , "ANYEMI14182027236332");
//			jsonObject.put("ORDER_ID",orderId);
//			jsonObject.put("CUST_ID",paymentRequestModel.getUser_id());
//			jsonObject.put("INDUSTRY_TYPE_ID","Retail");
//			jsonObject.put("CHANNEL_ID","WAP");
//			jsonObject.put("TXN_AMOUNT", paymentRequestModel.getActualDueAmount());
//			jsonObject.put("WEBSITE" , "APP_STAGING");
//			jsonObject.put("EMAIL" , "test@gmail.com");
//			jsonObject.put("MOBILE_NO" , "9642444934");
//			jsonObject.put("CALLBACK_URL","https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
//			return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

//    public void onStartTransaction(View view) {
//        PaytmPGService Service = PaytmPGService.getProductionService();
//
//
//        //Kindly create complete Map and checksum on your server side and then put it here in paramMap.
//
//        Map<String, String> paramMap = new HashMap<String, String>();
//        paramMap.put("MID", "ANYEMI14182027236332");
//        paramMap.put("ORDER_ID", orderId);
//        paramMap.put("CUST_ID", String.valueOf(paymentRequestModel.getUser_id()));
//        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
//        paramMap.put("CHANNEL_ID", "WAP");
//        paramMap.put("TXN_AMOUNT", paymentRequestModel.getActualDueAmount());
//        paramMap.put("WEBSITE", "APP_STAGING");
//        paramMap.put("EMAIL", "test@gmail.com");
//        paramMap.put("MOBILE_NO", "9642444934");
//        paramMap.put("CALLBACK_URL", "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
//        paramMap.put("CHECKSUMHASH", hash);
//        PaytmOrder Order = new PaytmOrder((HashMap<String, String>) paramMap);
//
//
//        Service.initialize(Order, null);
//
//        Service.startPaymentTransaction(this, true, true,
//                new PaytmPaymentTransactionCallback() {
//
//                    @Override
//                    public void someUIErrorOccurred(String inErrorMessage) {
//                        // Some UI Error Occurred in Payment Gateway Activity.
//                        // // This may be due to initialization of views in
//                        // Payment Gateway Activity or may be due to //
//                        // initialization of webview. // Error Message details
//                        // the error occurred.
//                    }
//
//                    @Override
//                    public void onTransactionResponse(Bundle inResponse) {
//                        Log.d("LOG", "Payment Transaction : " + inResponse);
//                        String status = inResponse.getString("STATUS");
//                        Log.d("Status", status);
//                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
//                        paymentRequestModel.setRr_number(inResponse.getString("TXNID"));
//                        paymentRequestModel.setPayment_type(inResponse.getString("Paytm"));
//                        submitPayment();
//                    }
//
//                    @Override
//                    public void networkNotAvailable() {
//                        // If network is not
//                        // available, then this
//                        // method gets called.
//                    }
//
//                    @Override
//                    public void clientAuthenticationFailed(String inErrorMessage) {
//                        // This method gets called if client authentication
//                        // failed. // Failure may be due to following reasons //
//                        // 1. Server error or downtime. // 2. Server unable to
//                        // generate checksum or checksum response is not in
//                        // proper format. // 3. Server failed to authenticate
//                        // that client. That is value of payt_STATUS is 2. //
//                        // Error Message describes the reason for failure.
//                    }
//
//                    @Override
//                    public void onErrorLoadingWebPage(int iniErrorCode,
//                                                      String inErrorMessage, String inFailingUrl) {
//
//                    }
//
//                    // had to be added: NOTE
//                    @Override
//                    public void onBackPressedCancelTransaction() {
//                        // TODO Auto-generated method stub
//                    }
//
//                    @Override
//                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
//                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
//                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
//                    }
//
//                });
//    }

    private void submitPayment() {

        new BackgroundTask(MerchantActivity.this, new BackgroundThread() {
            @Override
            public Object runTask() {
                return HomeServices.submitPayment(MerchantActivity.this, paymentRequestModel);
            }

            public void taskCompleted(Object data) {
                if (data != null || data.equals("")) {
                    if (data.toString().contains("SUCCESS")) {
                        Globals.showToast(getApplicationContext(), "Payment Success");
                        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("FRAGMENT", "COLLECTION");
                        startActivity(intent);
                    }
                }
            }
        }, getString(R.string.loading_txt)).execute();
    }

}

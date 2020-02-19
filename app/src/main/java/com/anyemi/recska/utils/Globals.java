package com.anyemi.recska.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anyemi.recska.NavigationActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.activities.CollectionsDetailsActivity;
import com.anyemi.recska.bgtask.BackgroundTask;
import com.anyemi.recska.bgtask.BackgroundThread;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.connection.HomeServices;
import com.anyemi.recska.model.CollectionsModel;
import com.anyemi.recska.model.InstamojoPaymentModel;
import com.anyemi.recska.model.PaymentRequestModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Anuprog on 10/19/2016.
 */

public class Globals {
    public static final String SPF_NAME = "NEXER";

    public static void showToast(Context applicationContext, String msg) {
        try {
            Toast toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, applicationContext.getResources().getDimension(R.dimen.toast_size));
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static PaymentRequestModel getInstaAmount(
            Context activity,
            PaymentRequestModel paymentRequestModel,
            InstamojoPaymentModel InstamojoPaymentModel,
            String type) {

        try {

            List<InstamojoPaymentModel.InstServiceTaxBean> taxArrayBean = new ArrayList<>();
            taxArrayBean.addAll(InstamojoPaymentModel.getInst_service_tax());



            String GST_CARD = "";
            String SERVICE_TAX = "";
            String ANYEMI_CHARGES = paymentRequestModel.getServiceCharge();
            String GATE_WAY_CHARGE = "";
            String TOTAL_AMOUNT_WITH_ALL_CHARGES = "";


            double TERM_BILL_AMOUNT = 0; //with Arrears etc

          //  String FIN_ID = SharedPreferenceUtil.getFIN_ID(activity);
            //String FIN_ID = "53";


            for (int i = 0; i < taxArrayBean.size(); i++) {
                Double upper_limit = Double.parseDouble(taxArrayBean.get(i).getTo());
                Double lower_limit = Double.parseDouble(taxArrayBean.get(i).getFrom());
                TERM_BILL_AMOUNT = Double.parseDouble(paymentRequestModel.getActualDueAmount());


                if (type.equals(taxArrayBean.get(i).getPayment_type()) && upper_limit >
                        TERM_BILL_AMOUNT && lower_limit < TERM_BILL_AMOUNT) {

                    GATE_WAY_CHARGE = taxArrayBean.get(i).getExtra_tax();
                    GST_CARD = taxArrayBean.get(i).getGst_perc();
                    SERVICE_TAX = taxArrayBean.get(i).getExtra_tax_perc();




                    Double card_charges = Double.parseDouble(SERVICE_TAX);
                    Double gst_on_card_charges = Double.parseDouble(GST_CARD);

                    card_charges = (card_charges * TERM_BILL_AMOUNT) / 100;
                    gst_on_card_charges = (card_charges * gst_on_card_charges) / 100;

                    Double final_amount = 0.0;
                    Double bank_charges = 0.0;

                    final_amount = card_charges + TERM_BILL_AMOUNT + gst_on_card_charges;
                    bank_charges = card_charges  + gst_on_card_charges+Double.parseDouble(GATE_WAY_CHARGE);

                    TOTAL_AMOUNT_WITH_ALL_CHARGES = String.valueOf(
                            Math.round(final_amount +
                            Double.parseDouble(GATE_WAY_CHARGE)+
                                    Double.parseDouble(ANYEMI_CHARGES))

                    );

                    paymentRequestModel.setBankCharges(bank_charges+"");
                    paymentRequestModel.setTotal_amount(TOTAL_AMOUNT_WITH_ALL_CHARGES+"");

                    break;
                }
            }






            return paymentRequestModel;


        } catch (Exception e) {
            Globals.showToast(activity, "Invalid Data");
            return null;


        }

    }


    public static PaymentRequestModel getPaymentRequestModes(Bundle parametros) {
        try {
            String data = parametros.getString(Constants.PAYMENT_REQUEST_MODEL);
            PaymentRequestModel paymentRequestModel = new Gson().fromJson(data, PaymentRequestModel.class);
            return paymentRequestModel;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void ProceedNextScreen(Context applicationContext, PaymentRequestModel paymentRequestModel) {
//
//        Globals.showToast(applicationContext, "Payment Successful");
//        Intent intent = new Intent(applicationContext, NavigationActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.putExtra("FRAGMENT", "COLLECTION");
//        applicationContext.startActivity(intent);
//    }


    public static void ProceedNextScreen(final Context context, final PaymentRequestModel paymentRequestModel) {
        String l_type = SharedPreferenceUtil.getLoginType(context);

        try {
            if(l_type.equals(Constants.LOGIN_TYPE_CUSTOMER)) {
                Intent intent = new Intent(context, NavigationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("FRAGMENT", "COLLECTION");
                context.startActivity(intent);
            }else {
                Log.e("l_type",l_type);
                final Gson gson=new Gson();
                new BackgroundTask(context, new BackgroundThread() {
                    @Override
                    public Object runTask() {
                        return HomeServices.getCollections(context, SharedPreferenceUtil.getUserId(context)+"&loan_number="+SharedPreferenceUtil.getServiceNo(context)+"");
                    }

                    public void taskCompleted(Object data) {

                        if (data != null || data.equals("")) {
                           // parseData(data);
                            CollectionsModel mResponsedata = new CollectionsModel();
                            mResponsedata = gson.fromJson(data.toString(), CollectionsModel.class);
                            ArrayList<CollectionsModel.CollectionsBean> SCollections = new ArrayList<>();
                            SCollections.addAll(mResponsedata.getCollections());
                            Gson gson = new Gson();
                            Intent i = new Intent(context, CollectionsDetailsActivity.class);
                            i.putExtra(Constants.PAYMENTS_DATA, gson.toJson(SCollections.get(0)));
                            i.putExtra("redirect","collection");
                            context.startActivity(i);


                        } else {
                            Globals.showToast(context, "No Data Found");
                        }
                    }
                }, context.getString(R.string.loading_txt)).execute();

            }

            Globals.showToast(context, "Payment Successful");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

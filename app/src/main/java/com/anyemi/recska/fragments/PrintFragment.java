package com.anyemi.recska.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.anyemi.recska.R;
import com.anyemi.recska.bluetoothPrinter.BluetoothPrinterMain;
import com.anyemi.recska.bluetoothPrinter.SetLogo;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.model.CollectionsModel;
import com.anyemi.recska.model.ReportsModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.anyemi.recska.utils.Utils;
import com.google.gson.Gson;
import com.ngx.BluetoothPrinter;
import com.ngx.BtpCommands;
import com.ngx.NgxImageFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * Created by SuryaTejaChalla on 04-01-2018.
 */

public class PrintFragment extends Fragment implements View.OnClickListener {
    private BluetoothPrinter mBtp = BluetoothPrinterMain.mBtp;
    private IWoyouService woyouService=BluetoothPrinterMain.woyouService;
    private ICallback callback=BluetoothPrinterMain.callback;
    int fontSize=20;


    Bundle r_bundle;
    String print_type;
    Map<String, String> responseMap;
    CollectionsModel.CollectionsBean printDetails;
    private EditText mEdit;
    private TextView tvSeekBar;
    private int minutes = 1440;
    private CheckBox cbVolatile;
    Uri fileUri=null;

    ArrayList<String> paymmentModesNames = new ArrayList<>();
    ArrayList<String> paymmentModesId = new ArrayList<>();
    ArrayList<String> aryTaxNames = new ArrayList<>();
    ArrayList<String> aryTaxIds = new ArrayList<>();
    ArrayList<ReportsModel.PaymentWiseTranscitionsDetailsBean> mReports = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ascii_layout, container, false);
        r_bundle = getActivity().getIntent().getExtras();
        String uri=SharedPreferenceUtil.getFromPreference(getContext(),"LogoUri","");
        Log.e("LogoUri",uri+" logo");
        if(uri!="")
            fileUri= Uri.parse(uri);
        String data = r_bundle.getString("print_data");
        String reportData=r_bundle.getString("reports_data");
        if(reportData!=null){
            parseData(reportData);
        }

        paymmentModesNames.clear();
        paymmentModesId.clear();
        aryTaxNames.clear();
        aryTaxIds.clear();

        paymmentModesNames.addAll(Arrays.asList(getResources().getStringArray(R.array.payment_names)));
        paymmentModesId.addAll(Arrays.asList(getResources().getStringArray(R.array.payment_ids)));
        aryTaxNames.addAll(Arrays.asList(getResources().getStringArray(R.array.tax_type_array)));
        aryTaxIds.addAll(Arrays.asList(getResources().getStringArray(R.array.tax_id_array)));

        Gson gson = new Gson();
        printDetails = gson.fromJson(data, CollectionsModel.CollectionsBean.class);
        intiView(view);
        return view;
    }
    private void parseData(String data) {

        mReports.clear();
        Gson gson = new Gson();
        ReportsModel mResponsedata = new ReportsModel();
        mResponsedata = gson.fromJson(data, ReportsModel.class);
        try {
            mReports.addAll(mResponsedata.getPaymentWiseTranscitionsDetails());
        } catch (Exception e) {
            e.printStackTrace();
            mReports.clear();
            //Globals.showToast(getActivity(),mResponsedata.getMsg());
         //   Globals.showToast(getActivity(), "Sorry No data found");
        }
        String login_type= SharedPreferenceUtil.getLoginType(getActivity());
        if(login_type.equals(Constants.LOGIN_TYPE_COLLECTION_AGENT)|| login_type.equals(Constants.LOGIN_TYPE_CUSTOMER)){
            if(woyouService!=null)
            for(int i=0;i<mReports.size();i++){
                try {
                    woyouService.setAlignment(1, callback);
                    woyouService.printTextWithFont("\n-------" + "ARECS" + "-------\n", "BOLD", 30, callback);
                    woyouService.printTextWithFont("Reports\n", "BOLD", fontSize, callback);
                    woyouService.setAlignment(0,callback);
                    woyouService.printTextWithFont(mReports.get(i).getPayment_type()+": "+mReports.get(i).getTotalTranscitions()+"\n","",fontSize,callback);
                    woyouService.printTextWithFont("Bill Amount: "+mReports.get(i).getBillamount()+"\n","",fontSize,callback);
                    woyouService.printTextWithFont("Adjustment Amount: "+mReports.get(i).getAdjustmentamount()+"\n","",fontSize,callback);
                    woyouService.printTextWithFont("Arears Amount: "+mReports.get(i).getArrearsamount()+"\n","",fontSize,callback);
                    woyouService.printTextWithFont("Total Amount: "+mReports.get(i).getTotalamount()+"\n\n","",fontSize,callback);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            getActivity().finish();
        }
    }

    private void intiView(View v) {

        mEdit = v.findViewById(R.id.txt);
        mEdit.setText(print_type);

        Button btnPrintLogo = v.findViewById(R.id.btnPrintLogo);
        btnPrintLogo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mBtp.printLogo();
            }
        });

        Button btnPrintText = v.findViewById(R.id.btnPrintText);
        btnPrintText.setOnClickListener(this);
        btnPrintText.setText("Print");

        Button btnPrint = v.findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(this);

        Button btnSetLogo = v.findViewById(R.id.btnChangeLogo);
        btnSetLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment sl = new SetLogo();
                BluetoothPrinterMain.changeFragment(R.id.container, sl, true);
            }
        });

//        Button btnPrintLineFeed = (Button) v.findViewById(R.id.btnPrintLineFeed);
//        btnPrintLineFeed.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mBtp.printLineFeed();
//            }
//        });



//        Button btnPrintAsciiText = (Button) v.findViewById(R.id.btnPrintAsciiText);
//        btnPrintAsciiText.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                String txt = mEdit.getText().toString();
//                mBtp.setPrintFontStyle(BtpCommands.FONT_KANNADA);
//                mBtp.printAsciiText(txt);
//            }
//        });

//        Button btnPrintKnAksharamale = (Button) v
//                .findViewById(R.id.btnPrintKnAksharamale);
//        btnPrintKnAksharamale.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                String txt = "C D E F G H IÄ J K L M N O CA CB\n\n"
//                        +
//
//                        "PÀ R UÀ WÀ k \n"
//                        + "ZÀ bÀ d gÀhÄ Y \n"
//                        + "l oÀ qÀ qsÀ uÁ t \n"
//                        + "vÀ xÀ zÀ zsÀ £À \n"
//                        + "¥À ¥sÀ ¨Á ¨sÀ ªÀÄ \n"
//                        + "AiÀÄ ® ªÀ ±À µÀ ¸À ºÀ ¼À \n\n"
//                        +
//
//                        "Pï PÀ PÁ Q QÃ PÀÄ PÀÈ PÉ PÉÃ PÉÊ PÉÆ PÉÆÃ PË PÀA PÀB \n"
//                        + "Sï R SÁ T TÃ RÄ RÆ RÈ SÉ SÉÃ SÉÊ SÉÆ SÉÆÃ SË RA RB\n"
//                        + "Uï UÀ UÁ V VÃ UÀÄ UÀÆ UÀÈ UÉ UÉÃ UÉÊ UÉÆ UÉÆÃ UË UÀA UÀB\n"
//                        + "Wï WÀ WÁ X XÃ WÀÄ WÀÆ WÀÈ WÉ WÉÃ WÉÊ WÉÆ WÉÆÃ WË WÀA WÀB\n\n"
//                        +
//
//                        "Zï ZÀ ZÁ a aÃ ZÀÄ ZÀÆ ZÀÈ ZÉ ZÉÃ ZÉÊ ZÉÆ ZÉÆÃ ZË ZÀA ZÀB\n"
//                        + "bï bÀ bÁ c cÃ bÀÄ bÀÆ bÀÈ bÉ bÉÃ bÉÊ bÉÆ bÉÆÃ bË bÀA bÀB\n"
//                        + "eï d eÁ f fÃ dÄ dÆ dÈ eÉ eÉÃ eÉÊ eÉÆ eÉÆÃ eË dA dB\n"
//                        + "gÀhiï gÀhÄ gÀhiÁ jhÄ jhÄÃ gÀhÄÄ gÀhÄÆ gÀhÄÈ gÉhÄ gÉhÄÃ gÉhÄÊ gÉhÆ gÉhÆÃ gÀhiË gÀhÄA gÀhÄB\n"
//                        + "\n"
//                        + "mï l mÁ n nÃ lÄ lÆ lÈ mÉ mÉÃ mÉÊ mÉÆ mÉÆÃ mË lA lB\n"
//                        + "oï oÀ oÁ p pÃ oÀÄ oÀÆ oÀÈ oÉ oÉÃ oÉÊ oÉÆ oÉÆÃ oË oÀA oÀB \n"
//                        + "qï qÀ qÁ r rÃ qÀÄ qÀÆ qÀÈ qÉ qÉÃ qÉÊ qÉÆ qÉÆÃ qË qÀA qÀB \n"
//                        + "qsï qsÀ qsÁ rü rüÃ qsÀÄ qsÀÆ qsÀÈ qsÉ qsÉÃ qsÉÊ qsÉÆ qsÉÆÃ qsË qsÀA qsÀB\n"
//                        + "uï t uÁ tÂ tÂÃ tÄ tÆ tÈ uÉ uÉÃ uÉÊ uÉÆ uÉÆÃ uË tA tB \n"
//                        + "\n"
//                        + "vï vÀ vÁ w wÃ vÀÄ vÀÆ vÀÈ vÉ vÉÃ vÉÊ vÉÆ vÉÆÃ vË vÀA vÀB\n"
//                        + "xï xÀ xÁ y yÃ xÀÄ xÀÆ xÀÈ xÉ xÉÃ xÉÊ xÉÆ xÉÆÃ xË xÀA xÀB\n"
//                        + "zï zÀ zÁ ¢ ¢Ã zÀÄ zÀÆ zÀÈ zÉ zÉÃ zÉÊ zÉÆ zÉÆÃ zË zÀA zÀB\n"
//                        + "zsï zsÀ zsÁ ¢ü ¢üÃ zsÀÄ zsÀÆ zsÀÈ zsÉ zsÉÃ zsÉÊ zsÉÆ zsÉÆÃ zsË zsÀA zsÀB\n"
//                        + "£ï £À £Á ¤ ¤Ã £ÀÄ £ÀÆ £ÀÈ £É £ÉÃ £ÉÊ £ÉÆ £ÉÆÃ £Ë £ÀA £ÀB\n"
//                        + "\n"
//                        + "¥ï ¥À ¥Á ¦ ¦Ã ¥ÀÄ ¥ÀÆ ¥ÀÈ ¥É ¥ÉÃ ¥ÉÊ ¥ÉÆ ¥ÉÆÃ ¥Ë ¥ÀA ¥ÀB\n"
//                        + "¥sï ¥sÀ ¥sÁ ¦ü ¦üÃ ¥sÀÄ ¥sÀÆ ¥sÀÈ ¥sÉ ¥sÉÃ ¥sÉÊ ¥sÉÆ ¥sÉÆÃ ¥sË ¥sÀA ¥sÀB\n"
//                        + "¨ï § ¨Á © ©Ã §Ä §Æ §È ¨É ¨ÉÃ ¨ÉÊ ¨ÉÆ ¨ÉÆÃ ¨Ë §A §B \n"
//                        + "¨sï ¨sÀ ¨sÁ ©ü ©Ã ¨sÀÄ ¨sÀÆ ¨sÀÈ ¨sÉ ¨sÉÃ ¨sÉÊ ¨sÉÆ ¨sÉÆÃ ¨sË ¨sÀA ¨sÀB\n"
//                        + "ªÀiï ªÀÄ ªÀiÁ «Ä «ÄÃ ªÀÄÄ ªÀÄÆ ªÀÄÈ ªÉÄ ªÉÄÃ ªÉÄÊ ªÉÆ ªÉÆÃ ªÀiË ªÀÄA ªÀÄB \n"
//                        + "\n"
//                        + "AiÀiï AiÀÄ AiÀiÁ ¬Ä ¬ÄÃ AiÀÄÄ AiÀÄÆ AiÀÄÈ AiÉÄ AiÉÄÃ AiÉÄÊ AiÉÆ AiÉÆÃ AiÀiË AiÀÄA AiÀÄB\n"
//                        + "gï gÀ gÁ j jÃ gÀÄ gÀÆ gÀÈ gÉ gÉÃ gÉÊ gÉÆ gÉÆÃ gË gÀA gÀB \n"
//                        + "¯ï ® ¯Á ° °Ã ®Ä ®Æ ®È ¯É ¯ÉÃ ¯ÉÊ ¯ÉÆ ¯ÉÆÃ ¯Ë ®A ®B\n"
//                        + "ªï ªÀ ªÁ « «Ã ªÀÅ ªÀÇ ªÀÈ ªÉ ªÉÃ ªÉÊ ªÉÇ ªÉÇÃ ªË ªÀA ªÀB\n"
//                        + "±ï ±À ±Á ² ²Ã ±ÀÄ ±ÀÆ ±ÀÈ ±É ±ÉÃ ±ÉÊ ±ÉÆ ±ÉÆÃ ±Ë ±ÀA ±ÀB\n"
//                        + "µï µÀ µÁ ¶ ¶Ã µÀÄ µÀÆ µÀÈ µÉ µÉÃ µÉÊ µÉÆ µÉÆÃ µË µÀA µÀB\n"
//                        + "¸ï ¸À ¸Á ¹ ¹Ã ¸ÀÄ ¸ÀÆ ¸ÀÈ ¸É ¸ÉÃ ¸ÉÊ ¸ÉÆ ¸ÉÆÃ ¸Ë ¸ÀA ¸ÀB\n"
//                        + "ºï ºÀ ºÁ » »Ã ºÀÄ ºÀÆ ºÀÈ ºÉ ºÉÃ ºÉÊ ºÉÆ ºÉÆÃ ºË ºÀA ºÀB\n"
//                        + "¼ï ¼À ¼Á ½ ½Ã ¼ÀÄ ¼ÀÆ ¼ÀÈ ¼É ¼ÉÃ ¼ÉÊ ¼ÉÆ ¼ÉÆÃ ¼Ë ¼ÀA ¼ÀB \n\n";
//                mEdit.setText(txt);
//                mBtp.setPrintFontStyle(BtpCommands.FONT_KANNADA);
//                mBtp.printAsciiText(txt);
//            }
//        });

//        Button btnSetFontBold = (Button) v.findViewById(R.id.btnSetFontBold);
//        btnSetFontBold.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
//            }
//        });

//        Button btnSetFontRegular = (Button) v.findViewById(R.id.btnSetFontRegular);
//        btnSetFontRegular.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_REGULAR);
//            }
//        });

//        Button btnSetFontNormal = (Button) v.findViewById(R.id.btnSetFontNormal);
//        btnSetFontNormal.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
//            }
//        });
//
//        Button btnSetFontKannada = (Button) v.findViewById(R.id.btnSetFontkannada);
//        btnSetFontKannada.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mBtp.setPrintFontStyle(BtpCommands.FONT_KANNADA);
//            }
//        });
//
//
//        Button btnSetFontDoubleWidth = (Button) v
//                .findViewById(R.id.btnSetFontDoubleWidth);
//        btnSetFontDoubleWidth.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_WIDTH);
//            }
//        });
//
//        Button btnSetFontDoubleHeight = (Button) v
//                .findViewById(R.id.btnSetFontDoubleHeight);
//        btnSetFontDoubleHeight.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_HEIGHT);
//            }
//        });
//
//        Button btnSetFontDoubleWidthAndHeight = (Button) v
//                .findViewById(R.id.btnSetFontDoubleWidthAndHeight);
//        btnSetFontDoubleWidthAndHeight
//                .setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_W_H);
//                    }
//                });



//        Button btnTestBill = (Button) v.findViewById(R.id.btnTestBill);
//        btnTestBill.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
////				mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_REGULAR);
////				mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
////				mBtp.printLogo();
////				mBtp.printLineFeed();
////				mBtp.printTextLine("----------------------------------------");
////				mBtp.printTextLine("Item              Quantity        Price");
////				mBtp.printTextLine("----------------------------------------");
////				mBtp.printTextLine("Item 1                1            1.00");
////				mBtp.printTextLine("Some big item        10         7890.00");
////				mBtp.printTextLine("Next Item           999        10000.00");
////				mBtp.printLineFeed();
////				mBtp.printTextLine("----------------------------------------");
////				mBtp.printTextLine("Total                          17891.00");
////				mBtp.printTextLine("----------------------------------------");
////				mBtp.printLineFeed();
////				mBtp.printTextLine("         Thank you ! Visit Again");
////				mBtp.printLineFeed();
////				mBtp.printTextLine("****************************************");
////				mBtp.printLineFeed();
//
//            }
//        });
//
//        Button btnNumber = (Button) v.findViewById(R.id.btnNumber);
//        btnNumber.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mBtp.printTextLine("          #   ");
//                mBtp.printTextLine("         ##   ");
//                mBtp.printTextLine("        # #   ");
//                mBtp.printTextLine("          #   ");
//                mBtp.printTextLine("          #   ");
//                mBtp.printTextLine("          #   ");
//                mBtp.printTextLine("        ##### ");
//            }
//        });
//
//        Button btnNumber1 = (Button) v.findViewById(R.id.btnNumber1);
//        btnNumber1.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mBtp.printTextLine("          _    ");
//                mBtp.printTextLine("       _ (_)   ");
//                mBtp.printTextLine("      (_)(_)   ");
//                mBtp.printTextLine("         (_)   ");
//                mBtp.printTextLine("         (_)   ");
//                mBtp.printTextLine("         (_)   ");
//                mBtp.printTextLine("       _ (_) _ ");
//                mBtp.printTextLine("      (_)(_)(_)");
//                mBtp.printLineFeed();
//            }
//        });
//
//        Button btnAbtDriver = (Button) v.findViewById(R.id.btnAbtDriver);
//        btnAbtDriver.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                String s = mBtp.aboutDriver();
//                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        Button btnBatteryStatus = (Button) v.findViewById(R.id.btnBatteryStatus);
//        btnBatteryStatus.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mBtp.GetBatteryStatus();
//            }
//        });

//        Button btnPaperStatus = (Button) v.findViewById(R.id.btnPaperStatus);
//        btnPaperStatus.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mBtp.GetPaperStatus();
//            }
//        });
//
//        cbVolatile = (CheckBox) v.findViewById(R.id.checkBox1);

//        SeekBar seekBar = (SeekBar) v.findViewById(R.id.seekBar1);
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progresValue,
//                                          boolean fromUser) {
//                minutes = progresValue;
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                tvSeekBar.setText("Auto switch off idle Minutes: " + minutes);
//            }
//        });
//
//        tvSeekBar = (TextView) v.findViewById(R.id.tvSeekBar);
//        minutes = seekBar.getProgress();
//        tvSeekBar.setText("Auto switch off idle Minutes: " + minutes);
//
//        Button btnEnableAutoSwitchOff = (Button) v.findViewById(R.id.btnEnableAutoSwitchOff);
//        btnEnableAutoSwitchOff.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mBtp.setAutoSwitchOffTime(minutes, cbVolatile.isChecked());
//            }
//        });
//
//        Button btnDisableAutoSwitchOff = (Button) v.findViewById(R.id.btnDisableAutoSwitchOff);
//        btnDisableAutoSwitchOff.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mBtp.disableAutoSwitchOff();
//            }
//        });
//
//        Button btnLeftAlign = (Button) v.findViewById(R.id.btnLeftAlign);
//        btnLeftAlign.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mBtp.setAlignment(BtpCommands.LEFT_ALIGN);
//            }
//        });
//
//        Button btnCenterAlign = (Button) v.findViewById(R.id.btnCenterAlign);
//        btnCenterAlign.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mBtp.setAlignment(BtpCommands.CENTER_ALIGN);
//            }
//        });

    }

    @Override
    public void onClick(View view) {
        String login_type= SharedPreferenceUtil.getLoginType(getActivity());

        switch (view.getId()) {

            case R.id.btnPrintText:

//


                if (printDetails != null) {
                    if(login_type.equals(Constants.LOGIN_TYPE_COLLECTION_AGENT)|| login_type.equals(Constants.LOGIN_TYPE_CUSTOMER)){

                        String t_type = "";
                        String p_type = "";

//                        if (printDetails.getTax_type() != null) {
//                            int taxtype = aryTaxIds.indexOf(printDetails.getTax_type());
//                            t_type=(aryTaxNames.get(taxtype));
//                        }
                        if (printDetails.getPayment_type() != null) {
//                            int taxtype = paymmentModesId.indexOf(printDetails.getPayment_type());
//                            p_type=(paymmentModesNames.get(taxtype));

                            p_type = printDetails.getPayment_type();
                        }

                        // p_type = printDetails.getPayment_type();

                        String date = Utils.parseDate(printDetails.getDue_date());

                        String p_total_amt="";
                        String p_ref="";

                        if (printDetails.getTotal_emi() != null) {
                            p_total_amt=printDetails.getTotal_emi();
                        }else if(printDetails.getTotal_amount() != null){
                            p_total_amt=printDetails.getTotal_amount();
                        }

                        if (printDetails.getPay_ref() != null) {
                            p_ref=printDetails.getPay_ref();
                        }else if(printDetails.getTotal_amount() != null){
                            p_ref="";
                        }


                        Double emi_amount=Double.parseDouble(printDetails.getTotal_amount());
                        Double bankCharges=Double.parseDouble(printDetails.getBank_charges());
                        Double user_charges=Double.parseDouble(printDetails.getService_charge());
                        Double emi_amount_without_charge=emi_amount-user_charges;
                        Double total_amount=emi_amount+bankCharges;


                        System.out.println("------- ARECS -------");
                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_WIDTH);
                        System.out.println("Payment Slip");
                        System.out.println("Payment Id    : " + printDetails.getTxn_payment_id());
                        System.out.println("Pay Ref Id    : " + p_ref);
                        System.out.println("Service No    : " + printDetails.getLoan_number());
                        System.out.println("Name          : " + printDetails.getCustomer_name());
                        System.out.println("Due Date      : " + date);
                        // System.out.println("Paid Date     : " + Utils.getCurrentDateTime());
                        System.out.println("Paid Date     : " + Utils.parseDateTime(printDetails.getDate_time()));
                        System.out.println("Payment Type  : " + p_type);
                        System.out.println("Bill Amount   : " + Utils.parseAmount(String.valueOf(emi_amount_without_charge)));
                        System.out.println("User Charges  : " + Utils.parseAmount(printDetails.getService_charge()));
                        System.out.println("Bank Charges  : " + Utils.parseAmount(printDetails.getBank_charges()));
                        System.out.println("Total Amount  : " + Utils.parseAmount(String.valueOf(total_amount)));





                        mBtp.printLogo();
                        mBtp.printLineFeed();
                        mBtp.setAlignment(BtpCommands.CENTER_ALIGN);
                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_W_H);
                        // mBtp.printTextLine("-------" + "CDMA" + "-------");
                        mBtp.printTextLine("------- ARECS -------");
                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_WIDTH);
                        mBtp.printTextLine("Payment Slip");
                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_REGULAR);
                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
                        // mBtp.printLineFeed();
                        // mBtp.printTextLine("Narsipatnam Muncipality");
                        mBtp.printLineFeed();
                        mBtp.setAlignment(BtpCommands.LEFT_ALIGN);
                        mBtp.printTextLine("Payment Id    : " + printDetails.getTxn_payment_id());
                        if (printDetails.getPay_ref() != null) {
                            mBtp.printTextLine("Pay Ref Id    : " + printDetails.getPay_ref());
                        }
                        mBtp.printTextLine("Service No    : " + printDetails.getLoan_number());
                        mBtp.printTextLine("Name          : " + printDetails.getCustomer_name());
                        mBtp.printTextLine("Due Date      : " + date);
                        mBtp.printTextLine("Payment Date  : " + Utils.parseDateTime(printDetails.getDate_time()));
                        mBtp.printTextLine("Payment Type  : " + p_type);
                        mBtp.printTextLine("Bill Amount   : " + "Rs. "+Utils.parseAmount(printDetails.getEmi_amount())+" /-");
                        mBtp.printTextLine("Adj Amount    : " + "Rs. "+Utils.parseAmount(printDetails.getAdjamt())+" /-");
                        mBtp.printTextLine("Arrears       : " + "Rs. "+Utils.parseAmount(printDetails.getNewarrears())+" /-");
                        mBtp.printTextLine("Reconnect Fee : " + "Rs. "+Utils.parseAmount(printDetails.getReconnection_fee())+" /-");
                        // mBtp.printTextLine("Sur Charge    : " + "Rs. "+Utils.parseAmount(printDetails.getSurcharge_fee())+" /-");
                        mBtp.printTextLine("Sur Charge    : " + "Rs. 0 /-");
                        mBtp.printTextLine("User Charges  : " + "Rs. "+Utils.parseAmount(printDetails.getService_charge())+" /-");
                        mBtp.printTextLine("Bank Charges  : " + "Rs. "+Utils.parseAmount(printDetails.getBank_charges())+" /-");
                        mBtp.printTextLine("Total Amount  : " + "Rs. "+Utils.parseAmount(String.valueOf(total_amount))+" /-");
                        mBtp.printLineFeed();
                        //mBtp.printTextLine("********Charges as per CDMA Norms********");
                        mBtp.printTextLine("********Charges as per ARECS Norms********");
                        mBtp.printLineFeed();
                        mBtp.printTextLine("                             Signature");
                        mBtp.printLineFeed();
                        mBtp.printTextLine("         Thank you Payment Success        ");
                        mBtp.printTextLine(" *****************************************");
                        mBtp.printLineFeed();
                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
                        mBtp.printTextLine(" Contact Tollfree : 18004251539");
                        mBtp.printTextLine(" Contact : 8008615500/2200/3300");
                        mBtp.printTextLine(" Support: recsrevenue@gmail.com");
                        mBtp.printTextLine(" Support: helpdesk@anyemi.com");
                        mBtp.printLineFeed();
                        mBtp.printLineFeed();

                    }else if(login_type.equals(Constants.LOGIN_TYPE_GVMC)){


                        mBtp.printLogo();
                        mBtp.printLineFeed();
                        mBtp.setAlignment(BtpCommands.CENTER_ALIGN);
                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_W_H);
                        mBtp.printTextLine("-------" + print_type + "-------");
                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_WIDTH);
                        mBtp.printTextLine("Payment Slip");
                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_REGULAR);
                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
                        mBtp.printLineFeed();
                        mBtp.setAlignment(BtpCommands.LEFT_ALIGN);

                        System.out.println(responseMap.get("Payment Id     "));
                        System.out.println(responseMap.get("Name           "));
                        System.out.println(responseMap.get("Due Date       "));
                        System.out.println(responseMap.get("Payment Date   "));
                        System.out.println(responseMap.get("Assessment NO  "));
                        System.out.println(responseMap.get("Tax Type       "));
                        System.out.println(responseMap.get("Payment Type   "));
                        System.out.println(responseMap.get("Bill Amount    "));
                        System.out.println(responseMap.get("Bank Charges   "));
                        System.out.println(responseMap.get("Fine Amount    "));
                        System.out.println(responseMap.get("Total Amount   "));

                        mBtp.printTextLine("Payment Id     : " + responseMap.get("Payment Id     "));
                        mBtp.printTextLine("Name           : " + responseMap.get("Name           "));
                        mBtp.printTextLine("Due Date       : " + responseMap.get("Due Date       "));
                        mBtp.printTextLine("Payment Date   : " + responseMap.get("Payment Date   "));
                        mBtp.printTextLine("Assessment No  : " + responseMap.get("Assessment NO  "));
                        mBtp.printTextLine("Tax Type       : " + responseMap.get("Tax Type       "));
                        mBtp.printTextLine("Payment Type   : " + responseMap.get("Payment Type   "));
                        mBtp.printTextLine("Bill Amount    : " + responseMap.get("Bill Amount    "));
                        mBtp.printTextLine("Fine Amount    : " + responseMap.get("Fine Amount    "));
                        mBtp.printTextLine("Bank Charge    : " + responseMap.get("Bank Charges   "));
                        mBtp.printTextLine("Total Amount   : " + responseMap.get("Total Amount   "));
                        mBtp.printLineFeed();
                        mBtp.printTextLine("********Charges as per GVMC Norms********");
                        mBtp.printLineFeed();
                        mBtp.printTextLine("                             Signature");
                        mBtp.printLineFeed();
                        mBtp.printTextLine("         Thank you Payment Sucess         ");
                        mBtp.printTextLine(" *****************************************");
                        mBtp.printLineFeed();
                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
                        mBtp.printTextLine(" Contact: 8008612200/8008615500");
                        mBtp.printTextLine(" Support: helpdesk@anyemi.com");
                        mBtp.printLineFeed();
                        mBtp.printLineFeed();

                    }else if(login_type.equals(Constants.LOGIN_TYPE_APEPDCL)){


                        mBtp.printLogo();
                        mBtp.printLineFeed();
                        mBtp.setAlignment(BtpCommands.CENTER_ALIGN);
                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_W_H);
                        mBtp.printTextLine("-----" + print_type + "-----");
                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_WIDTH);
                        mBtp.printTextLine("Payment Slip");
                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_REGULAR);
                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
                        mBtp.printLineFeed();
                        mBtp.setAlignment(BtpCommands.LEFT_ALIGN);

                        System.out.println(responseMap.get("Payment Id     "));
                        System.out.println(responseMap.get("Payment Ref Id "));
                        System.out.println(responseMap.get("Service Number "));
                        System.out.println(responseMap.get("Name           "));
                        System.out.println(responseMap.get("Bill Date      "));
                        System.out.println(responseMap.get("Paid Date      "));
                        System.out.println(responseMap.get("Payment Type   "));
                        System.out.println(responseMap.get("Bill Amount    "));
                        System.out.println(responseMap.get("Service Charge "));
                        System.out.println(responseMap.get("Bank Charge    "));
                        System.out.println(responseMap.get("Total Amount   "));

                        mBtp.printTextLine("Payment Id     : " + responseMap.get("Payment Id     "));
                        mBtp.printTextLine("Payment Ref Id : " + responseMap.get("Payment Ref Id "));
                        mBtp.printTextLine("Service Number : " + responseMap.get("Service Number "));
                        mBtp.printTextLine("Name           : " + responseMap.get("Name           "));
                        mBtp.printTextLine("Bill Date      : " + responseMap.get("Bill Date      "));
                        mBtp.printTextLine("Paid Date      : " + responseMap.get("Paid Date      "));
                        mBtp.printTextLine("Payment Type   : " + responseMap.get("Payment Type   "));
                        mBtp.printTextLine("Bill Amount    : " + responseMap.get("Bill Amount    "));
                        mBtp.printTextLine("Service Charge : " + responseMap.get("Service Charge "));
                        mBtp.printTextLine("Bank Charge    : " + responseMap.get("Bank Charge    "));
                        mBtp.printTextLine("Total Amount   : " + responseMap.get("Total Amount   "));
                        mBtp.printLineFeed();
                        mBtp.printTextLine("********Charges as per APEPDCL Norms********");
                        mBtp.printLineFeed();
                        mBtp.printTextLine("                             Signature");
                        mBtp.printLineFeed();
                        mBtp.printTextLine("         Thank you Payment Sucess         ");
                        mBtp.printTextLine(" *****************************************");
                        mBtp.printLineFeed();
                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
                        mBtp.printTextLine(" Contact: 8008612200/8008615500");
                        mBtp.printTextLine(" Support: helpdesk@anyemi.com");
                        mBtp.printLineFeed();
                        mBtp.printLineFeed();
                    }


                }else{
                    Globals.showToast(getActivity(),"Error while reading data");
                }



                break;
            case R.id.btnPrint:

                if (printDetails != null) {
                    if(woyouService!=null) {
                        try {
                            if(fileUri!=null) {
                                //  BitmapFactory.Options options = new BitmapFactory.Options();
/*
                            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                                    options);
*/
                                Bitmap bitmap = NgxImageFactory.LoadLogo(fileUri.getPath());
                                woyouService.setAlignment(1, callback);
                                woyouService.printBitmap(bitmap, callback);
                            }


                            if (login_type.equals(Constants.LOGIN_TYPE_COLLECTION_AGENT) || login_type.equals(Constants.LOGIN_TYPE_CUSTOMER)) {

                                String t_type = "";
                                String p_type = "";

//                        if (printDetails.getTax_type() != null) {
//                            int taxtype = aryTaxIds.indexOf(printDetails.getTax_type());
//                            t_type=(aryTaxNames.get(taxtype));
//                        }
                                if (printDetails.getPayment_type() != null) {
//                            int taxtype = paymmentModesId.indexOf(printDetails.getPayment_type());
//                            p_type=(paymmentModesNames.get(taxtype));

                                    p_type = printDetails.getPayment_type();
                                }

                                // p_type = printDetails.getPayment_type();

                                String date = Utils.parseDate(printDetails.getDue_date());

                                String p_total_amt = "";
                                String p_ref = "";

                                if (printDetails.getTotal_emi() != null) {
                                    p_total_amt = printDetails.getTotal_emi();
                                } else if (printDetails.getTotal_amount() != null) {
                                    p_total_amt = printDetails.getTotal_amount();
                                }

                                if (printDetails.getPay_ref() != null) {
                                    p_ref = printDetails.getPay_ref();
                                } else if (printDetails.getTotal_amount() != null) {
                                    p_ref = "";
                                }


                                Double emi_amount = Double.parseDouble(printDetails.getTotal_amount());
                                Double bankCharges = Double.parseDouble(printDetails.getBank_charges());
                                Double user_charges = Double.parseDouble(printDetails.getService_charge());
                                Double emi_amount_without_charge = emi_amount - user_charges;
                                Double total_amount = emi_amount + bankCharges;

                               /* woyouService.setAlignment(1, callback);
                                woyouService.printTextWithFont("\n-------" + "ARECS" + "-------\n", "BOLD", 30, callback);
                                woyouService.printTextWithFont("Payment Slip\n", "BOLD", fontSize, callback);
                                woyouService.setAlignment(0,callback);

                                woyouService.printTextWithFont("Payment Id    : " + printDetails.getTxn_payment_id() + "\n", "", fontSize, callback);
                                woyouService.printTextWithFont("Pay Ref Id    : " + p_ref+ "\n", "", fontSize, callback);
                                woyouService.printTextWithFont("Service No    : " + printDetails.getLoan_number()+ "\n", "", fontSize, callback);
                                woyouService.printTextWithFont("Name          : " + printDetails.getCustomer_name()+ "\n", "", fontSize, callback);
                                woyouService.printTextWithFont("Due Date      : " + date+ "\n", "", fontSize, callback);
                                woyouService.printTextWithFont("Paid Date     : " + Utils.parseDateTime(printDetails.getDate_time())+ "\n", "", fontSize, callback);
                                woyouService.printTextWithFont("Payment Type  : " + p_type+ "\n", "", fontSize, callback);
                                woyouService.printTextWithFont("Bill Amount   : " + Utils.parseAmount(String.valueOf(emi_amount_without_charge))+ "\n", "", fontSize, callback);
                                woyouService.printTextWithFont("User Charges  : " + Utils.parseAmount(printDetails.getService_charge())+ "\n", "", fontSize, callback);
                                woyouService.printTextWithFont("Bank Charges  : " + Utils.parseAmount(printDetails.getBank_charges())+ "\n", "", fontSize, callback);
                                woyouService.printTextWithFont("Total Amount  : " + Utils.parseAmount(String.valueOf(total_amount))+ "\n", "", fontSize, callback);

*/
                                //   mBtp.printLogo();
                              /*  mBtp.printLineFeed();
                                mBtp.setAlignment(BtpCommands.CENTER_ALIGN);
                                mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
                                mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_W_H);*/
                                // mBtp.printTextLine("-------" + "CDMA" + "-------");
                                woyouService.setAlignment(1, callback);
                                woyouService.printTextWithFont("\n-------" + "ARECS" + "-------\n", "BOLD", 30, callback);
                                woyouService.printTextWithFont("Payment Slip\n", "BOLD", fontSize, callback);

                                // mBtp.printLineFeed();
                                // mBtp.printTextLine("Narsipatnam Muncipality");
                                woyouService.setAlignment(0,callback);
                                woyouService.printTextWithFont("Payment Id    : " + printDetails.getTxn_payment_id()+"\n", "BOLD", fontSize, callback);

                                if (printDetails.getPay_ref() != null) {
                                    woyouService.printTextWithFont("Pay Ref Id    : "+ printDetails.getPay_ref()+"\n", "BOLD", fontSize, callback);
                                }

                                woyouService.printTextWithFont("Service No    : " + printDetails.getLoan_number()+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Name          : " + printDetails.getCustomer_name()+"\n", "BOLD", fontSize, callback);

                                woyouService.printTextWithFont("Due Date      : " + date+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Payment Date  : " + Utils.parseDateTime(printDetails.getDate_time())+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Payment Type  : " + p_type+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Bill Amount   : " + "Rs. " + Utils.parseAmount(printDetails.getEmi_amount()) + " /-\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Adj Amount    : " + "Rs. " + Utils.parseAmount(printDetails.getAdjamt()) + " /-\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Arrears       : " + "Rs. " + Utils.parseAmount(printDetails.getNewarrears()) + " /-\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Reconnect Fee : " + "Rs. " + Utils.parseAmount(printDetails.getReconnection_fee()) + " /-\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Sur Charge    : " + "Rs. 0 /-\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("User Charges  : " + "Rs. " + Utils.parseAmount(printDetails.getService_charge()) + " /-\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Bank Charges  : " + "Rs. " + Utils.parseAmount(printDetails.getBank_charges()) + " /-\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Total Amount  : " + "Rs. " + Utils.parseAmount(String.valueOf(total_amount)) + " /-\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("******Charges as per ARECS Norms******\n\n\n", "", fontSize, callback);
                                woyouService.printTextWithFont("                             Signature\n", "", fontSize, callback);
                                woyouService.printTextWithFont("         Thank you Payment Success    \n", "", fontSize, callback);

                                woyouService.printTextWithFont("**************************************\n", "", fontSize, callback);

                                //mBtp.printTextLine("********Charges as per CDMA Norms********");
                                woyouService.printTextWithFont("Contact Tollfree:18004251539\n", "BOLD", 30, callback);
                             //   woyouService.printTextWithFont(" Contact : 8008615500/2200/3300\n", "BOLD", 30, callback);
                                woyouService.printTextWithFont("Support:recsrevenue@gmail.com\n", "BOLD", 30, callback);
                             //   woyouService.printTextWithFont(" Support: helpdesk@anyemi.com\n\n", "BOLD", 30, callback);




                            } else if (login_type.equals(Constants.LOGIN_TYPE_GVMC)) {


                                //  mBtp.printLogo();
                                //  mBtp.printLineFeed();
                                woyouService.setAlignment(1, callback);
                                woyouService.printTextWithFont("\n-------" + print_type + "-------\n", "BOLD", 30, callback);
                                woyouService.printTextWithFont("Payment Slip\n", "BOLD", fontSize, callback);

                                woyouService.setAlignment(0,callback);
                                woyouService.printTextWithFont("Payment Id     : " + responseMap.get("Payment Id     ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Name           : " + responseMap.get("Name           ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Due Date       : " + responseMap.get("Due Date       ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Payment Date   : " + responseMap.get("Payment Date   ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Assessment No  : " + responseMap.get("Assessment NO  ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Tax Type       : " + responseMap.get("Tax Type       ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Payment Type   : " + responseMap.get("Payment Type   ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Bill Amount    : " + responseMap.get("Bill Amount    ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Fine Amount    : " + responseMap.get("Fine Amount    ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Bank Charge    : " + responseMap.get("Bank Charges   ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Total Amount   : " + responseMap.get("Total Amount   "+"\n"), "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("********Charges as per GVMC Norms********\n", "", fontSize, callback);
                                woyouService.printTextWithFont("                                Signature\n", "", fontSize, callback);
                                woyouService.printTextWithFont("         Thank you Payment Success       \n", "", fontSize, callback);

                                woyouService.printTextWithFont("*****************************************\n", "", fontSize, callback);
                                woyouService.printTextWithFont("Contact: 8008612200/8008615500\n", "BOLD", 30, callback);
                                woyouService.printTextWithFont("Support: helpdesk@anyemi.com\n\n", "BOLD", 30, callback);



                            } else if (login_type.equals(Constants.LOGIN_TYPE_APEPDCL)) {


                                //  mBtp.printLogo();
                                woyouService.setAlignment(1, callback);
                                woyouService.printTextWithFont("\n-------" + print_type + "-------\n", "BOLD", 30, callback);
                                woyouService.printTextWithFont("Payment Slip\n", "BOLD", fontSize, callback);

                                woyouService.setAlignment(0,callback);


                              /*  System.out.println(responseMap.get("Payment Id     "));
                                System.out.println(responseMap.get("Payment Ref Id "));
                                System.out.println(responseMap.get("Service Number "));
                                System.out.println(responseMap.get("Name           "));
                                System.out.println(responseMap.get("Bill Date      "));
                                System.out.println(responseMap.get("Paid Date      "));
                                System.out.println(responseMap.get("Payment Type   "));
                                System.out.println(responseMap.get("Bill Amount    "));
                                System.out.println(responseMap.get("Service Charge "));
                                System.out.println(responseMap.get("Bank Charge    "));
                                System.out.println(responseMap.get("Total Amount   "));
*/
                                woyouService.printTextWithFont("Payment Id     : " + responseMap.get("Payment Id     ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Name           : " + responseMap.get("Name           ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Due Date       : " + responseMap.get("Due Date       ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Payment Date   : " + responseMap.get("Payment Date   ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Assessment No  : " + responseMap.get("Assessment NO  ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Tax Type       : " + responseMap.get("Tax Type       ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Payment Type   : " + responseMap.get("Payment Type   ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Bill Amount    : " + responseMap.get("Bill Amount    ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Fine Amount    : " + responseMap.get("Fine Amount    ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Bank Charge    : " + responseMap.get("Bank Charges   ")+"\n", "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("Total Amount   : " + responseMap.get("Total Amount   "+"\n"), "BOLD", fontSize, callback);
                                woyouService.printTextWithFont("*******Charges as per APEPDCL Norms*******\n", "", fontSize, callback);
                                woyouService.printTextWithFont("                                 Signature\n", "", fontSize, callback);
                                woyouService.printTextWithFont("         Thank you Payment Success        \n", "", fontSize, callback);

                                woyouService.printTextWithFont("****************************************\n", "", fontSize, callback);
                                woyouService.printTextWithFont("Contact : 8008612200/8008615500", "BOLD", 30, callback);
                                woyouService.printTextWithFont("Support : helpdesk@anyemi.com\n\n", "BOLD", 30, callback);

                            }

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    Globals.showToast(getActivity(),"Error while reading data");
                }

                break;
        }

    }
}


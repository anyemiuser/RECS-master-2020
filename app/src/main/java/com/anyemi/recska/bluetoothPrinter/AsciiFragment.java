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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.anyemi.recska.R;
import com.anyemi.recska.model.CollectionsModel;
import com.google.gson.Gson;
import com.ngx.BluetoothPrinter;
import com.ngx.BtpCommands;

import java.util.HashMap;
import java.util.Map;

public class AsciiFragment extends Fragment implements OnClickListener {
    private BluetoothPrinter mBtp = BluetoothPrinterMain.mBtp;
    Bundle r_bundle;
    String print_type;
    Map<String, String> responseMap;
    CollectionsModel.CollectionsBean item_details;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.ascii_layout, container, false);

        r_bundle = getActivity().getIntent().getExtras();
       // String dataa = r_bundle.getString("doctype");
        String data = r_bundle.getString("print_data");
        Gson gson = new Gson();

        item_details = gson.fromJson(data, CollectionsModel.CollectionsBean.class);
        initControls(view);
        return view;
    }

    private EditText mEdit;
    private TextView tvSeekBar;
    private int minutes = 1440;
    private CheckBox cbVolatile;



    public static Map<String, String> splitToMap(String source, String entriesSeparator, String keyValueSeparator) {
        Map<String, String> map = new HashMap<String, String>();
        String[] entries = source.split(entriesSeparator);
        for (String entry : entries) {
            if (!TextUtils.isEmpty(entry) && entry.contains(keyValueSeparator)) {
                String[] keyValue = entry.split(keyValueSeparator);
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }

    private void initControls(View v) {

        mEdit = v.findViewById(R.id.txt);
        mEdit.setText(print_type);
        Button btnPrintLogo = v.findViewById(R.id.btnPrintLogo);
        btnPrintLogo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mBtp.printLogo();
            }
        });

        Button btnPrintLineFeed = v.findViewById(R.id.btnPrintLineFeed);
        btnPrintLineFeed.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mBtp.printLineFeed();
            }
        });

        Button btnPrintText = v.findViewById(R.id.btnPrintText);
        btnPrintText.setOnClickListener(this);
        btnPrintText.setText("Print");

        Button btnPrintAsciiText = v.findViewById(R.id.btnPrintAsciiText);
        btnPrintAsciiText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String txt = mEdit.getText().toString();
                mBtp.setPrintFontStyle(BtpCommands.FONT_KANNADA);
                mBtp.printAsciiText(txt);
            }
        });

        Button btnPrintKnAksharamale = v
                .findViewById(R.id.btnPrintKnAksharamale);
        btnPrintKnAksharamale.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String txt = "C D E F G H IÄ J K L M N O CA CB\n\n"
                        +

                        "PÀ R UÀ WÀ k \n"
                        + "ZÀ bÀ d gÀhÄ Y \n"
                        + "l oÀ qÀ qsÀ uÁ t \n"
                        + "vÀ xÀ zÀ zsÀ £À \n"
                        + "¥À ¥sÀ ¨Á ¨sÀ ªÀÄ \n"
                        + "AiÀÄ ® ªÀ ±À µÀ ¸À ºÀ ¼À \n\n"
                        +

                        "Pï PÀ PÁ Q QÃ PÀÄ PÀÈ PÉ PÉÃ PÉÊ PÉÆ PÉÆÃ PË PÀA PÀB \n"
                        + "Sï R SÁ T TÃ RÄ RÆ RÈ SÉ SÉÃ SÉÊ SÉÆ SÉÆÃ SË RA RB\n"
                        + "Uï UÀ UÁ V VÃ UÀÄ UÀÆ UÀÈ UÉ UÉÃ UÉÊ UÉÆ UÉÆÃ UË UÀA UÀB\n"
                        + "Wï WÀ WÁ X XÃ WÀÄ WÀÆ WÀÈ WÉ WÉÃ WÉÊ WÉÆ WÉÆÃ WË WÀA WÀB\n\n"
                        +

                        "Zï ZÀ ZÁ a aÃ ZÀÄ ZÀÆ ZÀÈ ZÉ ZÉÃ ZÉÊ ZÉÆ ZÉÆÃ ZË ZÀA ZÀB\n"
                        + "bï bÀ bÁ c cÃ bÀÄ bÀÆ bÀÈ bÉ bÉÃ bÉÊ bÉÆ bÉÆÃ bË bÀA bÀB\n"
                        + "eï d eÁ f fÃ dÄ dÆ dÈ eÉ eÉÃ eÉÊ eÉÆ eÉÆÃ eË dA dB\n"
                        + "gÀhiï gÀhÄ gÀhiÁ jhÄ jhÄÃ gÀhÄÄ gÀhÄÆ gÀhÄÈ gÉhÄ gÉhÄÃ gÉhÄÊ gÉhÆ gÉhÆÃ gÀhiË gÀhÄA gÀhÄB\n"
                        + "\n"
                        + "mï l mÁ n nÃ lÄ lÆ lÈ mÉ mÉÃ mÉÊ mÉÆ mÉÆÃ mË lA lB\n"
                        + "oï oÀ oÁ p pÃ oÀÄ oÀÆ oÀÈ oÉ oÉÃ oÉÊ oÉÆ oÉÆÃ oË oÀA oÀB \n"
                        + "qï qÀ qÁ r rÃ qÀÄ qÀÆ qÀÈ qÉ qÉÃ qÉÊ qÉÆ qÉÆÃ qË qÀA qÀB \n"
                        + "qsï qsÀ qsÁ rü rüÃ qsÀÄ qsÀÆ qsÀÈ qsÉ qsÉÃ qsÉÊ qsÉÆ qsÉÆÃ qsË qsÀA qsÀB\n"
                        + "uï t uÁ tÂ tÂÃ tÄ tÆ tÈ uÉ uÉÃ uÉÊ uÉÆ uÉÆÃ uË tA tB \n"
                        + "\n"
                        + "vï vÀ vÁ w wÃ vÀÄ vÀÆ vÀÈ vÉ vÉÃ vÉÊ vÉÆ vÉÆÃ vË vÀA vÀB\n"
                        + "xï xÀ xÁ y yÃ xÀÄ xÀÆ xÀÈ xÉ xÉÃ xÉÊ xÉÆ xÉÆÃ xË xÀA xÀB\n"
                        + "zï zÀ zÁ ¢ ¢Ã zÀÄ zÀÆ zÀÈ zÉ zÉÃ zÉÊ zÉÆ zÉÆÃ zË zÀA zÀB\n"
                        + "zsï zsÀ zsÁ ¢ü ¢üÃ zsÀÄ zsÀÆ zsÀÈ zsÉ zsÉÃ zsÉÊ zsÉÆ zsÉÆÃ zsË zsÀA zsÀB\n"
                        + "£ï £À £Á ¤ ¤Ã £ÀÄ £ÀÆ £ÀÈ £É £ÉÃ £ÉÊ £ÉÆ £ÉÆÃ £Ë £ÀA £ÀB\n"
                        + "\n"
                        + "¥ï ¥À ¥Á ¦ ¦Ã ¥ÀÄ ¥ÀÆ ¥ÀÈ ¥É ¥ÉÃ ¥ÉÊ ¥ÉÆ ¥ÉÆÃ ¥Ë ¥ÀA ¥ÀB\n"
                        + "¥sï ¥sÀ ¥sÁ ¦ü ¦üÃ ¥sÀÄ ¥sÀÆ ¥sÀÈ ¥sÉ ¥sÉÃ ¥sÉÊ ¥sÉÆ ¥sÉÆÃ ¥sË ¥sÀA ¥sÀB\n"
                        + "¨ï § ¨Á © ©Ã §Ä §Æ §È ¨É ¨ÉÃ ¨ÉÊ ¨ÉÆ ¨ÉÆÃ ¨Ë §A §B \n"
                        + "¨sï ¨sÀ ¨sÁ ©ü ©Ã ¨sÀÄ ¨sÀÆ ¨sÀÈ ¨sÉ ¨sÉÃ ¨sÉÊ ¨sÉÆ ¨sÉÆÃ ¨sË ¨sÀA ¨sÀB\n"
                        + "ªÀiï ªÀÄ ªÀiÁ «Ä «ÄÃ ªÀÄÄ ªÀÄÆ ªÀÄÈ ªÉÄ ªÉÄÃ ªÉÄÊ ªÉÆ ªÉÆÃ ªÀiË ªÀÄA ªÀÄB \n"
                        + "\n"
                        + "AiÀiï AiÀÄ AiÀiÁ ¬Ä ¬ÄÃ AiÀÄÄ AiÀÄÆ AiÀÄÈ AiÉÄ AiÉÄÃ AiÉÄÊ AiÉÆ AiÉÆÃ AiÀiË AiÀÄA AiÀÄB\n"
                        + "gï gÀ gÁ j jÃ gÀÄ gÀÆ gÀÈ gÉ gÉÃ gÉÊ gÉÆ gÉÆÃ gË gÀA gÀB \n"
                        + "¯ï ® ¯Á ° °Ã ®Ä ®Æ ®È ¯É ¯ÉÃ ¯ÉÊ ¯ÉÆ ¯ÉÆÃ ¯Ë ®A ®B\n"
                        + "ªï ªÀ ªÁ « «Ã ªÀÅ ªÀÇ ªÀÈ ªÉ ªÉÃ ªÉÊ ªÉÇ ªÉÇÃ ªË ªÀA ªÀB\n"
                        + "±ï ±À ±Á ² ²Ã ±ÀÄ ±ÀÆ ±ÀÈ ±É ±ÉÃ ±ÉÊ ±ÉÆ ±ÉÆÃ ±Ë ±ÀA ±ÀB\n"
                        + "µï µÀ µÁ ¶ ¶Ã µÀÄ µÀÆ µÀÈ µÉ µÉÃ µÉÊ µÉÆ µÉÆÃ µË µÀA µÀB\n"
                        + "¸ï ¸À ¸Á ¹ ¹Ã ¸ÀÄ ¸ÀÆ ¸ÀÈ ¸É ¸ÉÃ ¸ÉÊ ¸ÉÆ ¸ÉÆÃ ¸Ë ¸ÀA ¸ÀB\n"
                        + "ºï ºÀ ºÁ » »Ã ºÀÄ ºÀÆ ºÀÈ ºÉ ºÉÃ ºÉÊ ºÉÆ ºÉÆÃ ºË ºÀA ºÀB\n"
                        + "¼ï ¼À ¼Á ½ ½Ã ¼ÀÄ ¼ÀÆ ¼ÀÈ ¼É ¼ÉÃ ¼ÉÊ ¼ÉÆ ¼ÉÆÃ ¼Ë ¼ÀA ¼ÀB \n\n";
                mEdit.setText(txt);
                mBtp.setPrintFontStyle(BtpCommands.FONT_KANNADA);
                mBtp.printAsciiText(txt);
            }
        });

        Button btnSetFontBold = v.findViewById(R.id.btnSetFontBold);
        btnSetFontBold.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
            }
        });

        Button btnSetFontRegular = v.findViewById(R.id.btnSetFontRegular);
        btnSetFontRegular.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_REGULAR);
            }
        });

        Button btnSetFontNormal = v.findViewById(R.id.btnSetFontNormal);
        btnSetFontNormal.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
            }
        });

        Button btnSetFontKannada = v.findViewById(R.id.btnSetFontkannada);
        btnSetFontKannada.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mBtp.setPrintFontStyle(BtpCommands.FONT_KANNADA);
            }
        });


        Button btnSetFontDoubleWidth = v
                .findViewById(R.id.btnSetFontDoubleWidth);
        btnSetFontDoubleWidth.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_WIDTH);
            }
        });

        Button btnSetFontDoubleHeight = v
                .findViewById(R.id.btnSetFontDoubleHeight);
        btnSetFontDoubleHeight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_HEIGHT);
            }
        });

        Button btnSetFontDoubleWidthAndHeight = v
                .findViewById(R.id.btnSetFontDoubleWidthAndHeight);
        btnSetFontDoubleWidthAndHeight
                .setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_W_H);
                    }
                });

        Button btnSetLogo = v.findViewById(R.id.btnChangeLogo);
        btnSetLogo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment sl = new SetLogo();
                BluetoothPrinterMain.changeFragment(R.id.container, sl, true);
            }
        });

        Button btnTestBill = v.findViewById(R.id.btnTestBill);
        btnTestBill.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_REGULAR);
//				mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
//				mBtp.printLogo();
//				mBtp.printLineFeed();
//				mBtp.printTextLine("----------------------------------------");
//				mBtp.printTextLine("Item              Quantity        Price");
//				mBtp.printTextLine("----------------------------------------");
//				mBtp.printTextLine("Item 1                1            1.00");
//				mBtp.printTextLine("Some big item        10         7890.00");
//				mBtp.printTextLine("Next Item           999        10000.00");
//				mBtp.printLineFeed();
//				mBtp.printTextLine("----------------------------------------");
//				mBtp.printTextLine("Total                          17891.00");
//				mBtp.printTextLine("----------------------------------------");
//				mBtp.printLineFeed();
//				mBtp.printTextLine("         Thank you ! Visit Again");
//				mBtp.printLineFeed();
//				mBtp.printTextLine("****************************************");
//				mBtp.printLineFeed();

            }
        });

        Button btnNumber = v.findViewById(R.id.btnNumber);
        btnNumber.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mBtp.printTextLine("          #   ");
                mBtp.printTextLine("         ##   ");
                mBtp.printTextLine("        # #   ");
                mBtp.printTextLine("          #   ");
                mBtp.printTextLine("          #   ");
                mBtp.printTextLine("          #   ");
                mBtp.printTextLine("        ##### ");
            }
        });

        Button btnNumber1 = v.findViewById(R.id.btnNumber1);
        btnNumber1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mBtp.printTextLine("          _    ");
                mBtp.printTextLine("       _ (_)   ");
                mBtp.printTextLine("      (_)(_)   ");
                mBtp.printTextLine("         (_)   ");
                mBtp.printTextLine("         (_)   ");
                mBtp.printTextLine("         (_)   ");
                mBtp.printTextLine("       _ (_) _ ");
                mBtp.printTextLine("      (_)(_)(_)");
                mBtp.printLineFeed();
            }
        });

        Button btnAbtDriver = v.findViewById(R.id.btnAbtDriver);
        btnAbtDriver.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String s = mBtp.aboutDriver();
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnBatteryStatus = v.findViewById(R.id.btnBatteryStatus);
        btnBatteryStatus.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mBtp.GetBatteryStatus();
            }
        });

        Button btnPaperStatus = v.findViewById(R.id.btnPaperStatus);
        btnPaperStatus.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mBtp.GetPaperStatus();
            }
        });

        cbVolatile = v.findViewById(R.id.checkBox1);

        SeekBar seekBar = v.findViewById(R.id.seekBar1);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue,
                                          boolean fromUser) {
                minutes = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvSeekBar.setText("Auto switch off idle Minutes: " + minutes);
            }
        });

        tvSeekBar = v.findViewById(R.id.tvSeekBar);
        minutes = seekBar.getProgress();
        tvSeekBar.setText("Auto switch off idle Minutes: " + minutes);

        Button btnEnableAutoSwitchOff = v.findViewById(R.id.btnEnableAutoSwitchOff);
        btnEnableAutoSwitchOff.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mBtp.setAutoSwitchOffTime(minutes, cbVolatile.isChecked());
            }
        });

        Button btnDisableAutoSwitchOff = v.findViewById(R.id.btnDisableAutoSwitchOff);
        btnDisableAutoSwitchOff.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mBtp.disableAutoSwitchOff();
            }
        });

        Button btnLeftAlign = v.findViewById(R.id.btnLeftAlign);
        btnLeftAlign.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mBtp.setAlignment(BtpCommands.LEFT_ALIGN);
            }
        });

        Button btnCenterAlign = v.findViewById(R.id.btnCenterAlign);
        btnCenterAlign.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mBtp.setAlignment(BtpCommands.CENTER_ALIGN);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPrintText:

//                if(item_details!=null){
//
//
//
//                }
//
////                if(item_details!=null){
////                    String t_type="";
////                    String p_type="";
////                    if (item_details.getTax_type() != null) {
////
////                        if (item_details.getTax_type().equals("HT")) {
////                             t_type= "Property Tax";
////                        } else {
////                             t_type=item_details.getTax_type();
////                        }
////                    }
////
////
////                    if (item_details.getPayment_type().equals("DC")) {
////                        p_type= "Debit Card";
////                    } else {
////                        p_type=item_details.getTax_type();
////                    }
//                    mBtp.printLogo();
//                    mBtp.printLineFeed();
//                    mBtp.setAlignment(BtpCommands.CENTER_ALIGN);
//                    mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
//                    mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_W_H);
//                    mBtp.printTextLine("-------" + "CDMA" + "-------");
////                        mBtp.printLineFeed();
//                    mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_WIDTH);
//                    mBtp.printTextLine("Payment Slip");
//                    mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_REGULAR);
//                    mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
//                    mBtp.printLineFeed();
//                    mBtp.printTextLine("Narsipatnam Muncipility");
//                    mBtp.printLineFeed();
//                    mBtp.setAlignment(BtpCommands.LEFT_ALIGN);
//                    mBtp.printTextLine("Payment Id    : " + item_details.getTxn_payment_id());
//                    mBtp.printTextLine("Name          : " + item_details.getCustomer_name());
//                    mBtp.printTextLine("Due Date      : " + item_details.getDuedates());
//                    mBtp.printTextLine("Payment Date  : " + "09-12-2017");
//                    mBtp.printTextLine("Assessment No : " + item_details.getLoan_number());
//                    mBtp.printTextLine("Pay Ref No    : " + item_details.getPay_txn());
//                    mBtp.printTextLine("Tax Type      : " + t_type );
//                    mBtp.printTextLine("Payment Type  : " + p_type);
//                    mBtp.printTextLine("Bill Amount   : " + item_details.getPaid_amount());
//                    mBtp.printTextLine("Bank Charges  : " + item_details.getBank_charges());
//                    mBtp.printTextLine("Fine Amount   : " + item_details.getPaid_over_due());
//                    mBtp.printTextLine("User Charges  : " + item_details.getService_tax());
//                    mBtp.printTextLine("Total Amount  : " + item_details.getTotal_amount());
//                    mBtp.printLineFeed();
//                    mBtp.printTextLine("********Charges as per CDMA Norms********");
//                    mBtp.printLineFeed();
//                    mBtp.printTextLine("                             Signature");
//                    mBtp.printLineFeed();
//                    mBtp.printTextLine("         Thank you Payment Sucess         ");
//                    mBtp.printTextLine(" *****************************************");
//                    mBtp.printLineFeed();
//                    mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
//                    mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
//                    mBtp.printTextLine(" Contact: 8008612200/8008615500");
//                    mBtp.printTextLine(" Support: helpdesk@anyemi.com");
//                    mBtp.printLineFeed();
//                    mBtp.printLineFeed();
//
//
//                }else if (responseMap != null) {
//
//                    if (print_type.equals("APEPDCL")) {
//                        mBtp.printLogo();
//                        mBtp.printLineFeed();
//                        mBtp.setAlignment(BtpCommands.CENTER_ALIGN);
//                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_W_H);
//                        mBtp.printTextLine("-----" + print_type + "-----");
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_WIDTH);
//                        mBtp.printTextLine("Payment Slip");
//                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_REGULAR);
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
//                        mBtp.printLineFeed();
//                        mBtp.setAlignment(BtpCommands.LEFT_ALIGN);
//
//                        System.out.println(responseMap.get("Payment Id     "));
//                        System.out.println(responseMap.get("Payment Ref Id "));
//                        System.out.println(responseMap.get("Service Number "));
//                        System.out.println(responseMap.get("Name           "));
//                        System.out.println(responseMap.get("Bill Date      "));
//                        System.out.println(responseMap.get("Paid Date      "));
//                        System.out.println(responseMap.get("Payment Type   "));
//                        System.out.println(responseMap.get("Bill Amount    "));
//                        System.out.println(responseMap.get("Service Charge "));
//                        System.out.println(responseMap.get("Bank Charge    "));
//                        System.out.println(responseMap.get("Total Amount   "));
//
//                        mBtp.printTextLine("Payment Id     : " + responseMap.get("Payment Id     "));
//                        mBtp.printTextLine("Payment Ref Id : " + responseMap.get("Payment Ref Id "));
//                        mBtp.printTextLine("Service Number : " + responseMap.get("Service Number "));
//                        mBtp.printTextLine("Name           : " + responseMap.get("Name           "));
//                        mBtp.printTextLine("Bill Date      : " + responseMap.get("Bill Date      "));
//                        mBtp.printTextLine("Paid Date      : " + responseMap.get("Paid Date      "));
//                        mBtp.printTextLine("Payment Type   : " + responseMap.get("Payment Type   "));
//                        mBtp.printTextLine("Bill Amount    : " + responseMap.get("Bill Amount    "));
//                        mBtp.printTextLine("Service Charge : " + responseMap.get("Service Charge "));
//                        mBtp.printTextLine("Bank Charge    : " + responseMap.get("Bank Charge    "));
//                        mBtp.printTextLine("Total Amount   : " + responseMap.get("Total Amount   "));
//                        mBtp.printLineFeed();
//                        mBtp.printTextLine("********Charges as per APEPDCL Norms********");
//                        mBtp.printLineFeed();
//                        mBtp.printTextLine("                             Signature");
//                        mBtp.printLineFeed();
//                        mBtp.printTextLine("         Thank you Payment Sucess         ");
//                        mBtp.printTextLine(" *****************************************");
//                        mBtp.printLineFeed();
//                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
//                        mBtp.printTextLine(" Contact: 8008612200/8008615500");
//                        mBtp.printTextLine(" Support: helpdesk@anyemi.com");
//                        mBtp.printLineFeed();
//                        mBtp.printLineFeed();
//
//                    } else if (print_type.equals("GVMC")) {
//                        mBtp.printLogo();
//                        mBtp.printLineFeed();
//                        mBtp.setAlignment(BtpCommands.CENTER_ALIGN);
//                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_W_H);
//                        mBtp.printTextLine("-------" + print_type + "-------");
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_WIDTH);
//                        mBtp.printTextLine("Payment Slip");
//                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_REGULAR);
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
//                        mBtp.printLineFeed();
//                        mBtp.setAlignment(BtpCommands.LEFT_ALIGN);
//
//                        System.out.println(responseMap.get("Payment Id     "));
//                        System.out.println(responseMap.get("Name           "));
//                        System.out.println(responseMap.get("Due Date       "));
//                        System.out.println(responseMap.get("Payment Date   "));
//                        System.out.println(responseMap.get("Assessment NO  "));
//                        System.out.println(responseMap.get("Tax Type       "));
//                        System.out.println(responseMap.get("Payment Type   "));
//                        System.out.println(responseMap.get("Bill Amount    "));
//                        System.out.println(responseMap.get("Bank Charges   "));
//                        System.out.println(responseMap.get("Fine Amount    "));
//                        System.out.println(responseMap.get("Total Amount   "));
//
//                        mBtp.printTextLine("Payment Id     : " + responseMap.get("Payment Id     "));
//                        mBtp.printTextLine("Name           : " + responseMap.get("Name           "));
//                        mBtp.printTextLine("Due Date       : " + responseMap.get("Due Date       "));
//                        mBtp.printTextLine("Payment Date   : " + responseMap.get("Payment Date   "));
//                        mBtp.printTextLine("Assessment No  : " + responseMap.get("Assessment NO  "));
//                        mBtp.printTextLine("Tax Type       : " + responseMap.get("Tax Type       "));
//                        mBtp.printTextLine("Payment Type   : " + responseMap.get("Payment Type   "));
//                        mBtp.printTextLine("Bill Amount    : " + responseMap.get("Bill Amount    "));
//                        mBtp.printTextLine("Fine Amount    : " + responseMap.get("Fine Amount    "));
//                        mBtp.printTextLine("Bank Charge    : " + responseMap.get("Bank Charges   "));
//                        mBtp.printTextLine("Total Amount   : " + responseMap.get("Total Amount   "));
//                        mBtp.printLineFeed();
//                        mBtp.printTextLine("********Charges as per GVMC Norms********");
//                        mBtp.printLineFeed();
//                        mBtp.printTextLine("                             Signature");
//                        mBtp.printLineFeed();
//                        mBtp.printTextLine("         Thank you Payment Sucess         ");
//                        mBtp.printTextLine(" *****************************************");
//                        mBtp.printLineFeed();
//                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
//                        mBtp.printTextLine(" Contact: 8008612200/8008615500");
//                        mBtp.printTextLine(" Support: helpdesk@anyemi.com");
//                        mBtp.printLineFeed();
//                        mBtp.printLineFeed();
//                    } else if (responseMap.get("S.billtype").equals("payment slip")) {
//
//                        mBtp.printLogo();
//                        mBtp.printLineFeed();
//                        mBtp.setAlignment(BtpCommands.CENTER_ALIGN);
//                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_W_H);
//                        mBtp.printTextLine("-------" + responseMap.get("S.doctype") + "-------");
//
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_WIDTH);
//                        mBtp.printTextLine("Payment Slip");
//                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_REGULAR);
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
//                        mBtp.printLineFeed();
//                        mBtp.printTextLine(responseMap.get("S.mtype"));
//                        mBtp.printLineFeed();
//                        mBtp.setAlignment(BtpCommands.LEFT_ALIGN);
//                        mBtp.printTextLine("Payment Id    : " + responseMap.get("S.payment_id"));
//                        mBtp.printTextLine("Name          : " + responseMap.get("S.name"));
//                        mBtp.printTextLine("Due Date      : " + responseMap.get("S.due_date"));
//                        mBtp.printTextLine("Payment Date  : " + responseMap.get("S.payment_date"));
//                        mBtp.printTextLine("Assessment No : " + responseMap.get("S.assment_number"));
//                        mBtp.printTextLine("Pay Ref No    : " + responseMap.get("S.payment_ref_id"));
//                        mBtp.printTextLine("Tax Type      : " + responseMap.get("S.tax_type"));
//                        mBtp.printTextLine("Payment Type  : " + responseMap.get("S.payment_type"));
//                        mBtp.printTextLine("Bill Amount   : " + responseMap.get("S.bill_amount"));
//                        mBtp.printTextLine("Bank Charges  : " + responseMap.get("S.bank_charges"));
//                        mBtp.printTextLine("Fine Amount   : " + responseMap.get("S.fine_amount"));
//                        mBtp.printTextLine("User Charges  : " + responseMap.get("S.user_charge"));
//                        mBtp.printTextLine("Total Amount  : " + responseMap.get("S.total_amount"));
//                        mBtp.printLineFeed();
//                        mBtp.printTextLine("********Charges as per CDMA Norms********");
//                        mBtp.printLineFeed();
//                        mBtp.printTextLine("                             Signature");
//                        mBtp.printLineFeed();
//                        mBtp.printTextLine("         Thank you Payment Sucess         ");
//                        mBtp.printTextLine(" *****************************************");
//                        mBtp.printLineFeed();
//                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
//                        mBtp.printTextLine(" Contact: 8008612200/8008615500");
//                        mBtp.printTextLine(" Support: helpdesk@anyemi.com");
//                        mBtp.printLineFeed();
//                        mBtp.printLineFeed();
//                    } else if (responseMap.get("S.billtype").equals("Demandslip")) {
//
//                        mBtp.printLogo();
//                        mBtp.printLineFeed();
//                        mBtp.setAlignment(BtpCommands.CENTER_ALIGN);
//                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_W_H);
//                        mBtp.printTextLine("-------" + responseMap.get("S.doctype") + "-------");
////                        mBtp.printLineFeed();
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_DOUBLE_WIDTH);
//                        mBtp.printTextLine("Demand Slip");
//                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_REGULAR);
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
//                        mBtp.printLineFeed();
//                        mBtp.printTextLine(responseMap.get("S.mtype"));
//                        mBtp.printLineFeed();
//                        mBtp.setAlignment(BtpCommands.LEFT_ALIGN);
//                        mBtp.printTextLine("Demand No      : " + responseMap.get("S.demand_no"));
//                        mBtp.printTextLine("Assessment No  : " + responseMap.get("S.assessment_no"));
//                        mBtp.printTextLine("Name           : " + responseMap.get("S.name"));
//                        mBtp.printTextLine("Tax Type       : " + responseMap.get("S.tax_type"));
//                        mBtp.printTextLine("Generated Date : " + responseMap.get("S.generated_data"));
//                        mBtp.printTextLine("Due Amount     : " + responseMap.get("S.due_amount"));
//                        mBtp.printTextLine("Fine           : " + responseMap.get("S.fine"));
//                        mBtp.printTextLine("User Charges   : " + responseMap.get("S.user_charges"));
//                        mBtp.printTextLine("Total Amount   : " + responseMap.get("S.total_amount"));
//                        mBtp.printLineFeed();
//                        mBtp.printTextLine("********Charges as per CDMA Norms********");
//                        mBtp.printLineFeed();
//                        mBtp.printTextLine("                             Signature");
//                        mBtp.printLineFeed();
//                        mBtp.printTextLine("               Thank you            ");
//                        mBtp.printTextLine(" *****************************************");
//                        mBtp.printLineFeed();
//                        mBtp.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
//                        mBtp.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
//                        mBtp.printTextLine(" Contact: 8008612200/8008615500");
//                        mBtp.printTextLine(" Support: helpdesk@anyemi.com");
//                        mBtp.printLineFeed();
//                        mBtp.printLineFeed();
//                    } else {
//                        Toast.makeText(getActivity(), "Error while fetching data", Toast.LENGTH_SHORT);
//                    }
//
//
//                    break;
//                }
//
//        }
        }
    }
}

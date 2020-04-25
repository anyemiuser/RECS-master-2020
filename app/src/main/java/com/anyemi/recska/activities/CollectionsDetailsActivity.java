package com.anyemi.recska.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anyemi.recska.NavigationActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.bluetoothPrinter.BluetoothPrinterMain;
import com.anyemi.recska.connection.Constants;
import com.anyemi.recska.model.CollectionsModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.anyemi.recska.utils.Utils;
import com.google.gson.Gson;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


/**
 * Created by SuryaTejaChalla on 07-02-2018.
 * To do Check Get get User paid history
 */

public class CollectionsDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView aTitle, notification_count;
    RelativeLayout rl_new_mails;
    ImageView iv_add_new;
    Toolbar toolbar;

    TextView tv_payment_date, tv_tax_type, tv_c_name, tv_pay_receipt, tv_transaction_id, tv_payment_ref_id,
            tv_assment_num, tv_agent_name, tv_phone_num, tv_due_date, tv_payment_type, tv_due_amount, tv_fine_amount,
            tv_user_charges, tv_bank_charges, tv_total_amount, tv_adj_amount, tv_arrears_amount, tv_reconnection_fee,
            tv_surcharge;
    Button btn_print_on_device;
    String data = "";

    static CollectionsModel.CollectionsBean item_details;

    ArrayList<String> paymmentModesNames = new ArrayList<>();
    ArrayList<String> paymmentModesId = new ArrayList<>();
    ArrayList<String> aryTaxNames = new ArrayList<>();
    ArrayList<String> aryTaxIds = new ArrayList<>();

    private static String FILE = "c:/temp/FirstPdf.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    static Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);
        createActionBar();

        context = CollectionsDetailsActivity.this;

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(CollectionsDetailsActivity.this, R.color.colorPrimaryDark));
        }

        // this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Intent mIntent = getIntent();
        data = mIntent.getStringExtra(Constants.PAYMENTS_DATA);
        Gson gson = new Gson();

        item_details = gson.fromJson(data, CollectionsModel.CollectionsBean.class);
        // paymmentModesNames.addAll(Arrays.asList(getResources().getStringArray(R.array.retailer_type_array)));
        paymmentModesNames.clear();
        paymmentModesId.clear();
        aryTaxNames.clear();
        aryTaxIds.clear();

        paymmentModesNames.addAll(Arrays.asList(getResources().getStringArray(R.array.payment_names)));
        paymmentModesId.addAll(Arrays.asList(getResources().getStringArray(R.array.payment_ids)));
        aryTaxNames.addAll(Arrays.asList(getResources().getStringArray(R.array.tax_type_array)));
        aryTaxIds.addAll(Arrays.asList(getResources().getStringArray(R.array.tax_id_array)));


        initializeView();
        setData();

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
        aTitle.setText("Payment Details");

    }


    private void setData() {

        try {

            tv_tax_type.setVisibility(View.GONE);
            tv_agent_name.setVisibility(View.GONE);

            tv_phone_num.setText("");

            tv_payment_date.setText(Utils.getCurrentDateTime());


            /*
             **Bill Details
             */

            if (item_details.getDate_time() != null) {
                tv_payment_date.setText(Utils.parseDateTime(item_details.getDate_time()));
            }
            if (item_details.getCustomer_name() != null) {
                tv_c_name.setText(item_details.getCustomer_name());
            }
            if (item_details.getPay_ref() != null) {
                tv_pay_receipt.setText(item_details.getPay_ref());
            }
            if (item_details.getTxn_payment_id() != null) {
                tv_transaction_id.setText(item_details.getTxn_payment_id());
            }
            if (item_details.getPay_ref() != null) {
                tv_payment_ref_id.setText(item_details.getPay_ref());
            }
            if (item_details.getLoan_number() != null) {
                tv_assment_num.setText(item_details.getLoan_number());
            }

      /*      if (item_details.getDate_time() != null) {
                tv_assment_num.setText(Utils.parseDateTime(item_details.getDate_time()));
            }
*/
            if (item_details.getDuedates() != null) {
                String date = Utils.parseDate(item_details.getDuedates());
                tv_due_date.setText(date);
            }


            /*
            /Payment Details
             */


            if (item_details.getPayment_type() != null) {
                int position = paymmentModesId.indexOf(item_details.getPayment_type());
                tv_payment_type.setText(item_details.getPayment_type());
            }

            if (item_details.getEmi_amount() != null) {
                String resultStr = Utils.parseAmount(item_details.getEmi_amount());
                tv_due_amount.setText("Rs." + resultStr + " /-");
            }

            if (item_details.getAdjamt() != null) {
                tv_adj_amount.setText("Rs." + Utils.parseAmount(item_details.getAdjamt()) + "/-");
            }
            if (item_details.getNewarrears() != null) {
                tv_arrears_amount.setText("Rs." + Utils.parseAmount(item_details.getNewarrears()) + "/-");
            }

            if (item_details.getService_charge() != null) {
                String service_charge = item_details.getService_charge();
                tv_user_charges.setText("Rs." + service_charge + " /-");
            }

            if (item_details.getReconnection_fee() != null) {
                tv_reconnection_fee.setText("Rs." + Utils.parseAmount(item_details.getReconnection_fee()) + "/-");
            }

            if (item_details.getSurcharge_fee() != null) {
                tv_surcharge.setText("Rs." + Utils.parseAmount(item_details.getSurcharge_fee()) + "/-");
            }

            if (item_details.getBank_charges() != null) {
                String bank_charges = item_details.getBank_charges();
                tv_bank_charges.setText("Rs." + bank_charges + " /-");
            }


//            Double Bill_Amount = Double.parseDouble(item_details.getEmi_amount());
//            Double Adjustment_Amount = Double.parseDouble(item_details.getAdjamt());
//            Double Arrears = Double.parseDouble(item_details.getNewarrears());
//            Double Service_Charge = Double.parseDouble(item_details.getService_charge());
//            Double Reconnection_Fee = Double.parseDouble(item_details.getReconnection_fee());

            Double bank_charges = Double.parseDouble(item_details.getBank_charges());
            Double total_amount = Double.parseDouble(item_details.getTotal_amount());
            Double total_amount_with_bank_charges = bank_charges + total_amount;


            String resultStr = Utils.parseAmount(String.valueOf(total_amount_with_bank_charges));
            tv_total_amount.setText("Rs." + resultStr + " /-");


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void initializeView() {


        tv_payment_date = findViewById(R.id.tv_payment_date);
        tv_tax_type = findViewById(R.id.tv_tax_type);
        tv_c_name = findViewById(R.id.tv_c_name);
        tv_pay_receipt = findViewById(R.id.tv_pay_receipt);
        tv_transaction_id = findViewById(R.id.tv_transaction_id);
        tv_payment_ref_id = findViewById(R.id.tv_payment_ref_id);
        tv_assment_num = findViewById(R.id.tv_assment_num);
        tv_agent_name = findViewById(R.id.tv_agent_name);
        tv_phone_num = findViewById(R.id.tv_phone_num);
        tv_due_date = findViewById(R.id.tv_due_date);
        tv_payment_type = findViewById(R.id.tv_payment_type);
        tv_due_amount = findViewById(R.id.tv_due_amount);
        tv_fine_amount = findViewById(R.id.tv_fine_amount);
        tv_user_charges = findViewById(R.id.tv_user_charges);
        tv_bank_charges = findViewById(R.id.tv_bank_charges);
        tv_total_amount = findViewById(R.id.tv_total_amount);

        tv_adj_amount = findViewById(R.id.tv_adj_amount);
        tv_arrears_amount = findViewById(R.id.tv_arrears_amount);
        tv_reconnection_fee = findViewById(R.id.tv_reconnection_fee);
        tv_surcharge = findViewById(R.id.tv_surcharge);

        btn_print_on_device = findViewById(R.id.btn_print_on_device);
        btn_print_on_device.setOnClickListener(this);


        String l_type = SharedPreferenceUtil.getLoginType(getApplicationContext());


        if (l_type.equals(Constants.LOGIN_TYPE_CUSTOMER)) {
            btn_print_on_device.setText("Download Bill");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_print_on_device:
                selectPaymentMethod();
                break;
        }
    }

    private void selectPaymentMethod() {


        String l_type = SharedPreferenceUtil.getLoginType(getApplicationContext());

        if (!l_type.equals(Constants.LOGIN_TYPE_CUSTOMER)) {
            Intent i = new Intent(getApplicationContext(), BluetoothPrinterMain.class);
            i.putExtra("print_data", data);
            startActivity(i);
        } else {

            item_details.getLoan_number();

            final String fpath = "/sdcard/Download/" + item_details.getLoan_number() + Utils.getCurrentDateTime() + ".pdf";
            File file = new File(fpath);
            if (!file.exists()) {
                try {
                    file.createNewFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(fpath));
                document.open();
                //addMetaData(document);
                //addTitlePage(document);
                addContent(document);
                document.close();


                MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()},
                        null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });


                Globals.showToast(CollectionsDetailsActivity.this, "Bill Downloaded Successfully");
//
//                infoDialog = new Dialog(CollectionsDetailsActivity.this);
//
//
//                infoDialog.setContentView(R.layout.dialog_info);
//
//                final TextView tv_info = (TextView) infoDialog.findViewById(R.id.tv_info);
//                final Button btn_send_sms = (Button) infoDialog.findViewById(R.id.btn_send_sms);
//                final ProgressBar prgs_load = (ProgressBar) infoDialog.findViewById(R.id.prgs_load);
//
//                btn_send_sms.setText("");
//
//                btn_send_sms.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                        Uri uri = Uri.parse(fpath);
////                        intent.setDataAndType(uri, "text/csv");
////                        startActivity(Intent.createChooser(intent, "Open folder"));
//
//                        intent.setDataAndType(uri, "application/pdf");
//                        intent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(Intent.createChooser(intent, "Open folder"));
//
//
//                    }
//                });
//
//                infoDialog.show();




        } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private static void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    private static void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Title of the document", catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(
                "Report generated by: " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph(
                "This document describes something which is very important ",
                smallBold));

        addEmptyLine(preface, 8);

        preface.add(new Paragraph(
                "This document is a preliminary version and not subject to your license agreement or any other agreement with vogella.com ;-).",
                redFont));

        document.add(preface);
        // Start a new page
        document.newPage();
    }


    private static void addContent(Document document) throws DocumentException {

        Anchor anchor = new Anchor("Payment Slip", catFont);
        anchor.setName("Payment Slip");

        String date = Utils.parseDate(item_details.getDue_date());

        String p_type = "";
        ArrayList<String> paymmentModesId = new ArrayList<>();
        ArrayList<String> paymmentModesNames = new ArrayList<>();
        paymmentModesId.clear();
        paymmentModesNames.clear();
        paymmentModesId.addAll(Arrays.asList(context.getResources().getStringArray(R.array.payment_ids)));
        paymmentModesNames.addAll(Arrays.asList(context.getResources().getStringArray(R.array.payment_names)));


        Double emi_amount = Double.parseDouble(item_details.getTotal_amount());
        Double bankCharges = Double.parseDouble(item_details.getBank_charges());
        Double user_charges = Double.parseDouble(item_details.getService_charge());
        Double emi_amount_without_charge = emi_amount - user_charges;
        Double total_amount = emi_amount + bankCharges;


        if (item_details.getPayment_type() != null) {
            //int taxtype = paymmentModesId.indexOf();
            p_type =item_details.getPayment_type();
        }

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("", subFont);
        //Section subCatPart = catPart.addSection("");
        Section subCatPart = catPart.addSection("");
        //subCatPart.add(new Paragraph("Hello"));

        // subPara = new Paragraph("Subcategory 2", subFont);
        //  subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Payment Id\t\t\t\t\t\t: " + item_details.getTxn_payment_id()));
        subCatPart.add(new Paragraph("Pay Ref Id\t\t\t\t\t\t: " + item_details.getPay_ref()));
        subCatPart.add(new Paragraph("Service No\t\t\t\t\t\t: " + item_details.getLoan_number()));
        subCatPart.add(new Paragraph("Name\t\t\t\t\t\t\t\t\t: " + item_details.getCustomer_name()));
        subCatPart.add(new Paragraph("Due Date\t\t\t\t\t\t\t: " + date));
        subCatPart.add(new Paragraph("Payment Date\t\t\t\t\t: " + Utils.parseDateTime(item_details.getDate_time())));
        subCatPart.add(new Paragraph("Payment Type\t\t\t\t\t: " + p_type));
        subCatPart.add(new Paragraph("Bill Amount \t\t\t\t\t: " + "Rs. " + Utils.parseAmount(item_details.getEmi_amount()) + " /-"));
        subCatPart.add(new Paragraph("Adj Amount\t\t\t\t\t\t: " + "Rs. " + Utils.parseAmount(item_details.getAdjamt()) + " /-"));
        subCatPart.add(new Paragraph("Arrears \t\t\t\t\t\t\t: " + "Rs. " + Utils.parseAmount(item_details.getNewarrears()) + " /-"));
        subCatPart.add(new Paragraph("Reconnect Fee \t\t\t\t: " + "Rs. " + Utils.parseAmount(item_details.getReconnection_fee()) + " /-"));
        subCatPart.add(new Paragraph("Sur Charge\t\t\t\t\t\t: " + "Rs. " + Utils.parseAmount(item_details.getSurcharge_fee()) + " /-"));
        //  subCatPart.add(new Paragraph("Sur Charge            : " + "Rs. 0 /-"));
        subCatPart.add(new Paragraph("User Charges\t\t\t\t\t: " + "Rs. " + Utils.parseAmount(item_details.getService_charge()) + " /-"));
        subCatPart.add(new Paragraph("Bank Charges\t\t\t\t\t: " + "Rs. " + Utils.parseAmount(item_details.getBank_charges()) + " /-"));
        subCatPart.add(new Paragraph("Total Amount\t\t\t\t\t: " + "Rs. " + Utils.parseAmount(String.valueOf(total_amount)) + " /-"));

        subCatPart.add(new Paragraph(""));
        subCatPart.add(new Paragraph(""));
        subCatPart.add(new Paragraph(""));

        subCatPart.add(new Paragraph("********Charges as per " + "ARECS" + " Norms********"));

        subCatPart.add(new Paragraph(""));
        subCatPart.add(new Paragraph(""));

        subCatPart.add(new Paragraph("                             Signature"));

        subCatPart.add(new Paragraph(""));
        subCatPart.add(new Paragraph(""));

        subCatPart.add(new Paragraph("         Thank you Payment Success         "));
        subCatPart.add(new Paragraph(" *****************************************"));

        subCatPart.add(new Paragraph(" Contact: 18004251539"));
        subCatPart.add(new Paragraph(" Support: recsrevenue@gmail.com"));


        // add a list
        // createList(subCatPart);

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // add a table
        //  createTable(subCatPart);

        // now add all this to the document
        document.add(catPart);

        // Next section
        //  anchor = new Anchor("Second Chapter", catFont);
        // anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
//        catPart = new Chapter(new Paragraph(anchor), 1);
//
//        subPara = new Paragraph("Subcategory", subFont);
//        subCatPart = catPart.addSection(subPara);
//        subCatPart.add(new Paragraph("This is a very important message"));
//
//        // now add all this to the document
//        document.add(catPart);

    }

    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        subCatPart.add(table);

    }

    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    private void openSmsDialog() {
        Dialog dialog = new Dialog(CollectionsDetailsActivity.this);
        dialog.setTitle("Send Sms");
        dialog.setContentView(R.layout.dialog_sms);
        final EditText et_phone_num = dialog.findViewById(R.id.et_phone_num);
        Button btn_send_sms = dialog.findViewById(R.id.btn_send_sms);
        btn_send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_phone_num.getText().toString().length() == 10) {
                    Intent i = new Intent(getApplicationContext(), BluetoothPrinterMain.class);
                    i.putExtra("print_data", data);
                    startActivity(i);
                } else {
                    Globals.showToast(getApplicationContext(), "Invalid Mobile Numebr");
                }
            }
        });
        dialog.show();
    }

    private boolean performValidation() {
        boolean isValid = false;
        return isValid;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(getIntent().getStringExtra("redirect").equals("collection")){
                    Intent intent = new Intent(context, NavigationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //    intent.putExtra("FRAGMENT", "COLLECTION");
                    context.startActivity(intent);
                }else
                    finish();


                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getIntent().getStringExtra("redirect").equals("collection")){
            Intent intent = new Intent(context, NavigationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         //   intent.putExtra("FRAGMENT", "COLLECTION");
            context.startActivity(intent);
        }else
            finish();


    }
}

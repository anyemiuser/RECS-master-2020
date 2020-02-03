package com.anyemi.recska.connection;

/**
 * Created by Rama on 17-09-2016.
 * <p>
 * http://www.roommatesportal.com/api/Supplier/Login
 */

public class Constants {

    public static final String PAYMENT_ID = "paymentID";

    public static final String LOGIN_TYPE_CDMA = "CDMA Taxes/Bills";
    public static final String LOGIN_TYPE_GVMC = "GVMC Taxes/Bills";
    public static final String LOGIN_TYPE_APEPDCL = "APEPDCL Power Bills";
    public static final String LOGIN_TYPE_RECS = "RECS BILLS";
    public static final String LOGIN_TYPE_CUSTOMER = "LOGIN_TYPE_CUSTOMER";
        public static final String LOGIN_TYPE_COLLECTION_AGENT = "LOGIN_TYPE_COLLECTION_AGENT";
    public static final String PAYMENT_REQUEST_MODEL = "PAYMENT_REQUEST_MODEL";
    public static final String PAYMENTS_DATA = "PAYMENTS_DATA";
    public static final String SERVICE_NUMBER = "SERVICE_NUMBER";


    public static final String PRINT_HEADER_APEPDCL = "APEPDCL";
    public static final String PRINT_HEADER_CDMA = "CDMA";
    public static final String PRINT_HEADER_GVMC = "GVMC";
    public static final String PRINT_HEADER_RECS = "RECS";
    public static final String VERSION_ID = "1.0"; // 13/08/2018  10/08/2019 0.9-11/10/19 --1.0  17/10/2019
    public static String SHOW_ADDS = "0";

    public static final String PAYMENT_MODE_ANYEMI_WALLET = "AW";
    public static final String PAYMENT_MODE_AADHAR = "AADHAR";
    public static final String PAYMENT_MODE_BHARATH_QR = "BQ";
    public static final String PAYMENT_MODE_BHIM = "BHIM";
    public static final String PAYMENT_MODE_CASH = "C";
    public static final String PAYMENT_MODE_CREDIT_CARD = "CC";
    public static final String PAYMENT_MODE_CHEQUE = "CQ";
    public static final String PAYMENT_MODE_DEBIT_CARD = "DC";
    public static final String PAYMENT_MODE_JIO = "Jio";
    public static final String PAYMENT_MODE_NET_BANKING = "NB";
    public static final String PAYMENT_MODE_PAYTM_PG = "Paytm";
    public static final String PAYMENT_MODE_PHONE_PE = "PhonePe";
    public static final String PAYMENT_MODE_PAYTM_QR = "PQ";
    public static final String PAYMENT_MODE_PAYTM_SBI_UPI = "SBIUPI";
   // public static final String PAYMENT_MODE_PAY_U_MONEY = "PayU";
    public static final String PAYMENT_MODE_FREE_CHARGE = "FC";
    public static final String PAYMENT_MODE_FREE_AIRTEL = "Airtel";
    public static final String PAYMENT_MODE_FREE_M_PESA = "M-Pesa";
    public static final String PAYMENT_MODE_INSTAMOJO = "INSTAMOJO";
    public static final String PAYMENT_MODE_HDFC = "HDFC";
    public static final String PAYMENT_MODE_HDFC_UPI = "HDFCUPI";
    public static final String LOGIN_TYPE_AGENT = "11";

    public static String base_url = "https://api.anyemi.com/recs/";

    public static final String LOGIN_URL = base_url + "login";
    public static final String GET_SERVICES_LIST =base_url+"getfinancer";

    public static final String POST_PAYMENT_ADD_MULTI_VPA  =base_url+"storeUPI_user";
    public static final String FORGET_PASSWORD = base_url + "forgetPassword";
    public static final String GET_PENDING_TRANSACTIONS = base_url + "getPendingTransactions?id=";
    public static final String RESET_PASSWORD = base_url + "changePassword";
//    public static final String ASS_URL = base_url + "getassment_details";
    public static final String ASS_URL = "https://api.anyemi.com/recs_v1/getassment_details";
    public static final String UPI_LIST = base_url + "getuserUPI";
    public static final String GET_SUB_CATEGORIES = base_url + "collections?id=";
    public static final String POST_PAYMENT_ADD_VPA = base_url + "storeUPI";
    public static final String POST_PAYMENT_REMOVE_VPA = base_url + "removeUPI";
    public static final String POST_PAYMENT_DETAILS = base_url + "payment";
    public static final String POST_VALID_VPA = base_url + "checkvpa";
    public static final String POST_PAYMENT_REQUEST_TWO = base_url + "meTranStatusQueryWeb";
    public static final String POST_PAYMENT_REGISTER = base_url + "register";
    public static final String POST_FEEDBACK = base_url + "feedback";
    public static final String POST_UPDATE_PROFILE = base_url + "updateprofile";
    public static final String POST_UPDATE_NUMBER = base_url + "updatePhoneNumber";
    public static final String POST_UPDATE_AADHAR = base_url + "updateUpdateAadhar";
    public static final String POST_RESEND_OTP = base_url + "resendotp";
    public static final String POST_OTP_VERIFY = base_url + "otpverify";
  //  public static final String POST_OTP_VERIFY = "https://dev.anyemi.com/webservices/anyemi/otpverify";
    public static final String POST_PAYMENT_SUBMIT = base_url + "pay";
    public static final String GET_SAMPLE_PAYMENT_SUBMIT = "https://pguat.paytm.com/oltp/HANDLER_INTERNAL/getTxnStatus?JsonData=";
    public static final String POST_LOCATION = base_url + "location";
    public static final String GET_BANKS = base_url + "getbanks";
    public static final String GET_BRANCHES = base_url + "getbranches";
    public static final String POST_REPORTS = base_url + "PaymentWiseTranscitions";


    public static final String GET_INSTA_PAYMENT_MODES = base_url + "instpmodes_servtax ";
    public static final String POST_GENERATE_OID = base_url + "instamojoorderid";
    public static final String POST_GENERATE_HASH2 = "http://192.168.0.16:7071/Nexer/rest/NexerUser/PaytmSample";
    public static final String POST_GENERATE_HASH = base_url + "checksum";
    public static final String PAYMENT_REQ_ERROR = "Unable To Handle Request";

}

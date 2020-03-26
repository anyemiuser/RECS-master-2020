package com.anyemi.recska.connection;

import android.content.Context;

import com.anyemi.recska.instamojo.GetOrderIDRequest;
import com.anyemi.recska.model.CheackValidVpaModel;
import com.anyemi.recska.model.PaymentRequestModel;


/**
 * Created by SuryaTejaChalla on 19-08-2017.
 */

public class HomeServices {

    public static Object Register(Context aContext, String loginRequest) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.LOGIN_URL, loginRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object addMultiVpa(Context aContext, String request) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.POST_PAYMENT_ADD_MULTI_VPA, request);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object generateOrderId(Context aContext, GetOrderIDRequest getOrderIDRequest) {
        try {
            return Connection.callHttpPostRequestsV2(aContext, Constants.POST_GENERATE_OID, getOrderIDRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object getInstaPaymodes(Context aContext) {
        try {
            return Connection.callHttpGetRequestsV2(aContext, Constants.GET_INSTA_PAYMENT_MODES, null);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }


    public static Object removeUPI(Context aContext, String request) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.POST_PAYMENT_REMOVE_VPA, request);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }


    public static Object vpaList(Context aContext, String loginRequest) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.UPI_LIST, loginRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object addVpa(Context aContext, String request) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.POST_PAYMENT_ADD_VPA, request);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }


    public static Object getPendingTransaction(Context aContext, String id) {
        try {
            return Connection.callHttpGetRequestsV2(aContext, Constants.GET_PENDING_TRANSACTIONS + id, null);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object password(Context aContext, String loginRequest) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.FORGET_PASSWORD, loginRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object resetPassword(Context aContext, String loginRequest) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.RESET_PASSWORD, loginRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object getAssessment(Context aContext, String loginRequest) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.ASS_URL, loginRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }public static Object updateBill(Context aContext, String loginRequest) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.UPDATE_BILL, loginRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }


    public static Object getCollections(Context aContext, String id) {
        try {
            return Connection.callHttpGetRequestsV2(aContext, Constants.GET_SUB_CATEGORIES + id, null);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object getFina(Context aContext) {
        try {
            return Connection.callHttpGetRequestsV2(aContext, Constants.GET_SERVICES_LIST , null);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object getReports(Context aContext, String loginRequest) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.POST_REPORTS_NEW, loginRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }


    public static Object getBanks(Context aContext) {
        try {
            return Connection.callHttpGetRequestsV2(aContext, Constants.GET_BANKS, null);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object getBranches(Context aContext, String loginRequest) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.GET_BRANCHES, loginRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }


    public static Object checkValidVpa(Context aContext, CheackValidVpaModel request) {
        try {
            return Connection.callHttpPostRequestsV2(aContext, Constants.POST_VALID_VPA, request);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object postPayment(Context aContext, PaymentRequestModel request) {
        try {
            return Connection.callHttpPostRequestsV2(aContext, Constants.POST_PAYMENT_DETAILS, request);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object register(Context aContext, String request) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.POST_PAYMENT_REGISTER, request);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }


    public static Object submitFeedBack(Context aContext, String request) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.POST_FEEDBACK_NEW, request);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object updateProfile(Context aContext, String request) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.POST_UPDATE_PROFILE, request);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object updatePhoneNumber(Context aContext, String request) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.POST_UPDATE_NUMBER, request);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object updateAadharNumber(Context aContext, String request) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.POST_UPDATE_AADHAR, request);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object reSendOtp(Context aContext, String request) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.POST_RESEND_OTP, request);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object verifyOtp(Context aContext, String request) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.POST_OTP_VERIFY, request);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object postPaymentTwo(Context aContext, String request) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.POST_PAYMENT_REQUEST_TWO, request);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object submitPayment(Context aContext, PaymentRequestModel request) {
        try {
            return Connection.callHttpPostRequestsV2(aContext, Constants.POST_PAYMENT_SUBMIT, request);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }


    public static Object samplePaytmRequest(Context aContext, String request) {
        try {
            return Connection.callHttpGetRequestsV2(aContext, Constants.GET_SAMPLE_PAYMENT_SUBMIT + request, null);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object upDateUserLocation(Context aContext, String loginRequest) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.POST_LOCATION, loginRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object generateHash(Context aContext, String loginRequest) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.POST_GENERATE_HASH, loginRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static Object generateHash2(Context aContext, String loginRequest) {
        try {
            return Connection.callHttpPostRequestsV2Jobj(aContext, Constants.POST_GENERATE_HASH2, loginRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

}

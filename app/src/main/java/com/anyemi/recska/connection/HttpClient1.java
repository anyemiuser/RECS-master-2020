package com.anyemi.recska.connection;

import android.app.Activity;
import android.util.Log;

import com.agsindia.mpos_integration.models.LogUtils;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import functions.*;

public class HttpClient1 {
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL = "https://pos.indiatransact.com:7600/iso8583service.asmx";
    private static int timeout = 60000;

    public HttpClient1() {
    }

    public String getDataFromUrl(String data, String METHOD_NAME, Activity activity) {
        String xml = null;

        try {
            SoapObject request = new SoapObject("http://tempuri.org/", METHOD_NAME);
            if (METHOD_NAME.equalsIgnoreCase("search")) {
                request.addProperty("terminal_id", data.split("\\|")[0]);
                request.addProperty("device_id", data.split("\\|")[1]);
                request.addProperty("imei", data.split("\\|")[2]);
                request.addProperty("sim_id", data.split("\\|")[3]);
            } else if (METHOD_NAME.equalsIgnoreCase("register")) {
                request.addProperty("terminal_id", data.split("\\|")[0]);
                request.addProperty("merchant_id", data.split("\\|")[1]);
                request.addProperty("device_id", data.split("\\|")[2]);
                request.addProperty("imei", data.split("\\|")[3]);
                request.addProperty("sim_id", data.split("\\|")[4]);
            } else if (METHOD_NAME.equalsIgnoreCase("getSessionKey")) {
                request.addProperty("terminal_id", data.split("\\|")[0]);
                request.addProperty("merchant_id", data.split("\\|")[1]);
                request.addProperty("platform_type", data.split("\\|")[2]);
                request.addProperty("app_ver", data.split("\\|")[3]);
            } else if (METHOD_NAME.equalsIgnoreCase("changePassword")) {
                request.addProperty("terminal_id", data.split("\\|")[0]);
                request.addProperty("merchant_id", data.split("\\|")[1]);
                request.addProperty("username", data.split("\\|")[2]);
                request.addProperty("old_pass", data.split("\\|")[3]);
                request.addProperty("new_pass", data.split("\\|")[4]);
            } else if (METHOD_NAME.equalsIgnoreCase("login")) {
                request.addProperty("terminal_id", data.split("\\|")[0]);
                request.addProperty("merchant_id", data.split("\\|")[1]);
                request.addProperty("username", data.split("\\|")[2]);
                request.addProperty("password", data.split("\\|")[3]);
                request.addProperty("device_id", data.split("\\|")[4]);
                request.addProperty("imei", data.split("\\|")[5]);
                request.addProperty("sim_id", data.split("\\|")[6]);
                request.addProperty("application_version_no", data.split("\\|")[7]);

                try {
                    request.addProperty("ipaddr", data.split("\\|")[8]);
                } catch (Exception var41) {
                    request.addProperty("ipaddr", " ");
                }
            } else if (!METHOD_NAME.equalsIgnoreCase("doPurchase") && !METHOD_NAME.equalsIgnoreCase("doCashPOS")) {
                if (!METHOD_NAME.equalsIgnoreCase("doPurchase") && !METHOD_NAME.equalsIgnoreCase("doCashPOS")) {
                    if (METHOD_NAME.equalsIgnoreCase("doRefund")) {
                        request.addProperty("terminal_id", data.split("\\|")[0]);
                        request.addProperty("merchant_id", data.split("\\|")[1]);
                        request.addProperty("card_no", data.split("\\|")[2]);
                        request.addProperty("amount", data.split("\\|")[3]);
                        request.addProperty("stan", data.split("\\|")[4]);
                        request.addProperty("date", data.split("\\|")[5]);
                        request.addProperty("time", data.split("\\|")[6]);
                        request.addProperty("ksn", data.split("\\|")[7]);
                        request.addProperty("enc_track", data.split("\\|")[8]);
                        request.addProperty("card_holder_name", data.split("\\|")[9]);

                        try {
                            request.addProperty("pinblock", data.split("\\|")[10]);
                        } catch (Exception var17) {
                            request.addProperty("pinblock", "");
                        }

                        try {
                            request.addProperty("icc_data", data.split("\\|")[11]);
                        } catch (Exception var16) {
                            request.addProperty("icc_data", "");
                        }

                        try {
                            request.addProperty("application_name", data.split("\\|")[12]);
                        } catch (Exception var15) {
                            request.addProperty("application_name", "");
                        }

                        try {
                            request.addProperty("aid", data.split("\\|")[13]);
                        } catch (Exception var14) {
                            request.addProperty("aid", "");
                        }

                        try {
                            request.addProperty("tvr", data.split("\\|")[14]);
                        } catch (Exception var13) {
                            request.addProperty("tvr", "");
                        }

                        try {
                            request.addProperty("tsi", data.split("\\|")[15]);
                        } catch (Exception var12) {
                            request.addProperty("tsi", "");
                        }
                    } else if (METHOD_NAME.equalsIgnoreCase("getSaleTxn")) {
                        request.addProperty("terminal_id", data.split("\\|")[0]);
                        request.addProperty("card_no", data.split("\\|")[1]);
                        request.addProperty("platform_type", data.split("\\|")[2]);
                        request.addProperty("app_ver", data.split("\\|")[3]);
                    } else if (METHOD_NAME.equalsIgnoreCase("doVoid")) {
                        request.addProperty("terminal_id", data.split("\\|")[0]);
                        request.addProperty("merchant_id", data.split("\\|")[1]);
                        request.addProperty("card_no", data.split("\\|")[2]);
                        request.addProperty("originalAmount", data.split("\\|")[3]);
                        request.addProperty("originalDate", data.split("\\|")[4]);
                        request.addProperty("originalTime", data.split("\\|")[5]);
                        request.addProperty("rrn", data.split("\\|")[6]);
                        request.addProperty("ksn", data.split("\\|")[7]);
                        request.addProperty("enc_track", data.split("\\|")[8]);
                        request.addProperty("card_holder_name", data.split("\\|")[9]);
                        request.addProperty("platform_type", data.split("\\|")[10]);
                        request.addProperty("app_ver", data.split("\\|")[11]);

                        try {
                            request.addProperty("StrNPCI_RRN", data.split("\\|")[12]);
                        } catch (Exception var11) {
                            var11.printStackTrace();
                        }
                    } else if (METHOD_NAME.equalsIgnoreCase("doReversal_ByRRN")) {
                        request.addProperty("terminal_id", data.split("\\|")[0]);
                        request.addProperty("merchant_id", data.split("\\|")[1]);
                        request.addProperty("platform_type", data.split("\\|")[2]);
                        request.addProperty("app_ver", data.split("\\|")[3]);
                        request.addProperty("stanId", data.split("\\|")[4]);
                    } else if (METHOD_NAME.equalsIgnoreCase("doReversal")) {
                        request.addProperty("terminal_id", data.split("\\|")[0]);
                        request.addProperty("merchant_id", data.split("\\|")[1]);
                        request.addProperty("card_no", data.split("\\|")[2]);
                        request.addProperty("rrn", data.split("\\|")[3]);
                        request.addProperty("platform_type", data.split("\\|")[4]);
                        request.addProperty("app_ver", data.split("\\|")[5]);
                        request.addProperty("enc_Track", data.split("\\|")[6]);
                        request.addProperty("reversal_tags", data.split("\\|")[7]);
                        request.addProperty("response_code", data.split("\\|")[8]);
                    } else if (METHOD_NAME.equalsIgnoreCase("getChargeSlip_ByRRN")) {
                        request.addProperty("terminal_id", data.split("\\|")[0]);
                        request.addProperty("strRRN", data.split("\\|")[1]);
                        request.addProperty("platform_type", data.split("\\|")[2]);
                        request.addProperty("app_ver", data.split("\\|")[3]);
                    } else if (METHOD_NAME.equalsIgnoreCase("doSettlement")) {
                        request.addProperty("terminal_id", data.split("\\|")[0]);
                        request.addProperty("platform_type", data.split("\\|")[1]);
                        request.addProperty("app_ver", "Ongo Mpos Ver " + data.split("\\|")[2]);
                    } else if (METHOD_NAME.equalsIgnoreCase("mPOSgetDetailReport")) {
                        request.addProperty("terminal_id", data.split("\\|")[0]);
                        request.addProperty("merchant_id", data.split("\\|")[1]);
                        request.addProperty("platform_type", data.split("\\|")[2]);
                        request.addProperty("app_ver", data.split("\\|")[3]);
                    }
                } else {
                    request.addProperty("terminal_id", data.split("\\|")[0]);
                    request.addProperty("merchant_id", data.split("\\|")[1]);
                    request.addProperty("card_no", data.split("\\|")[2]);
                    request.addProperty("amount", data.split("\\|")[3]);
                    request.addProperty("tip_amount", data.split("\\|")[4]);
                    request.addProperty("stan", data.split("\\|")[5]);
                    request.addProperty("date", data.split("\\|")[6]);
                    request.addProperty("time", data.split("\\|")[7]);
                    request.addProperty("ksn", data.split("\\|")[8]);
                    request.addProperty("enc_track", data.split("\\|")[9]);

                    try {
                        request.addProperty("card_holder_name", data.split("\\|")[10]);
                    } catch (Exception var29) {
                        request.addProperty("card_holder_name", "");
                    }

                    try {
                        request.addProperty("pinblock", data.split("\\|")[11]);
                    } catch (Exception var28) {
                        request.addProperty("pinblock", "");
                    }

                    try {
                        request.addProperty("icc_data", data.split("\\|")[12]);
                    } catch (Exception var27) {
                        request.addProperty("icc_data", "");
                    }

                    try {
                        request.addProperty("application_name", data.split("\\|")[13]);
                    } catch (Exception var26) {
                        request.addProperty("application_name", "");
                    }

                    try {
                        request.addProperty("aid", data.split("\\|")[14]);
                    } catch (Exception var25) {
                        request.addProperty("aid", "");
                    }

                    try {
                        request.addProperty("tvr", data.split("\\|")[15]);
                    } catch (Exception var24) {
                        request.addProperty("tvr", "");
                    }

                    try {
                        request.addProperty("tsi", data.split("\\|")[16]);
                    } catch (Exception var23) {
                        request.addProperty("tsi", "");
                    }

                    try {
                        request.addProperty("card_type", data.split("\\|")[17]);
                    } catch (Exception var22) {
                        request.addProperty("card_type", "");
                    }

                    try {
                        request.addProperty("additional_data", data.split("\\|")[18]);
                    } catch (Exception var21) {
                        request.addProperty("additional_data", "");
                    }

                    try {
                        request.addProperty("platform_type", data.split("\\|")[19]);
                    } catch (Exception var20) {
                        request.addProperty("platform_type", "");
                    }

                    try {
                        request.addProperty("app_ver", data.split("\\|")[20]);
                    } catch (Exception var19) {
                        request.addProperty("app_ver", "");
                    }

                    try {
                        request.addProperty("addn_private_data", data.split("\\|")[21]);
                    } catch (Exception var18) {
                        request.addProperty("addn_private_data", "");
                    }
                }
            } else {
                request.addProperty("terminal_id", data.split("\\|")[0]);
                request.addProperty("merchant_id", data.split("\\|")[1]);
                request.addProperty("card_no", data.split("\\|")[2]);
                request.addProperty("amount", data.split("\\|")[3]);
                request.addProperty("tip_amount", "303030303030303030303030");
                request.addProperty("stan", data.split("\\|")[5]);
                request.addProperty("date", data.split("\\|")[6]);
                request.addProperty("time", data.split("\\|")[7]);
                request.addProperty("ksn", data.split("\\|")[8]);
                request.addProperty("enc_track", data.split("\\|")[9]);

                try {
                    request.addProperty("card_holder_name", data.split("\\|")[10]);
                } catch (Exception var40) {
                    request.addProperty("card_holder_name", "");
                }

                try {
                    request.addProperty("pinblock", data.split("\\|")[11]);
                } catch (Exception var39) {
                    request.addProperty("pinblock", "");
                }

                try {
                    request.addProperty("icc_data", data.split("\\|")[12]);
                } catch (Exception var38) {
                    request.addProperty("icc_data", "");
                }

                try {
                    request.addProperty("application_name", data.split("\\|")[13]);
                } catch (Exception var37) {
                    request.addProperty("application_name", "");
                }

                try {
                    request.addProperty("aid", data.split("\\|")[14]);
                } catch (Exception var36) {
                    request.addProperty("aid", "");
                }

                try {
                    request.addProperty("tvr", data.split("\\|")[15]);
                } catch (Exception var35) {
                    request.addProperty("tvr", "");
                }

                try {
                    request.addProperty("tsi", data.split("\\|")[16]);
                } catch (Exception var34) {
                    request.addProperty("tsi", "");
                }

                try {
                    request.addProperty("card_type", data.split("\\|")[17]);
                } catch (Exception var33) {
                    request.addProperty("card_type", "");
                }

                try {
                    if (data.split("\\|")[17].equalsIgnoreCase("AMEX")) {
                        request.addProperty("additional_data", "");
                    } else {
                        request.addProperty("additional_data", data.split("\\|")[18]);
                    }
                } catch (Exception var32) {
                    request.addProperty("additional_data", "");
                }

                try {
                    request.addProperty("platform_type", data.split("\\|")[19]);
                } catch (Exception var31) {
                    request.addProperty("platform_type", "");
                }

                try {
                    request.addProperty("app_ver", "Ongo Mpos Ver " + data.split("\\|")[20]);
                } catch (Exception var30) {
                    request.addProperty("app_ver", "");
                }
            }

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            String requestStr = "myreq" + request.toString();
            LogUtils.d("MyRequest = ", requestStr + "URL=" + "https://pos.indiatransact.com:7600/iso8583service.asmx" + "METHOD_NAME=" + METHOD_NAME);
            SSLConection.allowAllSSL(activity, METHOD_NAME, "https://pos.indiatransact.com:7600/iso8583service.asmx");
            HttpTransportSE androidHttpTransport = new HttpTransportSE("https://pos.indiatransact.com:7600/iso8583service.asmx", timeout);

            try {
                androidHttpTransport.call("http://tempuri.org/" + METHOD_NAME, envelope);
            } catch (Exception var10) {
                Log.e("Exception = ", "" + var10.toString());
            }

            Object result = envelope.getResponse();


            if (result != null) {
                xml = result.toString();

            }

            LogUtils.d("RES", xml);
        } catch (Exception var42) {
            Log.e("Exception = ", "" + var42.toString());
        }

        return xml == null ? "No Response From Server" : xml;
    }
}


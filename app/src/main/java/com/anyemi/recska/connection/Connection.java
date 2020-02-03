/**
 *
 */
package com.anyemi.recska.connection;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.anyemi.recska.exceptions.BadRequestException;
import com.anyemi.recska.exceptions.InternalServerError;
import com.anyemi.recska.exceptions.JavaLangException;
import com.anyemi.recska.utils.AppLogs;
import com.anyemi.recska.utils.FileUtil;
import com.anyemi.recska.utils.Network;
import com.anyemi.recska.utils.PrintLog;
import com.anyemi.recska.utils.SharedPreferenceUtil;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * @author Surya Teja Challa
 * connecting to the server apis
 */
public class Connection {

    public static final int STATUS_FOUND = 200;
    public static final int STATUS_204 = 204;
    public static final int STATUS_400 = 400;
    public static final int STATUS_401 = 401;
    public static final int STATUS_403 = 403;
    public static final int STATUS_404 = 404;
    public static final int STATUS_412 = 412;
    public static final int STATUS_423 = 423;
    public static final int STATUS_ERROR = 500;
    private static int TIMEOUT = 30 * 1000;

    private static final String TAG = "Connection";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static Object HTTPRequestPost(Context context, String url, String aContentType, String aBody) {


        HttpURLConnection connection;
        url = url.replaceAll(" ", "%20");
        AppLogs.log(TAG, "****** request url : == " + url);
        AppLogs.log(TAG, "****** request body : == " + aBody);

        //String accessToken = SharedPreferenceUtil.getAccessToken(context);

        try {
            initSsl(context);
            HttpURLConnection.setFollowRedirects(false);
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Type", aContentType);
            //connection.setRequestProperty("Authorization", "bearer " + accessToken);
            connection.setConnectTimeout(TIMEOUT);
            if (aBody != null) {
                connection.setRequestProperty("Content-Length", "" + aBody.getBytes().length);
                // Send request as data output stream
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                //wr.writeBytes(aBody);
                wr.write(aBody.getBytes(StandardCharsets.UTF_8));
                wr.flush();
                wr.close();
            } else {
                connection.setRequestProperty("Content-Length", "" + 0);
            }
            connection.connect();

            int responseCode = connection.getResponseCode();
            AppLogs.log(TAG, "****** Response code: " + connection.getResponseCode() + ", Message: " + connection.getResponseMessage());
            if (responseCode == STATUS_FOUND) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                AppLogs.log(TAG, "****** response Message: " + response.toString());
                if (!response.equals("")) {
                    return response.toString();
                } else {
                    return new BadRequestException("Unable Process Your Request Please try Again later");
                }

            } else if (responseCode == STATUS_204) {
                return new BadRequestException("Invalid Login credentials");
            } else if (responseCode == STATUS_400 || responseCode != STATUS_FOUND) {
                return new BadRequestException("Invalid Credentials or bad request. Please try again.");
            } else if (responseCode == STATUS_ERROR) {
                return new InternalServerError("Internal Server Error. Please try again later.");
            } else {
                String errorMessage = new String(FileUtil.toByteArray(connection.getErrorStream()));
                PrintLog.print(TAG, "****** Error Message: " + errorMessage);
                return new JavaLangException("Unable to fetch data from. Please try again later.");
            }
        } catch (Exception e) {
            return new InternalServerError("Unable to connect to server. Please try again later.");
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Object callHttpPostRequestsV2(Context context, String url, Object requestBody) {
        if (Network.isAvailable(context)) {
            String requestString = new Gson().toJson(requestBody);
            String AccessToken = new Gson().toJson(requestBody);
            if (requestBody == null) {
                return HTTPRequestPost(context, url, "application/json", null);
            } else {
                return HTTPRequestPost(context, url, "application/json", requestString.replace("\\\\", ""));
            }
        } else {
            return new BadRequestException("No Internet Connection Please Connect to Internet");
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Object callHttpPostRequestsV2Jobj(Context context, String url, String requestBody) {

        if (Network.isAvailable(context)) {
            if (requestBody == null) {
                return HTTPRequestPost(context, url, "application/json", null);
            } else {
                return HTTPRequestPost(context, url, "application/json", requestBody);
            }
        } else {
            return new BadRequestException("No Internet Connection Please Connect to Internet");
        }
    }


    public static Object callHttpGetRequestsV2(Context aContext, String s, String url) {
        if (Network.isAvailable(aContext)) {

            HttpURLConnection connection;

            // url = url.replaceAll(" ", "%20");
            url = SharedPreferenceUtil.getBaseUrl(aContext) + s;
            url = url.replaceAll(" ", "%20");
            PrintLog.print(TAG, url);
            // String accessToken = SharedPreferenceUtil.getAccessToken(aContext);


            try {
                initSsl(aContext);
                connection = (HttpURLConnection) new URL(url).openConnection();
                // connection.setDoOutput(true);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                // connection.setRequestProperty("Authorization", "bearer " + accessToken);
                connection.connect();
                int responseCode = connection.getResponseCode();

                if (responseCode == STATUS_FOUND) {
                    PrintLog.print(TAG, "Success....");
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    PrintLog.print(TAG, response.toString());
                    return response.toString();

                } else if (responseCode > 201 && responseCode < 300) {
                    return new JavaLangException("Sorry No data found");

                } else {
                    return new JavaLangException("Unable to fetch data from . Please try again later.");
                }
            } catch (Exception e) {
                return new InternalServerError(e.toString());
            }
        } else {
            return new BadRequestException("No Internet Connection Please Connect to Internet");
        }

    }


    static void initSsl(Context aContext) {


        try {
            HostnameVerifier var10000 = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            BufferedInputStream caInputNew = new BufferedInputStream(aContext.getAssets()
                    .open("www.anyemi.com.crt"));

            Certificate caNew;
            try {
                caNew = cf.generateCertificate(caInputNew);
                System.out.println("ca=" + ((X509Certificate) caNew).getSubjectDN());
            } finally {
                caInputNew.close();
            }

            String var10 = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(var10);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("caNew", caNew);
            String var12 = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(var12);
            tmf.init(keyStore);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

        } catch (NoSuchAlgorithmException var22) {
//            LogUtils.d("allowAllSSL", var22.toString());
        } catch (KeyManagementException var23) {
//            LogUtils.d("allowAllSSL", var23.toString());
        } catch (CertificateException var24) {
//            LogUtils.d("Exception = ", "" + var24.toString());
        } catch (KeyStoreException var25) {
//            LogUtils.d("Exception = ", "" + var25.toString());
        } catch (IOException var26) {
//            LogUtils.d("Exception = ", "" + var26.toString());
        }


    }
}

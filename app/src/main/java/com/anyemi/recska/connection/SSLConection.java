package com.anyemi.recska.connection;

import android.content.Context;

import com.agsindia.mpos_integration.models.LogUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SSLConection {
    private static TrustManager[] trustManagers;
    private static final String STR_PROD_CER_NEW = "indiatransact_prod_18_19.crt";
    public static String CERTIFICATE;

    SSLConection() {
    }

    public static void allowAllSSL(Context mContext, String methodname, String URL1) {
        if (trustManagers == null) {
            trustManagers = new TrustManager[]{new SSLConection._FakeX509TrustManager()};
        }

        try {
            HostnameVerifier var10000 = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection connection = null;
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            BufferedInputStream caInputNew = new BufferedInputStream(mContext.getAssets()
                    .open("indiatransact_prod_18_19.crt"));

            Certificate caNew;
            try {
                caNew = cf.generateCertificate(caInputNew);
                System.out.println("ca=" + ((X509Certificate)caNew).getSubjectDN());
            } finally {
                caInputNew.close();
            }

            String var10 = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(var10);
            keyStore.load((InputStream)null, (char[])null);
            keyStore.setCertificateEntry("caNew", caNew);
            String var12 = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(var12);
            tmf.init(keyStore);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init((KeyManager[])null, tmf.getTrustManagers(), (SecureRandom)null);
            URL var14 = new URL(URL1 + methodname);
            connection = (HttpsURLConnection)var14.openConnection();
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (NoSuchAlgorithmException var22) {
            LogUtils.d("allowAllSSL", var22.toString());
        } catch (KeyManagementException var23) {
            LogUtils.d("allowAllSSL", var23.toString());
        } catch (CertificateException var24) {
            LogUtils.d("Exception = ", "" + var24.toString());
        } catch (KeyStoreException var25) {
            LogUtils.d("Exception = ", "" + var25.toString());
        } catch (IOException var26) {
            LogUtils.d("Exception = ", "" + var26.toString());
        }

    }

    public static class _FakeX509TrustManager implements X509TrustManager {
        private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[0];

        public _FakeX509TrustManager() {
        }

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return _AcceptedIssuers;
        }
    }
}


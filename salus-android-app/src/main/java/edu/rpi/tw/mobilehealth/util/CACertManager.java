package edu.rpi.tw.mobilehealth.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import android.content.Context;
import android.util.Log;

public class CACertManager {

    private static final String TAG = CACertManager.class.getSimpleName();
    private static SSLContext context = null;
    
    public static SSLContext getContext() {
        return context;
    }

    public static boolean installCertificate(Context context) {
        boolean success = false;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream is = context.getAssets().open("cacert.pem");
            Certificate ca = cf.generateCertificate(is);
            is.close();
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("twca", ca);
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
            CACertManager.context = SSLContext.getInstance("TLS");
            CACertManager.context.init(null, tmf.getTrustManagers(), null);
        } catch (CertificateException e) {
            Log.e(TAG, "Unable to create certificate factory", e);
        } catch (IOException e) {
            Log.e(TAG, "Unable to open cacert.pem", e);
        } catch (KeyStoreException e) {
            Log.e(TAG, "Unable to create new keystore", e);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Unable to create new keystore", e);
        } catch (KeyManagementException e) {
            Log.e(TAG, "Unable to create SSL context", e);
        }
        return success;
    }

}

package com.twinofthings.sezamecore.security;

import android.util.Log;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import static com.twinofthings.sezamecore.security.KeyStoreUtility.KEY_NAME;

/**
 * @author Felix Tutzer
 *         © NOUS Wissensmanagement GmbH, 2017
 */

public class SslUtilities {

    /**
     * This is needed for the SSL SOCKET FACTORY
     * returns null if no certificate is stored in the keychain
     *
     * @return
     */
    public static synchronized X509TrustManager createTrustManager() {
        TrustManagerFactory trustManagerFactory;
        try {
            KeyStore store = KeyStore.getInstance(KeyStoreUtility.SecurityConstants.ANDROID_KEY_STORE);
            store.load(null);
            if (store.containsAlias(KEY_NAME)) {
                trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(store);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                    throw new IllegalStateException("Unexpected default trust managers:"
                            + Arrays.toString(trustManagers));
                }
                return (X509TrustManager) trustManagers[0];
            } else {
            }
        } catch (NoSuchAlgorithmException e) {
        } catch (KeyStoreException e) {
        } catch (CertificateException e) {
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * This is needed for the SSL SOCKET FACTORY
     *
     * @return
     */
    public static synchronized SSLSocketFactory createSocketFactory() {
        SSLContext sslContext;
        try {
            KeyStore store = KeyStore.getInstance(KeyStoreUtility.SecurityConstants.ANDROID_KEY_STORE);
            store.load(null);
            if (store.containsAlias(KEY_NAME)) {
                sslContext = SSLContext.getInstance("TLS");
                KeyManagerFactory factory = KeyManagerFactory.getInstance("X509");
                factory.init(store, null);
                sslContext.init(factory.getKeyManagers(), null, null);
                return sslContext.getSocketFactory();
            }
        } catch (NoSuchAlgorithmException e) {
        } catch (KeyManagementException e) {
        } catch (UnrecoverableKeyException e) {
        } catch (KeyStoreException e) {
        } catch (CertificateException e) {
        } catch (IOException e) {
        }
        return null;
    }
}

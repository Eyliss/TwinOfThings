package com.twinofthings.helpers;

import org.spongycastle.openssl.jcajce.JcaPEMWriter;
import org.spongycastle.operator.ContentSigner;
import org.spongycastle.operator.OperatorCreationException;
import org.spongycastle.operator.jcajce.JcaContentSignerBuilder;
import org.spongycastle.pkcs.PKCS10CertificationRequest;
import org.spongycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.spongycastle.util.encoders.Hex;
import org.spongycastle.util.io.pem.PemObject;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

import static com.twinofthings.helpers.KeyStoreUtility.SecurityConstants.ANDROID_KEY_STORE;

/**
 * Created by Eyliss on 12/1/17.
 */

public class KeyStoreUtility {

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    public static final String TAG = "Utility";
    public static final String KEY_NAME = "SM_KN_NG_11";
    private KeyStore store;
    private KeyPair keyPair;

    public KeyStoreUtility() {
        Log.d(TAG,KeyStoreUtility.class.getName());
        try {
            this.store = KeyStore.getInstance(ANDROID_KEY_STORE);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * In the registration process we need to create a certificate-request and send it to the
     * HQ-Server - once returned, we store the signed certificate in our keystore
     *
     * @param userName username as returned from hq
     * @param deviceId deviceId as returned from hq
     * @return certificate request in pem-format
     */
    public String createKeyPairAndReturnCertificateRequest(String userName, String deviceId) {
        try {
            store.load(null);
            if (store.containsAlias(KEY_NAME)) {
                store.deleteEntry(KEY_NAME);
            }

            try {

                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(SecurityConstants.TYPE_RSA);
                keyPairGenerator.initialize(4096);
                keyPair = keyPairGenerator.generateKeyPair();

                String certificateRequestString = generateCertificateRequestString(userName, deviceId, keyPair);

                return certificateRequestString;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Failed to get an instance of KeyGenerator", e);
            }
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return S.EMPTY;
    }

    private String generateCertificateRequestString(String userName, String deviceId, KeyPair keyPair) {
        try {
            JcaPKCS10CertificationRequestBuilder p10Builder =
                  new JcaPKCS10CertificationRequestBuilder(new X500Principal("CN=" + userName + "@" + deviceId), keyPair.getPublic());
            JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder(SecurityConstants.SIGNATURE_SHA256withRSA);
            ContentSigner signer = csBuilder.build(keyPair.getPrivate());
            PKCS10CertificationRequest csr = p10Builder.build(signer);
            PemObject pemObject = new PemObject("CERTIFICATE REQUEST", csr.getEncoded());
            StringWriter stringWriter = new StringWriter();
            JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(stringWriter);
            jcaPEMWriter.writeObject(pemObject);
            jcaPEMWriter.close();
            stringWriter.close();
            return stringWriter.toString();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return S.EMPTY;
    }

    /**
     * @param certificate
     */
    public boolean storeCertificateInKeystore(@NonNull String certificate) {
        try {
            store.load(null);
            //create certificate from string
            X509Certificate x509Certificate = (X509Certificate) CertificateFactory.getInstance("X.509")
                  .generateCertificate(new ByteArrayInputStream(certificate.getBytes()));

            //store certificate with private key
            store.setKeyEntry(KEY_NAME, keyPair.getPrivate(), null,
                  new Certificate[]{x509Certificate});
            keyPair = null;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void removeCertificateFromKeystore() {
        try {
            store.load(null);
            store.deleteEntry(KEY_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }


    /**
     * encrypt any string with a cipher that is stored in the android keychain
     *
     * @param what string to encrypt
     * @return encrypted string
     */
    public String encryptString(String what) {
        try {
            store.load(null);
            PublicKey publicKey = getPrivateKeyFromKeyStore(store, KEY_NAME).getCertificate().getPublicKey();
            Cipher cipher = getCipher();
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Hex.toHexString(cipher.doFinal(Hex.decode(what)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * decrypt a string that has been encrypted previously via {@Link KeyStoreUtility#encryptString}
     *
     * @param encrypted
     * @return decrypted String
     */
    public String decryptString(String encrypted) {
        try {
            store.load(null);
            return decrypt(store, KEY_NAME, encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private KeyStore.PrivateKeyEntry getPrivateKeyFromKeyStore(KeyStore keyStore, String alias) {
        try {
            KeyStore.Entry entry = keyStore.getEntry(alias, null);

            if (entry == null) {
                Log.d(TAG,"No key found under alias: %s", alias);
                Timber.w("Exiting signData()...");
                return null;
            }

            if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
                Timber.w("Not an instance of a PrivateKeyEntry");
                Timber.w("Exiting signData()...");
                return null;
            }
            return (KeyStore.PrivateKeyEntry) entry;
        } catch (Exception e) {
            Timber.e(e);
            return null;
        }
    }

    private static Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance(
              String.format("%s/%s/%s",
                    SecurityConstants.TYPE_RSA,
                    SecurityConstants.BLOCKING_MODE,
                    SecurityConstants.PADDING_TYPE));
    }

    private String decrypt(KeyStore keyStore, String alias, String cipherText) {
        try {
            PrivateKey privateKey = getPrivateKeyFromKeyStore(keyStore, alias).getPrivateKey();
            Cipher cipher = getCipher();
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return Hex.toHexString(cipher.doFinal(Hex.decode(cipherText)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface SecurityConstants {
        String TYPE_RSA = "RSA";
        String PADDING_TYPE = "PKCS1Padding";
        String BLOCKING_MODE = "NONE";
        String SIGNATURE_SHA256withRSA = "SHA256withRSA";
        String ANDROID_KEY_STORE = "AndroidKeyStore";
    }
}
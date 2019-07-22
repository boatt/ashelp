package com.example.asmhelp;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CryUtils {

    public static String akt(String str, String str2) {

        try {
            byte[] akx = akx(Base64.decode(str, 0), str2);
            if (akx != null) {
                return new String(akx);
            }
        } catch (Exception e) {
        }
        return null;
    }

    private static byte[] akx(byte[] bArr, String str) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(aky(str), "AES");
            Cipher instance = Cipher.getInstance("AES");
            instance.init(2, secretKeySpec);
            return instance.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] aky(String str) {
        byte[] bArr = new byte[24];
        byte[] bytes = str.getBytes();
        if (bArr.length <= bytes.length) {
            System.arraycopy(bytes, 0, bArr, 0, bArr.length);
        } else {
            System.arraycopy(bytes, 0, bArr, 0, bytes.length);
        }
        return bArr;
    }


}

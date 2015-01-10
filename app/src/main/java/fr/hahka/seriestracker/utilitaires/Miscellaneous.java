package fr.hahka.seriestracker.utilitaires;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by thibautvirolle on 07/01/15.
 * Classe contenant diverses méthodes basique possiblement appelées par plusieurs classes
 */
public class Miscellaneous {


    public static String md5(String s) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(), 0, s.length());
            return new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}

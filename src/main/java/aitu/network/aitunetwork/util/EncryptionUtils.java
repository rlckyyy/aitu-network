package aitu.network.aitunetwork.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@UtilityClass
public class EncryptionUtils {

    public static boolean validatePublicKey(String publicKeyBase64) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            keyFactory.generatePublic(keySpec);
            return true;
        } catch (Exception e) {
            log.error("Invalid public key format", e);
            return false;
        }
    }
}
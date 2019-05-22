import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.Assert.assertEquals;


import java.io.ByteArrayOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class TestRSA {

    String hash = "9af287883fc10626b147296adb0e67dd01958ec8eba0049786aa89c25c234e52";
    String encryptedHash = "imHLognvklseJRonp2f1tWXd2ltnIHtjzs1EK6eYyugJTpZ1EgzhE/97ebRZLSvJWRGP/BHijd2bEJ9ykQNbnffsl8E9z/4kqguOR0VVWYBmdYyHso+pkEKtzmKsM7TCv/xL1LfBjgMwrjdi7N6/RD5zJDigLIq7wdliG6fPR98=";

    rsaGenerator rsa = new rsaGenerator();

    @Before
    public void setUp() throws Exception {
        //rsa.generateRSAKey("client");
    }

    @Test
    public void testEncryptHash() throws Exception {
        System.out.println("*** Testing Encryption of Hash in RSA Class ***");
        assertEquals(encryptedHash, Base64.getEncoder().encodeToString(rsa.encryptHash(hash)));
        System.out.println();
    }

}
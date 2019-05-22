
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;



public class Hash
{ 
 
    public static String hash(String input) throws NoSuchAlgorithmException 
    {
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest md = MessageDigest.getInstance("SHA3-256");

 

            // digest() method is called

            // to calculate message digest of the input string

            // returned as array of byte

            byte[] messageDigest = md.digest(input.getBytes());

 

            // Convert byte array into signum representation

            BigInteger no = new BigInteger(1, messageDigest);

 

            // Convert message digest into hex value

            String hashtext = no.toString(16);

 

            // Add preceding 0s to make it 32 bit

            while (hashtext.length() < 32) {

                hashtext = "0" + hashtext;

            }

 

            // return the HashText

            return hashtext;           

    }


}

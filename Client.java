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

public class Client
{
	public static void main(String[] args) throws Exception
	{
            //add bouncy castle as a service provided
            Security.addProvider(new BouncyCastleProvider());
            
            //generate pub/prv keys 
            rsaGenerator rsa = new rsaGenerator();
            rsa.generateRSAKey("client");

            //connect to local host on port 3000
            Socket s = new Socket("127.0.0.1", 3000);
            //input stream to recieve messages
            ObjectInputStream dis = new ObjectInputStream(s.getInputStream());
            //output stream to send messages
            ObjectOutputStream dos = new ObjectOutputStream(s.getOutputStream());
            System.out.println(dis.readObject()); //reads welcome message

            //session key generation with AES
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey key = keyGenerator.generateKey();
            System.out.println("Decoded Key: "+key);
            //encrypt the key
            SecretKey encKey = new SecretKeySpec(rsa.encryptSharedKey(key), "AES");
            System.out.println("Encoded Key: "+ encKey);
            dos.writeObject(encKey); //send key to server
       	

            // Generate initialization vector.
            byte[] IV = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(IV);
            dos.writeObject(IV); //send initialization to server (think it also needs to be encrypted?) 

            System.out.print("Enter message to send: ");
            Scanner sc = new Scanner(System.in);
            String msg = sc.nextLine();
            String hash = Hash.hash(msg);
            System.out.println("Hash: "+hash);
            System.out.println();
            String encHash = Base64.getEncoder().encodeToString(rsa.encryptHash(hash));
            System.out.println("Encrpted Hash: "+encHash);
            System.out.println();
            byte[] toSend = compress(encHash+"|||"+msg);
            System.out.println("Compressed concatenation: "+Base64.getEncoder().encodeToString(toSend));
            System.out.println();
            
            byte[] encMsg = encrypt(toSend, key, IV); //call to encrypt with the key an iv
            dos.writeObject(encMsg); //sends the encryption 
            
            
            System.out.println("Encrypted Text : " + Base64.getEncoder().encodeToString(encMsg));
            System.out.println();
            System.out.println("Message sent. Goodbye");
	}

    public static byte[] encrypt (byte[] plaintext,SecretKey key,byte[] IV ) throws Exception
    {
        //Create cipher for AES with PKCS Padding
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //Geth the spec of the genetated key, required for ciper
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        //Get the spec of the generated iv, reuired for ciper
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        
        //use the cipher to encrypt
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] cipherText = cipher.doFinal(plaintext);
        
        return cipherText;
    }
    public static byte[] compress(String str) throws Exception {

        if (str == null || str.length() == 0) {

            return null;

        }

        //System.out.println("String length : " + str.length());

        ByteArrayOutputStream obj=new ByteArrayOutputStream();

        GZIPOutputStream gzip = new GZIPOutputStream(obj);

        gzip.write(str.getBytes("UTF-8"));

        gzip.close();

        String outStr = obj.toString("UTF-8");
        //System.out.println("Output String length : " + outStr.length());

        return obj.toByteArray();

}
}
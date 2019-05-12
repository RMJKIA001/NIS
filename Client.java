import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.DataOutputStream;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Client
{
	public static void main(String[] args) throws Exception
	{
		Security.addProvider(new BouncyCastleProvider());
		Socket s = new Socket("127.0.0.1",3000);
		ObjectInputStream dis = new ObjectInputStream(s.getInputStream());
		ObjectOutputStream dos = new ObjectOutputStream(s.getOutputStream());
		System.out.println(dis.readObject()); //reads welcome message
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        // Generate Key
        SecretKey key = keyGenerator.generateKey();
        dos.writeObject(key);
       	// Generating IV.
        byte[] IV = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);
        dos.writeObject(IV);
        
		DataOutputStream out =  new DataOutputStream(s.getOutputStream());

		System.out.print("Enter message to send: ");
		Scanner sc = new Scanner(System.in);
		String msg = sc.nextLine();
		byte[] encMsg = encrypt(msg.getBytes(), key,IV);
		dos.writeObject(encMsg);
		//out.writeUTF(Base64.getEncoder().encodeToString(encMsg));
		System.out.println("Encrypted Text : "+Base64.getEncoder().encodeToString(encMsg) );
		System.out.println("Message sent. Goodbye");
	}

	public static byte[] encrypt (byte[] plaintext,SecretKey key,byte[] IV ) throws Exception
    {
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        
        //Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        
        //Create IvParameterSpec
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        
        //Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        
        //Perform Encryption
        byte[] cipherText = cipher.doFinal(plaintext);
        
        return cipherText;
    }
}
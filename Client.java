import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
		//add bouncy castle as a service provided
		Security.addProvider(new BouncyCastleProvider());
		//connect to local host on port 3000
		Socket s = new Socket("127.0.0.1",3000);
		//input stream to recieve messages
		ObjectInputStream dis = new ObjectInputStream(s.getInputStream());
		//output stream to send messages
		ObjectOutputStream dos = new ObjectOutputStream(s.getOutputStream());
		System.out.println(dis.readObject()); //reads welcome message
		
		//session key generation with AES
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
      
        SecretKey key = keyGenerator.generateKey();
        dos.writeObject(key); //send key to server(Needs to be encrypted)
       	// Generate initialization vector.
        byte[] IV = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);
        dos.writeObject(IV); //send initialization to server (think it also needs to be encrypted?) 
        
		System.out.print("Enter message to send: ");
		Scanner sc = new Scanner(System.in);
		String msg = sc.nextLine();
		byte[] encMsg = encrypt(msg.getBytes(), key,IV); //call to encrypt with the key an iv
		dos.writeObject(encMsg); //sends the encryption 
		System.out.println("Encrypted Text : "+Base64.getEncoder().encodeToString(encMsg) );
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
}
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Server
{
	
	public static void main(String[] args) throws Exception
	{

		Security.addProvider(new BouncyCastleProvider());
		ServerSocket ss =null;
		try
		{
			ss = new ServerSocket(3000);
		}
		catch(Exception e)
		{
			System.out.println("Server connection failed");
		}
			System.out.println("Waiting for client to connect...");
			Socket s = ss.accept();
			System.out.println("Client connected");
			ObjectOutputStream dos = new ObjectOutputStream(s.getOutputStream());
			dos.writeObject("You are connected");
			
			ObjectInputStream dis = new ObjectInputStream(s.getInputStream());
			SecretKey k = (SecretKey) dis.readObject();
			byte[] iv = (byte[])dis.readObject();

			//String msg = (String) dis.readObject();
			DataInputStream in = new DataInputStream(s.getInputStream());
			//String msg = in.readUTF();
			byte[] message = (byte[])dis.readObject();
			System.out.println("Received: "+Base64.getEncoder().encodeToString(message));
			
			System.out.println(decrypt(message, k, iv));
			

	}
	public static String decrypt (byte[] cipherText, SecretKey key,byte[] IV) throws Exception
    {
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        
        //Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        
        //Create IvParameterSpec
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        
        //Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        
        //Perform Decryption
        byte[] decryptedText = cipher.doFinal(cipherText);
        
        return new String(decryptedText);
    }
}
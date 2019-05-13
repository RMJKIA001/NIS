import java.net.ServerSocket;
import java.net.Socket;
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
		//add bouncy castle as a service provider
		Security.addProvider(new BouncyCastleProvider());
		//Create a socket on port 3000
		ServerSocket ss = new ServerSocket(3000);	
		System.out.println("Waiting for client to connect...");
		Socket s = ss.accept(); //Accept client connection 
		System.out.println("Client connected");
		ObjectOutputStream dos = new ObjectOutputStream(s.getOutputStream()); //outputstream to send message to client
		dos.writeObject("You are connected");
			
		ObjectInputStream dis = new ObjectInputStream(s.getInputStream()); //input stream to recieve message from client
		SecretKey k = (SecretKey) dis.readObject(); //recieves the key
		byte[] iv = (byte[])dis.readObject(); //receives the iv
		byte[] message = (byte[])dis.readObject(); //recieves the message

		System.out.println("Received: "+Base64.getEncoder().encodeToString(message));			
		System.out.println(decrypt(message, k, iv)); //decrypts and prints message
			

	}
	public static String decrypt (byte[] cipherText, SecretKey key,byte[] IV) throws Exception
    {
        //Create cipher for AES with PKCS Padding
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //Get the spec of the genetated key, required for ciper
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        //Get the spec of the generated iv, reuired for ciper
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        
        //use cipher to decrypt
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decryptedText = cipher.doFinal(cipherText);
        
        return new String(decryptedText);
    }
}
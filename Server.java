import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
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
		//generate pub/prv keys 
		rsaGenerator rsa = new rsaGenerator();
		rsa.generateRSAKey("server");

		//Create a socket on port 3000
		ServerSocket ss = new ServerSocket(3000);	
		System.out.println("Waiting for client to connect...");
		Socket s = ss.accept(); //Accept client connection 
		System.out.println("Client connected");
		ObjectOutputStream dos = new ObjectOutputStream(s.getOutputStream()); //outputstream to send message to client
		dos.writeObject("You are connected");
			
		ObjectInputStream dis = new ObjectInputStream(s.getInputStream()); //input stream to recieve message from client
		SecretKey k = (SecretKey) dis.readObject(); //recieves the key
                System.out.println("Encoded Key: "+ k);
                System.out.println();
                SecretKey decKey = rsa.decryptSharedKey(k.getEncoded());
                System.out.println("Decoded Key: "+decKey);
                System.out.println();
		byte[] iv = (byte[])dis.readObject(); //receives the iv
                
		byte[] received = (byte[])dis.readObject(); //recieves the encypted hash + message
                System.out.println("Received: "+Base64.getEncoder().encodeToString(received));
                System.out.println();
		//String decryptCompString = decryptString(received,decKey,iv);
                byte[] decryptCompByte = decryptByte(received,decKey,iv);
                //Base64.getEncoder().encodeToString
                System.out.println("Decryped compressed concatenation: "+Base64.getEncoder().encodeToString(decryptCompByte));
                System.out.println();
                String dcomp = decompress(decryptCompByte);
                System.out.println("Decrypted decompressed concatenation: "+dcomp);
                System.out.println();
                String hash = dcomp.substring(0,dcomp.indexOf("|||"));//dcomp.split("||")[0];
                System.out.println("Encrypted hash: "+hash);
                System.out.println();
                hash = rsa.decryptHas(Base64.getDecoder().decode(hash));
                //hash = rsa.decryptHas(hash.getBytes("UTF-8"));
                System.out.println("Decrypted hash: "+hash);
                String message = dcomp.substring(dcomp.indexOf("|||")+3);//dcomp.split("||")[1];
                System.out.println(message);
                String myHash = Hash.hash(message);
                if(hash.equals(myHash))
                {
                   System.out.println("Authenticated");
                }
                //System.out.println(decrypt(message.getBytes(), decKey, iv)); //decrypts and prints message
			

	}
	
        public static byte[] decryptByte (byte[] cipherText, SecretKey key,byte[] IV) throws Exception
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
        return decryptedText;
    }
    public static String decompress(byte[] str) throws Exception {

        if (str == null || str.length == 0) {

            return null;

        }

        //System.out.println("Input String length : " + str.length());

        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str));

        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));

        String outStr = "";

        String line;

        while ((line=bf.readLine())!=null) {

            outStr += line;

        }

        //System.out.println("Output String lenght : " + outStr.length());

        return outStr;

}

 


}
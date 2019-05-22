import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

public class TestClient {

    String message = "Tester";
    String compressedMessage = "H4sIAAAAAAAAAAtJLS5JLQIAc1P8+wYAAAA=";

    @Test
    public void testCompress() throws Exception {
        System.out.println("*** Testing Compression of Client Class ***");
        String afterCompression = Base64.getEncoder().encodeToString(Client.compress(message));
        assertEquals(compressedMessage, afterCompression);
        System.out.println();
    }

}
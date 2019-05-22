import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

public class TestServer {

    String message = "Tester";
    String compressedMessage = "H4sIAAAAAAAAAAtJLS5JLQIAc1P8+wYAAAA=";

    @Test
    public void testDecompress() throws Exception {
        System.out.println("*** Testing Decompression of Server Class ***");
        String afterDecompression = Server.decompress(Base64.getDecoder().decode(compressedMessage));
        assertEquals(message, afterDecompression);
        System.out.println();
    }

}
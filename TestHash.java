import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

public class TestHash {

    String message = "Tester";
    String finalHash = "9af287883fc10626b147296adb0e67dd01958ec8eba0049786aa89c25c234e52";

    @Test
    public void testHash() throws NoSuchAlgorithmException {
        System.out.println("*** Testing Hashing Class ***");
        String hashedMessage = Hash.hash(message);
        assertEquals(finalHash, hashedMessage);
        System.out.println();
    }

}
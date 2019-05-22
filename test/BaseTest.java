import junit.framework.*;

public class BaseTest extends TestCase {
    protected int value1, value2;

    protected void setUp(){
        value1 = 2;
        value2 = 3;
    }

    public void testAdd(){
        System.out.println("*** Running a base test ***");
        double result = value1 + value2;
        assertTrue(result == 5);
        System.out.println();
    }
}

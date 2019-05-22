import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        BaseTest.class, TestHash.class, TestClient.class, TestServer.class
})

public class JUnitTestSuite { }
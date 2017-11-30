package cn.eastseven;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

/**
 * Unit test for simple App.
 */
@Slf4j
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);

        DateTime dt = DateTime.now();
        log.info(">>> {}, {}", dt.getMonthOfYear(), dt.getDayOfMonth());
        DateTime dec = new DateTime("2017-12-15");
        log.info(">>> {}, {}", dec.getMonthOfYear(), dec.getDayOfMonth());
    }
}

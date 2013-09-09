package edu.rpi.tw.mobilehealth;

import javax.servlet.ServletContextEvent;

import org.junit.Test;

import edu.rpi.tw.escience.semanteco.test.MockServletContext;
import junit.framework.TestCase;

public class SalusContextListenerTestCase extends TestCase {

    @Test
    public void test() {
        MockServletContext context = new MockServletContext() {
            @Override
            public String getRealPath(String path) {
                assertEquals("/", path);
                return "/";
            }
        };
        ServletContextEvent sce = new ServletContextEvent(context);
        SalusContextListener listener = new SalusContextListener();
        listener.contextInitialized(sce);
        assertEquals(System.getProperty("SalusRootPath"), "/");
        listener.contextInitialized(sce);
        listener.contextDestroyed(sce);
    }

}

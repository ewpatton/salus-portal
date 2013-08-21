package edu.rpi.tw.mobilehealth;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SalusContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        if(System.getProperty("SalusRootPath") == null) {
            System.setProperty("SalusRootPath", context.getRealPath("/"));
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}

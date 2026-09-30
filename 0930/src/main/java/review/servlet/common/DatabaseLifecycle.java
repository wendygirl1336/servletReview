package review.servlet.common;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

/** Release this application's JDBC resources when Eclipse republishes the app. */
@WebListener
public class DatabaseLifecycle implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        AbandonedConnectionCleanupThread.checkedShutdown();
        ClassLoader loader = getClass().getClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == loader) {
                try { DriverManager.deregisterDriver(driver); }
                catch (SQLException e) { event.getServletContext().log("JDBC driver cleanup failed", e); }
            }
        }
    }
}

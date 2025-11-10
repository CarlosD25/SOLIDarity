/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.connection;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 *
 * @author Carlo
 */
@WebListener
public class MongoShutdownListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionMongoDB.close();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {}
}
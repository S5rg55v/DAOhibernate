package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class Util {

    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/jdbcconnect";
    public static final String USER = "root";
    public static final String PASSWORD = "root";
    private static SessionFactory sessionFactory = null;

    public static SessionFactory getConnection() {
        try {
            Configuration configuration = new Configuration()
                    .setProperty("hibernate.connection.driver_class", DRIVER)
                    .setProperty("hibernate.connection.url", URL)
                    .setProperty("hibernate.connection.username", USER)
                    .setProperty("hibernate.connection.password", PASSWORD)
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                    .addAnnotatedClass(User.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return sessionFactory;
    }
    public static void closeConnection() {
        if (sessionFactory != null)
            sessionFactory.close();
    }
}

package fr.astek.kata.databaselogger;

import java.sql.SQLException;
import java.util.Properties;

import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.h2.tools.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class TestConfiguration {

	private static final String DIALECT_PROPERTY = "org.hibernate.dialect.H2Dialect";
	private static final String DRIVER_CLASS_PROPERTY = "org.h2.Driver";
	private static final String CONNECTION_URL_PROPERTY = "jdbc:h2:mem";
	private static final String USERNAME_PROPERTY = "sa";
	private static final String PASSWORD_PROPERTY = "";

	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	
	
	public static Session openSession () {
		if (sessionFactory == null) {
			initConfiguration();
		}
		return sessionFactory.openSession();
	}
	
	private static void initConfiguration () {
		initHibernateSessionFactory();
		initDBUnitProperties();
		startH2Servers();
	}

	private static void startH2Servers() {
		try {
			Server.createTcpServer().start();
			Server.createWebServer().start();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void initDBUnitProperties() {
		System.setProperty(
				PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
				DRIVER_CLASS_PROPERTY);
		System.setProperty(
				PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
				CONNECTION_URL_PROPERTY);
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
				USERNAME_PROPERTY);
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
				PASSWORD_PROPERTY);
	}

	private static void initHibernateSessionFactory() {
		Configuration configuration = new Configuration();

		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", DIALECT_PROPERTY);
		properties.setProperty("hibernate.connection.driver_class",
				DRIVER_CLASS_PROPERTY);
		properties.setProperty("hibernate.connection.url",
				CONNECTION_URL_PROPERTY);
		properties.setProperty("hibernate.connection.username",
				USERNAME_PROPERTY);
		properties.setProperty("hibernate.connection.password", PASSWORD_PROPERTY);
		properties.setProperty("hibernate.show_sql", "true");
		properties.setProperty("hibernate.hbm2ddl.auto", "update");

		configuration.addAnnotatedClass(LoggerConfig.class);

		configuration.addProperties(properties);

		serviceRegistry = new ServiceRegistryBuilder().applySettings(
				configuration.getProperties()).buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}
}

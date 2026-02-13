package org.arispay.configuration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Connection factory for database access via Log4j2 JDBC appender.
 * Uses Spring-injected credentials from application configuration.
 */
@Component
public class ConnectionFactory {

    private static volatile DataSource staticDataSource = null;
    private static final Object LOCK = new Object();

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    @Value("${spring.datasource.hikari.maximum-pool-size:10}")
    private int maxPoolSize;

    @PostConstruct
    public void initializeDataSource() {
        synchronized (LOCK) {
            createDataSource(databaseUrl, databaseUsername, databasePassword, maxPoolSize);
        }
    }

    /**
     * Static method for Log4j2 to get database connections.
     * Only works after Spring initialization via @PostConstruct.
     */
    public static Connection getDatabaseConnection() throws SQLException {
        if (staticDataSource == null) {
            throw new SQLException("Database connection pool not initialized. Ensure Spring context is running.");
        }
        return staticDataSource.getConnection();
    }

    /**
     * Create and initialize the datasource.
     */
    private static void createDataSource(String url, String username, String password, int maxPoolSize) {
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);

        try {
            DriverManagerConnectionFactory cf = new DriverManagerConnectionFactory(url, properties);

            PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, null);
            pcf.setValidationQuery("SELECT 1");

            GenericObjectPoolConfig<PoolableConnection> config = new GenericObjectPoolConfig<>();
            config.setTestOnBorrow(true);
            config.setMaxTotal(maxPoolSize);

            GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(pcf, config);
            pcf.setPool(connectionPool);

            staticDataSource = new PoolingDataSource<>(connectionPool);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database connection pool", e);
        }
    }
}
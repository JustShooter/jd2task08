package by.itacademy.justshooter.task08.dao.impl;

import by.itacademy.justshooter.task08.dao.EntityDaoException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public final class ConnectionPool {
    /**
     * Max pool size.
     */
    public static final int MAX_POOL_SIZE = 10;
    /**
     * Max statements.
     */
    public static final int MAX_STATEMENTS = 100;
    /**
     * Resource bundle to get needed param from property file.
     */
    private final ResourceBundle bundle;
    /**
     * This is a constant for resource bundle for url param.
     */
    private static final String URL_ALIAS = "url";
    /**
     * This is a constant for resource bundle for user param.
     */
    private static final String USER_ALIAS = "user";
    /**
     * This is a constant for resource bundle for password param.
     */
    private static final String PASSWORD_ALIAS = "password";
    /**
     * THis is Hikari database driver.
     */
    private DataSource dataSource;

    /**
     * Constructor for our connection pool.
     * @param incomingBundle Resource bundle for our pool
     */
    public ConnectionPool(final ResourceBundle incomingBundle) {
        this.bundle = incomingBundle;
    }


    /**
     * Method to create connection with database by Hikari driver.
     * @return Connection
     * @throws EntityDaoException Some very rare exception
     */
    public Connection getConnection() throws EntityDaoException {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        if (dataSource == null) {
            cpds.setJdbcUrl(bundle.getString(URL_ALIAS));
            cpds.setUser(bundle.getString(USER_ALIAS));
            cpds.setPassword(bundle.getString(PASSWORD_ALIAS));
            cpds.setInitialPoolSize(2);
            cpds.setMinPoolSize(2);
            cpds.setAcquireIncrement(1);
            cpds.setAutoCommitOnClose(true);
            cpds.setMaxPoolSize(MAX_POOL_SIZE);
            cpds.setMaxStatements(MAX_STATEMENTS);
        }
        Connection connection;
        try {
            connection = cpds.getConnection();
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }
        return connection;
    }
}

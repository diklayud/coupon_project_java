package sql;

import exceptions.CouponSystemException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

public class ConnectionPool {

    private static final int MAX_CONNECTIONS = 10;
    private Stack<Connection> connections = new Stack<>();
    private static ConnectionPool instance = null;

    private ConnectionPool() throws CouponSystemException {
        openAllConnections();
    }

    public static ConnectionPool getInstance() throws CouponSystemException {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    private void openAllConnections() throws CouponSystemException {
        try {
            for (int index = 0; index < MAX_CONNECTIONS; index += 1) {
                Connection connection = DriverManager.getConnection(DataBaseManager.URL, DataBaseManager.USER_NAME, DataBaseManager.PASSWORD);
                connections.push(connection);

            }
        } catch (SQLException e) {
            throw new CouponSystemException("ConnectionPool initialization failed", e);
        }
    }

    public Connection getConnection() throws InterruptedException {
        synchronized (connections) {
            if (connections.isEmpty()) {
                connections.wait();
            }
            return connections.pop();
        }
    }

    public void restoreConnection(Connection connection) {
        synchronized (connections) {
            connections.push(connection);
            connections.notify();
        }
    }

    public void closeAllConnections() throws InterruptedException {
        synchronized (connections) {
            while (connections.size() < MAX_CONNECTIONS) {
                connections.wait();
            }
            connections.removeAllElements();
        }
    }

}

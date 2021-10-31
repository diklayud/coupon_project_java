package sql;

import exceptions.CouponSystemException;

import java.sql.*;
import java.util.Map;

public class DataBaseUtil {

    /**
     * This method runs queries without placeholders
     * @param query
     * @throws CouponSystemException
     */
    public static void runQuery(String query) throws CouponSystemException {
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException(e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * This method runs queries with placeholders params
     * @param query
     * @param params
     * @throws CouponSystemException
     */
    public static void runPlaceHolderQuery(String query, Map<Integer, Object> params) throws CouponSystemException {
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            params.forEach((key, value) -> {
                try {
                    if (value instanceof Integer) {
                        statement.setInt(key, (int) value);
                    } else if (value instanceof String) {
                        statement.setString(key, String.valueOf(value));
                    } else if (value instanceof Date) {
                        statement.setDate(key, (Date) value);
                    } else if (value instanceof Double) {
                        statement.setDouble(key, (Double) value);
                    }
                } catch (SQLException e) {
                    System.out.println("Error in SQL: " + e.getMessage());
                }
            });
            statement.execute();
        } catch (InterruptedException | SQLException e) {
            System.out.println("Error in executing SQL: " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

}

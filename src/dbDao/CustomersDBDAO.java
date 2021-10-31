package dbDao;

import beans.Customer;
import dao.CustomersDAO;
import exceptions.CouponSystemException;
import sql.ConnectionPool;
import sql.DataBaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomersDBDAO implements CustomersDAO {

    private final String ADD_CUSTOMER = "INSERT INTO `couponSystem`.`customers` (`first_name`, `last_name`, `email`, `password`) values (?,?,?,?);";
    private final String UPDATE_CUSTOMER = "UPDATE `couponSystem`.`customers` SET first_name=?, last_name=?, email=?, password=? WHERE id=?;";
    private final String DELETE_CUSTOMER = "DELETE FROM `couponSystem`.`customers` WHERE id=?;";
    private final String GET_ALL_CUSTOMERS = "SELECT * FROM `couponSystem`.`customers`;";
    private final String GET_ONE_CUSTOMER_BY_ID = "SELECT * FROM `couponSystem`.`customers` WHERE id=?;";
    private final String GET_ONE_CUSTOMER_BY_EMAIL = "SELECT * FROM `couponSystem`.`customers` WHERE email=?;";
    private final String IS_CUSTOMER_EXIST_BY_EMAIL_PASSWORD = "SELECT count(*) FROM `couponSystem`.`customers` WHERE email=? AND password=?;";
    private final String IS_CUSTOMER_EXIST_BY_EMAIL = "SELECT count(*) FROM `couponSystem`.`customers` WHERE email=?;";
    private final String GET_CUSTOMER_ID = "SELECT `id` FROM `couponSystem`.`customers` WHERE email=? AND password=?;";

    Connection connection = null;
    CouponsDBDAO couponsDBDAO = new CouponsDBDAO();

    /**
     * Get customer id by email and password from database
     *
     * @param email    search by it
     * @param password search by it
     * @return customer id from database or -1 if customer not found
     * @throws CouponSystemException
     */
    @Override
    public int getCustomerIdByEmailAndPassword(String email, String password) throws CouponSystemException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_CUSTOMER_ID);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            int id = -1;
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            return id;
        } catch (SQLException | InterruptedException e) {
            throw new CouponSystemException("Couldn't get customer because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Check if customer exists in database by email and password
     *
     * @param email    search by it
     * @param password search by it
     * @return true if customer exists in database, false if not
     * @throws CouponSystemException
     */
    @Override
    public boolean isCustomerExistsByEmailPassword(String email, String password) throws CouponSystemException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(IS_CUSTOMER_EXIST_BY_EMAIL_PASSWORD);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return (resultSet.getInt(1) > 0);
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't find customer because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Check if customer exists in database by email
     *
     * @param email search by it
     * @return true if customer exists in database, false if not
     * @throws CouponSystemException
     */
    @Override
    public boolean isCustomerExistsByEmail(String email) throws CouponSystemException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(IS_CUSTOMER_EXIST_BY_EMAIL);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return (resultSet.getInt(1) > 0);
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't find customer because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Add a customer to database
     *
     * @param customer the data of this instance will be sent to database
     * @throws CouponSystemException
     */
    @Override
    public void addCustomer(Customer customer) throws CouponSystemException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, customer.getFirstName());
        params.put(2, customer.getLastName());
        params.put(3, customer.getEmail());
        params.put(4, customer.getPassword());
        DataBaseUtil.runPlaceHolderQuery(ADD_CUSTOMER, params);
    }

    /**
     * Update customer's data in database
     *
     * @param customer with the new details to update
     * @throws CouponSystemException
     */
    @Override
    public void updateCustomer(Customer customer) throws CouponSystemException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, customer.getFirstName());
        params.put(2, customer.getLastName());
        params.put(3, customer.getEmail());
        params.put(4, customer.getPassword());
        params.put(5, customer.getId());
        DataBaseUtil.runPlaceHolderQuery(UPDATE_CUSTOMER, params);
    }

    /**
     * Delete customer from database
     *
     * @param customerID to delete by
     * @throws CouponSystemException
     */
    @Override
    public void deleteCustomer(int customerID) throws CouponSystemException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_CUSTOMER);
            statement.setInt(1, customerID);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new CouponSystemException("Couldn't delete customer. Customer id NOT exists in the system");
            }
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't delete customer because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Get all customers from database
     *
     * @return list of customers from database
     * @throws CouponSystemException
     */
    @Override
    public List<Customer> getAllCustomers() throws CouponSystemException {
        List<Customer> customers = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_CUSTOMERS);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Customer customer = new Customer(resultSet.getInt("id"),
                        resultSet.getString("first_name"), resultSet.getString("last_name"),
                        resultSet.getString("email"), resultSet.getString("password"));
                customer.setCoupons(couponsDBDAO.getAllCouponsPurchasesByCustomerID(customer.getId()));
                customers.add(customer);
            }
            return customers;
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get all customers because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Get a single customer from database
     *
     * @param customerID search by it
     * @return a customer
     * @throws CouponSystemException
     */
    @Override
    public Customer getOneCustomerByID(int customerID) throws CouponSystemException {
        Customer customer = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ONE_CUSTOMER_BY_ID);
            statement.setInt(1, customerID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customer = new Customer(resultSet.getInt("id"),
                        resultSet.getString("first_name"), resultSet.getString("last_name"),
                        resultSet.getString("email"), resultSet.getString("password"));
                customer.setCoupons(couponsDBDAO.getAllCouponsPurchasesByCustomerID(customerID));
            }
            return customer;
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get customer because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Get a single customer from database
     *
     * @param customerEmail search by it
     * @return a customer
     * @throws CouponSystemException
     */
    @Override
    public Customer getOneCustomerByEmail(String customerEmail) throws CouponSystemException {
        Customer customer = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ONE_CUSTOMER_BY_EMAIL);
            statement.setString(1, customerEmail);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customer = new Customer(resultSet.getInt("id"),
                        resultSet.getString("first_name"), resultSet.getString("last_name"),
                        resultSet.getString("email"), resultSet.getString("password"));
                customer.setCoupons(couponsDBDAO.getAllCouponsPurchasesByCustomerID(customer.getId()));
            }
            return customer;
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get customer because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

}

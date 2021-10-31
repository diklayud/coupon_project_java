package dao;

import beans.Customer;
import exceptions.CouponSystemException;

import java.util.List;

public interface CustomersDAO {

    boolean isCustomerExistsByEmailPassword(String email, String password) throws CouponSystemException;

    boolean isCustomerExistsByEmail(String email) throws CouponSystemException;

    void addCustomer(Customer customer) throws CouponSystemException;

    void updateCustomer(Customer customer) throws CouponSystemException;

    void deleteCustomer(int customerID) throws CouponSystemException;

    List<Customer> getAllCustomers() throws CouponSystemException;

    Customer getOneCustomerByID(int customerID) throws CouponSystemException;

    Customer getOneCustomerByEmail(String customerEmail) throws CouponSystemException;

    int getCustomerIdByEmailAndPassword(String email, String password) throws CouponSystemException;

}

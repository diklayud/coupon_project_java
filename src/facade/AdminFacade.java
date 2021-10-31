package facade;

import beans.Company;
import beans.Customer;
import dbDao.CompaniesDBDAO;
import dbDao.CustomersDBDAO;
import exceptions.CouponSystemException;

import java.util.List;

public class AdminFacade extends ClientFacade {

    /**
     * Constructor which initializes CompaniesDBDAO and CustomersDBDAO
     */
    public AdminFacade() {
        companiesDAO = new CompaniesDBDAO();
        customersDAO = new CustomersDBDAO();
    }

    /**
     * Login to the system as administrator
     *
     * @param email
     * @param password
     * @return true if data sent to method is correct and logged in successfully, false if not
     */
    @Override
    public boolean login(String email, String password) {
        try {
            boolean isLogin = (email.equals("admin@admin.com") && password.equals("admin"));
            if (isLogin) {
                return true;
            }
            throw new CouponSystemException("COULDN'T LOG IN, INCORRECT EMAIL OR PASSWORD");
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Add new company to the database if not already exist company with the same name or same email
     *
     * @param company
     * @throws CouponSystemException
     */
    public void addCompany(Company company) throws CouponSystemException {
        if (companiesDAO.getOneCompanyByName(company.getName()) == null) {
            if (companiesDAO.getOneCompanyByEmail(company.getEmail()) == null) {
                companiesDAO.addCompany(company);
            } else {
                throw new CouponSystemException("Add company failed, company email is already exists");
            }
        } else {
            throw new CouponSystemException("Add company failed, company name is already exists");
        }
    }

    /**
     * Update company details in database after checking the company id that sent to the method
     * already exist in the database.
     * Will not update email to an email that exist under other company
     *
     * @param company
     * @throws CouponSystemException
     */
    public void updateCompany(Company company) throws CouponSystemException {
        Company companyToUpdate = companiesDAO.getOneCompanyByID(company.getId());
        if (companyToUpdate == null) {
            throw new CouponSystemException("Cannot update company. Company ID not in the system");
        }
        if (!company.getEmail().equals(companyToUpdate.getEmail())) {
            //CHECK IF WE ASK TO CHANGE MAIL TO EXISTING ONE
            Company companyIfExist = companiesDAO.getOneCompanyByEmail(company.getEmail());
            if (companyIfExist != null) {
                throw new CouponSystemException("Cannot update the company email. Already exists in the system");
            }
        }
        companyToUpdate.setEmail(company.getEmail());
        companyToUpdate.setPassword(company.getPassword());
        companyToUpdate.setCoupons(company.getCoupons());
        companiesDAO.updateCompany(companyToUpdate);
    }

    /**
     * Get all companies from database
     *
     * @return list of companies from database
     * @throws CouponSystemException
     */
    public List<Company> getAllCompanies() throws CouponSystemException {
        return companiesDAO.getAllCompanies();
    }

    /**
     * Get a company from database by id
     *
     * @param companyId
     * @return a company from database
     * @throws CouponSystemException
     */
    public Company getOneCompanyByID(int companyId) throws CouponSystemException {
        Company company = companiesDAO.getOneCompanyByID(companyId);
        if (company == null) {
            throw new CouponSystemException("Cannot get company by id. Company is NOT in the system");
        }
        return company;
    }

    /**
     * Delete a company (and its coupons) from database by id
     *
     * @param companyId
     * @throws CouponSystemException
     */
    public void deleteCompany(int companyId) throws CouponSystemException {
        companiesDAO.deleteCompany(companyId);
    }

    /**
     * Add new customer to the database if not already exist customer with the same email
     *
     * @param customer
     * @throws CouponSystemException
     */
    public void addCustomer(Customer customer) throws CouponSystemException {
        boolean isExist = customersDAO.isCustomerExistsByEmail(customer.getEmail());
        if (isExist) {
            throw new CouponSystemException("Cannot add the new customer, email already exists in the system");
        } else {
            customersDAO.addCustomer(customer);
        }
    }

    /**
     * Update customer details in database after checking the customer id that sent to the method
     * already exist in the database.
     * Will not update email to an email that exist under other customer
     *
     * @param customer
     * @throws CouponSystemException
     */
    public void updateCustomer(Customer customer) throws CouponSystemException {
        Customer customerToUpdate = customersDAO.getOneCustomerByID(customer.getId());
        if (customerToUpdate == null) {
            throw new CouponSystemException("Cannot update the customer. Customer ID not in the system");
        }
        if (!customer.getEmail().equals(customerToUpdate.getEmail())) {
            Customer customerIfExist = customersDAO.getOneCustomerByEmail(customer.getEmail());
            if (customerIfExist != null) {
                throw new CouponSystemException("Cannot update the customer email. Already exists in the system");
            }
        }
        customerToUpdate.setFirstName(customer.getFirstName());
        customerToUpdate.setLastName(customer.getLastName());
        customerToUpdate.setEmail(customer.getEmail());
        customerToUpdate.setPassword(customer.getPassword());
        customerToUpdate.setCoupons(customer.getCoupons());
        customersDAO.updateCustomer(customerToUpdate);
    }

    /**
     * Get all customers from database
     *
     * @return list of customers from database
     * @throws CouponSystemException
     */
    public List<Customer> getAllCustomers() throws CouponSystemException {
        return customersDAO.getAllCustomers();
    }

    /**
     * Get a customer from database by id
     *
     * @param customerId
     * @return a customer from database
     * @throws CouponSystemException
     */
    public Customer getOneCustomerByID(int customerId) throws CouponSystemException {
        Customer customer = customersDAO.getOneCustomerByID(customerId);
        if (customer == null) {
            throw new CouponSystemException("Cannot get customer by id. Customer is NOT in the system");
        }
        return customer;
    }

    /**
     * Delete a customer (and his coupon purchases) from database by id
     *
     * @param customerId
     * @throws CouponSystemException
     */
    public void deleteCustomer(int customerId) throws CouponSystemException {
        customersDAO.deleteCustomer(customerId);
    }

}

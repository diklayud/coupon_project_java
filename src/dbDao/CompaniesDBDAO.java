package dbDao;

import beans.Company;
import dao.CompaniesDAO;
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

public class CompaniesDBDAO implements CompaniesDAO {

    private final String ADD_COMPANY = "INSERT INTO `couponSystem`.`companies` (`name`, `email`, `password`) values (?,?,?);";
    private final String UPDATE_COMPANY = "UPDATE `couponSystem`.`companies` SET name=?, email=?, password=? WHERE id=?;";
    private final String DELETE_COMPANY = "DELETE FROM `couponSystem`.`companies` WHERE id=?;";
    private final String GET_ALL_COMPANIES = "SELECT * FROM `couponSystem`.`companies`;";
    private final String GET_ONE_COMPANY_BY_ID = "SELECT * FROM `couponSystem`.`companies` WHERE id=?;";
    private final String GET_ONE_COMPANY_BY_NAME = "SELECT * FROM `couponSystem`.`companies` WHERE name=?;";
    private final String GET_ONE_COMPANY_BY_EMAIL = "SELECT * FROM `couponSystem`.`companies` WHERE email=?;";
    private final String IS_COMPANY_EXIST = "SELECT count(*) FROM `couponSystem`.`companies` WHERE email=? AND password=?;";
    private final String GET_COMPANY_ID = "SELECT `id` FROM `couponSystem`.`companies` WHERE email=? AND password=?;";

    Connection connection = null;
    CouponsDBDAO couponsDBDAO = new CouponsDBDAO();

    /**
     * Check if company exists in database by given email and password
     *
     * @param email    search by it
     * @param password search by it
     * @return true if company exists in database, false if not
     * @throws CouponSystemException
     */
    @Override
    public boolean isCompanyExists(String email, String password) throws CouponSystemException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(IS_COMPANY_EXIST);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            boolean isExist = (resultSet.getInt(1) > 0);
            return isExist;
        } catch (SQLException | InterruptedException e) {
            throw new CouponSystemException("Couldn't find company because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Get company id by email and password from database
     *
     * @param email    search by it
     * @param password search by it
     * @return company id from database or -1 if company not found
     * @throws CouponSystemException
     */
    @Override
    public int getCompanyIdByEmailAndPassword(String email, String password) throws CouponSystemException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_COMPANY_ID);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            int id = -1;
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            return id;
        } catch (SQLException | InterruptedException e) {
            throw new CouponSystemException("Couldn't get company id because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Add a company to database
     *
     * @param company the data of this instance will be sent to database
     * @throws CouponSystemException
     */
    @Override
    public void addCompany(Company company) throws CouponSystemException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, company.getName());
        params.put(2, company.getEmail());
        params.put(3, company.getPassword());
        DataBaseUtil.runPlaceHolderQuery(ADD_COMPANY, params);
    }

    /**
     * Update company's data in database
     *
     * @param company object of company with the new details to be updated
     * @throws CouponSystemException
     */
    @Override
    public void updateCompany(Company company) throws CouponSystemException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, company.getName());
        params.put(2, company.getEmail());
        params.put(3, company.getPassword());
        params.put(4, company.getId());
        DataBaseUtil.runPlaceHolderQuery(UPDATE_COMPANY, params);
    }

    /**
     * Delete company from database
     *
     * @param companyID to delete by
     * @throws CouponSystemException
     */
    @Override
    public void deleteCompany(int companyID) throws CouponSystemException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_COMPANY);
            statement.setInt(1, companyID);
            if (statement.executeUpdate() == 0) {
                throw new CouponSystemException("Couldn't delete company. Company id NOT exists in the system");
            }
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException(e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Get all companies from database
     *
     * @return list of companies from database
     * @throws CouponSystemException
     */
    @Override
    public List<Company> getAllCompanies() throws CouponSystemException {
        List<Company> companies = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_COMPANIES);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Company company = new Company(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("email"), resultSet.getString("password"));
                company.setCoupons(couponsDBDAO.getAllCouponsByCompanyId(company.getId()));
                companies.add(company);
            }
            return companies;
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get all companies because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Get a single company from database filtered by id
     *
     * @param companyID search by it
     * @return a company
     * @throws CouponSystemException
     */
    @Override
    public Company getOneCompanyByID(int companyID) throws CouponSystemException {
        Company company = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ONE_COMPANY_BY_ID);
            statement.setInt(1, companyID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                company = new Company(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("email"), resultSet.getString("password"));
                company.setCoupons(couponsDBDAO.getAllCouponsByCompanyId(company.getId()));
            }
            return company;
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get company because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Get a single company from database filtered by unique companyName
     *
     * @param companyName search by it
     * @return a company
     * @throws CouponSystemException
     */
    @Override
    public Company getOneCompanyByName(String companyName) throws CouponSystemException {
        Company company = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ONE_COMPANY_BY_NAME);
            statement.setString(1, companyName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                company = new Company(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("email"), resultSet.getString("password"));
                company.setCoupons(couponsDBDAO.getAllCouponsByCompanyId(company.getId()));
            }
            return company;
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get company because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Get a single company from database filtered by unique email
     *
     * @param companyEmail search by it
     * @return a company
     * @throws CouponSystemException
     */
    @Override
    public Company getOneCompanyByEmail(String companyEmail) throws CouponSystemException {
        Company company = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ONE_COMPANY_BY_EMAIL);
            statement.setString(1, companyEmail);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                company = new Company(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("email"), resultSet.getString("password"));
                company.setCoupons(couponsDBDAO.getAllCouponsByCompanyId(company.getId()));
            }
            return company;
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get company because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

}

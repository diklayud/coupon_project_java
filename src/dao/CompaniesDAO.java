package dao;

import beans.Company;
import exceptions.CouponSystemException;

import java.util.List;

public interface CompaniesDAO {

    boolean isCompanyExists(String email, String password) throws CouponSystemException;

    void addCompany(Company company) throws CouponSystemException;

    void updateCompany(Company company) throws CouponSystemException;

    void deleteCompany(int companyID) throws CouponSystemException;

    List<Company> getAllCompanies() throws CouponSystemException;

    Company getOneCompanyByID(int companyID) throws CouponSystemException;

    Company getOneCompanyByName(String companyName) throws CouponSystemException;

    Company getOneCompanyByEmail(String companyEmail) throws CouponSystemException;

    int getCompanyIdByEmailAndPassword(String email, String password) throws CouponSystemException;
}

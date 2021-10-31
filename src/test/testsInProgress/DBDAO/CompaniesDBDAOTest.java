package test.testsInProgress.DBDAO;

import dbDao.CompaniesDBDAO;
import exceptions.CouponSystemException;

public class CompaniesDBDAOTest {

    public static void main(String[] args) {

        CompaniesDBDAO companiesDBDAO = new CompaniesDBDAO();

        try {
            // TEST ADD COMPANY
//            companiesDBDAO.addCompany(new Company("c1", "comp@a", "111"));
//            companiesDBDAO.addCompany(new Company("c2", "comp@b", "222"));
//            companiesDBDAO.addCompany(new Company("c3", "comp@c", "333"));
//            companiesDBDAO.addCompany(new Company("c4", "comp", "444"));
//            Company company = null;
//            company = new Company("c5", "compi5", "555");
//            companiesDBDAO.addCompany(company);

            // TEST SEARCH COMPANY
//            System.out.println(companiesDBDAO.isCompanyExists("comp", "4414"));
//            System.out.println(companiesDBDAO.isCompanyExists("comp@c", "333"));
//            System.out.println(companiesDBDAO.isCompanyExists("comp@b", "333"));

            // TEST GET COMPANY ID
            // expected - get real id
            System.out.println(companiesDBDAO.getCompanyIdByEmailAndPassword("comp@c", "333"));
            // expected null or 0
            System.out.println(companiesDBDAO.getCompanyIdByEmailAndPassword("comp@b", "333"));

            // TEST GET ALL COMPANIES
//            List<Company> companies = companiesDBDAO.getAllCompanies();
//            companies.forEach(System.out::println);

            // TEST GET A SINGLE COMPANY
//            System.out.println(companiesDBDAO.getOneCompany(0));
//            Company company1 = companiesDBDAO.getOneCompany(1);
//            System.out.println(company1);

            // TEST UPDATE COMPANY
//            company1.setEmail("company15@111");
//            company1.setName("cco111");
//            company1.setPassword("1212");
//            companiesDBDAO.updateCompany(company1);

            // TEST DELETE COMPANY
//            companiesDBDAO.deleteCompany(company1.getId());
//            companiesDBDAO.deleteCompany(2);


        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


    }
}

package test.testsInProgress.FACADE;

import beans.Company;
import beans.Customer;
import exceptions.CouponSystemException;
import facade.AdminFacade;

import java.util.List;

public class AdminFacadeTest {

    public static void main(String[] args) {

        AdminFacade adminFacade = new AdminFacade();
        try {
            // ================================ LOGIN ========================================
//        System.out.println(adminFacade.login("admin1@admin", "admin1"));
        System.out.println(adminFacade.login("admin@admin.com", "admin"));

            // ================================ ADD COMPANY ========================================
            // new - expected to insert
            adminFacade.addCompany(new Company("compT11", "1cpmT1@comp", "123"));
//            adminFacade.addCompany(new Company("compT5", "cpmT5@comp5", "12345"));

            // new - expected to drop - name and email already exist
//            adminFacade.addCompany(new Company("compT1", "cpmT1@comp", "123"));

            // new - expected to drop - email already exist
//            adminFacade.addCompany(new Company("compT2", "cpmT1@comp", "123"));

            // new - expected to insert - name already exist
//            adminFacade.addCompany(new Company("compT1", "123@123", "123"));

            // ================================ GET ALL COMPANIES ========================================
            List<Company> companies = adminFacade.getAllCompanies();
            companies.forEach(System.out::println);

            // ================================ UPDATE COMPANY ========================================
            Company company = companies.get(6);
            System.out.println(company);
            company.setEmail("comp@be");
            company.setPassword("newPasse");
            System.out.println(company);
            adminFacade.updateCompany(company);

            // ================================ GET SINGLE COMPANY BY COMPANY_ID ========================================
            Company companyAfterUpdate = adminFacade.getOneCompanyByID(company.getId());
            System.out.println(companyAfterUpdate);
            Company company1 = adminFacade.getOneCompanyByID(17);
            System.out.println(company1);

            // ================================ DELETE COMPANY ========================================
//            adminFacade.deleteCompany(4);


            // ================================ ADD CUSTOMER ========================================
            // new - expected to insert
//            adminFacade.addCustomer(new Customer("fname", "lname", "cus@mail", "112233"));
            // new - expected to insert
//            adminFacade.addCustomer(new Customer("fname22", "lname22", "cus@mail22", "11223344"));
            // new - expected to drop - email already exist
//            adminFacade.addCustomer(new Customer("fname11", "lname22", "cus@mail22", "11223344"));

            // ================================ GET ALL CUSTOMERS ========================================
            List<Customer> customers = adminFacade.getAllCustomers();
            customers.forEach(System.out::println);

            // ================================ UPDATE CUSTOMER ========================================
            Customer customer = customers.get(0);
            System.out.println(customer);
            customer.setEmail("c3@custir");
            adminFacade.updateCustomer(customer);

            // ================================ GET ONE CUSTOMER ========================================
//            System.out.println(adminFacade.getOneCustomerByID(customers.get(1).getId()));

            // ================================ DELETE CUSTOMER ========================================
//            adminFacade.deleteCustomer(3);


        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

    }
}

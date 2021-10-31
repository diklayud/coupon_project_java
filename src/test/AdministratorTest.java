package test;

import beans.Company;
import beans.Customer;
import enums.ClientType;
import exceptions.CouponSystemException;
import facade.AdminFacade;
import loginManager.LoginManager;

import java.util.List;

public class AdministratorTest {

    public static void adminTest() {

        // =========== LOGIN ===========
        // try to login with wrong email. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        LoginManager.getInstance().login("1admin1@admin.com", "admin", ClientType.Administrator);

        // try to login with wrong password. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        LoginManager.getInstance().login("admin@admin.com", "1admin1", ClientType.Administrator);

        // try to login with right email and password. EXPECTED RESULT ==> SHOULD LOGIN SUCCESSFULLY :)
        AdminFacade adminFacade = (AdminFacade) LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.Administrator);


        // =========== ADD COMPANY ===========
        // try to add 5 companies. EXPECTED RESULT ==> SHOULD ADD SUCCESSFULLY :)
        try {
            adminFacade.addCompany(new Company("company1", "comp1@a.com", "comp1"));
            adminFacade.addCompany(new Company("company2", "comp2@b.com", "comp2"));
            adminFacade.addCompany(new Company("company3", "comp3@c.com", "comp3"));
            adminFacade.addCompany(new Company("company4", "comp4@d.com", "comp4"));
            adminFacade.addCompany(new Company("company5", "comp5@e.com", "comp5"));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to add company with existing name. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        try {
            adminFacade.addCompany(new Company("company1", "1comp1@a.com", "1comp1"));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to add company with existing email. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        try {
            adminFacade.addCompany(new Company("company6", "comp1@a.com", "2comp1"));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // =========== GET ALL COMPANIES ===========
        // get all companies from DB. EXPECTED RESULT ==> SHOULD GET THEM SUCCESSFULLY AND SHOW THAT THE COMPANIES
        // WITH THE EXISTING VALUES REALLY NOT ADDED TO DB :)
        List<Company> companies = null;
        try {
            companies = adminFacade.getAllCompanies();
            System.out.println("Is list size 5? " + (companies.size() == 5));
            System.out.println("Our LIST of companies:");
            companies.forEach(System.out::println);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // =========== UPDATE COMPANY ===========
        // try to update existing company (it's email and password to new unique values).
        // EXPECTED RESULT ==> SHOULD UPDATE SUCCESSFULLY :)
        Company companyToUpdate1 = companies.get(0);
        companyToUpdate1.setEmail("new@email.com");
        companyToUpdate1.setPassword("newPass123");
        try {
            adminFacade.updateCompany(companyToUpdate1);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to update company's email to existing email of other company.
        // EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        Company companyToUpdate2 = companies.get(1);
        companyToUpdate2.setEmail("new@email.com");
        try {
            adminFacade.updateCompany(companyToUpdate2);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to update company with not existing id in the system.
        // EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        Company companyToUpdate4 = new Company(200, "newComp", "nc@comp.com", "nc123");
        companyToUpdate4.setEmail("new@comp.com");
        companyToUpdate4.setPassword("nc112233");
        try {
            adminFacade.updateCompany(companyToUpdate4);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // =========== GET A SINGLE COMPANY ===========
        // get a company by id from DB. EXPECTED RESULT ==> SHOULD GET IT SUCCESSFULLY AND SHOW THAT THE COMPANY'S
        // DATA UPDATED IN DB ACCORDING TO THE UPDATE TEST ABOVE :)
        try {
            Company companyAfterUpdate1 = adminFacade.getOneCompanyByID(1);
            System.out.println(companyAfterUpdate1);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // get a company from DB by NOT existing id. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        try {
            Company companyAfterUpdate2 = adminFacade.getOneCompanyByID(1000);
            System.out.println(companyAfterUpdate2);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // =========== DELETE A SINGLE COMPANY ===========
        // try to delete a company by existing id. EXPECTED RESULT ==> SHOULD DELETE SUCCESSFULLY :)
        try {
            adminFacade.deleteCompany(5);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to delete a company by not existing id. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        try {
            adminFacade.deleteCompany(202);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // =========== GET ALL COMPANIES ===========
        // get all companies from DB. EXPECTED RESULT ==> SHOULD GET THEM SUCCESSFULLY AND SHOW THAT
        // THE COMPANY THAT JUST WAS DELETED IS GONE FROM DB
        List<Company> companiesAfterDelete = null;
        try {
            companiesAfterDelete = adminFacade.getAllCompanies();
            System.out.println("Is list size 4? " + (companiesAfterDelete.size() == 4));
            System.out.println("Our NEW LIST of companies:");
            companiesAfterDelete.forEach(System.out::println);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // =========== ADD CUSTOMER ===========
        // try to add 5 customers. EXPECTED RESULT ==> SHOULD ADD SUCCESSFULLY :)
        try {
            adminFacade.addCustomer(new Customer("customer1", "Lcustomer1", "cust1@mail.com", "111"));
            adminFacade.addCustomer(new Customer("customer2", "Lcustomer2", "cust2@mail.com", "222"));
            adminFacade.addCustomer(new Customer("customer3", "Lcustomer3", "cust3@mail.com", "333"));
            adminFacade.addCustomer(new Customer("customer4", "Lcustomer4", "cust4@mail.com", "444"));
            adminFacade.addCustomer(new Customer("customer5", "Lcustomer5", "cust5@mail.com", "555"));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to add customer with existing email. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        try {
            adminFacade.addCustomer(new Customer("customer6", "Lcustomer6", "cust5@mail.com", "616"));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // =========== GET ALL CUSTOMERS ===========
        // get all customer from DB. EXPECTED RESULT ==> SHOULD GET THEM SUCCESSFULLY AND SHOW THAT THE CUSTOMER
        // WITH THE EXISTING EMAIL REALLY NOT ADDED TO DB :)
        List<Customer> customers = null;
        try {
            customers = adminFacade.getAllCustomers();
            System.out.println("Is list size 5? " + (customers.size() == 5));
            System.out.println("Our LIST of customers:");
            customers.forEach(System.out::println);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // =========== UPDATE CUSTOMER ===========
        // try to update existing customer (it's first name, last name, email and password to new unique values).
        // EXPECTED RESULT ==> SHOULD UPDATE SUCCESSFULLY :)
        Customer customerToUpdate1 = customers.get(0);
        customerToUpdate1.setFirstName("newName");
        customerToUpdate1.setLastName("newLname");
        customerToUpdate1.setEmail("customerNew@email.com");
        customerToUpdate1.setPassword("newPass111");
        try {
            adminFacade.updateCustomer(customerToUpdate1);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to update customer's email to existing email of other customer.
        // EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        Customer customerToUpdate2 = customers.get(1);
        customerToUpdate2.setEmail("customerNew@email.com");
        try {
            adminFacade.updateCustomer(customerToUpdate2);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to update customer with not existing id in the system.
        // EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        Customer customerToUpdate3 = new Customer(200, "customer7", "Lcustomer7", "cust7@mail.com", "777");
        customerToUpdate3.setEmail("new@customer.com");
        customerToUpdate3.setPassword("7171");
        try {
            adminFacade.updateCustomer(customerToUpdate3);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // =========== GET A SINGLE CUSTOMER ===========
        // get a customer by id from DB. EXPECTED RESULT ==> SHOULD GET IT SUCCESSFULLY AND SHOW THAT THE CUSTOMER'S
        // DATA UPDATED IN DB ACCORDING TO THE UPDATE TEST ABOVE :)
        try {
            Customer customerAfterUpdate1 = adminFacade.getOneCustomerByID(1);
            System.out.println(customerAfterUpdate1);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // get a customer from DB by NOT existing id. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        try {
            Customer customerAfterUpdate2 = adminFacade.getOneCustomerByID(1000);
            System.out.println(customerAfterUpdate2);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // =========== DELETE A SINGLE CUSTOMER ===========
        // try to delete a customer by existing id. EXPECTED RESULT ==> SHOULD DELETE SUCCESSFULLY :)
        try {
            adminFacade.deleteCustomer(5);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to delete a customer by not existing id. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        try {
            adminFacade.deleteCustomer(202);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // =========== GET ALL CUSTOMERS ===========
        // get all customers from DB. EXPECTED RESULT ==> SHOULD GET THEM SUCCESSFULLY AND SHOW THAT
        // THE CUSTOMER THAT JUST WAS DELETED IS GONE FROM DB
        List<Customer> customersAfterDelete = null;
        try {
            customersAfterDelete = adminFacade.getAllCustomers();
            System.out.println("Is list size 4? " + (customersAfterDelete.size() == 4));
            System.out.println("Our NEW LIST of customers:");
            customersAfterDelete.forEach(System.out::println);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

    }
}

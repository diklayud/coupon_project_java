package test.testsInProgress.LOGIN_MANAGER;

import enums.ClientType;
import facade.ClientFacade;
import loginManager.LoginManager;

public class LoginManagerTest {
    public static void main(String[] args) {

        // LOG IN AS ADMIN
        // wrong email
        ClientFacade adminFacade2 = LoginManager.getInstance().login("admin!@admin.com", "admin", ClientType.Administrator);
        if (adminFacade2 == null) {
            System.out.println("log in as admin failed");
        } else {
            System.out.println("log in as admin successfully");
        }

        // wrong password
        ClientFacade adminFacade1 = LoginManager.getInstance().login("admin@admin.com", "admin!", ClientType.Administrator);
        if (adminFacade1 == null) {
            System.out.println("log in as admin failed");
        } else {
            System.out.println("log in as admin successfully");
        }

        // right data
        ClientFacade adminFacade = LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.Administrator);
        if (adminFacade == null) {
            System.out.println("log in as admin failed");
        } else {
            System.out.println("log in as admin successfully");
        }

        System.out.println("========================================================================================");

        // LOG IN AS COMPANY
        // wrong email
        ClientFacade companyFacade2 = LoginManager.getInstance().login("comp!!@a", "111", ClientType.Company);
        if (companyFacade2 == null) {
            System.out.println("log in as company failed");
        } else {
            System.out.println("log in as company successfully");
        }

        // wrong password
        ClientFacade companyFacade1 = LoginManager.getInstance().login("comp@a", "123", ClientType.Company);
        if (companyFacade1 == null) {
            System.out.println("log in as company failed");
        } else {
            System.out.println("log in as company successfully");
        }

        // right data
        ClientFacade companyFacade = LoginManager.getInstance().login("comp@a", "111", ClientType.Company);
        if (companyFacade == null) {
            System.out.println("log in as company failed");
        } else {
            System.out.println("log in as company successfully");
        }

        System.out.println("========================================================================================");

        // LOG IN AS CUSTOMER
        // wrong email
        ClientFacade customerFacade2 = LoginManager.getInstance().login("c3@custir!!!", "c2c2", ClientType.Customer);
        if (customerFacade2 == null) {
            System.out.println("log in as customer failed");
        } else {
            System.out.println("log in as customer successfully");
        }

        // wrong password
        ClientFacade customerFacade1 = LoginManager.getInstance().login("c3@custir", "c2c2!!!", ClientType.Customer);
        if (customerFacade1 == null) {
            System.out.println("log in as customer failed");
        } else {
            System.out.println("log in as customer successfully");
        }

        // right data
        ClientFacade customerFacade = LoginManager.getInstance().login("c3@custir", "c2c2", ClientType.Customer);
        if (customerFacade == null) {
            System.out.println("log in as customer failed");
        } else {
            System.out.println("log in as customer successfully");
        }


    }
}

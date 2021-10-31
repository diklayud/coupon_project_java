package test;

import art.Art;
import sql.ConnectionPool;
import sql.DataBaseManager;
import thread.CouponExpirationDailyJob;

public class Test {

    public static void testAll() {

        // fresh start :)
        DataBaseManager.dropDataBase();
        DataBaseManager.createDataBase();
        DataBaseManager.createTables();

        // start thread
        CouponExpirationDailyJob couponExpirationDailyJob = new CouponExpirationDailyJob();
        Thread dailyJob = new Thread(couponExpirationDailyJob);
        dailyJob.start();

        // ===================================== ADMINISTRATOR CASE =====================================
        System.out.println(Art.adminTest);
        AdministratorTest.adminTest();

        // ===================================== COMPANY CASE =====================================
        System.out.println("\n");
        System.out.println(Art.companyTest);
        CompanyTest.companyTest();

        // ===================================== CUSTOMER CASE =====================================
        System.out.println("\n");
        System.out.println(Art.customerTest);
        CustomerTest.customerTest();

        // ===================================== DELETE COUPON/CUSTOMER/COMPANY =====================================
        System.out.println("\n");
        DeletingTest.deleteTest();

        // stop thread
        dailyJob.interrupt();

        try {
            // wait for thread to finish
            dailyJob.join();
            System.out.println("Bye bye");
            ConnectionPool.getInstance().closeAllConnections();
            System.out.println("System closed");
        } catch (Exception e) {
            System.out.println("Something went terribly wrong: " + e.getMessage());
        }

    }
}


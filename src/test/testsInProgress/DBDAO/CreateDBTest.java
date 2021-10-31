package test.testsInProgress.DBDAO;

import sql.DataBaseManager;

public class CreateDBTest {

    public static void main(String[] args) {

        DataBaseManager.dropDataBase();
        DataBaseManager.createDataBase();

        // when queries are private
        DataBaseManager.createTables();

        // when queries are public
//        DataBaseManager.createTables(DataBaseManager.CREATE_COMPANIES_TABLE);
//        DataBaseManager.createTables(DataBaseManager.CREATE_CUSTOMERS_TABLE);
//        DataBaseManager.createTables(DataBaseManager.CREATE_CATEGORIES_TABLE);
//        DataBaseManager.createTables(DataBaseManager.CREATE_COUPONS_TABLE);
//        DataBaseManager.createTables(DataBaseManager.CREATE_CUSTOMERS_VS_COUPONS_TABLE);

        System.out.println("finished successfully!!");

    }
}

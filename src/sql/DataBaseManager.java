package sql;

import exceptions.CouponSystemException;

import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {

    //connection DB
    public static final String URL = "jdbc:mysql://localhost:3306?createDatabaseIfNotExist=FALSE";
    public static final String USER_NAME = "dikla";
    public static final String PASSWORD = "1994";

    //create & drop database
    private static final String CREATE_DB = "CREATE SCHEMA if not exists couponSystem;";
    private static final String DROP_DB = "DROP SCHEMA couponSystem;";

    //create & drop tables
    private static final String CREATE_COMPANIES_TABLE = "CREATE TABLE if not exists `couponSystem`.`companies` " +
            " (`id` INT NOT NULL AUTO_INCREMENT," +
            " `name` VARCHAR(150) NOT NULL," +
            " `email` VARCHAR(150) NOT NULL," +
            " `password` VARCHAR(15) NOT NULL," +
            " PRIMARY KEY (`id`));";

    private static final String CREATE_CUSTOMERS_TABLE = "CREATE TABLE if not exists `couponSystem`.`customers` " +
            " (`id` INT NOT NULL AUTO_INCREMENT," +
            " `first_name` VARCHAR(150) NOT NULL, " +
            " `last_name` VARCHAR(150) NOT NULL," +
            " `email` VARCHAR(150) NOT NULL," +
            " `password` VARCHAR(15) NOT NULL," +
            " PRIMARY KEY (`id`));";

    private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE if not exists `couponSystem`.`categories` " +
            " (`id` INT NOT NULL AUTO_INCREMENT," +
            " `name` VARCHAR(30) NOT NULL," +
            " PRIMARY KEY (`id`));";

    private static final String CREATE_COUPONS_TABLE = "CREATE TABLE if not exists `couponSystem`.`coupons` " +
            " (`coupon_id` INT NOT NULL AUTO_INCREMENT," +
            " `company_id` INT," +
            " `category_id` INT," +
            " `title` VARCHAR(150) NOT NULL," +
            " `description` VARCHAR(170) NOT NULL," +
            " `start_date` DATE NOT NULL," +
            " `end_date` DATE NOT NULL," +
            " `amount` INT NOT NULL," +
            " `price` DOUBLE NOT NULL," +
            " `image` VARCHAR(200) NOT NULL," +
            " PRIMARY KEY (`coupon_id`)," +
            " FOREIGN KEY(`company_id`)" +
            " REFERENCES `companies`(`id`) ON DELETE CASCADE," + // COMPANY_ID foreign key
            " FOREIGN KEY (`category_id`)" +
            " REFERENCES `categories`(`id`) ON DELETE CASCADE);"; // CATEGORY_ID foreign key

    private static final String CREATE_CUSTOMERS_VS_COUPONS_TABLE = "CREATE TABLE if not exists `couponSystem`.`customers_vs_coupons`" +
            " (`customer_id` INT NOT NULL," +
            " `coupon_id` INT NOT NULL," +
            " PRIMARY KEY (`customer_id`, `coupon_id`)," +
            " INDEX `coupon_id_idx` (`coupon_id` ASC) VISIBLE," +
            " FOREIGN KEY (`customer_id`)" +
            " REFERENCES `customers` (`id`) ON DELETE CASCADE," +
            " FOREIGN KEY (`coupon_id`)" +
            " REFERENCES `coupons` (`coupon_id`) ON DELETE CASCADE);";

    public static final String CREATE_CATEGORIES = "INSERT INTO `couponSystem`.`categories` (`name`) " +
            "VALUES ('food'), ('electricity'), ('restaurant'), ('vacation'), ('fashion');";

    /**
     * This method runs the create database query
     */
    public static void createDataBase() {
        try {
            DataBaseUtil.runQuery(CREATE_DB);
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method runs the drop database query
     */
    public static void dropDataBase() {
        try {
            DataBaseUtil.runQuery(DROP_DB);
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method runs queries of creating tables in database
     */
    public static void createTables() {
        List<String> queriesList = getListOfTables();
        try {
            for (String item : queriesList) {
                DataBaseUtil.runQuery(item);
            }
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method called by createTables() method
     * @return list of all queries that create system's tables
     */
    private static List<String> getListOfTables() {
        List<String> list = new ArrayList<>();
        list.add(DataBaseManager.CREATE_COMPANIES_TABLE);
        list.add(CREATE_CUSTOMERS_TABLE);
        list.add(CREATE_CATEGORIES_TABLE);
        list.add(CREATE_COUPONS_TABLE);
        list.add(CREATE_CUSTOMERS_VS_COUPONS_TABLE);
        list.add(CREATE_CATEGORIES);
        return list;
    }

}

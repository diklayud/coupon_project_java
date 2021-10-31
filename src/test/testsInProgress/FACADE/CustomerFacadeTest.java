package test.testsInProgress.FACADE;

import enums.Category;
import beans.Coupon;
import beans.Customer;
import exceptions.CouponSystemException;
import facade.CustomerFacade;

import java.sql.Date;
import java.util.List;

public class CustomerFacadeTest {

    public static void main(String[] args) {

        CustomerFacade customerFacade = new CustomerFacade();

        // =========== LOGIN ===========
        // log in with right password and wrong email - throws exception - SUCCESS
        boolean isLogged3 = customerFacade.login("c3@custir!!", "c2c2");
        System.out.println(isLogged3);

        // log in with right email and wrong password - throws exception - SUCCESS
        boolean isLogged2 = customerFacade.login("c3@custir", "c2c2!");
        System.out.println(isLogged2);

        // log in with right email and password - customer id in facade updated - SUCCESS
        boolean isLogged = customerFacade.login("c3@custir", "c2c2");
        System.out.println(isLogged);


        // =========== ADD COUPON PURCHASE ===========
//        testCOUPONPURCHASE(customerFacade);


        // =========== GET CUSTOMER COUPONS BY CUSTOMER ID ===========
//        testGetCustomerCoupons(customerFacade);

        // =========== GET CUSTOMER COUPONS BY CATEGORY ===========
//        testGetCustomerCouponsByCategory(customerFacade);

        // =========== GET CUSTOMER COUPONS BY MAX PRICE ===========
        testGetCustomerCouponsByMaximumPrice(customerFacade);

        // =========== GET CUSTOMER DETAILS ===========
        // get details of logged in customer
        testGetCustomerDetails(customerFacade);


    }

    private static void testGetCustomerDetails(CustomerFacade customerFacade) {
        Customer customer = customerFacade.getCustomerDetails();
        System.out.println(customer);
        if (customer.getId() == 2) {
            System.out.println("test get customer details passed successfully");
        } else {
            System.out.println("test get customer details failed");
        }
    }

    private static void testGetCustomerCouponsByMaximumPrice(CustomerFacade customerFacade) {
        // price with 0 coupons
        List<Coupon> chipCoupons = customerFacade.getCustomerCouponsByMaximumPrice(15);
        if (chipCoupons.size() == 0) {
            System.out.println("test get coupons by maximum price passed successfully");
        } else {
            System.out.println("test get coupons by maximum price failed");
        }

        // price with 2 coupons
        List<Coupon> mediumCoupons = customerFacade.getCustomerCouponsByMaximumPrice(70.15);
        if (mediumCoupons.size() == 3) {
            System.out.println("test get coupons by maximum price passed successfully");
        } else {
            System.out.println("test get coupons by maximum price failed");
        }

        // price with 3 coupons
        List<Coupon> expensiveCoupons = customerFacade.getCustomerCouponsByMaximumPrice(150);
        if (expensiveCoupons.size() == 4) {
            System.out.println("test get coupons by maximum price passed successfully");
        } else {
            System.out.println("test get coupons by maximum price failed");
        }

    }

    private static void testGetCustomerCouponsByCategory(CustomerFacade customerFacade) {
        // Category with 3 coupons
        List<Coupon> couponsOfFood = customerFacade.getCustomerCouponsByCategory(Category.Food);
        if (couponsOfFood.size() == 3) {
            System.out.println("test get coupons by category FOOD passed successfully");
        } else {
            System.out.println("test get coupons by category FOOD failed");
        }

        // Category with 1 coupons
        List<Coupon> couponsOfElectricity = customerFacade.getCustomerCouponsByCategory(Category.Electricity);
        if (couponsOfElectricity.size() == 1) {
            System.out.println("test get coupons by category Electricity passed successfully");
        } else {
            System.out.println("test get coupons by category Electricity failed");
        }

        // Category with 0 coupons
        List<Coupon> couponsOfFashion = customerFacade.getCustomerCouponsByCategory(Category.Fashion);
        if (couponsOfFashion.size() == 0) {
            System.out.println("test get coupons by category Fashion passed successfully");
        } else {
            System.out.println("test get coupons by category Fashion failed");
        }

    }

    private static void testGetCustomerCoupons(CustomerFacade customerFacade) {
        List<Coupon> coupons = customerFacade.getCustomerCoupons();
        coupons.forEach(System.out::println);

        // log in with right email and password - customer id in facade updated - SUCCESS (**customer with no coupons**)
        boolean isLogged = customerFacade.login("cus@mail", "112233");
        System.out.println(isLogged);
        List<Coupon> coupons2 = customerFacade.getCustomerCoupons();
        coupons2.forEach(System.out::println);

    }

    private static void testCouponPurchase(CustomerFacade customerFacade) {
        // purchase coupon COUPON ID NOT EXIST - TROWS EXCEPTION
        try {
            customerFacade.purchaseCoupon(new Coupon(10, 10, Category.Fashion, "zara", "sale", Date.valueOf("2021-06-01"), Date.valueOf("2021-06-30"), 9, 250.9, "zara.img"));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // purchase coupon with today's end date
/*
        try {
            List<Coupon> beforePurchaseList = customerFacade.getCustomerCoupons();
            customerFacade.purchaseCoupon(new Coupon(15, 7, Category.Fashion, "zara", "sale", Date.valueOf("2021-06-01"), Date.valueOf("2021-06-14"), 9, 250.9, "zara.img"));
            List<Coupon> afterPurchaseList = customerFacade.getCustomerCoupons();
        } catch (CouponsException e) {
            System.out.println(e.getMessage());
        }

 */


        // purchase coupon with expired end date - TROWS EXCEPTION
        try {
            customerFacade.purchaseCoupon(new Coupon(16, 7, Category.Fashion, "zara", "sale", Date.valueOf("2021-06-01"), Date.valueOf("2021-06-20"), 9, 250.9, "zara.img"));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // purchase coupon with amount 0 - TROWS EXCEPTION
        try {
            customerFacade.purchaseCoupon(new Coupon(19, 7, Category.Fashion, "zara", "sale", Date.valueOf("2021-06-01"), Date.valueOf("2021-06-20"), 9, 250.9, "zara.img"));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // purchase coupon twice by the same customer
        try {
            customerFacade.purchaseCoupon(new Coupon(18, 7, Category.Fashion, "zara", "sale", Date.valueOf("2021-06-01"), Date.valueOf("2021-06-14"), 9, 250.9, "zara.img"));
            customerFacade.purchaseCoupon(new Coupon(18, 7, Category.Fashion, "zara", "sale", Date.valueOf("2021-06-01"), Date.valueOf("2021-06-14"), 9, 250.9, "zara.img"));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

    }
}

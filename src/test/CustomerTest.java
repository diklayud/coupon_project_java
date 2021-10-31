package test;

import beans.Coupon;
import beans.Customer;
import enums.Category;
import enums.ClientType;
import exceptions.CouponSystemException;
import facade.CustomerFacade;
import loginManager.LoginManager;

import java.sql.Date;
import java.util.List;

public class CustomerTest {

    public static void customerTest() {

        // =========== LOGIN ===========
        // try to login with wrong email. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        LoginManager.getInstance().login("22cust2@mail.com", "222", ClientType.Customer);

        // try to login with wrong password. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        LoginManager.getInstance().login("cust2@mail.com", "212", ClientType.Customer);

        // try to login with right email and password. EXPECTED RESULT ==> SHOULD LOGIN SUCCESSFULLY :)
        CustomerFacade customerFacade = (CustomerFacade) LoginManager.getInstance().login("cust2@mail.com", "222", ClientType.Customer);


        // =========== ADD PURCHASE COUPON ===========
        // try to purchase 6 coupons. EXPECTED RESULT ==> SHOULD ADD SUCCESSFULLY :)
        // add 3 coupons to customer #2
        try {
            customerFacade.purchaseCoupon(new Coupon(1, 4, Category.Food, "10% off!", Date.valueOf("2021-06-26"), Date.valueOf("2022-07-03"), 100));
            customerFacade.purchaseCoupon(new Coupon(3, 3, Category.Restaurant, "fashion loves food", Date.valueOf("2021-06-12"), Date.valueOf("2022-06-30"), 200));
            customerFacade.purchaseCoupon(new Coupon(4, 3, Category.Fashion, "Zara 10% off!", Date.valueOf("2021-06-28"), Date.valueOf("2022-07-01"), 150));

            // login with other customer to add 3 coupons into it
            // add 3 coupons to customer #3
            customerFacade = (CustomerFacade) LoginManager.getInstance().login("cust3@mail.com", "333", ClientType.Customer);
            customerFacade.purchaseCoupon(new Coupon(1, 4, Category.Food, "10% off!", Date.valueOf("2021-06-26"), Date.valueOf("2022-07-03"), 100));
            customerFacade.purchaseCoupon(new Coupon(4, 3, Category.Fashion, "Zara 10% off!", Date.valueOf("2021-06-28"), Date.valueOf("2022-07-01"), 150));
            customerFacade.purchaseCoupon(new Coupon(5, 3, Category.Fashion, "Zara 15% off!", Date.valueOf("2021-06-19"), Date.valueOf("2022-07-02"), 80));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to purchase a coupon with amount ZERO. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        try {
            customerFacade.purchaseCoupon(new Coupon(3, 3, Category.Restaurant, "fashion loves food", Date.valueOf("2021-06-12"), Date.valueOf("2021-06-30"), 200));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to purchase a coupon already purchased by this customer. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        try {
            customerFacade.purchaseCoupon(new Coupon(1, 4, Category.Food, "10% off!", Date.valueOf("2021-06-26"), Date.valueOf("2021-07-03"), 100));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        /*
        // try to purchase a coupon with expired end date. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        // ========== *** can't create the test: don't have expired coupons in DB and
        // can't add expired coupons to DB by using companyFacade.addCoupon.
        // I added the expired coupon directly through DB *** ==========
        try {
            customerFacade.purchaseCoupon(new Coupon(7, 4, Category.Food, "sale", Date.valueOf("2021-06-19"), Date.valueOf("2021-06-26"), 23));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }
         */


        // =========== GET CUSTOMER'S PURCHASED COUPON ===========
        // get all company's coupons from DB. EXPECTED RESULT ==> SHOULD GET THEM SUCCESSFULLY AND SHOW THAT THE COUPONS
        // WITH THE EXISTING VALUES REALLY NOT ADDED TO DB :)
        List<Coupon> couponsPurchased = customerFacade.getCustomerCoupons();
        System.out.println("Is list size 3? " + (couponsPurchased.size() == 3));
        System.out.println("Our LIST of coupons:");
        couponsPurchased.forEach(System.out::println);


        // =========== GET CUSTOMER'S PURCHASED COUPON OF CATEGORY ===========
        // get list of coupons of category this customer has. EXPECTED RESULT ==> SHOULD GET THEM SUCCESSFULLY :)
        List<Coupon> couponsPurchasedByCategory = customerFacade.getCustomerCouponsByCategory(Category.Fashion);
        System.out.println("Is list size 2? " + (couponsPurchasedByCategory.size() == 2));
        System.out.println("Our NEW LIST of coupons:");
        couponsPurchasedByCategory.forEach(System.out::println);

        // get list of coupons of category this customer didn't buy. EXPECTED RESULT ==> RETURNS AN EMPTY LIST
        List<Coupon> vacationCoupons = customerFacade.getCustomerCouponsByCategory(Category.Vacation);
        System.out.println("Is list size 0? " + (vacationCoupons.size() == 0));


        // =========== GET CUSTOMER'S PURCHASED COUPON TO MAXIMUM PRICE ===========
        // get list of coupons of price this customer bought. EXPECTED RESULT ==> SHOULD GET THEM SUCCESSFULLY :)
        List<Coupon> couponsPurchasedByMaximumPrice = customerFacade.getCustomerCouponsByMaximumPrice(95);
        System.out.println("Is list size 1? " + (couponsPurchasedByMaximumPrice.size() == 1));
        System.out.println("Our NEW LIST of coupons:");
        couponsPurchasedByMaximumPrice.forEach(System.out::println);

        // get list of coupons of price this customer didn't buy. EXPECTED RESULT ==> RETURNS AN EMPTY LIST
        List<Coupon> couponsPurchasedByMaximumPrice2 = customerFacade.getCustomerCouponsByMaximumPrice(50);
        System.out.println("Is list size 0? " + (couponsPurchasedByMaximumPrice2.size() == 0));


        // =========== GET CUSTOMER'S DETAILS ===========
        Customer customer = customerFacade.getCustomerDetails();
        System.out.println(customer);
        System.out.println("Is customer's id 3? " + (customer.getId() == 3));


    }
}

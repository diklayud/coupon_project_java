package test.testsInProgress.FACADE;

import beans.Coupon;
import enums.Category;
import exceptions.CouponSystemException;
import facade.CompanyFacade;

import java.sql.Date;

public class CompanyFacadeTest {

    public static void main(String[] args) {

        CompanyFacade companyFacade = new CompanyFacade();

        // =========== LOGIN ===========
        // log in with right email and password - SUCCESS
        System.out.println(companyFacade.login("new@email.com", "newPass123"));

        // log in with right email and wrong password - SUCCESS
//        System.out.println(companyFacade.login("comp@a", "111!"));

        // log in with right password and wrong email - SUCCESS
//        System.out.println(companyFacade.login("comp@a!", "111"));


//        try {
        // =========== ADD COUPON ===========
        // try to add a coupon with end date of today. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        try {
            companyFacade.addCoupon(new Coupon(1, Category.Fashion, "Mango 8% off!",
                    "men shirts", Date.valueOf("2021-06-10"), Date.valueOf("2021-06-28"), 10,
                    40, "shirt.img"));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // add coupon with not existing title - coupon added - SUCCESS
//            companyFacade.addCoupon(new Coupon(7, Category.Food, "food", "loveFoody", Date.valueOf("2021-06-18"),
//                    Date.valueOf("2021-06-25"), 3, 55.7, "food.img"));

        // add coupon with existing title under same company - coupon not added - SUCCESS
//            companyFacade.addCoupon(new Coupon(companyFacade.getCompanyID(), Category.Food, "food", "loveFoody!", Date.valueOf("2021-06-16"),
//                    Date.valueOf("2021-06-29"), 7, 50.7, "foody!.img"));

        // add coupon with existing title under other company - coupon added - SUCCESS
//            companyFacade.addCoupon(new Coupon(companyFacade.getCompanyID(), Category.Food, "groceries", "loveFoody!", Date.valueOf("2021-06-16"),
//                    Date.valueOf("2021-06-29"), 7, 50.7, "foody!.img"));

        // add coupon with passed end date - coupon not added - SUCCESS
//            companyFacade.addCoupon(new Coupon(companyFacade.getCompanyID(), Category.Food, "groceries!", "loveFoody!", Date.valueOf("2021-06-16"),
//                    Date.valueOf("2021-06-09"), 7, 50.7, "foody!.img"));


        // =========== UPDATE COUPON ===========
        // update coupon - happy case :)
//            List<Coupon> coupons = companyFacade.getCompanyCoupons();
//            Coupon coupon = coupons.get(0);
//            System.out.println(coupon);
//          coupon.setAmount(7);
//          coupon.setImage("tv.img");
//          coupon.setDescription("new tv");
//          coupon.setPrice(70);
//        companyFacade.updateCoupon(coupon);

//            List<Coupon> coupons2 = companyFacade.getCompanyCoupons();
//            Coupon couponAfterUpdate = coupons2.get(0);
//            System.out.println(couponAfterUpdate);

        // update company id - SHOULD THROW EXCEPTION (company id does not exist) - SUCCESS
//            companyFacade.updateCoupon(new Coupon(1000, Category.Food, "groceries!", "loveFoody!", Date.valueOf("2021-06-16"),
//                    Date.valueOf("2021-06-09"), 7, 50.7, "foody!.img"));

        // update coupon id - SHOULD THROW EXCEPTION (coupon id dose not exist) - SUCCESS
//            companyFacade.updateCoupon(new Coupon(7, 6, Category.Food, "groceries!", "loveFoody!", Date.valueOf("2021-06-16"),
//                    Date.valueOf("2021-06-09"), 7, 50.7, "foody!.img"));

        // update coupon title to existing title under other company - should update no problems - SUCCESS
//            Coupon couponToUpdate = coupons2.get(1);
//            System.out.println(couponToUpdate);
//            couponToUpdate.setTitle("food");
//            companyFacade.updateCoupon(couponToUpdate);

        // update coupon title to existing title under same company - SHOULD THROW EXCEPTION - SUCCESS
//            Coupon couponToUpdate = coupons2.get(2);
//            System.out.println(couponToUpdate);
//            couponToUpdate.setTitle("food");
//            companyFacade.updateCoupon(couponToUpdate);

        // update coupon end date to future date - should update no problems - SUCCESS
//            Coupon couponToUpdate = coupons2.get(2);
//            System.out.println(couponToUpdate);
//            couponToUpdate.setEndDate(Date.valueOf("2021-07-01"));
//            companyFacade.updateCoupon(couponToUpdate);

        // update coupon end date to passed date - SHOULD THROW EXCEPTION - SUCCESS
//            Coupon couponToUpdate = coupons2.get(1);
//            System.out.println(couponToUpdate);
//            couponToUpdate.setEndDate(Date.valueOf("2021-06-13"));
//            companyFacade.updateCoupon(couponToUpdate);


        // =========== DELETE COUPON ===========
        // delete exist coupon of logged in company (check if coupon purchases are deleted also) - SUCCESS
//        companyFacade.deleteCoupon(14);

        // delete exist coupon of other company - SHOULD THROW EXCEPTION - SUCCESS
//        companyFacade.deleteCoupon(12);

        // delete not exist coupon - SHOULD THROW EXCEPTION - SUCCESS
//        companyFacade.deleteCoupon(1);


        // =========== GET COMPANY COUPONS BY COMPANY ID ===========
        // get coupons of logged in company - SUCCESS
//            List<Coupon> coupons = companyFacade.getCompanyCoupons();
//            coupons.forEach(System.out::println);

        // get coupons of not existing company - CANNOT CHECK, I DON'T INSERT THE COMPANY ID

        // get coupons of company with no coupons - SUCCESS (logged in with company that doesn't have coupons)
//        List<Coupon> coupons = companyFacade.getCompanyCoupons();
//        System.out.println(coupons);
//        coupons.forEach(System.out::println);

        // get coupons of company with no coupons - SUCCESS
//        List<Coupon> coupons = companyFacade.getCompanyCoupons();
//        System.out.println(coupons);


        // =========== GET COMPANY COUPONS BY CATEGORY ===========
        // get coupons of logged in company - SUCCESS
//        List<Coupon> couponsElec = companyFacade.getCompanyCouponsByCategory(Category.Electricity);
//        System.out.println(couponsElec);
//        List<Coupon> couponsRest = companyFacade.getCompanyCouponsByCategory(Category.Restaurant);
//        System.out.println(couponsRest);
//        List<Coupon> couponsFood = companyFacade.getCompanyCouponsByCategory(Category.Food);
//        couponsFood.forEach(System.out::println);

        // get coupons of company with no coupons - SUCCESS
//        List<Coupon> coupons2 = companyFacade.getCompanyCouponsByCategory(Category.Food);
//        System.out.println(coupons2);


        // =========== GET COMPANY COUPONS BY MAX PRICE ===========
        // get coupons of logged in company - SUCCESS
//        List<Coupon> coupons1 = companyFacade.getCompanyCouponsByMaxPrice(40);
//        coupons1.forEach(System.out::println);
//        List<Coupon> coupons2 = companyFacade.getCompanyCouponsByMaxPrice(80);
//        coupons2.forEach(System.out::println);
//        List<Coupon> coupons3 = companyFacade.getCompanyCouponsByMaxPrice(140);
//        coupons3.forEach(System.out::println);

        // get coupons of company with no coupons - SUCCESS
//        companyFacade.getCompanyCouponsByMaxPrice(40);


        // =========== GET COMPANY DETAILS ===========
        // get details of logged in company
//        Company company = companyFacade.getCompanyDetails();
//        System.out.println(company);

//        } catch (CouponsException e) {
//            System.out.println(e.getMessage());
//        }

    }
}

package test;

import beans.Company;
import beans.Coupon;
import enums.Category;
import enums.ClientType;
import exceptions.CouponSystemException;
import facade.CompanyFacade;
import loginManager.LoginManager;

import java.sql.Date;
import java.util.List;

public class CompanyTest {

    public static void companyTest() {

        // =========== LOGIN ===========
        // try to login with wrong email. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        LoginManager.getInstance().login("44comp4@d.com", "comp4", ClientType.Company);

        // try to login with wrong password. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        LoginManager.getInstance().login("comp4@d.com", "44comp4", ClientType.Company);

        // try to login with right email and password. EXPECTED RESULT ==> SHOULD LOGIN SUCCESSFULLY :)
        CompanyFacade companyFacade = (CompanyFacade) LoginManager.getInstance().login("comp4@d.com",
                "comp4", ClientType.Company);


        // =========== ADD COUPON ===========
        // try to add 5 coupons. EXPECTED RESULT ==> SHOULD ADD SUCCESSFULLY :)
        // add 2 coupons to company #4
        try {
            companyFacade.addCoupon(new Coupon(companyFacade.getCompanyID(), Category.Food, "10% off!",
                    "discount in markets", Date.valueOf("2021-06-26"), Date.valueOf("2022-07-03"), 5,
                    100, "groceries.img"));
            companyFacade.addCoupon(new Coupon(companyFacade.getCompanyID(), Category.Food, "Groceries 12% off!",
                    "discount in markets", Date.valueOf("2021-06-25"), Date.valueOf("2022-06-30"), 7,
                    120, "groceries.img"));

            // login with other company to add 3 coupons into it
            // add 3 coupons to company #3
            companyFacade = (CompanyFacade) LoginManager.getInstance().login("comp3@c.com", "comp3", ClientType.Company);
            companyFacade.addCoupon(new Coupon(companyFacade.getCompanyID(), Category.Fashion, "Aldo 20% off!",
                    "men shoes", Date.valueOf("2021-06-12"), Date.valueOf("2022-06-30"), 3,
                    90, "shoes.img"));
            companyFacade.addCoupon(new Coupon(companyFacade.getCompanyID(), Category.Fashion, "Zara 10% off!",
                    "women dresses", Date.valueOf("2021-06-28"), Date.valueOf("2022-07-01"), 3,
                    150, "dress.img"));
            companyFacade.addCoupon(new Coupon(companyFacade.getCompanyID(), Category.Fashion, "Zara 15% off!",
                    "men T-shirts", Date.valueOf("2021-06-19"), Date.valueOf("2022-07-02"), 3,
                    80, "tshirt.img"));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to add a coupon with existing title of coupon of OTHER company. EXPECTED RESULT ==> SHOULD ADD SUCCESSFULLY :)
        try {
            companyFacade.addCoupon(new Coupon(companyFacade.getCompanyID(), Category.Fashion, "10% off!",
                    "women skirts", Date.valueOf("2021-06-25"), Date.valueOf("2022-06-30"), 2,
                    05, "skirt.img"));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to add a coupon with existing title of coupon of THIS company. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        try {
            companyFacade.addCoupon(new Coupon(companyFacade.getCompanyID(), Category.Fashion, "Zara 15% off!",
                    "men pants", Date.valueOf("2021-06-20"), Date.valueOf("2022-07-15"), 10,
                    60, "pants.img"));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to add a coupon with expired end date of coupon. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        try {
            companyFacade.addCoupon(new Coupon(companyFacade.getCompanyID(), Category.Fashion, "Mango 8% off!",
                    "men shirts", Date.valueOf("2021-06-10"), Date.valueOf("2021-06-25"), 10,
                    40, "shirt.img"));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to add a coupon to company with not existing company id in the system.
        // EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        try {
            companyFacade.addCoupon(new Coupon(101, Category.Electricity, "LG TV only 2,000$!",
                    "65' led", Date.valueOf("2021-06-10"), Date.valueOf("2022-06-30"), 3,
                    2000, "tv.img"));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // =========== GET ALL COMPANY'S COUPONS ===========
        // get all company's coupons from DB. EXPECTED RESULT ==> SHOULD GET THEM SUCCESSFULLY AND SHOW THAT THE COUPONS
        // WITH THE EXISTING VALUES REALLY NOT ADDED TO DB :)
        List<Coupon> allCoupons = companyFacade.getCompanyCoupons();
        System.out.println("Is list size 4? " + (allCoupons.size() == 4));
        System.out.println("Our LIST of coupons:");
        allCoupons.forEach(System.out::println);

        // =========== UPDATE COUPON ===========
        // try to update a coupon's details and it's title to a new (not existing) title of coupon of THIS company.
        // EXPECTED RESULT ==> SHOULD UPDATE SUCCESSFULLY :)
        Coupon couponToUpdate1 = allCoupons.get(0);
        couponToUpdate1.setTitle("fashion loves food");
        couponToUpdate1.setPrice(200);
        couponToUpdate1.setDescription("shoes store- get discount in restaurants");
        couponToUpdate1.setAmount(1);
        couponToUpdate1.setImage("shoesFashion.img");
        couponToUpdate1.setCategory(Category.Restaurant);
        couponToUpdate1.setEndDate(Date.valueOf("2023-06-30"));
        try {
            companyFacade.updateCoupon(couponToUpdate1);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to update a coupon's title to existing title of other coupon of THIS company.
        // EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        Coupon couponToUpdate2 = allCoupons.get(1);
        couponToUpdate2.setTitle("Zara 15% off!");
        try {
            companyFacade.updateCoupon(couponToUpdate2);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to update a coupon to expired end date. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        Coupon couponToUpdate3 = allCoupons.get(2);
        couponToUpdate3.setEndDate(Date.valueOf("2021-06-25"));
        try {
            companyFacade.updateCoupon(couponToUpdate3);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to update a coupon with not existing id in the system. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        Coupon couponToUpdate4 = new Coupon(101, 3, Category.Electricity, "TV", "LED", Date.valueOf("2021-07-01"), Date.valueOf("2021-07-20"), 5, 2_500, "tv.img");
        couponToUpdate4.setEndDate(Date.valueOf("2025-07-31"));
        try {
            companyFacade.updateCoupon(couponToUpdate4);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        // try to update a coupon with no companyID existing in the system. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        Coupon couponToUpdate5 = new Coupon(3, 102, Category.Electricity, "TV", "OLED", Date.valueOf("2021-07-02"), Date.valueOf("2021-07-22"), 5, 3_500, "tv.img");
        couponToUpdate5.setEndDate(Date.valueOf("2022-07-25"));
        try {
            companyFacade.updateCoupon(couponToUpdate5);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // =========== GET ALL COMPANY'S COUPONS OF CATEGORY ===========
        // get list of coupons of category this company has. EXPECTED RESULT ==> SHOULD GET THEM SUCCESSFULLY :)
        List<Coupon> fashionCoupons = companyFacade.getCompanyCouponsByCategory(Category.Fashion);
        System.out.println("Is list size 3? " + (fashionCoupons.size() == 3));
        System.out.println("Our NEW LIST of coupons:");
        fashionCoupons.forEach(System.out::println);

        // get list of coupons of category this company doesn't have. EXPECTED RESULT ==> RETURNS AN EMPTY LIST
        List<Coupon> vacationCoupons = companyFacade.getCompanyCouponsByCategory(Category.Vacation);
        System.out.println("Is list size 0? " + (vacationCoupons.size() == 0));


        // =========== GET ALL COMPANY'S COUPONS TO MAXIMUM PRICE ===========
        // get list of coupons of price this company has. EXPECTED RESULT ==> SHOULD GET THEM SUCCESSFULLY :)
        List<Coupon> maxPriceCoupons = companyFacade.getCompanyCouponsByMaxPrice(100);
        System.out.println("Is list size 2? " + (maxPriceCoupons.size() == 2));
        System.out.println("Our NEW LIST of coupons:");
        maxPriceCoupons.forEach(System.out::println);

        // get list of coupons of price this company doesn't have. EXPECTED RESULT ==> RETURNS AN EMPTY LIST
        List<Coupon> maxPriceCoupons2 = companyFacade.getCompanyCouponsByMaxPrice(1);
        System.out.println("Is list size 0? " + (maxPriceCoupons2.size() == 0));


        // =========== DELETE COUPON ===========
        // try to delete a coupon by existing id. EXPECTED RESULT ==> SHOULD DELETE SUCCESSFULLY :)
        companyFacade.deleteCoupon(6);

        // try to delete a coupon by not existing id. EXPECTED RESULT ==> SHOULD THROW EXCEPTION
        companyFacade.deleteCoupon(65);


        // =========== GET ALL COMPANY'S COUPONS ===========
        // get all company's coupons from DB. EXPECTED RESULT ==> SHOULD GET THEM SUCCESSFULLY AND SHOW THE COUPON WITH
        // THE UPDATED DETAILS AND SHOW THAT THE COUPON THAT JUST WAS DELETED IS GONE FROM DB
        List<Coupon> couponsAfterDelete = companyFacade.getCompanyCoupons();
        System.out.println("Is list size 3? " + (couponsAfterDelete.size() == 3));
        System.out.println("Our NEW LIST of coupons:");
        couponsAfterDelete.forEach(System.out::println);


        // =========== GET COMPANY'S DETAILS ===========
        Company company = companyFacade.getCompanyDetails();
        System.out.println(company);
        System.out.println("Is company's id 3? " + (company.getId() == 3));


    }
}

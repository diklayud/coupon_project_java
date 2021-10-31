package test.testsInProgress.DBDAO;

import dbDao.CouponsDBDAO;
import exceptions.CouponSystemException;

public class CouponsDBDAOTest {

    public static void main(String[] args) {

//        System.out.println(Category.values()[0]);

        CouponsDBDAO couponsDBDAO = new CouponsDBDAO();

        try {

//            DataBaseUtil.runQuery(DataBaseManager.CREATE_CATEGORIES);

            // TEST ADD COUPONS
//            couponsDBDAO.addCoupon(new Coupon(6, Category.Electricity, "TV", "PHILIPS AMBILIGHT",
//                    Date.valueOf("2021-06-11"), Date.valueOf("2021-06-21"), 5, 4_000.70, "tvImage"));
//            couponsDBDAO.addCoupon(new Coupon(6, Category.Food, "groceries", "15% OFF!",
//                    Date.valueOf("2021-06-11"), Date.valueOf("2021-06-20"), 15, 100.00, "groceriesImage"));
//            couponsDBDAO.addCoupon(new Coupon(7, Category.Restaurant, "TAVERNA", "30% OFF ON BIERS",
//                    Date.valueOf("2021-06-15"), Date.valueOf("2021-06-25"), 50, 400.87, "TAVERNA_Image"));

            // TEST GET ALL COUPONS
//            List<Coupon> coupons = couponsDBDAO.getAllCoupons();
//            coupons.forEach(System.out::println);

            // TEST GET ALL COUPONS BY COMPANY ID
//            List<Coupon> coupons = couponsDBDAO.getAllCouponsByCompanyId(4);
//            coupons.forEach(System.out::println);

            // TEST GET ALL COUPONS BY COMPANY ID AND CATEGORY ID
//            List<Coupon> coupons = couponsDBDAO.getAllCouponsByCompanyIdAndCategory(6, Category.Electricity);
//            coupons.forEach(System.out::println);

            // TEST GET A SINGLE COUPON
//            Coupon couponExist = couponsDBDAO.getOneCoupon(12);
//            System.out.println(couponExist);
//
//            Coupon couponNotExist = couponsDBDAO.getOneCoupon(13);
//            System.out.println(couponNotExist);

            // TEST UPDATE COUPON
//            coupon.setAmount(2);
//            coupon.setDescription("new Philips");
//            coupon.setPrice(4200);
//            coupon.setImage("philipsImage");
//            coupon.setTitle("television");
//            coupon.setCategory(Category.Electricity);
//            couponsDBDAO.updateCoupon(coupon);

//            coupon = couponsDBDAO.getOneCoupon(3);
//            System.out.println(coupon);

            // TEST DELETE COUPONS
//            couponsDBDAO.deleteCoupon(9);

//            coupon = couponsDBDAO.getOneCoupon(5);
//            System.out.println(coupon);

            // TEST ADD COUPON PURCHASE
//            couponsDBDAO.addCouponPurchase(2, 12); // ADDED SUCCESSFULLY
//            couponsDBDAO.addCouponPurchase(2, 15); // ADDED SUCCESSFULLY
//            couponsDBDAO.addCouponPurchase(4, 15); // ADDED SUCCESSFULLY
//            couponsDBDAO.addCouponPurchase(5, 19); // ADDED SUCCESSFULLY
//            couponsDBDAO.addCouponPurchase(5, 19); // THROWS EXCEPTION-DUPLICATE
//            couponsDBDAO.addCouponPurchase(5, 11); // coupon id not exist. THROWS EXCEPTION-Cannot add or update a child row
//            couponsDBDAO.addCouponPurchase(1, 19); // customer id not exist. THROWS EXCEPTION-Cannot add or update a child row

            // TEST DELETE COUPON PURCHASE
//            couponsDBDAO.deleteCouponPurchase(3, 3);

            // TEST DECREASE COUPON AMOUNT
//            couponsDBDAO.decreaseCouponAmount(12); // exist coupon id - SUCCESS
//            couponsDBDAO.decreaseCouponAmount(11); // not exist coupon id

            // TEST GET COUPON AMOUNT
//            System.out.println(couponsDBDAO.getCouponAmount(12)); // exist coupon id - SUCCESS
//            couponsDBDAO.getCouponAmount(11); // not exist coupon id

            // TEST IS COUPON EXPIRED
//            System.out.println(couponsDBDAO.isCouponExpired(12)); // future date, valid - SUCCESS
//            System.out.println(couponsDBDAO.isCouponExpired(16)); // past date, expired - SUCCESS
//            System.out.println(couponsDBDAO.isCouponExpired(15)); // today's date, valid - SUCCESS

            // TEST DELETE EXPIRED COUPON
            couponsDBDAO.deleteExpiredCoupon();
            System.out.println("me finish :)");

        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

    }
}

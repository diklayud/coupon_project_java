package dao;

import enums.Category;
import beans.Coupon;
import exceptions.CouponSystemException;

import java.util.List;

public interface CouponsDAO {

    void addCoupon(Coupon coupon) throws CouponSystemException;

    void updateCoupon(Coupon coupon);

    void deleteCouponByCouponId(int couponID) throws CouponSystemException;

    void deleteCouponByCouponIdAndCompanyId(int couponID, int companyID) throws CouponSystemException;

    void deleteExpiredCoupon() throws CouponSystemException;

    List<Coupon> getAllCoupons() throws CouponSystemException;

    List<Coupon> getAllCouponsByCompanyId(int companyId) throws CouponSystemException;

    List<Coupon> getAllCouponsByCompanyIdAndCategory(int companyId, Category category) throws CouponSystemException;

    List<Coupon> getAllCouponsPurchasesByCustomerID(int customerID) throws CouponSystemException;

    List<Coupon> getAllCouponsPurchasesByCustomerIDAndCategory(int customerID, Category category) throws CouponSystemException;

    List<Coupon> getAllCouponsPurchasesByCustomerIDAndMaximumPrice(int customerID, double maximumPrice) throws CouponSystemException;

    List<Coupon> getAllCouponsByCompanyIdMaximumPrice(int companyId, double maxPrice) throws CouponSystemException;

    Coupon getOneCoupon(int couponID) throws CouponSystemException;

    void addCouponPurchase(int customerID, int couponID) throws CouponSystemException;

    void deleteCouponPurchase(int customerID, int couponID) throws CouponSystemException;

    boolean isCouponTitleExistUnderCompany(String couponTitle, int companyId) throws CouponSystemException;

    int getCouponAmount(int couponID) throws CouponSystemException;

    boolean isCouponExpired(int couponID) throws CouponSystemException;

    void decreaseCouponAmount(int couponID) throws CouponSystemException;

    boolean isCouponAlreadyPurchased(int customerID, int couponID) throws CouponSystemException;
}

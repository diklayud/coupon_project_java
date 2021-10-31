package facade;

import enums.Category;
import beans.Coupon;
import beans.Customer;
import dbDao.CouponsDBDAO;
import dbDao.CustomersDBDAO;
import exceptions.CouponSystemException;

import java.util.ArrayList;
import java.util.List;

public class CustomerFacade extends ClientFacade {

    /**
     * ID of the logged in customer
     */
    private int customerID;

    /**
     * Constructor which initializes CustomersDBDAO and CouponsDBDAO
     */
    public CustomerFacade() {
        customersDAO = new CustomersDBDAO();
        couponsDAO = new CouponsDBDAO();
    }

    /**
     * Login to the system as customer, updates the customerID field with data from database
     *
     * @param email
     * @param password
     * @return true if data sent to method is correct and logged in successfully, false if not
     */
    @Override
    public boolean login(String email, String password) {
        try {
            int id = customersDAO.getCustomerIdByEmailAndPassword(email, password);
            if (id != -1) {
                this.customerID = id;
                return true;
            }
            throw new CouponSystemException("COULDN'T LOG IN, INCORRECT EMAIL OR PASSWORD");
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Add coupon purchase by customer after checking couponID sent to the method exist in database,
     * the coupon is not expired, coupon's amount is not zero and the customer didn't buy this coupon already.
     * After adding the purchase, decrease coupon amount by 1.
     *
     * @param coupon
     * @throws CouponSystemException
     */
    public void purchaseCoupon(Coupon coupon) throws CouponSystemException {
        if (couponsDAO.getOneCoupon(coupon.getId()) == null) {
            throw new CouponSystemException("ERROR IN PURCHASING: COUPON ID NOT EXIST");
        }
        if (couponsDAO.isCouponExpired(coupon.getId())) {
            throw new CouponSystemException("ERROR IN PURCHASING: COUPON IS EXPIRED");
        }
        if (couponsDAO.getCouponAmount(coupon.getId()) > 0) {
            if (couponsDAO.isCouponAlreadyPurchased(this.customerID, coupon.getId())) {
                throw new CouponSystemException("ERROR IN PURCHASING: DUPLICATE PURCHASING");
            }
            couponsDAO.addCouponPurchase(this.customerID, coupon.getId()); // can't buy coupon more than once by the same customer
            couponsDAO.decreaseCouponAmount(coupon.getId());
        } else {
            throw new CouponSystemException("ERROR IN PURCHASING: COUPON AMOUNT IS ZERO");
        }
    }

    /**
     * Get the coupons of the logged in customer from database
     *
     * @return list of coupons of the logged in customer
     */
    public List<Coupon> getCustomerCoupons() {
        try {
            List<Coupon> coupons = couponsDAO.getAllCouponsPurchasesByCustomerID(this.customerID);
            if (coupons.size() == 0) {
                System.out.printf("no coupons found for customer #%d\n", this.customerID);
            }
            return coupons;
        } catch (CouponSystemException e) {
            System.out.println("ERROR IN GETTING CUSTOMER'S COUPONS: " + e.getMessage());
            return new ArrayList<Coupon>();
        }
    }

    /**
     * Get the coupons of specific category of the logged in customer from database
     *
     * @param category
     * @return list of coupons by specific category of the logged in customer or an empty list
     * if no coupons found for the customer
     */
    public List<Coupon> getCustomerCouponsByCategory(Category category) {
        try {
            List<Coupon> coupons = couponsDAO.getAllCouponsPurchasesByCustomerIDAndCategory(this.customerID, category);
            if (coupons.size() == 0) {
                System.out.printf("no coupons found for customer #%d\n", this.customerID);
            }
            return coupons;
        } catch (CouponSystemException e) {
            System.out.println("ERROR IN GETTING CUSTOMER'S COUPONS: " + e.getMessage());
            return new ArrayList<Coupon>();
        }
    }

    /**
     * Get the coupons under maximum price of the logged in customer from database
     *
     * @param maximumPrice
     * @return list of coupons under maximum price of the logged in customer or an empty list
     * if no coupons found for the customer
     */
    public List<Coupon> getCustomerCouponsByMaximumPrice(double maximumPrice) {
        try {
            List<Coupon> coupons = couponsDAO.getAllCouponsPurchasesByCustomerIDAndMaximumPrice(this.customerID, maximumPrice);
            if (coupons.size() == 0) {
                System.out.printf("no coupons found for customer #%d\n", this.customerID);
            }
            return coupons;
        } catch (CouponSystemException e) {
            System.out.println("ERROR IN GETTING CUSTOMER'S COUPONS: " + e.getMessage());
            return new ArrayList<Coupon>();
        }
    }

    /**
     * Get the logged in customer
     *
     * @return instance of the logged in customer
     */
    public Customer getCustomerDetails() {
        try {
            return customersDAO.getOneCustomerByID(this.customerID);
        } catch (CouponSystemException e) {
            System.out.println("ERROR IN GETTING CUSTOMER'S DETAILS: " + e.getMessage());
            return new Customer();
        }
    }

}

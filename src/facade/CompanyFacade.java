package facade;

import enums.Category;
import beans.Company;
import beans.Coupon;
import dbDao.CompaniesDBDAO;
import dbDao.CouponsDBDAO;
import exceptions.CouponSystemException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CompanyFacade extends ClientFacade {

    /**
     * ID of the logged in company
     */
    private int companyID;

    /**
     * Constructor which initializes CompaniesDBDAO and CouponsDBDAO
     */
    public CompanyFacade() {
        companiesDAO = new CompaniesDBDAO();
        couponsDAO = new CouponsDBDAO();
    }

    public int getCompanyID() {
        return companyID;
    }

    /**
     * Login to the system as company, updates the companyID field with data from database
     *
     * @param email
     * @param password
     * @return true if data sent to method is correct and logged in successfully, false if not
     */
    @Override
    public boolean login(String email, String password) {
        try {
            int checkID = companiesDAO.getCompanyIdByEmailAndPassword(email, password);
            if (checkID != -1) {
                this.companyID = checkID;
                return true;
            }
            throw new CouponSystemException("Couldn't login, incorrect email or password");
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Add new coupon to database if companyID of the coupon sent to method is equal
     * to the id of the logged in company, and the company doesn't have other coupon
     * with the same title and the coupon's end date is not expired
     *
     * @param coupon
     * @throws CouponSystemException
     */
    public void addCoupon(Coupon coupon) throws CouponSystemException {
        if (this.companyID != coupon.getCompanyId()) {
            throw new CouponSystemException("Cannot add coupon - coupon's company id is different of the logged-in company id!");
        }
        if (couponsDAO.isCouponTitleExistUnderCompany(coupon.getTitle(), this.companyID)) {
            throw new CouponSystemException("Cannot add coupon - coupon's title already exists under this company!");
        }
        if (!coupon.getEndDate().after(new Date(System.currentTimeMillis()))) {
            throw new CouponSystemException("Cannot add coupon - coupon's end date already passed!");
        }
        couponsDAO.addCoupon(coupon);
    }

    /**
     * Update coupon's details in database if companyID of the coupon sent to method is equal
     * to the id of the logged in company, and the coupon id is in the database, and the company
     * doesn't have other coupon with the same title and the coupon's end date is not expired
     *
     * @param coupon
     * @throws CouponSystemException
     */
    public void updateCoupon(Coupon coupon) throws CouponSystemException {
        if (this.companyID != coupon.getCompanyId()) {
            throw new CouponSystemException("Cannot update coupon - coupon's company id is different of the logged-in company id!");
        }
        Coupon couponToUpdate = couponsDAO.getOneCoupon(coupon.getId());
        if (couponToUpdate == null) {
            throw new CouponSystemException("Cannot update coupon - coupon id not exists!");
        }
        if (!couponToUpdate.getTitle().equals(coupon.getTitle())) {
            if (couponsDAO.isCouponTitleExistUnderCompany(coupon.getTitle(), companyID)) {
                throw new CouponSystemException("Cannot update coupon - coupon title already exists under this company!");
            }
        }
        if (!couponToUpdate.getEndDate().equals(coupon.getEndDate())) {
            if (coupon.getEndDate().after(new Date(System.currentTimeMillis()))) {
                couponToUpdate.setEndDate(coupon.getEndDate());
            } else {
                throw new CouponSystemException("Cannot update coupon - coupon's end date already passed");
            }
        }
        couponToUpdate.setTitle(coupon.getTitle());
        couponToUpdate.setCategory(coupon.getCategory());
        couponToUpdate.setImage(coupon.getImage());
        couponToUpdate.setPrice(coupon.getPrice());
        couponToUpdate.setDescription(coupon.getDescription());
        couponToUpdate.setAmount(coupon.getAmount());
        couponsDAO.updateCoupon(couponToUpdate);
    }

    /**
     * Delete coupon from database and its purchases
     *
     * @param couponID
     */
    public void deleteCoupon(int couponID) {
        try {
            couponsDAO.deleteCouponByCouponIdAndCompanyId(couponID, this.companyID);
        } catch (CouponSystemException e) {
            System.out.println("Error in deleting coupon: " + e.getMessage());
        }
    }

    /**
     * Get the coupons of the logged in company from database
     *
     * @return list of coupons of the logged in company
     */
    public List<Coupon> getCompanyCoupons() {
        try {
            List<Coupon> coupons = couponsDAO.getAllCouponsByCompanyId(this.companyID);
            if (coupons.size() == 0) {
                System.out.printf("No coupons found for company #%d\n", this.companyID);
            }
            return coupons;
        } catch (CouponSystemException e) {
            System.out.println("Error in getting company's coupons: " + e.getMessage());
            return new ArrayList<Coupon>();
        }
    }

    /**
     * Get the coupons of specific category of the logged in company from database
     *
     * @param category
     * @return list of coupons by specific category of the logged in company or an empty list
     * if no coupons found for the company
     */
    public List<Coupon> getCompanyCouponsByCategory(Category category) {
        try {
            List<Coupon> coupons = couponsDAO.getAllCouponsByCompanyIdAndCategory(this.companyID, category);
            if (coupons.size() == 0) {
                System.out.printf("No coupons found for company #%d\n", this.companyID);
            }
            return coupons;
        } catch (CouponSystemException e) {
            System.out.println("Error in getting company's coupons: " + e.getMessage());
            return new ArrayList<Coupon>();
        }
    }

    /**
     * Get the coupons under maximum price of the logged in company from database
     *
     * @param maxPrice
     * @return list of coupons under maximum price of the logged in company or an empty list
     * if no coupons found for the company
     */
    public List<Coupon> getCompanyCouponsByMaxPrice(double maxPrice) {
        try {
            List<Coupon> coupons = couponsDAO.getAllCouponsByCompanyIdMaximumPrice(this.companyID, maxPrice);
            if (coupons.size() == 0) {
                System.out.printf("No coupons found for company #%d\n", this.companyID);
            }
            return coupons;
        } catch (CouponSystemException e) {
            System.out.println("Error in getting company's coupons: " + e.getMessage());
            return new ArrayList<Coupon>();
        }
    }

    /**
     * Get the logged in company
     *
     * @return instance of the logged in company
     */
    public Company getCompanyDetails() {
        try {
            return companiesDAO.getOneCompanyByID(this.companyID);
        } catch (CouponSystemException e) {
            System.out.println("Error in getting company's details: " + e.getMessage());
            return new Company();
        }
    }

}

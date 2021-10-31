package thread;

import dao.CouponsDAO;
import dbDao.CouponsDBDAO;
import exceptions.CouponSystemException;

public class CouponExpirationDailyJob implements Runnable {

    private boolean quit;
    private CouponsDAO couponsDAO;

    public CouponExpirationDailyJob() {
        couponsDAO = new CouponsDBDAO();
        this.quit = false;
    }

    @Override
    public void run() {
        while (!quit) {
            try {
                System.out.println("Thread run looking for expired coupons!");
                // find expired coupons end delete them
                couponsDAO.deleteExpiredCoupon();
            } catch (CouponSystemException e) {
                System.out.println("problem in deleting expired coupon: " + e.getMessage());
            }
            try {
                Thread.sleep(1000 * 60 * 60 * 24); // runs every 24 hours
            } catch (InterruptedException e) {
                System.out.println("Daily job has been stopped: " + e.getMessage());
                stop();
            }
        }
    }

    public void stop() {
        this.quit = true;
    }

}

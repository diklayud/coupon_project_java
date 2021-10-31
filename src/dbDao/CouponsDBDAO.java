package dbDao;

import enums.Category;
import beans.Coupon;
import dao.CouponsDAO;
import exceptions.CouponSystemException;
import sql.ConnectionPool;
import sql.DataBaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouponsDBDAO implements CouponsDAO {

    private final String ADD_COUPON = "INSERT INTO `couponSystem`.`coupons` (`company_id`, `category_id`, `title`, " +
            "`description`, `start_date`, `end_date`, `amount`, `price`, `image`) VALUES (?,?,?,?,?,?,?,?,?);";
    private final String UPDATE_COUPON = "UPDATE `couponSystem`.`coupons` SET company_id=?, category_id=?, title=?, " +
            " description=?, start_date=?, end_date=?, amount=?, price=?, image=? WHERE coupon_id=?;";
    private final String DELETE_COUPON_BY_COUPON_ID = "DELETE FROM `couponSystem`.`coupons` WHERE coupon_id=?;";
    private final String DELETE_COUPON_BY_COUPON_ID_AND_COMPANY_ID = "DELETE FROM `couponSystem`.`coupons` WHERE coupon_id=? and company_id=?;";
    private final String GET_ALL_COUPONS = "SELECT * FROM `couponSystem`.`coupons`";
    private final String GET_ALL_COUPONS_BY_COMPANY_ID = "SELECT * FROM `couponSystem`.`coupons` WHERE company_id=?;";
    private final String GET_ALL_COUPONS_BY_COMPANY_ID_AND_CATEGORY = "SELECT * FROM `couponSystem`.`coupons` WHERE" +
            " company_id=? and category_id=?;";
    private final String GET_ALL_COUPONS_BY_COMPANY_ID_AND_MAXIMUM_PRICE = "SELECT * FROM `couponSystem`.`coupons` WHERE" +
            " company_id=? and price<?;";
    private final String GET_ONE_COUPON = "SELECT * FROM `couponSystem`.`coupons` WHERE coupon_id=?;";
    private final String ADD_COUPON_PURCHASE = "INSERT INTO `couponSystem`.`customers_vs_coupons`" +
            " (`customer_id`, `coupon_id`) VALUES (?,?);";
    private final String DELETE_COUPON_PURCHASE = "DELETE FROM `couponSystem`.`customers_vs_coupons` WHERE " +
            " customer_id=? and coupon_id=?;";
    private final String GET_ALL_COUPONS_PURCHASES_BY_CUSTOMER_ID = "SELECT * FROM `couponSystem`.`coupons` " +
            " WHERE `coupon_id` IN (SELECT `coupon_id` FROM `couponSystem`.`customers`, `couponSystem`.`customers_vs_coupons` " +
            " WHERE customers.id = customer_id and customer_id =?);";
    private final String GET_ALL_COUPONS_PURCHASES_BY_CUSTOMER_ID_AND_CATEGORY = "SELECT * FROM `couponSystem`.`coupons` " +
            " WHERE `coupon_id` IN (SELECT `coupon_id` FROM `couponSystem`.`customers`, `couponSystem`.`customers_vs_coupons` " +
            " WHERE customers.id = customer_id and customer_id =?) and category_id=?;";
    private final String GET_ALL_COUPONS_PURCHASES_BY_CUSTOMER_ID_AND_MAXIMUM_PRICE = "SELECT * FROM `couponSystem`.`coupons` " +
            " WHERE `coupon_id` IN (SELECT `coupon_id` FROM `couponSystem`.`customers`, `couponSystem`.`customers_vs_coupons` " +
            " WHERE customers.id = customer_id and customer_id =?) and price<?;";
    private final String IS_COUPON_TITLE_EXIST = "SELECT count(*) FROM `couponSystem`.`coupons` WHERE title=? and company_id=?;";
    private final String GET_COUPON_AMOUNT = "SELECT amount FROM `couponSystem`.`coupons` WHERE coupon_id=?;";
    private final String GET_COUPON_END_DATE = "SELECT end_date FROM `couponSystem`.`coupons` WHERE coupon_id=?;";
    private final String UPDATE_COUPON_AMOUNT = "UPDATE `couponSystem`.`coupons` SET amount=amount-1 WHERE coupon_id=?;";
    private final String IS_COUPON_PURCHASED_BY_CUSTOMER = "SELECT count(*) FROM `couponSystem`.`customers_vs_coupons`" +
            " WHERE customer_id=? and coupon_id=?;";
    private final String DELETE_EXPIRED_COUPON = "DELETE FROM `couponSystem`.`coupons` WHERE end_date<?;";

    Connection connection = null;

    /**
     * Add a coupon to database
     *
     * @param coupon the data of this instance will be sent to database
     * @throws CouponSystemException
     */
    @Override
    public void addCoupon(Coupon coupon) throws CouponSystemException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, coupon.getCompanyId());
        params.put(2, coupon.getCategory().ordinal() + 1);
        params.put(3, coupon.getTitle());
        params.put(4, coupon.getDescription());
        params.put(5, coupon.getStartDate());
        params.put(6, coupon.getEndDate());
        params.put(7, coupon.getAmount());
        params.put(8, coupon.getPrice());
        params.put(9, coupon.getImage());
        DataBaseUtil.runPlaceHolderQuery(ADD_COUPON, params);
    }

    /**
     * Update coupon's data in database
     *
     * @param coupon with the new details to update
     */
    @Override
    public void updateCoupon(Coupon coupon) {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, coupon.getCompanyId());
        params.put(2, coupon.getCategory().ordinal() + 1);
        params.put(3, coupon.getTitle());
        params.put(4, coupon.getDescription());
        params.put(5, coupon.getStartDate());
        params.put(6, coupon.getEndDate());
        params.put(7, coupon.getAmount());
        params.put(8, coupon.getPrice());
        params.put(9, coupon.getImage());
        params.put(10, coupon.getId());
        try {
            DataBaseUtil.runPlaceHolderQuery(UPDATE_COUPON, params);
        } catch (CouponSystemException e) {
            System.out.println("error in executing: " + e.getMessage());
        }
    }

    /**
     * Delete a coupon from database
     *
     * @param couponID to delete by
     * @throws CouponSystemException
     */
    @Override
    public void deleteCouponByCouponId(int couponID) throws CouponSystemException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_COUPON_BY_COUPON_ID);
            statement.setInt(1, couponID);
            if (!statement.execute()) {
                throw new CouponSystemException("NO COUPON DELETED - NO SUCH COUPON ID FOUND");
            }
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException(e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Delete expired coupon(s) from database
     *
     * @throws CouponSystemException
     */
    @Override
    public void deleteExpiredCoupon() throws CouponSystemException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_EXPIRED_COUPON);
            statement.setDate(1, new Date(System.currentTimeMillis()));
            statement.execute();
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException(e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Delete a coupon from database
     *
     * @param couponID  to delete by
     * @param companyID to delete by
     * @throws CouponSystemException
     */
    @Override
    public void deleteCouponByCouponIdAndCompanyId(int couponID, int companyID) throws CouponSystemException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_COUPON_BY_COUPON_ID_AND_COMPANY_ID);
            statement.setInt(1, couponID);
            statement.setInt(2, companyID);
            int ans = statement.executeUpdate();
            if (ans == 0) {
                throw new CouponSystemException("NO COUPON DELETED - NO SUCH COUPON ID FOUND");
            }
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't delete coupon because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Get all coupons from database
     *
     * @return list of coupons from database
     * @throws CouponSystemException
     */
    @Override
    public List<Coupon> getAllCoupons() throws CouponSystemException {
        List<Coupon> coupons = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_COUPONS);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Coupon coupon = new Coupon(resultSet.getInt("coupon_id"),
                        resultSet.getInt("company_id"),
                        Category.values()[resultSet.getInt("category_id") - 1],
                        resultSet.getString("title"), resultSet.getString("description"),
                        resultSet.getDate("start_date"), resultSet.getDate("end_date"),
                        resultSet.getInt("amount"), resultSet.getDouble("price"),
                        resultSet.getString("image"));
                coupons.add(coupon);
            }
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get all coupons because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
        return coupons;
    }

    /**
     * Get all coupons of certain company from database
     *
     * @param companyId to search by
     * @return list of company's coupons from database
     * @throws CouponSystemException
     */
    @Override
    public List<Coupon> getAllCouponsByCompanyId(int companyId) throws CouponSystemException {
        List<Coupon> coupons = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_COUPONS_BY_COMPANY_ID);
            statement.setInt(1, companyId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Coupon coupon = new Coupon(resultSet.getInt("coupon_id"),
                        resultSet.getInt("company_id"),
                        Category.values()[resultSet.getInt("category_id") - 1],
                        resultSet.getString("title"), resultSet.getString("description"),
                        resultSet.getDate("start_date"), resultSet.getDate("end_date"),
                        resultSet.getInt("amount"), resultSet.getDouble("price"),
                        resultSet.getString("image"));
                coupons.add(coupon);
            }
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get all coupons because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
        return coupons;
    }

    /**
     * Get all coupons of specific category of certain company from database
     *
     * @param companyId to search by
     * @param category  to search by
     * @return list of company's coupons of category from database
     * @throws CouponSystemException
     */
    @Override
    public List<Coupon> getAllCouponsByCompanyIdAndCategory(int companyId, Category category) throws CouponSystemException {
        List<Coupon> coupons = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_COUPONS_BY_COMPANY_ID_AND_CATEGORY);
            statement.setInt(1, companyId);
            statement.setInt(2, category.ordinal() + 1);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Coupon coupon = new Coupon(resultSet.getInt("coupon_id"),
                        resultSet.getInt("company_id"),
                        Category.values()[resultSet.getInt("category_id") - 1],
                        resultSet.getString("title"), resultSet.getString("description"),
                        resultSet.getDate("start_date"), resultSet.getDate("end_date"),
                        resultSet.getInt("amount"), resultSet.getDouble("price"),
                        resultSet.getString("image"));
                coupons.add(coupon);
            }
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get all coupons because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
        return coupons;
    }

    /**
     * Get all coupons of certain company under maximum price from database
     *
     * @param companyId to search by
     * @param maxPrice  to search by
     * @return list of company's coupons under maximum price from database
     * @throws CouponSystemException
     */
    @Override
    public List<Coupon> getAllCouponsByCompanyIdMaximumPrice(int companyId, double maxPrice) throws CouponSystemException {
        List<Coupon> coupons = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_COUPONS_BY_COMPANY_ID_AND_MAXIMUM_PRICE);
            statement.setInt(1, companyId);
            statement.setDouble(2, maxPrice);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Coupon coupon = new Coupon(resultSet.getInt("coupon_id"),
                        resultSet.getInt("company_id"),
                        Category.values()[resultSet.getInt("category_id") - 1],
                        resultSet.getString("title"), resultSet.getString("description"),
                        resultSet.getDate("start_date"), resultSet.getDate("end_date"),
                        resultSet.getInt("amount"), resultSet.getDouble("price"),
                        resultSet.getString("image"));
                coupons.add(coupon);
            }
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get all coupons because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
        return coupons;
    }

    /**
     * Get all coupons of certain customer from database
     *
     * @param customerID to search by
     * @return list of customer's coupons from database
     * @throws CouponSystemException
     */
    @Override
    public List<Coupon> getAllCouponsPurchasesByCustomerID(int customerID) throws CouponSystemException {
        List<Coupon> coupons = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_COUPONS_PURCHASES_BY_CUSTOMER_ID);
            statement.setInt(1, customerID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Coupon coupon = new Coupon(resultSet.getInt("coupon_id"), resultSet.getInt("company_id"),
                        Category.values()[resultSet.getInt("category_id") - 1],
                        resultSet.getString("title"), resultSet.getString("description"),
                        resultSet.getDate("start_date"), resultSet.getDate("end_date"),
                        resultSet.getInt("amount"), resultSet.getDouble("price"),
                        resultSet.getString("image"));
                coupons.add(coupon);
            }
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get all coupons purchases because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
        return coupons;
    }

    /**
     * Get all coupons of specific category of certain customer from database
     *
     * @param customerID to search by
     * @param category   to search by
     * @return list of customer's coupons of category from database
     * @throws CouponSystemException
     */
    @Override
    public List<Coupon> getAllCouponsPurchasesByCustomerIDAndCategory(int customerID, Category category) throws CouponSystemException {
        List<Coupon> coupons = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_COUPONS_PURCHASES_BY_CUSTOMER_ID_AND_CATEGORY);
            statement.setInt(1, customerID);
            statement.setInt(2, category.ordinal() + 1);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Coupon coupon = new Coupon(resultSet.getInt("coupon_id"), resultSet.getInt("company_id"),
                        Category.values()[resultSet.getInt("category_id") - 1],
                        resultSet.getString("title"), resultSet.getString("description"),
                        resultSet.getDate("start_date"), resultSet.getDate("end_date"),
                        resultSet.getInt("amount"), resultSet.getDouble("price"),
                        resultSet.getString("image"));
                coupons.add(coupon);
            }
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get all coupons purchases because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
        return coupons;
    }

    /**
     * Get all coupons of certain customer under maximum price from database
     *
     * @param customerID   to search by
     * @param maximumPrice to search by
     * @return list of customer's coupons under maximum price from database
     * @throws CouponSystemException
     */
    @Override
    public List<Coupon> getAllCouponsPurchasesByCustomerIDAndMaximumPrice(int customerID, double maximumPrice) throws CouponSystemException {
        List<Coupon> coupons = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_COUPONS_PURCHASES_BY_CUSTOMER_ID_AND_MAXIMUM_PRICE);
            statement.setInt(1, customerID);
            statement.setDouble(2, maximumPrice);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Coupon coupon = new Coupon(resultSet.getInt("coupon_id"), resultSet.getInt("company_id"),
                        Category.values()[resultSet.getInt("category_id") - 1],
                        resultSet.getString("title"), resultSet.getString("description"),
                        resultSet.getDate("start_date"), resultSet.getDate("end_date"),
                        resultSet.getInt("amount"), resultSet.getDouble("price"),
                        resultSet.getString("image"));
                coupons.add(coupon);
            }
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get all coupons purchases because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
        return coupons;
    }

    /**
     * Get a single customer from database
     *
     * @param couponID search by it
     * @return a customer
     * @throws CouponSystemException
     */
    @Override
    public Coupon getOneCoupon(int couponID) throws CouponSystemException {
        Coupon coupon = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ONE_COUPON);
            statement.setInt(1, couponID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                coupon = new Coupon(resultSet.getInt("coupon_id"),
                        resultSet.getInt("company_id"),
                        Category.values()[resultSet.getInt("category_id") - 1],
                        resultSet.getString("title"), resultSet.getString("description"),
                        resultSet.getDate("start_date"), resultSet.getDate("end_date"),
                        resultSet.getInt("amount"), resultSet.getDouble("price"),
                        resultSet.getString("image"));
            }
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get the coupon because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
        return coupon;
    }

    /**
     * Add a purchase of coupon by customer to database
     *
     * @param customerID who made the purchase
     * @param couponID   which purchased
     * @throws CouponSystemException
     */
    @Override
    public void addCouponPurchase(int customerID, int couponID) throws CouponSystemException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, customerID);
        params.put(2, couponID);
        DataBaseUtil.runPlaceHolderQuery(ADD_COUPON_PURCHASE, params);
    }

    /**
     * Delete a purchase of coupon by customer from database
     *
     * @param customerID to delete by it
     * @param couponID   to delete by it
     * @throws CouponSystemException
     */
    @Override
    public void deleteCouponPurchase(int customerID, int couponID) throws CouponSystemException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, customerID);
        params.put(2, couponID);
        DataBaseUtil.runPlaceHolderQuery(DELETE_COUPON_PURCHASE, params);
    }

    /**
     * Check if a coupon title already exists under certain company in database
     *
     * @param couponTitle search by it
     * @param companyId   search by it
     * @return true if coupon title already exist under the company, false if not
     * @throws CouponSystemException
     */
    @Override
    public boolean isCouponTitleExistUnderCompany(String couponTitle, int companyId) throws CouponSystemException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(IS_COUPON_TITLE_EXIST);
            statement.setString(1, couponTitle);
            statement.setInt(2, companyId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            boolean isExist = (resultSet.getInt(1) > 0);
            return isExist;
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't find coupon title because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Get amount of certain coupon from database
     *
     * @param couponID search by it
     * @return coupon amount or -1 if coupon not found
     * @throws CouponSystemException
     */
    @Override
    public int getCouponAmount(int couponID) throws CouponSystemException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_COUPON_AMOUNT);
            statement.setInt(1, couponID);
            ResultSet resultSet = statement.executeQuery();
            int amount = -1;
            while (resultSet.next()) {
                amount = resultSet.getInt(1);
            }
            return amount;
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't get coupon's amount because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * decrease by 1 the amount of certain coupon in database
     *
     * @param couponID search by it
     * @throws CouponSystemException
     */
    @Override
    public void decreaseCouponAmount(int couponID) throws CouponSystemException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_COUPON_AMOUNT);
            statement.setInt(1, couponID);
            int ans = statement.executeUpdate();
            if (ans == 0) {
                throw new CouponSystemException("NO SUCH COUPON ID FOUND");
            }
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't decrease coupon's amount because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Check if a certain customer already bought the certain coupon
     *
     * @param customerID search by it
     * @param couponID   search by it
     * @return true if the customer already bought the coupon, false if not
     * @throws CouponSystemException
     */
    @Override
    public boolean isCouponAlreadyPurchased(int customerID, int couponID) throws CouponSystemException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(IS_COUPON_PURCHASED_BY_CUSTOMER);
            statement.setInt(1, customerID);
            statement.setInt(2, couponID);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            boolean isPurchased = (resultSet.getInt(1) > 0);
            return isPurchased;
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't find coupon purchase because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * Check if a coupon is expired
     *
     * @param couponID search by it
     * @return true if coupon is expired, false if not
     * @throws CouponSystemException
     */
    @Override
    public boolean isCouponExpired(int couponID) throws CouponSystemException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_COUPON_END_DATE);
            statement.setInt(1, couponID);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Date endDate = resultSet.getDate(1);
            if (endDate.after(new Date(System.currentTimeMillis())) ||
                    endDate.toLocalDate().equals(new Date(System.currentTimeMillis()).toLocalDate())) {
                return false;
            }
            return true;
        } catch (InterruptedException | SQLException e) {
            throw new CouponSystemException("Couldn't check if coupon is expired because: " + e.getMessage(), e.getCause());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

}

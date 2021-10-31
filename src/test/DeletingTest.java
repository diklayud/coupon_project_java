package test;

import enums.ClientType;
import exceptions.CouponSystemException;
import facade.AdminFacade;
import facade.CompanyFacade;
import loginManager.LoginManager;

public class DeletingTest {

    public static void deleteTest() {

        // =========== LOGIN ===========
        AdminFacade adminFacade = (AdminFacade) LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.Administrator);
        CompanyFacade companyFacade = (CompanyFacade) LoginManager.getInstance().login("comp3@c.com",
                "comp3", ClientType.Company);


        // =========== DELETE COUPON ===========
        // delete a coupon and it's purchasing history. EXPECTED RESULT ==> SHOULD DELETE SUCCESSFULLY :)
        companyFacade.deleteCoupon(3);


        // =========== DELETE CUSTOMER ===========
        // delete a customer and it's purchasing history. EXPECTED RESULT ==> SHOULD DELETE SUCCESSFULLY :)
        try {
            adminFacade.deleteCustomer(3);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        // =========== DELETE COMPANY ===========
        // delete a company and its coupons and its purchasing history. EXPECTED RESULT ==> SHOULD DELETE SUCCESSFULLY :)
        try {
            adminFacade.deleteCompany(4);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

    }
}

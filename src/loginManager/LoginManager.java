package loginManager;

import enums.ClientType;
import facade.AdminFacade;
import facade.ClientFacade;
import facade.CompanyFacade;
import facade.CustomerFacade;

public class LoginManager {

    private static LoginManager instance = null;

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        if (instance == null) {
            synchronized (LoginManager.class) {
                if (instance == null) {
                    instance = new LoginManager();
                }
            }
        }
        return instance;
    }

    public ClientFacade login(String email, String password, ClientType clientType) {
        switch (clientType) {
            case Administrator:
                AdminFacade adminFacade = new AdminFacade();
                if (adminFacade.login(email, password)) {
                    return adminFacade;
                }
                return null;
            case Company:
                CompanyFacade companyFacade = new CompanyFacade();
                if (companyFacade.login(email, password)) {
                    return companyFacade;
                }
                return null;
            case Customer:
                CustomerFacade customerFacade = new CustomerFacade();
                if (customerFacade.login(email, password)) {
                    return customerFacade;
                }
                return null;
        }
        return null;
    }

}

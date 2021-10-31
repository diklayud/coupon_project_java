package beans;

import java.util.List;
import java.util.Objects;

public class Company {

    private int id;
    private String name;
    private String email;
    private String password;
    private List<Coupon> coupons;

    /**
     * Constructor for creating an instance of class,
     * used to return an empty Company object
     */
    public Company() {
    }

    /**
     * Constructor for creating instance of class,
     * used to create company without giving it an ID
     *
     * @param name
     * @param email
     * @param password
     */
    public Company(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor for creating instance of class,
     * used to create company with ID from database
     *
     * @param id
     * @param name
     * @param email
     * @param password
     */
    public Company(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", coupons=" + coupons +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return id == company.id && Objects.equals(name, company.name) && Objects.equals(email, company.email) && Objects.equals(password, company.password) && Objects.equals(coupons, company.coupons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, coupons);
    }

}

package beans;

import enums.Category;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

public class Coupon {

    private int id;
    private int companyId;
    private Category category;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int amount;
    private double price;
    private String image;

    /**
     * Constructor for creating instance of class,
     * used to create coupon for purchase in tests
     * @param id
     * @param companyId
     * @param category
     * @param title
     * @param startDate
     * @param endDate
     * @param price
     */
    public Coupon(int id, int companyId, Category category, String title, Date startDate, Date endDate, double price) {
        this.id = id;
        this.companyId = companyId;
        this.category = category;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

    /**
     * Constructor for creating instance of class,
     * used to create coupon without giving it an ID
     * @param companyId
     * @param category
     * @param title
     * @param description
     * @param startDate
     * @param endDate
     * @param amount
     * @param price
     * @param image
     */
    public Coupon(int companyId, Category category, String title, String description, Date startDate, Date endDate, int amount, double price, String image) {
        this.startDate = Date.valueOf(LocalDate.now());
        this.companyId = companyId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        this.image = image;
    }

    /**
     * Constructor for creating instance of class,
     * used to create coupon with ID from database
     * @param id
     * @param companyId
     * @param category
     * @param title
     * @param description
     * @param startDate
     * @param endDate
     * @param amount
     * @param price
     * @param image
     */
    public Coupon(int id, int companyId, Category category, String title, String description, Date startDate, Date endDate, int amount, double price, String image) {
        this.id = id;
        this.companyId = companyId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", companyId=" + companyId +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", amount=" + amount +
                ", price=" + price +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return id == coupon.id && companyId == coupon.companyId && amount == coupon.amount && Double.compare(coupon.price, price) == 0 && Objects.equals(category, coupon.category) && Objects.equals(title, coupon.title) && Objects.equals(description, coupon.description) && Objects.equals(startDate, coupon.startDate) && Objects.equals(endDate, coupon.endDate) && Objects.equals(image, coupon.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyId, category, title, description, startDate, endDate, amount, price, image);
    }

}

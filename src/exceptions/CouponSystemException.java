package exceptions;

public class CouponSystemException extends Exception{

    public CouponSystemException() {
        super("General Exception...");
    }

    public CouponSystemException(String message) {
        super(message);
    }

    public CouponSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}

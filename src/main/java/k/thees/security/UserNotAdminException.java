package k.thees.security;

public class UserNotAdminException extends RuntimeException {
    public UserNotAdminException() {
        super("User is not an admin user");
    }
}

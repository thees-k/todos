package k.thees.security;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super(String.format("User with name %s already exists. Please choose a different username.", username));
    }
}

package k.thees.validation;


public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(long userId) {
        super(String.format("User with ID %d not found", userId));
    }
}



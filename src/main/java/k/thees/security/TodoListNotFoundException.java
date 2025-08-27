package k.thees.security;


public class TodoListNotFoundException extends RuntimeException {

    public TodoListNotFoundException(long userId) {
        super(String.format("TodoList with ID %d not found", userId));
    }
}



package k.thees.security;


public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(long taskId) {
        super(String.format("Task with ID %d not found", taskId));
    }
}



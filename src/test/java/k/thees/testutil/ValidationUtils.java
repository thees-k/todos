package k.thees.testutil;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ValidationUtils {
    
    public static void validateIsBetween(LocalDateTime time, LocalDateTime before, LocalDateTime after) {
        assertNotNull(time);
        assertFalse(time.isBefore(before));
        assertFalse(time.isAfter(after));
    }
}

package k.thees.testutil;

import k.thees.entity.User;

public class TestDataFactory {

    public static User createUser(long id, String username, String email, String passwordHash) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        return user;
    }
}
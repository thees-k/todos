package k.thees.testutil;

import k.thees.entity.Role;
import k.thees.entity.User;

public class TestDataFactory {

    public static User createUser(long id, String username, String email, String passwordHash, Role.RoleType roleType) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(new Role(roleType));
        user.setPasswordHash(passwordHash);

        User admin = new User();
        admin.setId(1L);
        admin.setUsername("Admin");
        admin.setRole(new Role(Role.RoleType.ADMIN));
        user.setUpdatedBy(admin);

        return user;
    }
}
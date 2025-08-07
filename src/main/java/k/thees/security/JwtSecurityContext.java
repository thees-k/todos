package k.thees.security;

import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

public class JwtSecurityContext implements SecurityContext {

    private final String username;

    public JwtSecurityContext(String username) {
        this.username = username;
    }

    @Override
    public Principal getUserPrincipal() {
        return () -> username;
    }

    @Override
    public boolean isUserInRole(String role) {
        return false; // Optional: integrate roles if you have them
    }

    @Override
    public boolean isSecure() {
        return false; // or derive from request if needed
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }
}

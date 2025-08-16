package k.thees.security;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import k.thees.entity.User;
import k.thees.service.UserService;

import java.security.Principal;
import java.util.Optional;

@RequestScoped
public class SecurityService {

    @Context
    private SecurityContext securityContext;

    @Inject
    private UserService userService;

    public User getLoggedInUserOrThrow() {
        return Optional.ofNullable(securityContext.getUserPrincipal())
                       .map(Principal::getName)
                       .map(userService::findByUsername)
                       .filter(Optional::isPresent)
                       .map(Optional::get)
                       .orElseThrow(UserNotAuthenticatedException::new);
    }
}
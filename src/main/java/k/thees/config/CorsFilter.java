package k.thees.config;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class CorsFilter implements ContainerResponseFilter, ContainerRequestFilter {

    // private static final String UI_ORIGIN = "http://localhost:8081";
    private static final String UI_ORIGIN = "*";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {

            Response response = Response.ok()
                                        .header("Access-Control-Allow-Origin", UI_ORIGIN)
                                        .header("Access-Control-Allow-Credentials", "true")
                                        .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                                        .header("Access-Control-Allow-Headers", "Authorization,Content-Type,Accept")
                                        .header("Access-Control-Max-Age", "86400")
                                        .build();
            requestContext.abortWith(response);
        }
    }

    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) {

        res.getHeaders().putSingle("Access-Control-Allow-Origin", UI_ORIGIN);
        res.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        res.getHeaders().putSingle("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        res.getHeaders().putSingle("Access-Control-Allow-Headers", "Authorization,Content-Type,Accept");
        res.getHeaders().putSingle("Vary", "Origin");
    }
}

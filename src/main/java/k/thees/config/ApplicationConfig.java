package k.thees.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api") // Base path for all REST resources
public class ApplicationConfig extends Application {
    // No need to override anything unless you register classes manually
}

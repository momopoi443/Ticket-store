package ticket.store.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TicketStoreBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketStoreBackendApplication.class, args);
    }
}

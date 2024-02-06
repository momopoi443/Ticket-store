package ticket.store.backend.config.image;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "image.storage")
public record ImageStorageProperties(
        String directoryPath
) {}

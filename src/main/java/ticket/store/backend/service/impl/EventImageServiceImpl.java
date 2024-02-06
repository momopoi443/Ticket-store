package ticket.store.backend.service.impl;

import lombok.extern.slf4j.Slf4j;
import ticket.store.backend.config.image.ImageStorageProperties;
import ticket.store.backend.exception.internal.InternalImageStorageException;
import ticket.store.backend.exception.external.InvalidArgumentException;
import ticket.store.backend.exception.external.NotFoundException;
import ticket.store.backend.service.EventImageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

@Slf4j
@Service
public class EventImageServiceImpl implements EventImageService {

    private final Path imageDirectoryPath;

    public EventImageServiceImpl(ImageStorageProperties imageStorageProperties) {
        try {
            imageDirectoryPath = Paths.get(imageStorageProperties.directoryPath());
            Files.createDirectories(imageDirectoryPath);
        }
        catch (InvalidPathException | IOException e) {
            throw new InternalImageStorageException(e);
        }
    }

    @Override
    public String store(UUID eventUuid, MultipartFile image) {
        if (image.isEmpty()) {
            throw new InvalidArgumentException("Can't store empty file");
        }

        String imageFilename = eventUuid.toString() + "." + StringUtils.getFilenameExtension(image.getOriginalFilename());
        Path destinationPath = getImageLocation(imageFilename);

        try {
            Files.copy(image.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new InternalImageStorageException(e);
        }
        log.info("Event image: [" + imageFilename + "] saved");

        return imageFilename;
    }

    @Override
    public void delete(String imageFilename) {
        Path imageLocation = getImageLocation(imageFilename);

        if (!imageLocation.getParent().equals(this.imageDirectoryPath.toAbsolutePath())) {
            throw new InvalidArgumentException("Can't delete image outside current directory");
        }
        if (!imageLocation.toFile().exists()) {
            throw new NotFoundException("Can't find image: [" + imageFilename + "]");
        }
        if (imageLocation.toFile().isDirectory()) {
            throw new InvalidArgumentException("Given directory name not a image filename");
        }

        try {
            Files.delete(imageLocation);
        } catch (IOException e) {
            throw new InternalImageStorageException(e);
        }
        log.info("Event image: [" + imageFilename + "] deleted");
    }

    @Override
    public Resource loadAsResource(String imageFilename) {
        Path imageLocation = getImageLocation(imageFilename);

        Resource resource;
        try {
            resource = new UrlResource(imageLocation.toUri());
        } catch (MalformedURLException e) {
            throw new InvalidArgumentException(e.getMessage());
        }

        if (!resource.exists()) {
            throw new NotFoundException("Can't find image: [" + imageFilename + "]");
        }
        if (!resource.isReadable()) {
            throw new InternalImageStorageException("Can't read file: [" + imageFilename + "]");
        }

        return resource;
    }

    private Path getImageLocation(String imageFilename) {
        return imageDirectoryPath
                .resolve(imageFilename)
                .normalize()
                .toAbsolutePath();
    }
}
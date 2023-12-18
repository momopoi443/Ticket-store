package org.example.sbdcoursework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

//TODO write validation messages
//TODO decide if image for Event needed for now
@SpringBootApplication
@ConfigurationPropertiesScan
public class SbdCourseWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbdCourseWorkApplication.class, args);
    }

}

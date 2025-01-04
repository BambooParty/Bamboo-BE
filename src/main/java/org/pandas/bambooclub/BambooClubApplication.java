package org.pandas.bambooclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class BambooClubApplication {

    public static void main(String[] args) {
        SpringApplication.run(BambooClubApplication.class, args);
    }

}

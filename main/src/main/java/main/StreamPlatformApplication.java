package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {
        "main", "users", "movies", "auth", "shared"
})
@EnableJpaRepositories(basePackages = "users.repository")
@EntityScan(basePackages = "users.model")
public class StreamPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamPlatformApplication.class, args);
    }

}

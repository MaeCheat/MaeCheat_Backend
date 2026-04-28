package dev.shin.maecheat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MaeCheatApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaeCheatApplication.class, args);
    }

}

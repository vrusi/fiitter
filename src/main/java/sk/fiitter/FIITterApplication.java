package sk.fiitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("sk.fiitter")
@EntityScan("sk.fiitter")
@SpringBootApplication
public class FIITterApplication {

	public static void main(String[] args) {
		SpringApplication.run(FIITterApplication.class, args);
	}

}

package Comprehensive_Design_Project.CUK_Compasser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CukCompasserApplication {

	public static void main(String[] args) {
		SpringApplication.run(CukCompasserApplication.class, args);
	}

}

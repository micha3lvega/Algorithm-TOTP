package co.com.micha3lvega.totp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SpringAndTotpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAndTotpServerApplication.class, args);
	}

}

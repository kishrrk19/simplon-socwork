package co.simplon.socwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//(exclude = SecurityAutoConfiguration.class)
@SpringBootApplication
public class SocworkBuisinessApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocworkBuisinessApplication.class, args);
	}

}

package com.logikaintermedia.erp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.logikaintermedia.erp.validation.ErrorConfig;

@SpringBootApplication
@EnableConfigurationProperties(ErrorConfig.class)
public class ErpFinanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErpFinanceApplication.class, args);
	}

	@Bean
	public CommandLineRunner newPassword(PasswordEncoder encoder) {
		return args -> {
			String pass = encoder.encode("1");
			System.out.println("Password Baru ku : " + pass);
		};
	}
}

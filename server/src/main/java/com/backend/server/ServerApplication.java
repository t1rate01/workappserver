package com.backend.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaAuditing  // see Auditable luokat, ajan ja pvm tallentamista varten
@EnableScheduling //  mahdollisia Scheduled toimintoja varten, esim notifikaatiot ? 
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}


		@Bean
		public WebMvcConfigurer corsConfigurer() {
    	return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*") // SALLII KAIKKI ORIGINIT, TARKISTA MYÖHEMMIN TURVALLISUUS
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
						.allowedHeaders("Authorization", "content-type")
						.exposedHeaders("Authorization");
			}
    	};
	}
}

package br.com.msandredev.hubspotintegrationapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "br.com.msandredev.hubspotintegrationapi.client")
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}

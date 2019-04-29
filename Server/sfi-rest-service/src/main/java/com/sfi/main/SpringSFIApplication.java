package com.sfi.main;

import com.sf.server.archivemanager.util.JsonUtil;
import com.shakeel.model.AssetStatus;
import com.shakeel.model.Product;
import com.shakeel.repository.AssetRepository;
import com.shakeel.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@RestController
@EnableAutoConfiguration
//public class SpringcoffeeshopApplication implements CommandLineRunner {
public class SpringSFIApplication {
	//@Autowired
   // ProductRepository productRepository;
    public static void main(String[] args) {
    	System.getProperties().put( "server.port", 8088 );
        SpringApplication.run(SpringSFIApplication.class, args);
    }
    
    //Enable Global CORS support for the application
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") //http://localhost:8088
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
                        .allowedHeaders("header1", "header2", "content-type") //What is this for?
                        .exposedHeaders("header1", "header2", "content-type")
                        .allowCredentials(false).maxAge(3600);
            }
        };
    }
  /*  @Override
    public void run(String... strings) throws Exception {

        Product mocha = new Product();
        mocha.setProduct_name("Mocha");
        mocha.setProduct_price(3.95);

        Product mocha1 = new Product();
        mocha1.setProduct_name("Mocha1");
        mocha1.setProduct_price(3.95);

        productRepository.save(mocha);
        productRepository.save(mocha1);

    }*/


}
    

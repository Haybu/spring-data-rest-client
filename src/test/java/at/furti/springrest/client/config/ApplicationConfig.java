package at.furti.springrest.client.config;

import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDataRestClient(basePackage = "at.furti.springrest.client.data.find")
public class ApplicationConfig {

}

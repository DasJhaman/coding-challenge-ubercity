package jhaman.das.challangeubitricity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication()
@EnableSwagger2
@EnableCaching
public class ChallangeubitricityApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChallangeubitricityApplication.class, args);
    }

    @Bean
    public Docket ProductIntegrationApi() {
        Set<String> produceTypes = new HashSet<>();
        produceTypes.add("application/json");
        Set<String> consumeTypes = new HashSet<>(produceTypes);
        consumeTypes.add("application/json");

        return new Docket(DocumentationType.SWAGGER_2)
                .consumes(consumeTypes)
                .produces(produceTypes)
                .select()
                .apis(RequestHandlerSelectors.basePackage("jhaman.das.challangeubitricity"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public EvUbiState getState() {
        return new EvUbiState();
    }

}
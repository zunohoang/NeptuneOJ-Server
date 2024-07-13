package org.example.neptuneojserver;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(name = "Bearer Authentication", scheme = "Bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class NeptuneojServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeptuneojServerApplication.class, args);
    }

}

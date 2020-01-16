package com.arkea.oac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan
public class Application extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer{

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    /**
     * Configurer l'environnement de l'application
     * @param builder 
     * @return Ajouter cette classe dans la configuration
     */
    @Override
    protected SpringApplicationBuilder configure (SpringApplicationBuilder builder) {
    	return builder.sources(Application.class);
    }

    /**
     * Personnaliser la configuration. Ici, on peut changer le port par défaut en 8090
     * @param container 
     * @return void
     */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
    	container.setPort(8090);
    }
}
package com.tiendatextil.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permite solicitudes desde el frontend en el puerto 8081
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081") 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                .allowedHeaders("*") // Permite todos los headers
                .allowCredentials(true); // Si es necesario, para enviar cookies o credenciales
    }
}

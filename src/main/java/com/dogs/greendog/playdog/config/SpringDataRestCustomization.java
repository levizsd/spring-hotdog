package com.dogs.greendog.playdog.config;

import com.dogs.greendog.playdog.domain.Item;
import com.dogs.greendog.playdog.repository.ClienteRepository;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Component
public class SpringDataRestCustomization 
implements RepositoryRestConfigurer 
{

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(Item.class, ClienteRepository.class);
    }
}
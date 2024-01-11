package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.hawt.springboot.HawtioPlugin;
import io.hawt.config.ConfigFacade;


@SpringBootApplication
public class MySpringBootApplication {

    /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        SpringApplication.run(MySpringBootApplication.class, args);
    }

    @Bean
    public HawtioPlugin samplePlugin() {
        return new HawtioPlugin("sample-plugin",
            "plugins",
            "sample-plugin/sample-plugin.js");
    }

    @Bean
    public ConfigFacade configFacade() {
        return ConfigFacade.getSingleton();
    }

    /**
     * Enable HTTP tracing for Spring Boot
     */
    // @Bean
    // public HttpTraceRepository httpTraceRepository() {
    //     return new InMemoryHttpTraceRepository();
    // }    
}

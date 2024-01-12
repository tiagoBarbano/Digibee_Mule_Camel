package com.hawtio.hawtiocore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.hawt.config.ConfigFacade;
import io.hawt.springboot.HawtioPlugin;

@SpringBootApplication
public class HawtioCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(HawtioCoreApplication.class, args);
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
}

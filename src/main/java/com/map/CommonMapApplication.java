package com.map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
//@ComponentScans({@ComponentScan("com.map.common")})
public class CommonMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonMapApplication.class, args);
    }

}

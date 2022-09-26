package com.iamiagkov.reactiveapidemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ReactiveApiDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveApiDemoApplication.class, args);
    }


}

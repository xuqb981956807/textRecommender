package com.chrtc.textRecommend;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
//@EnableConfigurationProperties
@EnableScheduling
@SpringBootApplication
@ComponentScan(value = {"com.chrtc"})
public class TextRecommendStartupApplication {

public static void main(String[] args) {

    SpringApplication.run(TextRecommendStartupApplication.class, args);

}
}

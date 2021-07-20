package com.chrtc.textRecommend.config;


import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasourcesource.driver-class-name}")
    private String sourceDriverClassName;
    @Value("${spring.datasourcesource.url}")
    private String sourceUrl;
    @Value("${spring.datasourcesource.username}")
    private String username;
    @Value("${spring.datasourcesource.password}")
    private String password;

    @Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
    @Primary
    @ConfigurationProperties(prefix="spring.datasourcesource")
    public DataSource primaryDataSource() {
        return new DruidDataSource();
    }
    @Bean(name = "sourceDataSource")
    @Qualifier("sourceDataSource")
    @ConfigurationProperties(prefix="spring.datasourcesource")
    public DataSource sourceDataSource() {

        return new DruidDataSource();
    }

    @Bean(name = "sourceJdbcTemplate")
    public JdbcTemplate sourceJdbcTemplate(
            @Qualifier("sourceDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}



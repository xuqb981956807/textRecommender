package com.chrtc.textRecommend.config;


import com.chrtc.textRecommend.recommend.Collect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@EnableScheduling
public class CronTab {
    @Autowired
    @Qualifier("sourceJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Value("${kmeanskcuster}")
    private String kmeanskcuster;

    @Scheduled(cron = "0 0/5 * * * ?")
    private void cron() throws SQLException {
        Collect collect=new Collect();
        //System.out.println("============ ");

        String sqlCount = "select count(*) from user_collection_1";
        int listCount = jdbcTemplate.queryForObject(sqlCount, Integer.class);
        if(listCount>=5){
            jdbcTemplate.update("insert into user_collection select * from user_collection_1");
            jdbcTemplate.update("delete from user_collection_1");
           collect.getCluter(jdbcTemplate,kmeanskcuster);
        }
    }
}

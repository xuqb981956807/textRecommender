package com.chrtc.textRecommend.conteller;

import com.chrtc.textRecommend.domain.ResultData;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@RestController
public class collectionConteller {
    @Autowired
    @Qualifier("sourceJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    /**
     * @param userId
     * @return
     * @throws SQLException
     * @throws IOException
     */
    @RequestMapping(value = "getCollection", method = RequestMethod.POST)
    public ResultData getCollection(@RequestParam("userId") Integer userId) throws SQLException, IOException {
        ResultData resultData = new ResultData();
        Set<Integer> set = new HashSet<>();
        String sql = "SELECT * FROM news where id in (SELECT new_id FROM user_collection WHERE  user_id = " + userId + " and status_data  =  0 )";
        String sqltemporary = "SELECT * FROM news where id in (SELECT new_id FROM user_collection_1 WHERE  user_id = " + userId + " and status_data  =  0 )";
        List<Map<String, Object>> lists = jdbcTemplate.queryForList(sql);
        List<Map<String, Object>> listt = jdbcTemplate.queryForList(sqltemporary);
        //System.out.println(sql);
        List<Object> list = new ArrayList<>();
        List<Object> listTemporary = new ArrayList<>();
        lists.forEach(k -> {
            k.forEach((key, v) -> {
                list.add(v);
            });
        });
        listt.forEach(k -> {
            k.forEach((key, v) -> {
                listTemporary.add(v);
            });
        });
        List<Object> listObject = new ArrayList<>();
        listObject.add(list);
        listObject.add(listTemporary);
        resultData.setCode("1");
        resultData.setDesc("成功");
        resultData.setData(listObject);
        return resultData;
    }

    /**
     * 用户收藏新增
     *
     * @param userId
     * @param newId
     * @return
     */
    @RequestMapping(value = "addCollection", method = RequestMethod.POST)
    public ResultData addCollection(@RequestParam("userId") Integer userId, @RequestParam("newId") Integer newId) {
        ResultData resultData = new ResultData();
        long l = System.currentTimeMillis();
        Date time = new Date(l);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "Insert into user_collection_1(user_id,new_id,collectiondate) value(" + userId + "," + newId + "," + "'" + sdf.format(time) + "'" + ")";
        //System.out.println(sql);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                return ps;
            }
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        resultData.setCode("1");
        resultData.setDesc("成功");
        resultData.setData(id);
        return resultData;
    }

    @RequestMapping(value = "deleteCollection", method = RequestMethod.POST)
    public ResultData deleteCollection(@RequestParam("userId") Integer userId, @RequestParam("newId") Integer newId) {
        ResultData resultData = new ResultData();
        String sqldelete = "update user_collection set status_data=1 where user_id= " + userId + " AND new_id= " + newId;
        jdbcTemplate.update(sqldelete);
        String sqldeletet = "update user_collection_1 set status_data=1 where user_id= " + userId + " AND new_id= " + newId;
        jdbcTemplate.update(sqldeletet);
        resultData.setCode("1");
        resultData.setDesc("删除成功");
        return resultData;
    }

    /**
     * 用户登录系统会返回该用户待推荐的新闻列表
     * @param userId
     * @return
     */
    @RequestMapping(value = "loginRecommend", method = RequestMethod.POST)
    public ResultData loginRecommend(@RequestParam("userid") Integer userId) {
        ResultData resultData = new ResultData();
        String sql = "SELECT a.newid from (SELECT recommend_user, group_concat(newid) as newid FROM  news_user GROUP BY recommend_user)as a where recommend_user =" + userId;
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String newsRecommend = rs.getString(1);
                String[] news = newsRecommend.split(",");
                resultData.setCode("1");
                resultData.setDesc("成功");
                resultData.setData(newsRecommend);
            }
        });
        return resultData;
    }
}

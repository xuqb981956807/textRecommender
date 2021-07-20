package com.chrtc.textRecommend.recommend;
import com.chrtc.textRecommend.entry.IThreshold;
//import com.hankcs.hanlp.mining.cluster.ClusterAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文章id进行聚类
 * 求出用户和兴趣点的关系
 *
 */

public class Collect {

    public void getCluter(JdbcTemplate jdbcTemplate,String kmeanskcuster) throws SQLException {
   // public ClusterAnalyzer<String> getCluter() throws SQLException {
    //public ClusterAnalyzer<String> getCluter(JdbcTemplate jdbcTemplate) throws SQLException {
        //public static void main(String[] args) throws SQLException {

        TFIDF tfidf = new TFIDF();
        KmeansCluster kmeansCluster = new KmeansCluster();
        Collect collect = new Collect();
        Set<Integer> set = new HashSet<>();
        //获取收藏的新闻id  使用set去重
        List<Map<String, Object>> lists = jdbcTemplate.queryForList("SELECT new_id FROM user_collection where status_data =" + 0);
        lists.forEach(k -> {
            k.forEach((x, y) -> {
                set.add((Integer) y);
            });
        });
        HashMap<String, HashMap<String, Float>> idNewsMap = new HashMap<>();
        set.forEach((Integer str) -> {
            String sqlNews = "select content from news WHERE  id = " + str;
            HashMap<Integer, Object> hashMap = new HashMap<>();
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlNews);
            list.forEach(o -> {
                o.forEach((k, v) -> {
                            try {
                                HashMap<String, Float> newHashMap = tfidf.tfAllFilesNew((String) v);
                                idNewsMap.put(String.valueOf(str), newHashMap);
                                System.out.println("-----------" + newHashMap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
            });
        });
        HashMap<String, Float> idfs = tfidf.idf(idNewsMap);
        //计算出TF-IDF的值
        HashSet setAllData = new HashSet();
        //HashMap<String, HashSet> hashMapData = new HashMap<>();
        Map<String, Map<String, Double>> mapDate = new TreeMap<>();
        tfidf.tf_idf(idNewsMap, idfs, setAllData, mapDate);
        System.out.println("==" + mapDate);
        //实现聚类
      /*  ClusterAnalyzer<String> analyzers = new ClusterAnalyzer<String>();
        List<String> listdata = new ArrayList<>();
        hashMapData.forEach((k, v) -> {
            v.forEach(
                    sk -> {
                        listdata.add((String) sk);
                    }
            );
            analyzers.addDocument(k, listdata);
        });
        HashMap<Object, Object> hashMapClusterAccount = new HashMap<>();
        List<Set<String>> listcluster = analyzers.kmeans(3);
        System.out.println("===== " + listcluster);*/
        //用户和兴趣点的列表
        //System.out.println(Integer.parseInt(kmeanskcuster));
        Map<String, Integer> mapCluster = kmeansCluster.doProcess(mapDate, Integer.parseInt(kmeanskcuster),jdbcTemplate);
        System.out.println(mapCluster.toString());
        Map<Integer,List<String>>  mapNewsId=new HashMap<>();
        mapCluster.forEach((key,val)->{
            List<String> listDate=new ArrayList<>();
            mapCluster.forEach((k,v)->{
                if(v==val)
                {
                  listDate.add(k);
                }

            });
            mapNewsId.put(val,listDate);
        });
        System.out.println(mapNewsId.toString());
        mapNewsId.forEach((k,v)->{
            Set<Integer> setData = new HashSet<Integer>();
            v.forEach(value->{
                String sqlCluster = "select user_id from user_collection where new_id = " + value;
                List<Map<String, Object>> listUserdata = jdbcTemplate.queryForList(sqlCluster);
                listUserdata.forEach(x -> {
                    x.forEach((key, val) -> {
                        setData.add((Integer) val);
                    });
                });
            });
             String updateSql="update user_cluster set cluster ="+ "'"+v.toString().replace("[","").replace("]","")+"'" +" ,user_list= "+ "'"+ setData.toString().replace("[","").replace("]","")+ "'" +" where cluster_class="+k;
            jdbcTemplate.update(updateSql);
        });
    }
}
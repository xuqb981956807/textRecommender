package com.chrtc.textRecommend.conteller;

import com.alibaba.fastjson.JSON;
import com.chrtc.textRecommend.domain.NewParam;
import com.chrtc.textRecommend.domain.ResiveData;
import com.chrtc.textRecommend.domain.ResultData;
import com.chrtc.textRecommend.entry.UserCluster;
import com.chrtc.textRecommend.recommend.*;
import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@RestController
@PropertySource(value = {"application.properties"})
public class attentionConteller {
    @Autowired
    @Qualifier("sourceJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    @Value("${threshold}")
    private String threshold;
//    @RequestMapping(value = "resiveNews", method = RequestMethod.POST)
//    public ResultData resiveNews(String contents) throws SQLException, IOException {
//        ResultData resultData = new ResultData();
//
//        Collect collect = new Collect();
//        // Collect collect = null;
//        TFIDF tfidf = new TFIDF();
//        Connection conn = null;
//        String temp = contents;
//
//        //     String temp = new ReadFile().readDate();
//
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        HashMap<Integer, String> hashMapNew = new HashMap<>();
//        String sql = "INSERT INTO news(chinese_tag,content) VALUE(" + "' '" + ",\'" + temp + "\'" + ")";
//
//        jdbcTemplate.update(new PreparedStatementCreator() {
//            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
//                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//                return ps;
//            }
//        }, keyHolder);
//        int id = keyHolder.getKey().intValue();
//        //读取写入到txt文件中的数据，并写入到数据表中
//        HashMap<String, HashMap<String, Float>> newIdMap = new HashMap<String, HashMap<String, Float>>();
//        HashMap<String, Float> newHashMap = tfidf.tfAllFilesNew((String) temp);
//        //  System.out.println("数据主键："+id);
//        newIdMap.put(String.valueOf(id), newHashMap);
//        //  System.out.println("====="+newIdMap);
//        HashMap<String, Float> idfs = tfidf.idf(newIdMap);
//        HashSet setAllData = new HashSet();
//        HashMap<String, HashSet> hashMapDa = new HashMap<>();
//        //求出tf-idf
//        tfidf.tf_idf(newIdMap, idfs, setAllData, hashMapDa);
//
//        ClusterAnalyzer<String> analyzer = collect.getCluter(jdbcTemplate);
//        List<String> listdata = new ArrayList<>();
//        hashMapDa.forEach((k, v) -> {
//            v.forEach(
//                    sk -> {
//                        listdata.add((String) sk);
//                    }
//            );
//            analyzer.addDocument(k, listdata);
//        });
//        System.out.println("==========" + analyzer);
//        //实现归类兴趣点
//        HashMap<Set<String>, Set<Integer>> hashMapClusterAccounts = new HashMap<>();
//        List<Set<String>> listclusters = analyzer.repeatedBisection(3);
//        System.out.println("===== " + listclusters);
//        listclusters.forEach(
//                st -> {
//                    Set<Integer> setData = new HashSet<>();
//                    st.forEach(
//                            s -> {
//                                String sqlClusters = "select user_id from user_collection where new_id = " + s;
//                                List<Map<String, Object>> listUserdata = jdbcTemplate.queryForList(sqlClusters);
//                                listUserdata.forEach(x -> {
//                                    x.forEach((k, v) -> {
//                                        setData.add((Integer) v);
//                                    });
//                                });
//                                //System.out.println();
//                                hashMapClusterAccounts.put(st, setData);
//                            }
//                    );
//                }
//        );
//        Map<Integer, Set<Integer>> map = new HashMap<>();
//        Set<String> set = new HashSet<>();
//        hashMapClusterAccounts.forEach((k, v) -> {
//            k.forEach(
//                    s ->
//                    {
//                        if (s.toString().equals(String.valueOf(id))) {
//                            map.put(id, v);
//                        }
//                    }
//            );
//        });
//        System.out.println(map);
//        Set<Integer> setUser = map.get(id);
//        System.out.println(hashMapClusterAccounts);
//        resultData.setCode("1");
//        resultData.setDesc("成功");
//        resultData.setData(setUser);
//        return resultData;
//}

    /**
     * 单条新闻内容插入数据库中
     * @param resiveData
     * @return
     * @throws SQLException
     * @throws IOException
     */

    @RequestMapping(value = "resiveNew", method = RequestMethod.POST)
    public ResultData resiveNew( @RequestBody ResiveData resiveData) throws SQLException, IOException {
        System.out.println(JSON.toJSON(resiveData));
        System.out.println(JSON.toJSON(resiveData.getDocidContentMap()));
        NewsKclusterSimitly newsKclusterSimitly = new NewsKclusterSimitly();
        ResultData resultData = new ResultData();
        KmeansCluster kmeansCluster = new KmeansCluster();
        NewsTFIDF newsTFIDF = new NewsTFIDF();
        TFIDF tfidf = new TFIDF();
        Connection conn = null;
//        String docid=resiveData.getNewParam().getDocid();
//        String temp = resiveData.getNewParam().getContents();
        HashMap<Integer,Set<String>> hashMapIdUser=new HashMap<>();
       resiveData.getDocidContentMap().forEach((ky,ve)->{
           String docid=(String) ky;

           String temps = (String) ve;
           String temp=temps.replaceAll("\"","").replaceAll("\'","");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        HashMap<Integer, String> hashMapNew = new HashMap<>();
        String sql = "INSERT INTO news(docid,content) VALUE(" + "\""+ docid + "\"" + ",\"" + temp + "\"" + ")";
           System.out.println(sql);
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                return ps;
            }
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        HashMap<String, HashMap<String, Float>> newIdMap = new HashMap<String, HashMap<String, Float>>();
           HashMap<String, Float> newHashMap = null;
           try {
               newHashMap = TFIDF.tfAllFilesNew(temp);
           } catch (IOException e) {
               e.printStackTrace();
           }
           //  System.out.println("数据主键："+id);
        System.out.println(newHashMap.toString());
        HashMap<String, Double> mapHash = new HashMap<>();

        newHashMap.forEach((k, v) -> {
            mapHash.put(k, (double) v);
        });
        Set<String> cnSet = new HashSet<String>();
        cnSet.addAll(newHashMap.keySet());

        String[] cnList = cnSet.toArray(new String[0]);
        double[] vec = new double[cnList.length];
           HashMap<String, Float> finalNewHashMap = newHashMap;
           newHashMap.forEach((k, v) -> {
            for (int i = 0; i < cnList.length; i++) {
                if (finalNewHashMap.containsKey(cnList[i])) {
                    vec[i] = finalNewHashMap.get(cnList[i]);
                } else {
                    vec[i] = 0;
                }
            }
        });
        newIdMap.put(String.valueOf(id), newHashMap);
        HashMap<String, Float> idfs = TFIDF.idf(newIdMap);
        HashSet setAllData = new HashSet();
        HashMap<String, Double> hashMapDa = new HashMap<>();
        //求出tf-idf
        Map<String, Map<String, Double>> mapDate = new TreeMap<>();
        TFIDF.tf_idf(newIdMap, idfs, setAllData, mapDate);
        List<String> listNew = new ArrayList<>();
        mapDate.forEach((key, val) -> {
            val.forEach((k, v) -> {
                hashMapDa.put(k, v);
            });
        });

        //System.out.println(hashMapDa.toString());
        List customerList = new ArrayList();
        Set<String> clusterUser = new HashSet<>();
        String sqlCluster = "select cluster_class,middle_cluster,user_list from user_cluster";
        jdbcTemplate.query(sqlCluster, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                UserCluster uc = new UserCluster();
                HashMap<String, HashMap<String, Double>> all_tf = new HashMap<String, HashMap<String, Double>>();
                all_tf.put(String.valueOf(id), mapHash);
                Gson gson = new Gson();
                int cluster_class = rs.getInt(1);
                //hashMapMiddle=gson.fromJson(name,rs.getString(1));
                String mideleCluster = rs.getString(2);
                HashMap<Object, Object> hashMapMiddleCluster = JSON.parseObject(mideleCluster, HashMap.class);
                HashMap<String, Double> hashMapMiddle = new HashMap<>();
                hashMapMiddleCluster.forEach((k, v) -> {
                    Object object = v;
                    Double dou = Double.parseDouble(object.toString());
                    hashMapMiddle.put((String) k, dou);
                });
                all_tf.put(String.valueOf(cluster_class), hashMapMiddle);

                Set<String> cnSet = new HashSet<String>();
                all_tf.forEach((k, v) -> {
                    cnSet.addAll(v.keySet());
                });
                String[] cnList = cnSet.toArray(new String[0]);
                Map<String, double[]> fileVector = new HashMap<>();
                all_tf.forEach((k, v) -> {
                    double[] vec = new double[cnList.length];
                    for (int i = 0; i < cnList.length; i++) {
                        if (v.containsKey(cnList[i])) {
                            vec[i] = v.get(cnList[i]);
                        } else {
                            vec[i] = 0;
                        }
                    }
                    fileVector.put(k, vec);
                });
                Float distance = null;
                String[] fileNames = fileVector.keySet().toArray(new String[0]);
                for (int i = 0; i < fileNames.length; i++) {
                    for (int j = i + 1; j < fileNames.length; j++) {
                        System.out.println("file[" + fileNames[i] + "] and file[" + fileNames[j] + "] " +
                                "similary is:" + newsKclusterSimitly.getDoubleStrForCosValue(fileVector.get(fileNames[i]), fileVector.get(fileNames[j])));
                        distance = newsKclusterSimitly.getDoubleStrForCosValue(fileVector.get(fileNames[i]), fileVector.get(fileNames[j]));
                    }
                }
                System.out.println(distance);
                if (distance >= Double.parseDouble(threshold)) {
                    String userlist = rs.getString(3);
                    System.out.println(userlist);
                    String[] tag = userlist.split(",");
                    List<String> list = Arrays.asList(tag);
                    System.out.println(list);
                    list.forEach(k -> {
                        clusterUser.add(k.trim());
                    });
                }
            }
        });
           hashMapIdUser.put(id,clusterUser);
        clusterUser.forEach(k -> {
            String sqlString = "insert into news_user(newid,recommend_user) values(?,?)";
            jdbcTemplate.update(sqlString, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setInt(1, id);
                    ps.setString(2, k);
                }
            });
        });
       });
        resultData.setCode("1");
        resultData.setDesc("成功");
        resultData.setData(hashMapIdUser);
        return resultData;
    }

    /**
     * listContent 新闻的内容的list
     * @param newParam
     * @return 每篇内容对应的带推荐的用户
     * @throws SQLException
     * @throws IOException
     */


    /*@RequestMapping(value = "resiveNews", method = RequestMethod.POST)
    public ResultData resiveNews( @RequestBody NewParam newParam) throws SQLException, IOException {
        System.out.println("======="+newParam.getListContents());
        NewsKclusterSimitly newsKclusterSimitly = new NewsKclusterSimitly();
        ResultData resultData = new ResultData();
        KmeansCluster kmeansCluster = new KmeansCluster();
        NewsTFIDF newsTFIDF = new NewsTFIDF();
        TFIDF tfidf = new TFIDF();
        Connection conn = null;
        HashMap<Integer,Set<String>> hashMapIdUser=new HashMap<>();
        newParam.getListContents().forEach(x->{

        String temp = x;
            System.out.println(x);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        HashMap<Integer, String> hashMapNew = new HashMap<>();
        String sql = "INSERT INTO news(chinese_tag,content) VALUE(" + "' '" + ",\'" + temp + "\'" + ")";
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                return ps;
            }
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        HashMap<String, HashMap<String, Float>> newIdMap = new HashMap<String, HashMap<String, Float>>();
            HashMap<String, Float> newHashMap = null;
            try {
                newHashMap = TFIDF.tfAllFilesNew(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //  System.out.println("数据主键："+id);
        System.out.println(newHashMap.toString());
        HashMap<String, Double> mapHash = new HashMap<>();

        newHashMap.forEach((k, v) -> {
            mapHash.put(k, (double) v);
        });
        Set<String> cnSet = new HashSet<String>();
        cnSet.addAll(newHashMap.keySet());

        String[] cnList = cnSet.toArray(new String[0]);
        double[] vec = new double[cnList.length];
            HashMap<String, Float> finalNewHashMap = newHashMap;
            newHashMap.forEach((k, v) -> {
            for (int i = 0; i < cnList.length; i++) {
                if (finalNewHashMap.containsKey(cnList[i])) {
                    vec[i] = finalNewHashMap.get(cnList[i]);
                } else {
                    vec[i] = 0;
                }
            }
        });
        newIdMap.put(String.valueOf(id), newHashMap);
        HashMap<String, Float> idfs = TFIDF.idf(newIdMap);
        HashSet setAllData = new HashSet();
        HashMap<String, Double> hashMapDa = new HashMap<>();
        //求出tf-idf
        Map<String, Map<String, Double>> mapDate = new TreeMap<>();
        TFIDF.tf_idf(newIdMap, idfs, setAllData, mapDate);
        List<String> listNew = new ArrayList<>();
        mapDate.forEach((key, val) -> {
            val.forEach((k, v) -> {
                hashMapDa.put(k, v);
            });
        });

        //System.out.println(hashMapDa.toString());
        List customerList = new ArrayList();
        Set<String> clusterUser = new HashSet<>();
        String sqlCluster = "select cluster_class,middle_cluster,user_list from user_cluster";
        jdbcTemplate.query(sqlCluster, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                UserCluster uc = new UserCluster();
                HashMap<String, HashMap<String, Double>> all_tf = new HashMap<String, HashMap<String, Double>>();
                all_tf.put(String.valueOf(id), mapHash);
                Gson gson = new Gson();
                int cluster_class = rs.getInt(1);
                //hashMapMiddle=gson.fromJson(name,rs.getString(1));
                String mideleCluster = rs.getString(2);
                HashMap<Object, Object> hashMapMiddleCluster = JSON.parseObject(mideleCluster, HashMap.class);
                HashMap<String, Double> hashMapMiddle = new HashMap<>();
                hashMapMiddleCluster.forEach((k, v) -> {
                    Object object = v;
                    Double dou = Double.parseDouble(object.toString());
                    hashMapMiddle.put((String) k, dou);
                });
                all_tf.put(String.valueOf(cluster_class), hashMapMiddle);

                Set<String> cnSet = new HashSet<String>();
                all_tf.forEach((k, v) -> {
                    cnSet.addAll(v.keySet());
                });
                String[] cnList = cnSet.toArray(new String[0]);
                Map<String, double[]> fileVector = new HashMap<>();
                all_tf.forEach((k, v) -> {
                    double[] vec = new double[cnList.length];
                    for (int i = 0; i < cnList.length; i++) {
                        if (v.containsKey(cnList[i])) {
                            vec[i] = v.get(cnList[i]);
                        } else {
                            vec[i] = 0;
                        }
                    }
                    fileVector.put(k, vec);
                });
                Float distance = null;
                String[] fileNames = fileVector.keySet().toArray(new String[0]);
                for (int i = 0; i < fileNames.length; i++) {
                    for (int j = i + 1; j < fileNames.length; j++) {
                        System.out.println("file[" + fileNames[i] + "] and file[" + fileNames[j] + "] " +
                                "similary is:" + newsKclusterSimitly.getDoubleStrForCosValue(fileVector.get(fileNames[i]), fileVector.get(fileNames[j])));
                        distance = newsKclusterSimitly.getDoubleStrForCosValue(fileVector.get(fileNames[i]), fileVector.get(fileNames[j]));
                    }
                }
                System.out.println(distance);
                if (distance >= Double.parseDouble(threshold)) {
                    String userlist = rs.getString(3);
                    System.out.println(userlist);
                    String[] tag = userlist.split(",");
                    List<String> list = Arrays.asList(tag);
                    System.out.println(list);
                    list.forEach(k -> {
                        clusterUser.add(k.trim());
                    });
                }
            }
        });
            hashMapIdUser.put(id,clusterUser);
        clusterUser.forEach(k -> {
            String sqlString = "insert into news_user(newid,recommend_user) values(?,?)";
            jdbcTemplate.update(sqlString, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setInt(1, id);
                    ps.setString(2, k);
                }
            });
        });
        });
        resultData.setCode("1");
        resultData.setDesc("成功");
        resultData.setData(hashMapIdUser);
        return resultData;
    }*/

}
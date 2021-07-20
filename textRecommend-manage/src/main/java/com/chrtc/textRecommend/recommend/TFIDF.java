package com.chrtc.textRecommend.recommend;

import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.*;

/**
 *
 */
public class TFIDF {
    //文档的词的tf值
    public static  HashMap<String,Float> tfAllFilesNew(String o) throws IOException{
        ArrayList<String> cutwords=TFIDF.cutWords(o);
        HashMap<String,Float> dict=new HashMap<String,Float>();
        dict=TFIDF.tf(cutwords);
        return dict;
    }
    /**
     * 进行IK分词
     * @param o
     * @return words
     * @throws IOException
     */
    public static ArrayList<String> cutWords(Object o) throws IOException{
        ArrayList<String> words = new ArrayList<String>();
        IKAnalyzer analyzer = new IKAnalyzer();
        words = analyzer.split((String) o);
        return words;
    }
    public static HashMap<String, Integer> normalTF(ArrayList<String> cutwords){
        HashMap<String, Integer> resTF = new HashMap<String, Integer>();
        for(String word : cutwords){
            if(resTF.get(word) == null){
                resTF.put(word, 1);
               // System.out.println(word);
            }
            else{
                resTF.put(word, resTF.get(word) + 1);
               // System.out.println(word.toString());
            }
        }
        return resTF;
    }
    /**
     * 计算出TF值
     * @param cutwords
     * @return resTF
     */
    public static HashMap<String, Float> tf(ArrayList<String> cutwords){
        HashMap<String, Float> resTF = new HashMap<String, Float>();
        int wordLen = cutwords.size();
        HashMap<String, Integer> intTF = TFIDF.normalTF(cutwords);
        Iterator iter = intTF.entrySet().iterator();
        //iterator for that get from TF
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            resTF.put(entry.getKey().toString(), Float.parseFloat(entry.getValue().toString()) / wordLen);
            // System.out.println(entry.getKey().toString() + " = "+  Float.parseFloat(entry.getValue().toString()) / wordLen);
        }
        return resTF;
    }
    /**
     * 计算出IDF的值
     * @param all_tf
     * @return resIdf
     */
    public static HashMap<String, Float> idf(HashMap<String,HashMap<String, Float>> all_tf){
        HashMap<String, Float> resIdf = new HashMap<String, Float>();
        HashMap<String, Integer> dict = new HashMap<String, Integer>();
        int docNum =all_tf.keySet().size();
        for (String key : all_tf.keySet()) {
            for(String keyValue: all_tf.get(key).keySet() ) {
                if(dict.get(keyValue) == null){
                    dict.put(keyValue, 1);
                }else {
                    dict.put(keyValue, dict.get(keyValue) + 1);
                }
            }
        }
       // System.out.println("IDF for every word is:");
        Iterator iter_dict = dict.entrySet().iterator();
        while(iter_dict.hasNext()){
            Map.Entry entry = (Map.Entry)iter_dict.next();
            float value = (float)Math.log(docNum / Float.parseFloat(entry.getValue().toString()));
            resIdf.put(entry.getKey().toString(), value);
          //  System.out.println(entry.getKey().toString() + " = " + value);
        }
        return resIdf;
    }
    /**
     * 计算出tf-idf的值
     * @param all_tf
     * @param idfs
     */
    public static void tf_idf(HashMap<String,HashMap<String, Float>> all_tf,HashMap<String, Float> idfs,HashSet setAllData,Map<String,Map<String,Double>> mapDate){
        HashMap<String, HashMap<String, Float>> resTfIdf = new HashMap<String, HashMap<String, Float>>();
        int docNum =all_tf.keySet().size();
        for (String key : all_tf.keySet()) {
            HashMap<String, Float> tfidf = new HashMap<String, Float>();
            for(String keyValue: all_tf.get(key).keySet() ) {
                Float value = (float)Float.parseFloat(all_tf.get(key).get(keyValue).toString()) * idfs.get(keyValue);
                tfidf.put(keyValue, value);
            }
            resTfIdf.put(key, tfidf);
        }
        System.out.println("TF-IDF for Every file is :");
        DisTfIdf(resTfIdf,setAllData,mapDate);
    }
    /**
     *
     * @param tfidf  tf-idf的值
     * @param setAllData
     * @param mapDate  二十个关键词的tf-idf的值
     */
    public static void DisTfIdf(HashMap<String, HashMap<String, Float>> tfidf,HashSet setAllData,Map<String,Map<String,Double>> mapDate){
        Iterator iter1 = tfidf.entrySet().iterator();
        while(iter1.hasNext()){
            Map.Entry entrys = (Map.Entry)iter1.next();
           System.out.println("FileName: " + entrys.getKey().toString());
           HashMap<String, Float> temp = (HashMap<String, Float>) entrys.getValue();
            LinkedHashMap<String, Float> sortByMapValue = sortByMapValue(temp);
         System.out.println(sortByMapValue);
            int j=0;
            HashSet setAllDataOne  = new HashSet();
            Map<String,Double>  mapTfidfDate=new HashMap<>();
            for (String key : sortByMapValue.keySet()) {
                if (j<20) {
                    Float val = sortByMapValue.get(key);
                   // System.out.println(key+"="+val);
                    setAllData.add(key);
                    setAllDataOne.add(key);

                } mapTfidfDate.put(key,(double)sortByMapValue.get(key));
                j++;
            }
            mapDate.put((String) entrys.getKey(),mapTfidfDate);
            System.out.println();
        }
    }
    /**
     * 根据value从大到小进行排序
     * @param originalMap
     * @return
     */
    public static  LinkedHashMap<String,Float>  sortByMapValue(Map<String ,Float> originalMap){
        List<Map<String,Float>> sortMapList=new ArrayList<>();
        for (Map.Entry<String, Float> entry : originalMap.entrySet()) {
            String key = entry.getKey();
            Float value=entry.getValue();
            Map<String,Float> orderMap = new HashMap<>();
            orderMap.put(key,value);
            if(sortMapList.size()==0) {
                sortMapList.add(orderMap);
                continue;
            }
            for(int i=0;i<sortMapList.size();i++) {
                float ordreValue=Float.valueOf(String.valueOf((sortMapList.get(i).values().toArray())[0]));
                if(value>ordreValue) {
                    sortMapList.add(i,orderMap);
                    break;
                }
                if(i==(sortMapList.size()-1)) {
                    sortMapList.add(i+1,orderMap);
                    break;
                }
            }
        }
        LinkedHashMap<String,Float> sortedMap=new LinkedHashMap<>();
        for (Map<String, Float> singleMap : sortMapList) {
            @SuppressWarnings("unchecked")
            String orderKey=(String) singleMap.keySet().toArray()[0];
            sortedMap.put(orderKey,singleMap.get(orderKey));
        }
        return sortedMap;
    }
}

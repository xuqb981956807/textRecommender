package com.chrtc.textRecommend.recommend;

import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class NewsTFIDF {

    //文档的词的tf值
    public static  HashMap<String,Double> tfAllFilesNew(String o) throws IOException{
        ArrayList<String> cutwords=TFIDF.cutWords(o);
        HashMap<String,Double> dict=new HashMap<String,Double>();
        dict=NewsTFIDF.tf(cutwords);
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
    public static HashMap<String, Double> tf(ArrayList<String> cutwords){
        HashMap<String, Double> resTF = new HashMap<String, Double>();
        int wordLen = cutwords.size();
        HashMap<String, Integer> intTF = NewsTFIDF.normalTF(cutwords);
        Iterator iter = intTF.entrySet().iterator();
        //iterator for that get from TF
            while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            resTF.put(entry.getKey().toString(), (double)Float.parseFloat(entry.getValue().toString()) / wordLen);
             System.out.println(entry.getKey().toString() + " = "+  Float.parseFloat(entry.getValue().toString()) / wordLen);
        }
        return resTF;
    }
    /**
     * 计算出IDF的值
     * @param all_tf
     * @return resIdf
     */
    public static HashMap<String, Double> idf(HashMap<String,HashMap<String, Double>> all_tf){
        HashMap<String, Double> resIdf = new HashMap<String, Double>();
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
            Double value = (double)Math.log(docNum / (double)Float.parseFloat(entry.getValue().toString()));
            resIdf.put(entry.getKey().toString(), value);
              System.out.println(entry.getKey().toString() + " = " + value);
        }
        return resIdf;
    }
    /**
     * 计算出tf-idf的值
     * @param all_tf
     * @param idfs
     */
    public static void tf_idf(HashMap<String,HashMap<String, Double>> all_tf,HashMap<String, Double> idfs,HashSet setAllData,Map<String, Map<String, Double>> hashMapData){
        HashMap<String, HashMap<String, Double>> resTfIdf = new HashMap<String, HashMap<String, Double>>();
        int docNum =all_tf.keySet().size();
        for (String key : all_tf.keySet()) {
            HashMap<String, Double> tfidf = new HashMap<String, Double>();
            for(String keyValue: all_tf.get(key).keySet() ) {
                Double value = (double)Float.parseFloat(all_tf.get(key).get(keyValue).toString()) * idfs.get(keyValue);
                tfidf.put(keyValue, value);
            }
            resTfIdf.put(key, tfidf);
        }
        System.out.println("TF-IDF for Every file is :");
        DisTfIdf(resTfIdf,setAllData,hashMapData);
    }
    /**
     *
     * @param tfidf  tf-idf的值
     * @param setAllData
     * @param hashMapData  二十个关键词
     */
    public static void DisTfIdf(HashMap<String, HashMap<String, Double>> tfidf,HashSet setAllData,Map<String, Map<String, Double>> hashMapData){
        Iterator iter1 = tfidf.entrySet().iterator();
        while(iter1.hasNext()){
            Map.Entry entrys = (Map.Entry)iter1.next();
            System.out.println("FileName: " + entrys.getKey().toString());
            HashMap<String, Double> temp = (HashMap<String, Double>) entrys.getValue();
            LinkedHashMap<String, Double> sortByMapValue = sortByMapValue(temp);
            System.out.println(sortByMapValue);
            int j=0;
            HashSet setAllDataOne  = new HashSet();
            Map<String,Double>  mapTfidfDate=new HashMap<>();
            for (String key : sortByMapValue.keySet()) {

                if (j<20) {
                    Double val = sortByMapValue.get(key);
                    // System.out.println(key+"="+val);
                    setAllData.add(key);
                    setAllDataOne.add(key);
                }
                mapTfidfDate.put(key,(double)sortByMapValue.get(key));
                j++;
            }
            //System.out.println(setAllData);
            // hashMapData.put(entrys.getKey(),setAllData);
            hashMapData.put((String) entrys.getKey(),mapTfidfDate);
            //求出tf-idf的权重值和特征向量
            // List<Map.Entry<String,Float>> list=new LinkedList<Map.Entry<String, Float>>();
           /* Iterator iter2 = temp.entrySet().iterator();
            while(iter2.hasNext()){
                Map.Entry entry = (Map.Entry)iter2.next();
                System.out.print(entry.getKey().toString() + " = " + entry.getValue().toString() + ", ");
            }*/
        }
    }
    /**
     * 根据value从大到小进行排序
     * @param originalMap
     * @return
     */
    public static  LinkedHashMap<String,Double>  sortByMapValue(Map<String ,Double> originalMap){
        List<Map<String,Double>> sortMapList=new ArrayList<>();
        for (Map.Entry<String, Double> entry : originalMap.entrySet()) {
            String key = entry.getKey();
            Double value=entry.getValue();
            Map<String,Double> orderMap = new HashMap<>();
            orderMap.put(key,value);
            if(sortMapList.size()==0) {
                sortMapList.add(orderMap);
                continue;
            }
            for(int i=0;i<sortMapList.size();i++) {
                Double ordreValue=(double)Float.valueOf(String.valueOf((sortMapList.get(i).values().toArray())[0]));
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
        LinkedHashMap<String,Double> sortedMap=new LinkedHashMap<>();
        for (Map<String, Double> singleMap : sortMapList) {
            @SuppressWarnings("unchecked")
            String orderKey=(String) singleMap.keySet().toArray()[0];
            sortedMap.put(orderKey,singleMap.get(orderKey));
        }
        return sortedMap;
    }
}

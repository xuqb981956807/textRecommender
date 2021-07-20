package com.chrtc.textRecommend.recommend;

import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.*;
import java.util.*;

public class TFIDFTest {



    //word segmentation
    public static ArrayList<String> cutWords(String file) throws IOException{

        ArrayList<String> words = new ArrayList<String>();

        IKAnalyzer analyzer = new IKAnalyzer();
        words = analyzer.split(file);

        return words;
    }

    //term frequency in a file, times for each word
    public static HashMap<String, Integer> normalTF(ArrayList<String> cutwords){
        HashMap<String, Integer> resTF = new HashMap<String, Integer>();

        for(String word : cutwords){
            if(resTF.get(word) == null){
                resTF.put(word, 1);
                System.out.println(word);
            }
            else{
                resTF.put(word, resTF.get(word) + 1);
                System.out.println(word.toString());
            }
        }
        System.out.println(resTF.toString());
        return resTF;
    }

    //term frequency in a file, frequency of each word
    public static HashMap<String, Float> tf(ArrayList<String> cutwords){
        HashMap<String, Float> resTF = new HashMap<String, Float>();

        int wordLen = cutwords.size();
        HashMap<String, Integer> intTF = normalTF(cutwords);

        Iterator iter = intTF.entrySet().iterator(); //iterator for that get from TF
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            resTF.put(entry.getKey().toString(), Float.parseFloat(entry.getValue().toString()) / wordLen);
            System.out.println(entry.getKey().toString() + " = "+  Float.parseFloat(entry.getValue().toString()) / wordLen);
        }
        System.out.println("--------"+resTF.toString());
        return resTF;
    }
  /*  //tf times for file
    public static HashMap<String, Integer> normalTFAllFiles(String dirc) throws IOException{
        HashMap<String, HashMap<String, Integer>> allNormalTF = new HashMap<String, HashMap<String,Integer>>();


        for(String file : filelist){
            HashMap<String, Integer> dict = new HashMap<String, Integer>();
            ArrayList<String> cutwords = ReadFiles.cutWords(file); //get cut word for one file

            dict = ReadFiles.normalTF(cutwords);
            allNormalTF.put(file, dict);
        }
        return allNormalTF;
    }*/

    //tf for all file
    public static HashMap<String, Float> tfAllFiles(String content) throws IOException{

            ArrayList<String> cutwords = cutWords(content); //get cut words for one file


        //HashMap<String, Float> allTF = tf(cutwords);
        HashMap<String, Float> allTF = tf(cutwords);
        System.out.println(allTF.toString());
        return allTF;
    }
    public static HashMap<String, Float> idf(HashMap<String, Float> all_tf){
        HashMap<String, Float> resIdf = new HashMap<String, Float>();
        HashMap<String, Integer> dict = new HashMap<String, Integer>();
        int docNum = all_tf.size();


        Iterator iter_dict = dict.entrySet().iterator();
        while(iter_dict.hasNext()){
            Map.Entry entry = (Map.Entry)iter_dict.next();
            float value = (float)Math.log(docNum / Float.parseFloat(entry.getValue().toString()));
            resIdf.put(entry.getKey().toString(), value);
            System.out.println(entry.getKey().toString() + " = " + value);
        }
        return resIdf;
    }
    public static void tf_idf(HashMap<String, Float> all_tf,HashMap<String, Float> idfs){


            HashMap<String, Float> tfidf = new HashMap<String, Float>();

            Iterator iter = all_tf.entrySet().iterator();

                Map.Entry entry = (Map.Entry)iter.next();
                String word = entry.getKey().toString();
                Float value = (float)Float.parseFloat(entry.getValue().toString()) * idfs.get(word);
                tfidf.put(word, value);



        System.out.println("TF-IDF for Every file is :");
        System.out.println(tfidf);
        DisTfIdf(tfidf);
    }
    public static void DisTfIdf(HashMap<String, Float> tfidf){

            Iterator iter2 = tfidf.entrySet().iterator();
                Map.Entry entry = (Map.Entry)iter2.next();
                System.out.print("======"+entry.getKey().toString() + " = " + entry.getValue().toString() + ", ");
        }


    public static void main(String[] args) throws IOException {
        TFIDFTest tfidfTest=new TFIDFTest();
        String content = "NBA本赛季可谓是受到了不小的打击，自从“莫雷事件”开始，NBA联赛的影响力大大下滑，尤其是中国市场，总裁萧华在之前的采访";
       HashMap<String, Float> all_tf = tfAllFiles(content);
        System.out.println();
        HashMap<String, Float> idfs = idf(all_tf);
        System.out.println("++"+idfs);
        tfidfTest.tf_idf(all_tf, idfs);

    }

}

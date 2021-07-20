package com.chrtc.textRecommend.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewParam {


    private String docid;


    @Override
    public String toString() {
        return "NewParam{" +
                "docid='" + docid + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }

    private String contents;
    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }


    public List<Map> getListContents() {
        return listContents;
    }

    public void setListContents(List<Map> listContents) {
        this.listContents = listContents;
    }

    private List<Map> listContents;

}

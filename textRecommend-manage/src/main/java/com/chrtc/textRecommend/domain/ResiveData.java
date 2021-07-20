package com.chrtc.textRecommend.domain;

import java.util.List;
import java.util.Map;

public class ResiveData {
    public NewParam getNewParam() {
        return newParam;
    }

    public void setNewParam(NewParam newParam) {
        this.newParam = newParam;
    }
    public List<Map> getListContents() {
        return listContents;
    }

    public void setListContents(List<Map> listContents) {
        this.listContents = listContents;
    }

    private List<Map> listContents;

    private NewParam newParam;

    public Map getDocidContentMap() {
        return docidContentMap;
    }

    public void setDocidContentMap(Map docidContentMap) {
        this.docidContentMap = docidContentMap;
    }

    private Map docidContentMap;
}

package com.chrtc.textRecommend.domain;

import java.util.List;

public class ResultData {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private Object data;
}

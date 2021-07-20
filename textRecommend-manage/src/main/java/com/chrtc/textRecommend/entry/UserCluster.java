package com.chrtc.textRecommend.entry;

public class UserCluster {
    private String cluster;
    private String middleCluster;
    private String userList;


    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getMiddleCluster() {
        return middleCluster;
    }

    public void setMiddleCluster(String middleCluster) {
        this.middleCluster = middleCluster;
    }

    public String getUserList() {
        return userList;
    }

    public void setUserList(String userList) {
        this.userList = userList;
    }


    public UserCluster() {
        this.cluster = cluster;
        this.middleCluster = middleCluster;
        this.userList = userList;

    }
}

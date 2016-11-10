package com.sjw.youshi.Bean;


import cn.bmob.v3.BmobUser;

/**
 * Created by jianweishao on 2016/8/1.
 */
public class UserBean extends BmobUser {

    private String nickName;
    private String sex;
    private String qinming;
    private String headerUrl;

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getQinming() {
        return qinming;
    }

    public void setQinming(String qinming) {
        this.qinming = qinming;
    }

    private int allGold;
    public int getAllGold() {
        return allGold;
    }

    public void setAllGold(int allGold) {
        this.allGold = allGold;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}

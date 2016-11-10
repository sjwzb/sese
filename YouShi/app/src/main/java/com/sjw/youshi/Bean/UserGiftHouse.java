package com.sjw.youshi.Bean;

/**
 * Created by jianweishao on 2016/8/16.
 */
public class UserGiftHouse extends BaseBean{
    private String userID;
    private UserBean fromUser;
    private GiftBean giftBean;
    private String glod;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public UserBean getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserBean fromUser) {
        this.fromUser = fromUser;
    }

    public GiftBean getGiftBean() {
        return giftBean;
    }

    public void setGiftBean(GiftBean giftBean) {
        this.giftBean = giftBean;
    }

    public String getGlod() {
        return glod;
    }

    public void setGlod(String glod) {
        this.glod = glod;
    }
}

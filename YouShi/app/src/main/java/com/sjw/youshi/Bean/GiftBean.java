package com.sjw.youshi.Bean;

/**
 * Created by jianweishao on 2016/8/9.
 */
public class GiftBean extends BaseBean {
    private String giftName;
    private int giftIcon;
    private String giftPrice;
    private UserBean userBean;
    private String videoId;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getGiftIcon() {
        return giftIcon;
    }

    public void setGiftIcon(int giftIcon) {
        this.giftIcon = giftIcon;
    }

    public String getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(String giftPrice) {
        this.giftPrice = giftPrice;
    }
}

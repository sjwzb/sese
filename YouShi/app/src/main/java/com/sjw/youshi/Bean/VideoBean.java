package com.sjw.youshi.Bean;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by jianweishao on 2016/7/29.
 */
public class VideoBean extends BaseBean {
    private String videoName;
    private UserBean videoUser;
    private int playCount;
    private BmobFile bmobFile;
    private BmobFile videoCover;
    private String playUrl;
    private String coverUrl;
    private int price;
    private boolean isPay;

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public BmobFile getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(BmobFile videoCover) {
        this.videoCover = videoCover;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public BmobFile getBmobFile() {
        return bmobFile;
    }

    public void setBmobFile(BmobFile bmobFile) {
        this.bmobFile = bmobFile;
    }

    public String getVideoName() {
        return videoName;
    }

    public UserBean getVideoUser() {
        return videoUser;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public void setVideoUser(UserBean videoUser) {
        this.videoUser = videoUser;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }
}

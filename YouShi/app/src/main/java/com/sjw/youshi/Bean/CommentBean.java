package com.sjw.youshi.Bean;

/**
 * Created by jianweishao on 2016/8/9.
 */
public class CommentBean extends BaseBean {
    private String comment;
    private String videoId;
    private UserBean commentUser;
    private VideoBean commentVideo;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserBean getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(UserBean commentUser) {
        this.commentUser = commentUser;
    }

    public VideoBean getCommentVideo() {
        return commentVideo;
    }

    public void setCommentVideo(VideoBean commentVideo) {
        this.commentVideo = commentVideo;
    }
}

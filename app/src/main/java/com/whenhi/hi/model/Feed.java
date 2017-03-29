package com.whenhi.hi.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 王雷 on 2017/1/6.
 */

public class Feed implements Serializable {
    private int id;
    private String idStr;
    private String content;
    private int userId;
    private long createTime;
    private int opState;
    private  int commentCount;
    private  int likeCount;
    private String pubTimeNice;
    private long pubTimeStamp;
    private int feedCategory;
    private int likeState;
    private  String userName;
    private String userLogo;
    private String source;
    private String imageUrl;
    private String playUrl;
    private List<Image> resList;
    private String shareUrl;
    private List<Comment> comments;
    private int favoriteCount;
    private int favoriteState;
    private int shareCount;
    private String maskUrl;
    private String maskContent;
    private boolean click;
    private String qiniuPlayUrl;
    private String linkUrl;
    private String title;
    private String summary;
    private int type;//1代表邀请好友，2代表彩蛋 3代表充值成功

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQiniuPlayUrl() {
        return qiniuPlayUrl;
    }

    public void setQiniuPlayUrl(String qiniuPlayUrl) {
        this.qiniuPlayUrl = qiniuPlayUrl;
    }

    public boolean getClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public String getMaskUrl() {
        return maskUrl;
    }

    public void setMaskUrl(String maskUrl) {
        this.maskUrl = maskUrl;
    }

    public String getMaskContent() {
        return maskContent;
    }

    public void setMaskContent(String maskContent) {
        this.maskContent = maskContent;
    }


    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getOpState() {
        return opState;
    }

    public void setOpState(int opState) {
        this.opState = opState;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public long getPubTimeStamp() {
        return pubTimeStamp;
    }

    public void setPubTimeStamp(long pubTimeStamp) {
        this.pubTimeStamp = pubTimeStamp;
    }

    public String getPubTimeNice() {
        return pubTimeNice;
    }

    public void setPubTimeNice(String pubTimeNice) {
        this.pubTimeNice = pubTimeNice;
    }

    public int getFeedCategory() {
        return feedCategory;
    }

    public void setFeedCategory(int feedCategory) {
        this.feedCategory = feedCategory;
    }

    public int getLikeState() {
        return likeState;
    }

    public void setLikeState(int likeState) {
        this.likeState = likeState;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public List<Image> getResList() {
        return resList;
    }

    public void setResList(List<Image> resList) {
        this.resList = resList;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getFavoriteState() {
        return favoriteState;
    }

    public void setFavoriteState(int favoriteState) {
        this.favoriteState = favoriteState;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }
}

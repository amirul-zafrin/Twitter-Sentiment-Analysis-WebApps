package org.backEnd.twitterSA.model;

public class Twitter {

    private String text;
    private String userName;
    private String screenName;
    private String picUrl;
    private String id;
    private String prob;
    private String sentiment;

    public Twitter(String text, String userName, String screenName, String picUrl, String id, String prob, String sentiment) {
        this.text = text;
        this.userName = userName;
        this.screenName = screenName;
        this.picUrl = picUrl;
        this.id = id;
        this.prob = prob;
        this.sentiment = sentiment;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProb() {
        return prob;
    }

    public void setProb(String prob) {
        this.prob = prob;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }
}

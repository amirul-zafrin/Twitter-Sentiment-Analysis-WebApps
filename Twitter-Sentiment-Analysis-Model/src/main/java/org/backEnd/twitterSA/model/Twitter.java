package org.backEnd.twitterSA.model;

public class Twitter {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;
    private String twtID;

    public Twitter(Integer id, String twtID) {
        this.id = id;
        this.twtID = twtID;
    }

    public String getTwtID() {
        return twtID;
    }

    public void setTwtID(String twtID) {
        this.twtID = twtID;
    }

}

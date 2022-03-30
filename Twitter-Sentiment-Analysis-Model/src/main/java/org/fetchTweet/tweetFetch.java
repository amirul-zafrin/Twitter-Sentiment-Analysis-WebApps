package org.fetchTweet;

import org.backEnd.twitterSA.model.Twitter;
import twitter4j.Status;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class tweetFetch {

    private static PrintStream consolePrint;
    private List<Status> statuses;
    static Twitterer twt = new Twitterer(consolePrint);

    public static ArrayList<Twitter> getTweetId(String keyWord) {
        ArrayList<Long> twtID = twt.getTwtId(keyWord);
        ArrayList<Twitter> twt = new ArrayList<>();
        int idx = 0;
        for(Long t: twtID) {
            ++idx;
            twt.add(new Twitter(idx, t.toString()));
        }
        return twt;
    }

    public static ArrayList<Twitter> getDefTwtId() {
        ArrayList<Long> twtID = twt.getTwtIdFromUser("elonmusk");
        ArrayList<Twitter> twt = new ArrayList<>();
        int idx = 0;
        for(Long t: twtID) {
            ++idx;
            twt.add(new Twitter(idx, t.toString()));
        }
        return twt;
    }

}

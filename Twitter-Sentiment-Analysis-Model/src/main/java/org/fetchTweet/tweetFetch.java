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

//    public static void main(String[] args) throws IOException, TwitterException {
//        Twitterer bigBird = new Twitterer(consolePrint);
//        System.out.println("Trending topics");
//
//        //Malaysia woeid: 23424901
//        bigBird.trendingTwt(23424901);
//
//        Scanner scan = new Scanner(System.in);
//        System.out.println("Please enter a Twitter handle, do not include '@' symbol (type 'quit' to quit)");
////        System.out.println("Please enter a word");
//        String twitter_handle = scan.next();
//
//        while(!"quit".equalsIgnoreCase(twitter_handle)) {
//            bigBird.queryHandle(twitter_handle);
//            System.out.println("Please enter a Twitter handle, do not include '@' symbol (type 'quit' to quit)");
////            System.out.println("Please enter a word");
//            twitter_handle = scan.next();
//        }
//    }

//    public static void main(String[] args) throws IOException, TwitterException {
//        Twitterer bigBird = new Twitterer(consolePrint);
//        bigBird.queryHandle("elonmusk");
//
//    }

    public static ArrayList<Long> getDefTweetId() {
        return twt.getTwtIdFromUser("elonmusk");
    }

    public static ArrayList<Long> getTweetId(String keyWord) {
        return twt.getTwtId(keyWord);
    }

    public static ArrayList<Twitter> getTwtId() {
        ArrayList<Long> twtID = twt.getTwtIdFromUser("elonmusk");
        ArrayList<Twitter> twt = new ArrayList<>();
        int idx = 0;
        for(Long t: twtID) {
            ++idx;
            twt.add(new Twitter(idx, t.toString()));
        }
        return twt;
    }

//    public static void main(String[] args) {
////        System.out.println(getDefTweetId());
//        System.out.println(getTweetId("banjir"));
//    }

}

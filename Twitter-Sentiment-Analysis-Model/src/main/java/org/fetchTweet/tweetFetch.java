package org.fetchTweet;
import twitter4j.*;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class tweetFetch {

    private Twitter twitter;
    private static PrintStream consolePrint;
    private List<Status> statuses;

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

    public static void main(String[] args) throws IOException, TwitterException {
        Twitterer bigBird = new Twitterer(consolePrint);
        bigBird.queryHandle("elonmusk");

    }

}

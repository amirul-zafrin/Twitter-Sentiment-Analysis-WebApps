package org.fetchTweet;
import twitter4j.*;

import java.io.IOException;
import java.io.PrintStream;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Twitterer {
    private Twitter twitter;
    private PrintStream consolePrint;
    private List<Status> statuses;
    private ArrayList<Long> twtId;
    public static String lastMonth = LocalDateTime.now().minusMonths(1).toLocalDate().toString();
    private int radiusKM = 10;
    private int woeid = 23424901;

    public Twitterer(PrintStream console) {
        // Makes an instance of Twitter - this is re-useable and thread safe.
        // Connects to Twitter and performs authorizations.
        twitter = TwitterFactory.getSingleton();
        consolePrint = console;
        statuses = new ArrayList<Status>();
        twtId = new ArrayList<Long>();
    }

    public void queryHandle(String handle) throws TwitterException, IOException {
        statuses.clear();
        twtId.clear();
        getTwtId(handle);
        System.out.println(twtId);
    }

    public ArrayList<Long> getTwtId(String handle) {

        statuses.clear();
        twtId.clear();
        Query query = new Query(handle + " -filter:retweet -filter:media");
        query.setCount(10);
        query.setSince(lastMonth);

        try {
            QueryResult result = twitter.search(query);
            System.out.println("Count: "+result.getTweets().size());
            statuses.addAll(result.getTweets());

            for(Status tweet: result.getTweets()) {
                twtId.add(tweet.getId());
            }
        }catch (TwitterException e) {
            e.printStackTrace();
        }
        return twtId;
    }

    public ArrayList<Long> getTwtIdFromUser(String handle) {
        statuses.clear();
        twtId.clear();

        Query query = new Query("from:"+ handle + " -filter:retweet -filter:media");
        query.setCount(10);
        query.setSince(lastMonth);

        try {
            QueryResult result = twitter.search(query);
            System.out.println("Count: "+result.getTweets().size());
            statuses.addAll(result.getTweets());

            for(Status tweet: result.getTweets()) {
                twtId.add(tweet.getId());
            }

        }catch (TwitterException e) {
            e.printStackTrace();
        }
        return twtId;
    }

    private void fetchTwt(String handle) {
        Query query = new Query("from:"+ handle + " -filter:retweet -filter:media");
        query.setCount(10);
        query.setSince(lastMonth);

        try {
            QueryResult result = twitter.search(query);
            int counter = 0;
            System.out.println("Count: "+result.getTweets().size());

            for(Status tweet: result.getTweets()) {
                counter++;
                System.out.println(String.format("Tweet #%d: @%s  tweeted \"%s\"", counter, tweet.getUser().getName(),
                        tweet.getText()));
            }

        }catch (TwitterException e) {
            e.printStackTrace();
        }
        System.out.println();

    }

//    Malaysia (woeid:23424901)
    public void trendingTwt(int woeid) throws TwitterException {
        this.woeid = woeid;
        Trends trends = twitter.getPlaceTrends(woeid);
        System.out.println(trends.getAsOf());
        for (int i = 0; i < 10; i++) {
            System.out.println(trends.getTrends()[i].getName());
        }
    }

    public void locationWoeid() throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        ResponseList<Location> locations;
        locations = twitter.getAvailableTrends();
        System.out.println("Showing available trends");
        for (Location location : locations) {
            System.out.println(location.getName() + " (woeid:" + location.getWoeid() + ")");
        }
    }

    /**
     * This method will display tweets that have location with it
     * Need to provide lat/long of the location
     * And also the radius from the lat/long
     * @param searchTerm the term to search for
     * @param latitude the latitude of the location
     * @param longitude the latitude of the location
     * @param radiusKM the radius in kilometer (default = 10KM)
     */

    public void saQuery(String searchTerm, double latitude, double longitude, int radiusKM) {

        this.radiusKM = radiusKM;
        Query query = new Query(searchTerm + " -is:retweet");
        query.setCount(100);
        query.setGeoCode(new GeoLocation(latitude, longitude), radiusKM, Query.KILOMETERS);
        query.setSince("2022-01-01");

        try {
            QueryResult result = twitter.search(query);
            int counter = 0;
            System.out.println("Count: "+result.getTweets().size());

            for(Status tweet: result.getTweets()) {
                counter++;
                System.out.println(String.format("Tweet #%d: @%s  tweeted \"%s\"", counter, tweet.getUser().getName(),
                        tweet.getText()));
            }

        }catch (TwitterException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public void seachByWords(String terms) {
        Query query = new Query(terms + " -is:retweet");

        try {
            QueryResult result = twitter.search(query);
            statuses.addAll(result.getTweets());

            for (Status tweet : result.getTweets()) {
                System.out.println(String.format("@%s : %s", tweet.getUser().getScreenName(), tweet.getText()));
            }

        } catch(TwitterException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public void searchByScreenName(String username) {
        ResponseList<User> users;
        try {
            users = twitter.searchUsers(username, 0);
            for (User user: users) {
                if(user.getStatus() != null) {
                    System.out.println(String.format("@%s - %s", user.getScreenName(),user.getStatus().getText()));
                } else {
                    System.out.println("@" + user.getScreenName());
                }
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }

    }

}
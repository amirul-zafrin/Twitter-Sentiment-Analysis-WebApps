package org.backEnd.twitterSA.controller;

import org.backEnd.twitterSA.model.Twitter;
import org.fetchTweet.tweetFetch;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@CrossOrigin
public class ModelController {

    @GetMapping("/defaultTweet")
    public ArrayList<Twitter> getTwt() {
//        ArrayList<Long> defTweetId = tweetFetch.getDefTweetId();
//        ArrayList<Twitter> twt = new ArrayList<>();
//        twt.clear();
//        int idx = 0;
//        for(Long t: defTweetId) {
//            ++idx;
//            twt.add(new Twitter(idx, t));
//        }
//        return twt;
        return tweetFetch.getTwtId();
    }

//    @GetMapping("/queryResult")
//    public ArrayList<Twitter> queryParam(@RequestParam(name = "keyword") String handle) {
//
//    }

}

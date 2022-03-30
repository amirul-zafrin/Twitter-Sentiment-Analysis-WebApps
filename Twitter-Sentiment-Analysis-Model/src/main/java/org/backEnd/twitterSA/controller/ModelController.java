package org.backEnd.twitterSA.controller;

import org.backEnd.twitterSA.model.Twitter;
import org.fetchTweet.tweetFetch;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ModelController {

    @GetMapping("/defaultTweet")
    public ArrayList<Twitter> getTwt() {
        return tweetFetch.getDefTwtId();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/queryResult")
    public ArrayList<Twitter> queryParam(@NotBlank @NotNull @RequestParam(name = "keyword") String handle) {
        return tweetFetch.getTweetId(handle);
    }

}

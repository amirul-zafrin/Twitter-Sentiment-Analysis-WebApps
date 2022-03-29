import axios from "axios";

const TWITTER_SA_API = "http://localhost:8080/defaultTweet";

class TwitterService {

    getTweets() {
        return axios.get(TWITTER_SA_API);
    }

}

export default new TwitterService();
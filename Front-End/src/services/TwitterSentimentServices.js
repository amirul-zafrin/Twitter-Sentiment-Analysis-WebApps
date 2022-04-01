import axios from "axios";

const TWITTER_SA_API = "http://localhost:8080/queryResult?keyword=";

class TwitterSentimentSearchService {

    getTweets(props) {
        return axios.get(`${TWITTER_SA_API}${props}`)
    }

}

export default new TwitterSentimentSearchService()

// "react": "^16.13.1",
    // "react-dom": "^16.13.1",
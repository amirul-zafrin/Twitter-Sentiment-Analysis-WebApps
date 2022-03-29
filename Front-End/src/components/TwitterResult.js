import { Typography } from '@mui/material'
import React, { Component } from 'react'
import Tweet from '../components/Tweet'
import TwitterService from '../services/TwitterService'

class TwitterResult extends Component {
    constructor(props) {
        super(props)

        this.state = {
            twitter: []
        }
    }

    componentDidMount() {
        TwitterService.getTweets().then((res) => {
            this.setState({ twitter: res.data});
            
        })
    }

    render() {
        return (
        //     this.state.twitter.map(
        //         twt => <Tweet id={twt.twtID}/>)
        // )
        // this.state.twitter.map(twt => console.log(twt.twtID))
            this.state.twitter.map(
                twt => <Tweet key={twt.id} id={twt.twtID} />
                // twt => console.log(twt.twtID)
                // twt => <h3>{twt.twtID}</h3>
                // twt => <tweet key={twt.id} id={twt.twtID}/> 
                )
        )

    }
}

export default TwitterResult

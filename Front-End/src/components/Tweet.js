import Grid from '@mui/material/Grid'
import { Tweet } from 'react-twitter-widgets'

const tweet = ( { theme, id } ) => {
  return (
      <Grid item xs={3} display='flex'>
        <Tweet tweetId={id} options={{height: '400'}}/>
      </Grid>
  )
}

export default tweet
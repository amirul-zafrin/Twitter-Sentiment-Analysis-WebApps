import * as React from 'react';
import TwitterResult from '../components/TwitterResult';
import Tweet from '../components/Tweet'
import Container from '@mui/material/Container'
import Grid from '@mui/material/Grid';
import BottomScore from '../components/BottomScore';
import SearchAppBar from '../components/AppBar'

const Test = () => {
  return (
    <div>
    <SearchAppBar/>
    <Container>
        <Grid container spacing={2} justify='center'>
            <TwitterResult/>
            <BottomScore score='0.6'/>     
        </Grid>

    </Container>
</div>
  )
}

export default Test
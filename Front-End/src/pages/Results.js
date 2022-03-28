import Tweet from '../components/Tweet'
import Container from '@mui/material/Container'
import Grid from '@mui/material/Grid';
import BottomScore from '../components/BottomScore';
import SearchAppBar from '../components/AppBar'

const Search = () => {
  return (
    <div>
        <SearchAppBar/>
        <Container>
            <Grid container spacing={2} justify='center'>
                <Tweet theme="dark" id="1507168910835081225"/>
                <Tweet id="1507168910835081225"/>
                <Tweet id="1507168910835081225"/>
                <Tweet id="1507168910835081225"/>
                <Tweet id="1507168910835081225"/>
                <Tweet id="1507168910835081225"/>
                <Tweet id="1507168910835081225"/>
                <Tweet id="1507168910835081225"/>
                <Tweet id="1507168910835081225"/>
                <Tweet id="1507168910835081225"/>
                <Tweet id="1507168910835081225"/>
                <Tweet id="1507168910835081225"/>
                <BottomScore score='0.6'/>     
            </Grid>

        </Container>
    </div>
  )
}

export default Search
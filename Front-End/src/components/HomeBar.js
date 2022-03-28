import Typography from '@mui/material/Typography';
import Searchbar from '../components/SearchBar';
import Grid from '@mui/material/Grid';

export default function HomeBar() {
  return (
    <Grid 
      container
      justify='center'
      alignItems='center'
      direction='column'
      style={{ minHeight: '100vh'}}
      spacing={3}
      >
      <Grid item>
        <Typography
          variant="h1"
          component="div"
          sx={{ flexGrow: 2, display: { xs: 'none', sm: 'block' }}}
          color='primary'
        >
          Twitter Analysis
        </Typography>
      </Grid>
      <Grid item></Grid>
      <Grid item>
        <Typography variant='h4' component="div">
          Search any Malay word for sentiment analysis and score!
        </Typography>
      </Grid>
      <Grid item></Grid>
      <Grid item>
        <Searchbar/>
      </Grid>
    </Grid>
    
    
  );
}
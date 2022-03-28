import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import React from 'react'

const BottomScore = ( {score} ) => {
  return (
    <Paper component={Stack} direction="column" justifyContent="center" 
    sx={{ position: 'fixed', bottom: 0, left: 0, right: 0, height: 70 }} elevation={3}>
        <Typography variant='h4' component="h4" sx={{textAlign:'center'}}>
            Score: {score}
        </Typography>
    </Paper>
  )
}
export default BottomScore
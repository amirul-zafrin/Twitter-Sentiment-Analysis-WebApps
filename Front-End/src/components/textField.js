import * as React from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';

export default function BasicTextFields() {
  return (
    <Stack spacing={2} direction="row">
    <Box
      component="form"
      sx={{
        '& > :not(style)': { m: 1, width: '50ch' },
      }}
      noValidate
      autoComplete="on"
      textAlign='center'
      verticalAlign='center'
    >
      <TextField
          id="outlined-basic"
          variant="outlined"
          fullWidth
          label="Search"
        />

    </Box>
    <Button variant="contained" sx={{height:'50%'}}>Search</Button>
    </Stack>
  );
}
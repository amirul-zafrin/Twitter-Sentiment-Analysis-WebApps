import React from "react";
import { useState } from "react";
import TextField from "@mui/material/TextField";

function Searchbar(props) {
  const { 
    onSearch 
  } = props;

  const [searchText, setSearchText] = useState('')

  const handleInput = (e) => {
    const text = e.target.value
    setSearchText(text)
  }

  const handleEnterKeyPressed = (e) => {
    if(e.key=== 'Enter') {
    console.log(searchText)
    }
  }

  return (
    <div>
      <div className="control">
      <TextField id="outlined-basic" label="Search" variant="outlined"
      onChange={handleInput} onKeyPress={handleEnterKeyPressed}
      type="text" value={searchText} 
      placeholder="Enter keyword"
      margin="dense"
      style = {{width: 450}}
      />
      </div>
    </div>
  );
}

export default Searchbar;
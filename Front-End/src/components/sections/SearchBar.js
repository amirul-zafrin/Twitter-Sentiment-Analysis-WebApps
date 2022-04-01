import React, { useState } from "react";
import Input from "../elements/Input";

function SearchBar(props) {
  const { onSearch } = props;
  const [searchText, setSearchText] = useState("");

  const handleInput = (e) => {
    const text = e.target.value;
    setSearchText(text);
  };

  const handleKeyPressed = (e) => {
    if (e.key === "Enter") {
      onSearch(searchText);
    }
  };

  return (
    <Input
      id="kw-text"
      type="text"
      label="Subscribe"
      labelHidden
      hasIcon="right"
      placeholder="Word to Search"
      onChange={handleInput}
      onKeyPress={handleKeyPressed}
    >
      <svg width="16" height="12" color="#00acee">
        <path
          d="M9 5H1c-.6 0-1 .4-1 1s.4 1 1 1h8v5l7-6-7-6v5z"
          fill="#376DF9"
        />
      </svg>
    </Input>
  );
}

export default SearchBar;

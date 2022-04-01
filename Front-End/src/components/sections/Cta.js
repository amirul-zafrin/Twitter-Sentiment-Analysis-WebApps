import React, { useState } from "react";
import PropTypes from "prop-types";
import classNames from "classnames";
import { SectionProps } from "../../utils/SectionProps";
import TwitterSentimentServices from "../../services/TwitterSentimentServices";
import SearchBar from "./SearchBar";
import TweetPaper from "../elements/TweetPaper";
import Grid from "@mui/material/Grid";
import Container from "@mui/material/Container";
import { TransitionGroup } from "react-transition-group";
import { Fade } from "react-reveal";

const propTypes = {
  ...SectionProps.types,
  split: PropTypes.bool,
};

const defaultProps = {
  ...SectionProps.defaults,
  split: false,
};

const Cta = ({
  className,
  topOuterDivider,
  bottomOuterDivider,
  topDivider,
  bottomDivider,
  hasBgColor,
  invertColor,
  split,
  ...props
}) => {
  const [state, setState] = useState([]);

  const onSearch = async (text) => {
    const result = await TwitterSentimentServices.getTweets(text);
    setState(result.data);
  };

  const outerClasses = classNames(
    "cta section center-content-mobile reveal-from-bottom",
    topOuterDivider && "has-top-divider",
    bottomOuterDivider && "has-bottom-divider",
    hasBgColor && "has-bg-color",
    invertColor && "invert-color",
    className
  );

  const innerClasses = classNames(
    "cta-inner section-inner",
    topDivider && "has-top-divider",
    bottomDivider && "has-bottom-divider",
    split && "cta-split"
  );

  return (
    <div {...props} className={outerClasses}>
      <div
        className={innerClasses}
        style={{
          backgroundColor: "#00acee",
          height: "10px",
        }}
      >
        <div className="cta-slogan">
          <h3 className="m-0" style={{ fontSize: "30px" }}>
            This free tool can perform sentiment analysis on any Twitter search
            quickly.
          </h3>
        </div>
        <div className="cta-action">
          <SearchBar onSearch={onSearch} />
        </div>
      </div>

      <Container sx={{ marginTop: 5 }}>
        <Grid container spacing={3} justify="center">
          {state.map((t) => (
            // {/* <Tweet userName={t.userName} picUrl={t.picUrl} textTwt={t.text} sentiment={t.sentiment} score={t.prob}/> */}

            <TweetPaper t={t} />
          ))}
        </Grid>
      </Container>
    </div>
  );
};

Cta.propTypes = propTypes;
Cta.defaultProps = defaultProps;

export default Cta;

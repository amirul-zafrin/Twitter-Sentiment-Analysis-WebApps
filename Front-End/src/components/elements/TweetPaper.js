import React from "react";
import {
  Box,
  Typography,
  Grid,
  Card,
  CardHeader,
  Divider,
} from "@mui/material";

export default function TweetPaper({ t }) {
  return (
    <Grid item xs={3}>
      <Card sx={{ height: 500 }}>
        <CardHeader
          avatar={
            <img
              src={t.picUrl}
              style={{ width: 50, height: 50, borderRadius: 400 / 2 }}
            />
          }
          title={t.userName}
        />

        <Divider variant="middle" />
        <Box>
          <Typography
            display="block"
            color={
              t.sentiment === "Positive"
                ? "green"
                : t.sentiment === "Negative"
                ? "red"
                : "text.secondary"
            }
          >
            <Box sx={{ typography: "button", textAlign: "center" }}>
              {t.sentiment}
            </Box>
            <Box sx={{ typography: "overline", textAlign: "center" }}>
              Confident: {Math.trunc(Number(t.prob) * 100)}%
            </Box>
          </Typography>
        </Box>

        <Divider variant="middle" />

        <Box marginTop={0} padding={1}>
          <Typography
            variant="body1"
            color="text.secondary"
            sx={{ fontSize: 18 }}
          >
            {t.text}
          </Typography>
        </Box>
      </Card>
    </Grid>
  );
}

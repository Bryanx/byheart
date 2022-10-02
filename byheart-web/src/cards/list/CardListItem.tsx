import { Card } from "../models/Card";
import React from "react";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import { Delete } from "@mui/icons-material";
import IconButton from "@mui/material/IconButton";

interface CardListItemProps {
  index: number;
  card?: Card;
  color?: number;
}

export const CardListItem: React.FC<CardListItemProps> = ({ index, card }) => (
  <Box
    sx={{
      width: "100%",
      display: "flex",
      flexDirection: "row",
      backgroundColor: "card.main",
      p: 4,
      borderRadius: 2,
    }}
  >
    <Box sx={{ width: "50%", display: "flex" }}>
      <Typography>{index + 1}.&nbsp;</Typography>
      <Typography variant="body1">{card?.question || ""}</Typography>
    </Box>
    <Box sx={{ width: "2px", minHeight: "100%", backgroundColor: "white", mx: 3 }}></Box>
    <Box sx={{ width: "50%", display: "flex", justifyContent: "space-between" }}>
      <Typography>{card?.answer || ""}</Typography>
      <IconButton>
        <Delete />
      </IconButton>
    </Box>
  </Box>
);

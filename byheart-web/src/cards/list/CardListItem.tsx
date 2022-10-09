import { Card } from "../models/Card";
import React from "react";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import { Delete } from "@mui/icons-material";
import IconButton from "@mui/material/IconButton";
import { useAppDispatch, useAppSelector } from "../../app/hooks";
import { selectActivePileColor } from "../../piles/pileSlice";
import { InlineEditText } from "../../shared/components/InlineEditText";
import { updateCard } from "../cardSlice";

interface CardListItemProps {
  index: number;
  card: Card;
}

export const CardListItem: React.FC<CardListItemProps> = ({ index, card }) => {
  const color = useAppSelector(selectActivePileColor);
  const [question, setQuestion] = React.useState(card?.question || "");
  const [answer, setAnswer] = React.useState(card?.answer || "");
  const dispatch = useAppDispatch();

  const onSubmit = () => {
    dispatch(updateCard({ ...card, answer, question }));
  };

  return (
    <Box
      sx={{
        height: 75,
        width: "100%",
        display: "flex",
        flexDirection: "row",
        alignItems: "stretch",
        backgroundColor: "card.main",
        p: 2,
        borderRadius: 2,
      }}
    >
      <Box sx={{ width: "50%", display: "flex", alignItems: "center" }}>
        <Typography>{index + 1}.&nbsp;</Typography>
        <InlineEditText
          value={question}
          onChange={(value) => setQuestion(value)}
          onSubmit={onSubmit}
          color={color}
        />
      </Box>
      <Box
        sx={{
          width: "2px",
          my: 1,
          backgroundColor: "txt.main",
          mx: 3,
          opacity: "25%",
        }}
      ></Box>
      <Box
        sx={{
          width: "50%",
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <InlineEditText
          value={answer}
          onChange={(value) => setAnswer(value)}
          onSubmit={onSubmit}
          color={color}
        />
        <IconButton sx={{ opacity: "25%" }}>
          <Delete />
        </IconButton>
      </Box>
    </Box>
  );
};

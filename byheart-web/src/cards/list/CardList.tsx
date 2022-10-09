import { CardListItem } from "./CardListItem";
import { CardListItemPlaceholder } from "./CardListItemPlaceholder";
import { range } from "lodash-es";
import React from "react";
import { useAppSelector } from "../../app/hooks";
import { selectCardList } from "../cardSlice";
import Box from "@mui/material/Box";
import { AddCardListItem } from "./AddCardListItem";

interface CardListProps {
  loading: boolean;
}

export const CardList: React.FC<CardListProps> = ({ loading }) => {
  const cards = useAppSelector(selectCardList);

  return (
    <Box sx={{ mt: 3, width: "100%" }}>
      {loading
        ? range(0, 18).map((v, i) => <CardListItemPlaceholder key={i} />)
        : cards
            ?.slice()
            .sort((card1, card2) => card1.id - card2.id)
            .map((card, index) => (
              <Box key={card.id} sx={{ mb: 2 }}>
                <CardListItem index={index} card={card} />
              </Box>
            ))}
      <AddCardListItem />
    </Box>
  );
};

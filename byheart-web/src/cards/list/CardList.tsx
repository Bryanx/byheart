import { Pile } from "../../piles/models/Pile";
import { CardListItem } from "./CardListItem";
import { CardListItemPlaceholder } from "./CardListItemPlaceholder";
import { range } from "lodash-es";
import React from "react";
import { useAppSelector } from "../../app/hooks";
import { selectCardList } from "../cardSlice";
import Box from "@mui/material/Box";
import { Card } from "../models/Card";

interface CardListProps {
  pile?: Pile;
  loading: boolean;
}

export const CardList: React.FC<CardListProps> = ({ pile, loading }) => {
  const cards = useAppSelector(selectCardList);

  const getCard = (card: Card, index: number) => (
    <Box sx={{ mb: 2 }}>
      <CardListItem key={card.id} index={index} card={card} color={pile?.color} />
    </Box>
  );

  return (
    <Box sx={{ mt: 3, width: "100%" }}>
      {loading
        ? range(0, 18).map((v, i) => <CardListItemPlaceholder key={i} />)
        : cards?.map((card, index) => getCard(card, index))}
    </Box>
  );
};

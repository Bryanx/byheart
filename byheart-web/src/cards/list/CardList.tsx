import { Pile } from "../../piles/models/Pile";
import { CardListItem } from "./CardListItem";
import { CardListItemPlaceholder } from "./CardListItemPlaceholder";
import { range } from "lodash-es";
import React from "react";
import { useAppSelector } from "../../app/hooks";
import { selectCardList } from "../cardSlice";
import Box from "@mui/material/Box";

interface CardListProps {
  pile?: Pile;
  loading: boolean;
}

export const CardList: React.FC<CardListProps> = ({ pile, loading }) => {
  const cards = useAppSelector(selectCardList);
  return (
    <Box sx={{ mt: 3 }}>
      {loading
        ? range(0, 18).map((v, i) => <CardListItemPlaceholder key={i} />)
        : cards?.map((card) => <CardListItem key={card.id} card={card} color={pile?.color} />)}
    </Box>
  );
};

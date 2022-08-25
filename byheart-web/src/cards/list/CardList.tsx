import { Pile } from "../../piles/models/Pile";
import CardListItem from "./CardListItem";
import { CardListItemPlaceholder } from "./CardListItemPlaceholder";
import { range } from "lodash";
import React from "react";
import { useAppSelector } from "../../app/hooks";
import { selectCardList } from "../cardSlice";

interface CardListProps {
  pile?: Pile;
  loading: boolean;
}

export const CardList: React.FC<CardListProps> = ({ pile, loading }) => {
  const cards = useAppSelector(selectCardList);
  return (
    <div className="mt-8 px-5">
      {loading
        ? range(0, 18).map((v, i) => <CardListItemPlaceholder key={i} />)
        : cards?.map((card) => <CardListItem key={card.id} card={card} color={pile?.color} />)}
    </div>
  );
};

export default CardList;

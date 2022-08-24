import { Pile } from "../../piles/models/Pile";
import CardListItem from "./CardListItem";
import { CardListItemPlaceholder } from "./CardListItemPlaceholder";
import { range } from 'lodash';
import React from "react";

interface CardListProps {
  pile?: Pile;
  loading: boolean;
}

export const CardList: React.FC<CardListProps> = ({ pile, loading }) => (
    <div className="mt-8 px-5">
      {
        loading
            ? range(0, 18).map((v, i, arr) => <CardListItemPlaceholder key={i}/>)
            : pile?.cards?.map(card =>
                <CardListItem
                    key={card.id}
                    card={card}
                    color={pile?.color}/>
            )
      }
    </div>
);

export default CardList

import Pile from "../../piles/models/Pile";
import CardListItem from "./CardListItem";
import { CardListItemPlaceholder } from "./CardListItemPlaceholder";
import { range } from 'lodash';

export default function CardList(props: { pile?: Pile, loading: boolean }) {
  return (    
    <div className="mt-8 px-5">
      {
        props.loading 
        ? range(0, 18).map((v, i, arr) => <CardListItemPlaceholder key={i} />)
        : props.pile?.cards?.map(card => 
          <CardListItem 
            key={card.id} 
            card={card} 
            color={props.pile?.color} />
          )
      }
    </div>
  )
}
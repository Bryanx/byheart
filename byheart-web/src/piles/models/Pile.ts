import {Card} from "../../cards/models/Card";

export default interface Pile {
  id: number;
  name: string;
  color?: number;
  cards?: Card[];
}
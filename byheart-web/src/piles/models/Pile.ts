import { Card } from "../../cards/models/Card";

export interface Pile {
  id: number;
  name: string;
  color?: number;
  cards?: Card[];
}

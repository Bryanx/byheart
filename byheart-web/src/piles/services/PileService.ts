import {BehaviorSubject, delay, map, Observable} from "rxjs";
import Pile from "../models/Pile";

export const rawPiles$ = new BehaviorSubject<Pile[]>([]);

// const querySnapshot = await getDocs(collection(app, "users"));
// querySnapshot.forEach((doc) => {
//   console.log(`${doc.id} => ${doc.data()}`);
// });


rawPiles$.next([
  { id: 1, name: "French", color: -30107, cards: [{id: 0, question: "Maison", answer: "House"}, {id: 1, question: "Vélo", answer: "Bicycle"}, {id: 2, question: "Vélo", answer: "Bicycle"}, {id: 3, question: "Vélo", answer: "Bicycle"}, {id: 4, question: "Vélo", answer: "Bicycle"}, {id: 5, question: "Vélo", answer: "Bicycle"}] },
  { id: 2, name: "Periodic Table", color: -8336444, cards: [{id: 0, question: "Maison", answer: "House"}] },
  { id: 3, name: "Capitals", color: -18611, cards: [{id: 0, question: "Netherlands", answer: "Amsterdam"}] },
  { id: 4, name: "Hungarian", color: -18611, cards: [{id: 0, question: "Maison", answer: "House"}] },
  { id: 5, name: "Hungarian", color: -18611 },
]);

export const getPile = (pileId: number): Observable<Pile | undefined> => rawPiles$.pipe(
    delay(500),
    map((piles: Pile[]) => piles.find((pile: Pile) => pile.id === pileId))
);
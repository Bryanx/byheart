import { createSelector, createSlice } from '@reduxjs/toolkit'
import { RootState } from "../app/store";
import { Pile } from "./models/Pile";

interface PileState {
  list: Pile[]
}

const initialState: PileState = {
  list: [
    {
      id: 1,
      name: "French",
      color: -30107,
      cards: [{ id: 0, question: "Maison", answer: "House" }, { id: 1, question: "Vélo", answer: "Bicycle" }, { id: 2, question: "Vélo", answer: "Bicycle" }, {
        id: 3,
        question: "Vélo",
        answer: "Bicycle"
      }, { id: 4, question: "Vélo", answer: "Bicycle" }, { id: 5, question: "Vélo", answer: "Bicycle" }]
    },
    { id: 2, name: "Periodic Table", color: -8336444, cards: [{ id: 0, question: "Maison", answer: "House" }] },
    { id: 3, name: "Capitals", color: -18611, cards: [{ id: 0, question: "Netherlands", answer: "Amsterdam" }] },
    { id: 4, name: "Hungarian", color: -18611, cards: [{ id: 0, question: "Maison", answer: "House" }] },
    { id: 5, name: "Hungarian", color: -18611 },
  ],
}

export const pileSlice = createSlice({
  name: 'piles',
  // `createSlice` will infer the state type from the `initialState` argument
  initialState,
  reducers: {
    increment: (state) => {
      // @ts-ignore
      state.piles.push(null);
    },
  },
})

export const { increment } = pileSlice.actions

// Other code such as selectors can use the imported `RootState` type
const selectPiles = (state: RootState) => state.piles
export const selectPileList = createSelector(selectPiles, piles => piles.list)
export const selectByPileId = (pileId?: string) => createSelector(selectPileList,
    list => list.find(pile => `${pile.id}` === pileId))
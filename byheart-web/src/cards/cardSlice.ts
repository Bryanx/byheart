import { createAsyncThunk, createSelector, createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../app/store";
import { Card } from "./models/Card";
import { setSnackbar } from "../shared/uiSlice";
import { supabase } from "../shared/constants";

interface CardState {
  list: Card[];
}

const initialState: CardState = {
  list: [],
};

export const fetchCardsByPileId = createAsyncThunk<Card[], string, { rejectValue: string }>(
  "cards/fetchCardsByPileId",
  async (pileId: string, thunkApi) => {
    const { data, error } = await supabase.from("cards").select("*").eq("pile_id", pileId);
    if (error?.message) {
      thunkApi.dispatch(setSnackbar({ message: error.message, type: "error" }));
    }
    return data as Card[];
  },
);

export const updateCard = createAsyncThunk<void, Card, { rejectValue: string }>(
  "cards/updateCard",
  async (card: Card, thunkApi) => {
    const { data, error } = await supabase.from("cards").upsert(card);
    console.log("data", data);
    if (error?.message) {
      thunkApi.dispatch(setSnackbar({ message: error.message, type: "error" }));
    }
  },
);

export const cardSlice = createSlice({
  name: "cards",
  initialState,
  reducers: {
    addCard: (state, action: PayloadAction<Card>) => {
      state.list?.push(action.payload);
    },
  },
  extraReducers: (builder) => {
    builder.addCase(fetchCardsByPileId.fulfilled, (state, action: PayloadAction<Card[]>) => {
      state.list = action.payload;
    });
  },
});

export const { addCard } = cardSlice.actions;

const selectCards = (state: RootState) => state.cards;
export const selectCardList = createSelector(selectCards, (cards) => cards.list);

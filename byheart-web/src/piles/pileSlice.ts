import { createAsyncThunk, createSelector, createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../app/store";
import { Pile } from "./models/Pile";
import { setSnackbar } from "../shared/uiSlice";
import { supabase } from "../shared/constants";

interface PileState {
  list: Pile[];
}

const initialState: PileState = {
  list: [],
};

export const fetchPiles = createAsyncThunk<Pile[], void, { rejectValue: string }>(
  "piles/fetchPiles",
  async (_: void, thunkApi) => {
    const { data, error } = await supabase.from("piles").select("*");
    if (error?.message) {
      thunkApi.dispatch(setSnackbar({ message: error.message, type: "error" }));
    }
    return data as Pile[];
  },
);

export const pileSlice = createSlice({
  name: "piles",
  initialState,
  reducers: {
    increment: () => {
      // state.piles.push(null);
    },
  },
  extraReducers: (builder) => {
    builder.addCase(fetchPiles.fulfilled, (state, action: PayloadAction<Pile[]>) => {
      state.list = action.payload;
    });
  },
});

export const { increment } = pileSlice.actions;

const selectPiles = (state: RootState) => state.piles;
export const selectPileList = createSelector(selectPiles, (piles) => piles.list);
export const selectByPileId = (pileId?: string) =>
  createSelector(selectPileList, (list) => list.find((pile) => `${pile.id}` === pileId));

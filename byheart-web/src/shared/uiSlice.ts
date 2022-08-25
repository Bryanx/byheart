import { createSelector, createSlice } from '@reduxjs/toolkit'
import { RootState } from "../app/store";

interface UiState {
  colorMode: "light" | "dark";
}

const initialState: UiState = {
  colorMode: "dark",
}

export const uiSlice = createSlice({
  name: 'ui',
  initialState,
  reducers: {
    toggleColorMode: (state) => {
      if (state.colorMode === "light")
        state.colorMode = "dark"
      else
        state.colorMode = "light"
    },
  },
})

export const { toggleColorMode } = uiSlice.actions

const selectUi = (state: RootState) => state.ui;
export const selectColorMode = createSelector(selectUi, ui => ui.colorMode)
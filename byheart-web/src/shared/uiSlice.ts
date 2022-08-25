import { createSelector, createSlice, PayloadAction } from '@reduxjs/toolkit'
import { RootState } from "../app/store";

interface UiState {
  colorMode: "light" | "dark";
  snackbar?: Snackbar;
}

interface Snackbar {
  message: string;
  type: 'success' | 'info' | 'warning' | 'error';
}

const initialState: UiState = {
  colorMode: "dark",
}

export const uiSlice = createSlice({
  name: 'ui',
  initialState,
  reducers: {
    setSnackbar: (state, action: PayloadAction<Snackbar | undefined>) => {
      state.snackbar = action.payload;
    },
    toggleColorMode: (state) => {
      state.colorMode = state.colorMode === "light" ? "dark" : "light";
    },
  },
})

export const { setSnackbar, toggleColorMode } = uiSlice.actions

const selectUi = (state: RootState) => state.ui;
export const selectColorMode = createSelector(selectUi, ui => ui.colorMode)
export const selectSnackbar = createSelector(selectUi, ui => ui.snackbar)
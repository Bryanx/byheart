import { configureStore } from '@reduxjs/toolkit'
import { pileSlice } from "../piles/pileSlice";
import { profileSlice } from "../profile/profileSlice";
import { uiSlice } from "../shared/uiSlice";

export const store = configureStore({
  reducer: {
    piles: pileSlice.reducer,
    profile: profileSlice.reducer,
    ui: uiSlice.reducer,
  },
})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch
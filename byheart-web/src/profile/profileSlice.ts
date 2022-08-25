import { createSelector, createSlice, PayloadAction } from '@reduxjs/toolkit'
import { RootState } from "../app/store";
import { Profile } from "./models/Profile";

interface ProfileState {
  profile?: Profile;
}

const initialState: ProfileState = {}

export const profileSlice = createSlice({
  name: 'profile',
  initialState,
  reducers: {
    setProfile: (state, action: PayloadAction<Profile>) => {
      state.profile = action.payload;
    },
  },
})

export const { setProfile } = profileSlice.actions

export const selectProfile = (state: RootState) => state.profile;
export const selectEmail = createSelector(selectProfile, profile => profile.profile?.email || "")
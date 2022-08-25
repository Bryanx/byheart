import ThemeWrapper from "../shared/ThemeWrapper";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomeRoute from "../home/HomeRoute";
import PileRoute from "../piles/PileRoute";
import NotFoundRoute from "../error/NotFoundRoute";
import React, { useEffect } from "react";
import Auth from "../profile/Auth";
import { useAppDispatch, useAppSelector } from "./hooks";
import { selectProfile, setProfile } from "../profile/profileSlice";
import { supabase } from "../index";
import { fetchPiles } from "../piles/pileSlice";
import SnackbarProvider from "../shared/util/SnackbarProvider";

const App: React.FC = () => {
  const profile = useAppSelector(selectProfile);
  const dispatch = useAppDispatch();

  useEffect(() => {
    (async () => {
      const session = (await supabase.auth.getSession())?.data?.session;
      if (session === null) {
        await supabase.auth.signInWithOAuth({ provider: "google" });
      } else {
        dispatch(setProfile({ email: session?.user.email || "", token: session.access_token }));
        dispatch(fetchPiles());
      }
    })();
  }, [])

  return (
      <ThemeWrapper>
        <SnackbarProvider>
          {!profile?.profile?.token ? <Auth/> : <BrowserRouter>
            <Routes>
              <Route path="/" element={<HomeRoute/>}/>
              <Route path="stacks/:stackId" element={<PileRoute/>}/>
              <Route path="*" element={<NotFoundRoute/>}/>
            </Routes>
          </BrowserRouter>}
        </SnackbarProvider>
      </ThemeWrapper>
  );
};

export default App;

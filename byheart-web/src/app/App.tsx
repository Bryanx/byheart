import ThemeWrapper from "../shared/ThemeWrapper";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomeRoute from "../home/HomeRoute";
import PileRoute from "../piles/PileRoute";
import NotFoundRoute from "../error/NotFoundRoute";
import React from "react";
import Auth from "../profile/Auth";
import { useAppSelector } from "./hooks";
import { selectProfile } from "../profile/profileSlice";
import SnackbarProvider from "../shared/util/SnackbarProvider";

const App: React.FC = () => {
  const profile = useAppSelector(selectProfile);

  return (
    <ThemeWrapper>
      <SnackbarProvider>
        {!profile?.profile?.token ? (
          <Auth />
        ) : (
          <BrowserRouter>
            <Routes>
              <Route path="/" element={<HomeRoute />} />
              <Route path="stacks/:stackId" element={<PileRoute />} />
              <Route path="*" element={<NotFoundRoute />} />
            </Routes>
          </BrowserRouter>
        )}
      </SnackbarProvider>
    </ThemeWrapper>
  );
};

export default App;

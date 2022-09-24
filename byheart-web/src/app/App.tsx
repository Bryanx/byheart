import ThemeWrapper from "../shared/ThemeWrapper";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import HomeRoute from "../home/HomeRoute";
import PileRoute from "../piles/PileRoute";
import React from "react";
import SnackbarProvider from "../shared/util/SnackbarProvider";
import SignOut from "../profile/SignOut";
import SignIn from "../profile/SignIn";
import AuthGuard from "../profile/AuthGuard";
import MiniDrawer from "../Drawer";

const App: React.FC = () => {
  return (
    <ThemeWrapper>
      <SnackbarProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<AuthGuard element={<HomeRoute />} />} />
            <Route path="/signin" element={<SignIn />} />
            <Route path="/signout" element={<SignOut />} />
            <Route path="/drawer" element={<MiniDrawer />} />
            <Route path="stacks/:stackId" element={<AuthGuard element={<PileRoute />} />} />
            <Route path="*" element={<Navigate to="/" />} />
          </Routes>
        </BrowserRouter>
      </SnackbarProvider>
    </ThemeWrapper>
  );
};

export default App;

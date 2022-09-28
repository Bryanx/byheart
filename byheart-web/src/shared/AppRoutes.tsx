import { Navigate, Route, Routes, useLocation } from "react-router-dom";
import { HomeRoute } from "../home/HomeRoute";
import { PileRoute } from "../piles/PileRoute";
import React from "react";
import { SignOut } from "../profile/SignOut";
import { SignIn } from "../profile/SignIn";
import { AuthGuard } from "../profile/AuthGuard";
import { ResponsiveDrawer } from "./components/Drawer";
import { NewPileRoute } from "../piles/NewPileRoute";

export const AppRoutes: React.FC = () => {
  const { pathname } = useLocation();

  const isValidPathForDrawer = pathname !== "/signin" && pathname !== "/signout";

  return (
    <>
      <Routes>
        <Route path="/" element={<AuthGuard element={<HomeRoute />} />} />
        <Route path="/signin" element={<SignIn />} />
        <Route path="/signout" element={<SignOut />} />
        <Route path="stacks/:stackId" element={<AuthGuard element={<PileRoute />} />} />
        <Route path="stacks/new" element={<AuthGuard element={<NewPileRoute />} />} />
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
      {isValidPathForDrawer && <ResponsiveDrawer />}
    </>
  );
};

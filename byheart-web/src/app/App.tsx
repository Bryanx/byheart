import { ThemeWrapper } from "../shared/ThemeWrapper";
import { BrowserRouter } from "react-router-dom";
import React from "react";
import { SnackbarProvider } from "../shared/util/SnackbarProvider";
import { AppRoutes } from "../shared/AppRoutes";

export const App: React.FC = () => {
  return (
    <ThemeWrapper>
      <SnackbarProvider>
        <BrowserRouter>
          <AppRoutes />
        </BrowserRouter>
      </SnackbarProvider>
    </ThemeWrapper>
  );
};

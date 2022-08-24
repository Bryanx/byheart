import { createTheme, ThemeProvider } from "@mui/material";
import { orange } from "@mui/material/colors";
import "./spec.d.ts";
import React from "react";

interface ThemeWrapperProps {
  children?: JSX.Element;
}

const ThemeWrapper: React.FC<ThemeWrapperProps> = ({ children }) => {
  const theme = createTheme({
    status: {
      danger: orange[500],
    },
  });

  return (
      <ThemeProvider theme={theme}>{children}</ThemeProvider>
  )
};

export default ThemeWrapper


import { createTheme, ThemeProvider } from "@mui/material";
import { orange } from "@mui/material/colors";
import React from "react";
import { useAppSelector } from "../app/hooks";
import { selectColorMode } from "./uiSlice";

interface ThemeWrapperProps {
  children?: JSX.Element;
}

const ThemeWrapper: React.FC<ThemeWrapperProps> = ({ children }) => {
  const mode = useAppSelector(selectColorMode);

  const theme = createTheme({
    palette: {
      mode,
    },
    status: {
      danger: orange[500],
    },
  });

  return (
      <ThemeProvider theme={theme}>{children}</ThemeProvider>
  )
};

export default ThemeWrapper


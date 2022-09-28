import { createTheme, ThemeProvider } from "@mui/material/styles";
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
      primary: {
        main: "#5C6BC0",
      },
    },
    status: {
      danger: orange[500],
    },
    typography: {
      body1: {
        fontSize: 15,
        fontWeight: "500",
      },
      body2: {
        fontSize: 12,
      },
      subtitle1: {
        fontSize: 12,
        fontWeight: "700",
      },
      subtitle2: {
        fontSize: 10,
      },
    },
  });

  return <ThemeProvider theme={theme}>{children}</ThemeProvider>;
};

export default ThemeWrapper;

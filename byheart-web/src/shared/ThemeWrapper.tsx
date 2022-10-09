import { createTheme, ThemeProvider } from "@mui/material/styles";
import { orange } from "@mui/material/colors";
import React, { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../app/hooks";
import { selectColorMode, setColorMode } from "./uiSlice";

interface ThemeWrapperProps {
  children?: JSX.Element;
}

export const ThemeWrapper: React.FC<ThemeWrapperProps> = ({ children }) => {
  const mode = useAppSelector(selectColorMode);
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(setColorMode(document.documentElement.classList.contains("dark") ? "dark" : "light"));
  }, [dispatch]);

  const lightTheme = () => ({
    background: { default: "#FFFFFF" },
    bgLighter: { main: "#FCFCFC" },
    card: { main: "#F5F5F5" },
    txt: { main: "#ABABAB" },
    stack: { main: "#E6E6E6" },
    menuActive: { main: "#D4D4D4" },
  });

  const darkTheme = () => ({
    background: { default: "#111921" },
    bgLighter: { main: "#19222A" },
    card: { main: "#202B35" },
    txt: { main: "#ABABAB" },
    stack: { main: "#263440" },
    menuActive: { main: "#2C3C4A" },
  });

  const theme = createTheme({
    palette: {
      mode,
      primary: { main: "#5C6BC0" },
      ...(mode === "light" ? lightTheme() : darkTheme()),
    },
    components: {
      MuiButton: {
        styleOverrides: {
          root: {
            textTransform: "none",
          },
        },
        defaultProps: {
          disableElevation: true,
        },
      },
    },
    status: {
      danger: orange[500],
    },
    typography: {
      body1: {
        fontSize: 16,
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

declare module "@mui/material/styles" {
  interface Palette {
    bgLighter: PaletteOptions["primary"];
    card: PaletteOptions["primary"];
    txt: PaletteOptions["primary"];
    stack: PaletteOptions["primary"];
    menuActive: Palette["primary"];
  }
  interface PaletteOptions {
    bgLighter: PaletteOptions["primary"];
    card: PaletteOptions["primary"];
    txt: PaletteOptions["primary"];
    stack: PaletteOptions["primary"];
    menuActive: PaletteOptions["primary"];
  }
}

// Update the Button's color prop options
declare module "@mui/material/Button" {
  interface ButtonPropsColorOverrides {
    bgLighter: true;
    card: true;
    txt: true;
    stack: true;
    menuActive: true;
  }
}

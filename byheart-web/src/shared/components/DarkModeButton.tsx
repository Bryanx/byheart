import React, { useEffect } from "react";
import { useAppDispatch } from "../../app/hooks";
import { toggleColorMode } from "../uiSlice";
import Button from "@mui/material/Button";
import NightsStay from "@mui/icons-material/NightsStay";

export const DarkModeButton: React.FC = () => {
  const dispatch = useAppDispatch();

  const toggleDarkMode = () => {
    if (
      localStorage.theme === "dark" ||
      (!("theme" in localStorage) && window.matchMedia("(prefers-color-scheme: dark)").matches)
    )
      document.documentElement.classList.add("dark");
    else document.documentElement.classList.remove("dark");
  };

  const onClickDarkMode = () => {
    const isDarkTheme = localStorage.theme === "dark";
    localStorage.theme = isDarkTheme ? "light" : "dark";
    toggleDarkMode();
    dispatch(toggleColorMode());
  };

  useEffect(toggleDarkMode);

  return (
    <Button
      variant="contained"
      color="card"
      onClick={onClickDarkMode}
      size="small"
      sx={{ color: "txt.main" }}
      startIcon={<NightsStay fontSize="small" />}
    >
      Dark mode
    </Button>
  );
};

import NightsStay from "@mui/icons-material/NightsStay";
import Settings from "@mui/icons-material/Settings";
import React, { useEffect } from "react";
import ProfileMenu from "../profile/ProfileMenu";
import { useAppDispatch } from "../app/hooks";
import { toggleColorMode } from "../shared/uiSlice";
import { IconButton } from "@mui/material";
import ArrowBack from "@mui/icons-material/ArrowBack";

interface HeaderProps {
  title?: string;
  hasBackButton?: boolean;
}

const Header: React.FC<HeaderProps> = ({ title, hasBackButton }) => {
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
    <header>
      {hasBackButton && (
        <IconButton sx={{ height: "40px", width: "40px" }} onClick={() => window.history.back()}>
          <ArrowBack />
        </IconButton>
      )}
      <div>{title}</div>
      <section>
        <ProfileMenu />
        <IconButton onClick={onClickDarkMode}>
          <NightsStay />
        </IconButton>
        <IconButton onClick={() => console.log("click settings")}>
          <Settings />
        </IconButton>
      </section>
    </header>
  );
};

export default Header;

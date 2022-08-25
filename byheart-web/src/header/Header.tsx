import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import NightsStay from "@mui/icons-material/NightsStay";
import Settings from "@mui/icons-material/Settings";
import React, { useEffect } from "react";
import ProfileMenu from "../profile/ProfileMenu";
import { useAppDispatch } from "../app/hooks";
import { toggleColorMode } from "../shared/uiSlice";
import { IconButton } from "@mui/material";

interface HeaderProps {
  title?: string;
  hasBackButton?: boolean;
}

const Header: React.FC<HeaderProps> = ({ title, hasBackButton }) => {
  const dispatch = useAppDispatch();

  const toggleDarkMode = () => {
    if (localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches))
      document.documentElement.classList.add('dark');
    else
      document.documentElement.classList.remove('dark');
  }

  const onClickDarkMode = () => {
    const isDarkTheme = localStorage.theme === 'dark';
    localStorage.theme = isDarkTheme ? 'light' : 'dark';
    toggleDarkMode();
    dispatch(toggleColorMode())
  }

  useEffect(toggleDarkMode);

  return (
      <header className="text-gray-500 dark:text-gray-200 font-medium flex justify-between align-center h-16 px-5">
        {hasBackButton &&
            <IconButton
                className="flex self-center"
                sx={{ height: "40px", width: "40px" }}
                onClick={() => window.history.back()}
            >
              <FontAwesomeIcon icon={faArrowLeft} size="xs"/>
            </IconButton>}
        <div className="flex self-center">
          {title}
        </div>
        <section className="right-menu flex align-center">
          <ProfileMenu/>
          <IconButton className="flex self-center mr-3" onClick={onClickDarkMode}>
            <NightsStay className="text-gray-500 dark:text-gray-200"/>
          </IconButton>
          <IconButton className="flex self-center" onClick={() => console.log("click settings")}>
            <Settings className="text-gray-500 dark:text-gray-200"/>
          </IconButton>
        </section>
      </header>
  )
};

export default Header


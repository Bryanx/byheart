import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import NightsStay from "@mui/icons-material/NightsStay";
import Settings from "@mui/icons-material/Settings";
import React, { useEffect } from "react";

interface HeaderProps {
  title?: string;
  hasBackButton?: boolean;
}

const Header: React.FC<HeaderProps> = ({ title, hasBackButton }) => {
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
  }

  useEffect(toggleDarkMode);

  return (
      <header className="text-gray-500 dark:text-gray-200 font-medium flex justify-between">
        {hasBackButton &&
            <button className="cursor-pointer p-5 filter active:brightness-75" onClick={() => window.history.back()}>
              <FontAwesomeIcon icon={faArrowLeft}/>
            </button>}
        <div className="p-5">
          {title}
        </div>
        <section className="right-menu">
          <button className="cursor-pointer pt-5 pr-5 filter active:brightness-75" onClick={onClickDarkMode}>
            <NightsStay className="text-gray-500 dark:text-gray-200"/>
          </button>
          <button className="cursor-pointer pt-5 pr-5 filter active:brightness-75">
            <Settings className="text-gray-500 dark:text-gray-200"/>
          </button>
        </section>
      </header>
  )
};

export default Header


import React from "react";
import IconButton from "@mui/material/IconButton";
import ArrowBack from "@mui/icons-material/ArrowBack";

interface HeaderProps {
  title?: string;
  hasBackButton?: boolean;
}

export const Header: React.FC<HeaderProps> = ({ title, hasBackButton }) => {
  return (
    <header>
      {hasBackButton && (
        <IconButton sx={{ height: "40px", width: "40px" }} onClick={() => window.history.back()}>
          <ArrowBack />
        </IconButton>
      )}
      <div>{title}</div>
    </header>
  );
};

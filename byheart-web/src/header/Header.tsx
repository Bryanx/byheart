import React from "react";
import IconButton from "@mui/material/IconButton";
import ArrowBack from "@mui/icons-material/ArrowBack";
import ArrowForward from "@mui/icons-material/ArrowForward";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import { DarkModeButton } from "../shared/components/DarkModeButton";
import Share from "@mui/icons-material/Share";
import Palette from "@mui/icons-material/Palette";

interface HeaderProps {
  title?: string;
  hasBackButton?: boolean;
}

export const Header: React.FC<HeaderProps> = ({ title, hasBackButton }) => {
  return (
    <Box
      sx={{
        width: "100%",
        backgroundColor: "bgLighter.main",
        height: "47px",
      }}
    >
      <Box
        sx={{
          justifyContent: "space-between",
          maxWidth: "1280px",
          width: "100%",
          m: "auto",
          display: "flex",
          height: "100%",
          alignItems: "center",
        }}
      >
        <Box sx={{ display: "flex" }}>
          {hasBackButton && (
            <>
              <IconButton
                sx={{ height: "40px", width: "40px", color: "txt.main" }}
                onClick={() => window.history.back()}
              >
                <ArrowBack />
              </IconButton>
              <IconButton
                sx={{ height: "40px", width: "40px", color: "txt.main" }}
                onClick={() => window.history.forward()}
              >
                <ArrowForward />
              </IconButton>
            </>
          )}
          <Box sx={{ display: "flex", alignItems: "center" }}>{title}</Box>
        </Box>
        <Box>
          <DarkModeButton />
          <Button
            variant="contained"
            color="card"
            size="small"
            sx={{ color: "txt.main", ml: 1 }}
            startIcon={<Palette fontSize="small" />}
          >
            Change stack color
          </Button>
          <Button
            variant="contained"
            color="card"
            size="small"
            sx={{ color: "txt.main", ml: 1 }}
            startIcon={<Share fontSize="small" />}
          >
            Share cards
          </Button>
        </Box>
      </Box>
    </Box>
  );
};

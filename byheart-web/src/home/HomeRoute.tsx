import Header from "../header/Header";
import React from "react";
import { Box } from "@mui/material";

const HomeRoute: React.FC = () => {
  return (
    <Box sx={{ ml: "239px" }}>
      <Header title="My card stacks" />
    </Box>
  );
};

export default HomeRoute;

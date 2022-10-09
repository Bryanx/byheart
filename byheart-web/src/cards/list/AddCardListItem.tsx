import React from "react";
import { Add } from "@mui/icons-material";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";

export const AddCardListItem: React.FC = () => (
  <Button
    variant="contained"
    color="card"
    sx={{
      height: 75,
      width: "100%",
      display: "flex",
      flexDirection: "row",
      alignItems: "center",
      p: 2,
      borderRadius: 2,
      color: "txt.main",
      justifyContent: "center",
    }}
  >
    <Add sx={{ mr: 1 }} />
    <Typography variant="body1">Add a card</Typography>
  </Button>
);

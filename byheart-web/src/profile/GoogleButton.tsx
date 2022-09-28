import React from "react";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";

interface GoogleButtonProps {
  onClick: () => void;
}

export const GoogleButton: React.FC<GoogleButtonProps> = ({ onClick }) => (
  <>
    <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Roboto" />
    <Box
      sx={{
        width: "184px",
        height: "42px",
        backgroundColor: "#4285f4",
        borderRadius: "2px",
        boxShadow: "0 3px 4px 0 rgba(0,0,0,.25)",
        cursor: "pointer",
        "&:hover": {
          boxShadow: "0 0 6px #4285f4",
        },
        "&:active": {
          backgroundColor: "#1669F2",
        },
      }}
      onClick={onClick}
    >
      <Box
        sx={{
          position: "absolute",
          mt: "1px",
          ml: "1px",
          width: "40px",
          height: "40px",
          borderRadius: "2px",
          backgroundColor: "white",
        }}
      >
        <img
          style={{
            position: "absolute",
            marginTop: "11px",
            marginLeft: "11px",
            width: "18px",
            height: "18px",
          }}
          src="https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg"
        />
      </Box>
      <Typography
        sx={{
          float: "right",
          margin: "11px 11px 0 0",
          color: "white",
          fontSize: "14px",
          letterSpacing: "0.2px",
          fontFamily: "Roboto",
        }}
      >
        <b>Sign in with google</b>
      </Typography>
    </Box>
  </>
);

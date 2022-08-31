import React from "react";
import { supabase } from "../index";
import { Box } from "@mui/material";
import GoogleButton from "./GoogleButton";

const SignIn: React.FC = () => {
  const signInWithGoogle = async () => {
    await supabase.auth.signInWithOAuth({ provider: "google" });
  };

  return (
    <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh" }}>
      <GoogleButton onClick={() => signInWithGoogle()} />
    </Box>
  );
};

export default SignIn;

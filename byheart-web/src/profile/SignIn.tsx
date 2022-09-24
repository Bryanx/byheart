import React from "react";
import { Box } from "@mui/material";
import GoogleButton from "./GoogleButton";
import { supabase } from "../shared/constants";

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

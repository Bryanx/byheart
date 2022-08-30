import React, { useEffect } from "react";
import { supabase } from "../index";
import { useAppDispatch } from "../app/hooks";
import { setProfile } from "./profileSlice";
import { fetchPiles } from "../piles/pileSlice";
import { Box } from "@mui/material";
import GoogleButton from "./GoogleButton";

const Auth: React.FC = () => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    (async () => {
      const session = (await supabase.auth.getSession())?.data?.session;
      console.log("session", session);
      if (session !== null) {
        dispatch(setProfile({ email: session?.user.email || "", token: session.access_token }));
        dispatch(fetchPiles());
      }
    })();
  }, [dispatch]);

  const signInWithGoogle = async () => {
    await supabase.auth.signInWithOAuth({ provider: "google" });
  };

  return (
    <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh" }}>
      <GoogleButton onClick={() => signInWithGoogle()} />
    </Box>
  );
};

export default Auth;

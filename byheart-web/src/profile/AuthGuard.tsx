import React, { useEffect, useState } from "react";
import { useAppDispatch } from "../app/hooks";
import { setProfile } from "./profileSlice";
import { Navigate } from "react-router-dom";
import { Box, CircularProgress } from "@mui/material";
import { supabase } from "../index";
import { fetchPiles } from "../piles/pileSlice";

interface AuthGuardProps {
  element: JSX.Element;
}

const AuthGuard: React.FC<AuthGuardProps> = ({ element }) => {
  const [isLoggedIn, setLoggedIn] = useState(false);
  const [isLoading, setLoading] = useState(true);
  const dispatch = useAppDispatch();

  useEffect(() => {
    (async () => {
      const session = (await supabase.auth.getSession())?.data?.session;
      if (session !== null) {
        dispatch(setProfile({ email: session?.user.email || "", token: session.access_token }));
        dispatch(fetchPiles());
      }
      setLoggedIn(session !== null);
      setLoading(false);
    })();
  }, [dispatch]);

  if (isLoading) {
    return (
      <Box
        sx={{ display: "flex", alignItems: "center", justifyContent: "center", height: "100vh" }}
      >
        <CircularProgress />
      </Box>
    );
  } else {
    if (isLoggedIn) {
      return element;
    } else {
      return <Navigate to="/signin" />;
    }
  }
};

export default AuthGuard;

import React, { useEffect, useState } from "react";
import { useAppDispatch } from "../app/hooks";
import { setProfile } from "./profileSlice";
import { Navigate } from "react-router-dom";
import CircularProgress from "@mui/material/CircularProgress";
import Box from "@mui/material/Box";
import { fetchPiles } from "../piles/pileSlice";
import { supabase } from "../shared/constants";

interface AuthGuardProps {
  element: JSX.Element;
}

export const AuthGuard: React.FC<AuthGuardProps> = ({ element }) => {
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

import React, { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { supabase } from "../shared/constants";

const SignOut: React.FC = () => {
  const [isSignedOut, setSignedOut] = useState(false);

  useEffect(() => {
    (async () => {
      await supabase.auth.signOut();
      setSignedOut(true);
    })();
  }, []);

  return isSignedOut ? <Navigate to="/signin" replace={true} /> : null;
};

export default SignOut;

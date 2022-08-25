import React from "react";
import { supabase } from "../index";

const Auth: React.FC = () => {
  return (
    <button className="button block" aria-live="polite" onClick={() => supabase.auth.signOut()}>
      Signout
    </button>
  );
};

export default Auth;

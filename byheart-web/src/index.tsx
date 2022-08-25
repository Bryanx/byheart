import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import "react-spring-bottom-sheet/dist/style.css";
import { Provider } from "react-redux";
import { store } from "./app/store";
import App from "./app/App";
import { createClient } from "@supabase/supabase-js";

const supabaseUrl = process.env.REACT_APP_SUPABASE_URL || "";
const supabaseAnonKey = process.env.REACT_APP_SUPABASE_ANON_KEY || "";
export const supabase = createClient(supabaseUrl, supabaseAnonKey);

ReactDOM.render(
  <React.StrictMode>
    <Provider store={store}>
      <App />
    </Provider>
  </React.StrictMode>,
  document.getElementById("root"),
);

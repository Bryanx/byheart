import React from "react";
import "./index.css";
import { App } from "./app/App";
import { AppProviders } from "./shared/AppProviders";
import { createRoot } from "react-dom/client";

createRoot(document.getElementById("root") as HTMLElement).render(
  <React.StrictMode>
    <AppProviders>
      <App />
    </AppProviders>
  </React.StrictMode>,
);

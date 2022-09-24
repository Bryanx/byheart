import React from "react";
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import ReactDOM from "react-dom/client";
import "./index.css";
import "react-spring-bottom-sheet/dist/style.css";
import App from "./app/App";
import AppProviders from "./shared/AppProviders";

ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
  <React.StrictMode>
    <AppProviders>
      <App />
    </AppProviders>
  </React.StrictMode>,
);

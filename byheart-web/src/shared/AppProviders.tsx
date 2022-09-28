import React from "react";
import { store } from "../app/store";
import { Provider } from "react-redux";

interface AppProvidersProps {
  children?: React.ReactNode;
}

export const AppProviders: React.FC<AppProvidersProps> = ({ children }) => (
  <Provider store={store}>{children}</Provider>
);

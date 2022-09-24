import React from "react";
import { store } from "../app/store";
import { Provider } from "react-redux";

const AppProviders: React.FC = ({ children }) => <Provider store={store}>{children}</Provider>;

export default AppProviders;

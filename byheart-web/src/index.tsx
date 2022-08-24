import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import 'react-spring-bottom-sheet/dist/style.css'
import { Provider } from "react-redux";
import { store } from "./app/store";
import App from "./app/App";

// store.dispatch(fetchPiles())

ReactDOM.render(
    <React.StrictMode>
      <Provider store={store}>
        <App/>
      </Provider>
    </React.StrictMode>,
    document.getElementById('root')
)
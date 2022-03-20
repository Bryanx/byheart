 import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import 'react-spring-bottom-sheet/dist/style.css'
import HomeRoute from './home/HomeRoute';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import NotFoundRoute from './error/NotFoundRoute';
import PileRoute from './piles/PileRoute';
import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';
import { user } from 'rxfire/auth';
import { createTheme, ThemeProvider } from '@mui/material';
import { orange } from '@mui/material/colors';
import ThemeWrapper from './shared/ThemeWrapper';



ReactDOM.render(
  <React.StrictMode>
    <ThemeWrapper>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomeRoute />} />
          <Route path="stacks">
            <Route path=":stackId" element={<PileRoute />} />
          </Route>
          <Route path="*" element={<NotFoundRoute />} />
        </Routes>
      </BrowserRouter>
    </ThemeWrapper>
  </React.StrictMode>,
  document.getElementById('root')
);
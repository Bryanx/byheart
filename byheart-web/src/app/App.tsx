import ThemeWrapper from "../shared/ThemeWrapper";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomeRoute from "../home/HomeRoute";
import PileRoute from "../piles/PileRoute";
import NotFoundRoute from "../error/NotFoundRoute";
import React from "react";

const App: React.FC = () => (
    <ThemeWrapper>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomeRoute/>}/>
          <Route path="stacks/:stackId" element={<PileRoute/>}/>
          <Route path="*" element={<NotFoundRoute/>}/>
        </Routes>
      </BrowserRouter>
    </ThemeWrapper>
);

export default App;

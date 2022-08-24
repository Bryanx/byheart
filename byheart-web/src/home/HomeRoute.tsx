import Header from '../header/Header';
import PileList from '../piles/list/PileList';
import React from "react";

const HomeRoute: React.FC = () => (
    <div className="h-screen flex flex-col">
      <Header title="My card stacks"/>
      <PileList/>
    </div>
);

export default HomeRoute

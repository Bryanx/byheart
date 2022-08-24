import React from 'react';
import Header from '../header/Header';
import PileList from '../piles/list/PileList';

const HomeRoute = () => (
    <div className="h-screen flex flex-col">
      <Header title="My card stacks"/>
      <PileList/>
    </div>
);

export default HomeRoute

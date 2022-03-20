import React from 'react';
import Header from '../header/Header';
import PileList from '../piles/list/PileList';

export default function HomeRoute() {
  return (
    <div className="h-screen flex flex-col">
      <Header title="My card stacks"/>
      <PileList />
    </div>
  );
}
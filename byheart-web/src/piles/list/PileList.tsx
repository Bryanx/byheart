import Add from "@mui/icons-material/Add";
import { Fab } from "@mui/material";
import { Link } from "react-router-dom";
import './PileList.scss';
import PileListItem from "./PileListItem";
import { selectPileList } from "../pileSlice";
import { useAppSelector } from "../../app/hooks";
import React from "react";

const PileList: React.FC = () => {
  const piles = useAppSelector(selectPileList);

  return (
      <div className="relative flex flex-grow flex-col px-5">
        <div className="pile-grid grid grid-cols-2 gap-2">
          {
            piles.map(pile => (
                <Link to={`/stacks/${pile.id}`} key={pile.id} className="filter active:brightness-75">
                  <PileListItem pile={pile}/>
                </Link>
            ))
          }
        </div>
        <div className="fixed right-8 bottom-8">
          <Fab>
            <Add className="cursor-pointer text-white dark:text-gray-700"/>
          </Fab>
        </div>
      </div>
  )
};

export default PileList

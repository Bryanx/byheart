import Add from "@mui/icons-material/Add";
import { Fab } from "@mui/material";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { delay } from "rxjs";
import Pile from "../models/Pile";
import { rawPiles$ } from "../services/PileService";
import './PileList.scss';
import PileListItem from "./PileListItem";

export default function PileList() {
  const [piles, setPiles] = useState<Array<Pile>>([]);

  useEffect(() => {
    rawPiles$.pipe(delay(150)).subscribe(setPiles)
  }, [])

  return (
    <div className="relative flex flex-grow flex-col px-5">
      <div className="pile-grid grid grid-cols-2 gap-2">
        {
          piles.map(pile => (
            <Link to={`/stacks/${pile.id}`} key={pile.id} className="filter active:brightness-75">
              <PileListItem pile={pile} />
            </Link>
          ))
        }
      </div>
      <div className="fixed right-8 bottom-8">
        <Fab>
          <Add className="cursor-pointer text-white dark:text-gray-700" />
        </Fab>
      </div>
    </div>
  )
}
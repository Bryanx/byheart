import Add from "@mui/icons-material/Add";
import PlayArrow from "@mui/icons-material/PlayArrow";
import Share from "@mui/icons-material/Share";
import { Fab } from "@mui/material";
import { useState } from "react";
import { Outlet, useParams } from "react-router-dom";
import CardList from "../cards/list/CardList";
import Header from "../header/Header";
import RehearsalSetupBottomSheet from "../rehearsals/setup/RehearsalSetupBottomSheet";
import ColorUtil from "../shared/util/ColorUtil";
import { useAppSelector } from "../app/hooks";
import { selectByPileId } from "./pileSlice";

const PileRoute = () => {
  const params = useParams();
  const pile = useAppSelector(selectByPileId(params.stackId));
  const [openSetup, setOpenSetup] = useState(false)
  const loading = false;

  return (
      <div color="primary" className="h-full flex flex-col">
        <Header hasBackButton={true}/>
        {loading && <div className="bg-gray-400 dark:bg-gray-600 rounded-md h-7 w-40 mt-5 mb-9 mx-auto animate-pulse"></div>}
        {!loading && <div className="text-gray-500 dark:text-white mx-auto text-3xl block mt-4 mb-8 h-9">{pile?.name ?? "Unavailable"}</div>}
        <section className="flex gap-5 mx-auto">
          <Fab onClick={() => setOpenSetup(true)}>
            <PlayArrow style={{ color: ColorUtil.argbToRGB(pile?.color) }}/>
          </Fab>
          <Fab>
            <Add className="text-gray-500 dark:text-gray-100" style={{ color: ColorUtil.argbToRGB(pile?.color) }}/>
          </Fab>
          <Fab>
            <Share className="text-gray-500 dark:text-gray-100" style={{ color: ColorUtil.argbToRGB(pile?.color) }}/>
          </Fab>
        </section>
        <CardList loading={loading} pile={pile}/>
        <RehearsalSetupBottomSheet open={openSetup} onDismiss={() => setOpenSetup(false)}/>
        <Outlet/>
      </div>
  )
};

export default PileRoute



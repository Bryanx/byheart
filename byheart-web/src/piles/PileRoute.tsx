import Add from "@mui/icons-material/Add";
import PlayArrow from "@mui/icons-material/PlayArrow";
import Share from "@mui/icons-material/Share";
import { Fab } from "@mui/material";
import { useEffect, useState } from "react";
import { Outlet, useParams } from "react-router-dom";
import CardList from "../cards/list/CardList";
import Header from "../header/Header";
import RehearsalSetupBottomSheet from "../rehearsals/setup/RehearsalSetupBottomSheet";
import ColorUtil from "../shared/util/ColorUtil";
import Pile from "./models/Pile";
import { getPile } from "./services/PileService";

export default function PileRoute() {
  const [openSetup, setOpenSetup] = useState(false)
  const [loading, setLoading] = useState(true)
  const [pile, setPile] = useState<Pile>()
  let params = useParams();

  useEffect(() => {
    const sub = getPile(Number(params.stackId)).subscribe(result => {
      setPile(result);
      setLoading(false);
    });
    return () => sub.unsubscribe();
  }, [params.stackId])

  return (
    <div color="primary" className="h-full flex flex-col">
      <Header hasBackButton={true} />
      {loading && <div className="bg-gray-400 dark:bg-gray-600 rounded-md h-7 w-40 mt-5 mb-9 mx-auto animate-pulse"></div>}
      {!loading && <div className="text-gray-500 dark:text-white mx-auto text-3xl block mt-4 mb-8 h-9">{pile?.name ?? "Unavailable"}</div>}
      <section className="flex gap-5 mx-auto">
        <Fab onClick={() => setOpenSetup(true)}>
          <PlayArrow style={{ color: ColorUtil.argbToRGB(pile?.color) }} />
        </Fab>
        <Fab>
          <Add className="text-gray-500 dark:text-gray-100" style={{ color: ColorUtil.argbToRGB(pile?.color) }} />
        </Fab>
        <Fab>
          <Share className="text-gray-500 dark:text-gray-100" style={{ color: ColorUtil.argbToRGB(pile?.color) }} />
        </Fab>
      </section>
      <CardList loading={loading} pile={pile} />
      <RehearsalSetupBottomSheet open={openSetup} onDismiss={() => setOpenSetup(false)} />
      <Outlet />
    </div>
  )
}


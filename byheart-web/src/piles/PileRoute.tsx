import Add from "@mui/icons-material/Add";
import PlayArrow from "@mui/icons-material/PlayArrow";
import Share from "@mui/icons-material/Share";
import Box from "@mui/material/Box";
import Fab from "@mui/material/Fab";
import React, { useEffect, useState } from "react";
import { Outlet, useParams } from "react-router-dom";
import { CardList } from "../cards/list/CardList";
import { Header } from "../header/Header";
import { RehearsalSetupBottomSheet } from "../rehearsals/setup/RehearsalSetupBottomSheet";
import { ColorUtil } from "../shared/util/ColorUtil";
import { useAppDispatch, useAppSelector } from "../app/hooks";
import { selectByPileId } from "./pileSlice";
import { fetchCardsByPileId } from "../cards/cardSlice";

export const PileRoute: React.FC = () => {
  const params = useParams();
  const pile = useAppSelector(selectByPileId(params.stackId));
  const dispatch = useAppDispatch();
  const [openSetup, setOpenSetup] = useState(false);
  const loading = false;

  useEffect(() => {
    dispatch(fetchCardsByPileId(params.stackId || ""));
  }, [dispatch, params]);

  return (
    <Box
      sx={{
        maxWidth: "1280px",
        display: "flex",
        flexDirection: "column",
        margin: "auto",
        ml: "239px",
      }}
    >
      <Header hasBackButton={true} />
      {loading && <div></div>}
      {!loading && <div>{pile?.name ?? "Unavailable"}</div>}
      <section>
        <Fab onClick={() => setOpenSetup(true)}>
          <PlayArrow style={{ color: ColorUtil.argbToRGB(pile?.color) }} />
        </Fab>
        <Fab>
          <Add style={{ color: ColorUtil.argbToRGB(pile?.color) }} />
        </Fab>
        <Fab>
          <Share style={{ color: ColorUtil.argbToRGB(pile?.color) }} />
        </Fab>
      </section>
      <CardList loading={loading} pile={pile} />
      <RehearsalSetupBottomSheet open={openSetup} onDismiss={() => setOpenSetup(false)} />
      <Outlet />
    </Box>
  );
};

import Add from "@mui/icons-material/Add";
import PlayArrow from "@mui/icons-material/PlayArrow";
import Share from "@mui/icons-material/Share";
import Box from "@mui/material/Box";
import Fab from "@mui/material/Fab";
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { CardList } from "../cards/list/CardList";
import { Header } from "../header/Header";
import { RehearsalSetupBottomSheet } from "../rehearsals/setup/RehearsalSetupBottomSheet";
import { ColorUtil } from "../shared/util/ColorUtil";
import { useAppDispatch, useAppSelector } from "../app/hooks";
import { selectByPileId } from "./pileSlice";
import { fetchCardsByPileId } from "../cards/cardSlice";
import Typography from "@mui/material/Typography";

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
      }}
    >
      <Box
        sx={{
          ml: { sm: "240px" },
          width: "100%",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Header hasBackButton={true} />
        <Box sx={{ display: "flex", alignContent: "center", mt: 3 }}>
          {loading && <div></div>}
          {!loading && <Typography variant="h4">{pile?.name ?? "Unavailable"}</Typography>}
        </Box>
        <Box sx={{ mt: 3 }}>
          <Fab onClick={() => setOpenSetup(true)}>
            <PlayArrow style={{ color: ColorUtil.argbToRGB(pile?.color) }} />
          </Fab>
          <Fab>
            <Add style={{ color: ColorUtil.argbToRGB(pile?.color) }} />
          </Fab>
          <Fab>
            <Share style={{ color: ColorUtil.argbToRGB(pile?.color) }} />
          </Fab>
        </Box>
        <CardList loading={loading} pile={pile} />
      </Box>
      <RehearsalSetupBottomSheet open={openSetup} onDismiss={() => setOpenSetup(false)} />
    </Box>
  );
};

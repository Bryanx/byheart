import PlayArrow from "@mui/icons-material/PlayArrow";
import Sort from "@mui/icons-material/Sort";
import Box from "@mui/material/Box";
import React, { useEffect, useMemo } from "react";
import { useParams } from "react-router-dom";
import { CardList } from "../cards/list/CardList";
import { Header } from "../header/Header";
import { ColorUtil } from "../shared/util/ColorUtil";
import { useAppDispatch, useAppSelector } from "../app/hooks";
import { selectByPileId, setActivePile } from "./pileSlice";
import { fetchCardsByPileId } from "../cards/cardSlice";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";

export const PileRoute: React.FC = () => {
  const params = useParams();
  const pile = useAppSelector(selectByPileId(params.stackId));
  const dispatch = useAppDispatch();
  const loading = false;
  const color = useMemo(() => ColorUtil.argbToRGB(pile?.color), [pile]);

  useEffect(() => {
    dispatch(fetchCardsByPileId(params.stackId || ""));
  }, [dispatch, params]);

  useEffect(() => {
    if (pile) {
      dispatch(setActivePile(pile));
    }
  }, [dispatch, pile]);

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        ml: { sm: "240px" },
      }}
    >
      <Header hasBackButton={true} title={pile?.name || "Unavailable"} />
      <Box
        sx={{
          width: "100%",
          margin: "auto",
          display: "flex",
          maxWidth: "1280px",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Box sx={{ display: "flex", width: "100%", mt: 20, justifyContent: "space-between" }}>
          <Box sx={{ display: "flex", flexDirection: "column" }}>
            {loading && <div></div>}
            {!loading && (
              <Box sx={{ color: `${color}`, mb: 1 }}>
                <Typography variant="h4">{pile?.name ?? "Unavailable"}</Typography>
              </Box>
            )}
            <Typography color="txt">Type your description here...</Typography>
          </Box>
          <Box sx={{ display: "flex", alignItems: "end" }}>
            <Button variant="contained" sx={{ mr: 2 }} color="card" startIcon={<Sort />}>
              Sort cards
            </Button>
            <Button variant="contained" sx={{ backgroundColor: color }} startIcon={<PlayArrow />}>
              Start rehearsal
            </Button>
          </Box>
        </Box>
        <CardList loading={loading} />
      </Box>
    </Box>
  );
};

import { Pile } from "../models/Pile";
import ColorUtil from "../../shared/util/ColorUtil";
import React from "react";
import { Box } from "@mui/material";

interface PileListItemProps {
  pile?: Pile;
}

const PileListItem: React.FC<PileListItemProps> = ({ pile }) => (
  <Box
    className="stack bg-gray-100 dark:bg-gray-800 rounded-2xl flex justify-center items-center shadow"
    sx={{
      "&:before": {
        content: "''",
        display: "block",
        paddingTop: "100%",
        float: "left",
      },
    }}
  >
    <div className="text-center p-1">
      <p
        className="pile-name text-gray-500 dark:text-white text-2xl"
        style={{ color: ColorUtil.argbToRGB(pile?.color) }}
      >
        {pile?.name}
      </p>
      <p className="text-gray-500 dark:text-white text-xs">{pile?.cards?.length ?? 0} CARDS</p>
    </div>
  </Box>
);

export default PileListItem;

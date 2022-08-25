import ColorUtil from "../../shared/util/ColorUtil";
import { Card } from "../models/Card";
import React from "react";
import { Typography } from "@mui/material";

interface CardListItemProps {
  card?: Card;
  color?: number;
}

const CardListItem: React.FC<CardListItemProps> = ({ card, color }) => (
  <div className="cursor-pointer rounded-xl bg-gray-100 p-4 mb-2 text-xl shadow flex flex-col justify-center dark:bg-gray-800 filter active:brightness-75 h-20">
    <Typography sx={{ color: ColorUtil.argbToRGB(color) }}>{card?.question || ""}</Typography>
    <div>
      <Typography className="text-gray-500">{card?.answer || ""}</Typography>
    </div>
  </div>
);

export default CardListItem;

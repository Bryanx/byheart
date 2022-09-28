import ColorUtil from "../../shared/util/ColorUtil";
import { Card } from "../models/Card";
import React from "react";
import { Typography } from "@mui/material";

interface CardListItemProps {
  card?: Card;
  color?: number;
}

const CardListItem: React.FC<CardListItemProps> = ({ card, color }) => (
  <div>
    <Typography sx={{ color: ColorUtil.argbToRGB(color) }}>{card?.question || ""}</Typography>
    <div>
      <Typography>{card?.answer || ""}</Typography>
    </div>
  </div>
);

export default CardListItem;

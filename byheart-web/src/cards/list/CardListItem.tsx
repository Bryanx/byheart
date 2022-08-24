import ColorUtil from "../../shared/util/ColorUtil"
import { Card } from "../models/Card"
import React from "react";

interface CardListItemProps {
  card?: Card;
  color?: number;
}

const CardListItem: React.FC<CardListItemProps> = ({ card, color }) => (
    <div className="cursor-pointer rounded-xl bg-gray-100 p-4 mb-2 text-xl shadow flex flex-col justify-center dark:bg-gray-800 filter active:brightness-75 h-20">
      <h2 style={{ color: ColorUtil.argbToRGB(color) }}>
        {card?.question || ""}
      </h2>
      <span className="text-gray-500 text-xs">
        {card?.answer || ""}
      </span>
    </div>
);

export default CardListItem



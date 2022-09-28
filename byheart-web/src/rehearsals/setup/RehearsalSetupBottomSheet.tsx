import { ReactComponent as FlipIcon } from "../../shared/svgs/flip.svg";
import { ReactComponent as RandomIcon } from "../../shared/svgs/random.svg";
import Box from "@mui/material/Box";
import Switch from "@mui/material/Switch";
import Replay from "@mui/icons-material/Replay";
import VolumeUp from "@mui/icons-material/VolumeUp";
import React from "react";

interface RehearsalSetupBottomSheetProps {
  open: boolean;
  onDismiss: () => void;
}

export const RehearsalSetupBottomSheet: React.FC<RehearsalSetupBottomSheetProps> = () => (
  <Box
    sx={{
      backgroundColor: "white",
      position: "absolute",
      bottom: 0,
      right: 0,
      left: 0,
      borderRadius: "15px 15px 0px 0px",
    }}
  >
    <h2>Start practicing</h2>
    <Box display="flex" justifyContent="space-between">
      <div>
        <FlipIcon />
        <span>Reverse cards</span>
      </div>
      <Switch />
    </Box>
    <Box display="flex" justifyContent="space-between">
      <div>
        <VolumeUp />
        <span>Pronounce cards</span>
      </div>
      <Switch />
    </Box>
    <Box display="flex" justifyContent="space-between">
      <div>
        <RandomIcon />
        <span>Shuffle cards</span>
      </div>
      <Switch />
    </Box>
    <Box display="flex" justifyContent="space-between">
      <div>
        <Replay />
        <span>Repeat wrong answers</span>
      </div>
      <Switch />
    </Box>
  </Box>
);

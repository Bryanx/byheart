import { ReactComponent as FlipIcon } from '../../shared/svgs/flip.svg';
import { ReactComponent as RandomIcon } from '../../shared/svgs/random.svg';
import { BottomSheet } from "react-spring-bottom-sheet";
import { Box, Switch } from '@mui/material';
import Replay from '@mui/icons-material/Replay';
import VolumeUp from '@mui/icons-material/VolumeUp';

interface RehearsalSetupBottomSheetProps {
  open: boolean;
  onDismiss: () => void;
}

const RehearsalSetupBottomSheet: React.FC<RehearsalSetupBottomSheetProps> = ({ open, onDismiss }) => (
    <BottomSheet open={open} onDismiss={onDismiss}>
      <h2 className="text-center m-5">Start practicing</h2>
      <Box display="flex" justifyContent="space-between">
        <div className="flex">
          <FlipIcon/>
          <span>Reverse cards</span>
        </div>
        <Switch/>
      </Box>
      <Box display="flex" justifyContent="space-between">
        <div className="flex">
          <VolumeUp/>
          <span>Pronounce cards</span>
        </div>
        <Switch/>
      </Box>
      <Box display="flex" justifyContent="space-between">
        <div className="flex">
          <RandomIcon/>
          <span>Shuffle cards</span>
        </div>
        <Switch/>
      </Box>
      <Box display="flex" justifyContent="space-between">
        <div className="flex">
          <Replay/>
          <span>Repeat wrong answers</span>
        </div>
        <Switch/>
      </Box>
    </BottomSheet>
);

export default RehearsalSetupBottomSheet


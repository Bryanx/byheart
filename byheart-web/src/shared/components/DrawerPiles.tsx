import React from "react";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import Typography from "@mui/material/Typography";
import { Box, Button, Stack } from "@mui/material";
import { useAppSelector } from "../../app/hooks";
import { selectPileList } from "../../piles/pileSlice";
import ColorUtil from "../util/ColorUtil";
import { Link } from "react-router-dom";

const DrawerPiles: React.FC = () => {
  const piles = useAppSelector(selectPileList);

  return (
    <div>
      {piles.map((pile) => (
        <Box sx={{ m: 2 }}>
          <Link to={`/stacks/${pile.id}`} key={pile.id}>
            <ListItem key={pile?.name} sx={{ p: 0 }}>
              <ListItemButton
                sx={{ borderRadius: "10px", backgroundColor: "grey.900", py: "12px" }}
              >
                <Stack direction="column">
                  <Typography variant="body1" sx={{ color: ColorUtil.argbToRGB(pile?.color) }}>
                    {pile?.name}
                  </Typography>
                  <Typography variant="subtitle2" sx={{ color: "grey.200", fontStyle: "normal" }}>
                    {pile?.cards?.length ?? 0} CARDS
                  </Typography>
                </Stack>
              </ListItemButton>
            </ListItem>
          </Link>
        </Box>
      ))}
      <Box sx={{ mx: 2, mt: 1 }}>
        <Button
          component={Link}
          to="/stacks/new"
          sx={{
            px: 2,
            borderRadius: "10px",
            backgroundColor: "grey.900",
            py: "12px",
            textAlign: "left",
            textTransform: "none",
            display: "block",
            width: "100%",
          }}
        >
          <Stack direction="column">
            <Typography variant="body1">New stack</Typography>
            <Typography variant="subtitle2" sx={{ color: "grey.200", fontStyle: "normal" }}>
              0 CARDS
            </Typography>
          </Stack>
        </Button>
      </Box>
    </div>
  );
};

export default DrawerPiles;

import React from "react";
import Box from "@mui/material/Box";
import Divider from "@mui/material/Divider";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import Typography from "@mui/material/Typography";
import { ReactComponent as Logo } from "../svgs/logo.svg";
import Stack from "@mui/material/Stack";
import { DrawerPiles } from "./DrawerPiles";
import Search from "@mui/icons-material/Search";
import Settings from "@mui/icons-material/Settings";
import { ProfileMenu } from "../../profile/ProfileMenu";

export const DrawerTopMenu: React.FC = () => {
  return (
    <Box>
      <Stack direction="row" sx={{ p: 2 }}>
        <Box sx={{ width: 20, height: 20 }}>
          <Logo />
        </Box>
        <ProfileMenu />
      </Stack>
      <List>
        {["Search", "Settings"].map((text, index) => (
          <ListItem key={text} disablePadding dense>
            <ListItemButton>
              <ListItemIcon
                sx={{
                  minWidth: 0,
                  mr: 1,
                  justifyContent: "center",
                }}
              >
                {index === 0 && <Search fontSize="small" />}
                {index === 1 && <Settings fontSize="small" />}
              </ListItemIcon>
              <Typography variant="body1">{text}</Typography>
            </ListItemButton>
          </ListItem>
        ))}
      </List>
      <Divider />
      <List>
        <Box sx={{ py: 1, pl: 2 }}>
          <Typography variant="subtitle2" sx={{ fontWeight: "700" }}>
            STACKS
          </Typography>
        </Box>
        <DrawerPiles />
      </List>
    </Box>
  );
};

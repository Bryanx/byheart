import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import CssBaseline from "@mui/material/CssBaseline";
import Divider from "@mui/material/Divider";
import Drawer from "@mui/material/Drawer";
import IconButton from "@mui/material/IconButton";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import MenuIcon from "@mui/icons-material/Menu";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import { ReactComponent as Logo } from "../svgs/logo.svg";
import { selectEmail } from "../../profile/profileSlice";
import { useAppSelector } from "../../app/hooks";
import { Stack } from "@mui/material";
import DrawerPiles from "./DrawerPiles";
import Search from "@mui/icons-material/Search";
import Settings from "@mui/icons-material/Settings";

const drawerWidth = 240;

const ResponsiveDrawer: React.FC = () => {
  const [mobileOpen, setMobileOpen] = React.useState(false);
  const email = useAppSelector(selectEmail);

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  const drawer = (
    <Box>
      <Stack direction="row" sx={{ p: 2 }}>
        <Box sx={{ width: 20, height: 20 }}>
          <Logo />
        </Box>
        <Stack direction="column" sx={{ pl: 1, display: "flex", justifyContent: "center" }}>
          <Typography variant="body1">{email}</Typography>
        </Stack>
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

  return (
    <Box sx={{ display: "flex" }}>
      <CssBaseline />
      <AppBar
        position="fixed"
        sx={{
          width: { sm: `calc(100% - ${drawerWidth}px)` },
          ml: { sm: `${drawerWidth}px` },
        }}
      >
        <Toolbar sx={{ display: { sm: "none" } }}>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            edge="start"
            onClick={handleDrawerToggle}
            sx={{ mr: 2, display: { sm: "none" } }}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" noWrap component="div">
            Responsive drawer
          </Typography>
        </Toolbar>
      </AppBar>
      <Box
        component="nav"
        sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}
        aria-label="mailbox folders"
      >
        <Drawer
          className="mobile-drawer"
          variant="temporary"
          open={mobileOpen}
          onClose={handleDrawerToggle}
          ModalProps={{ keepMounted: true }}
          sx={{
            display: { xs: "block", sm: "none" },
            "& .MuiDrawer-paper": { boxSizing: "border-box", width: drawerWidth },
          }}
        >
          {drawer}
        </Drawer>
        <Drawer
          className="desktop-drawer"
          variant="permanent"
          sx={{
            display: { xs: "none", sm: "block" },
            "& .MuiDrawer-paper": { boxSizing: "border-box", width: drawerWidth },
            borderRight: "1px solid #0E121D",
          }}
          open
        >
          {drawer}
        </Drawer>
      </Box>
      <Box
        component="main"
        sx={{ flexGrow: 1, p: 1, width: { sm: `calc(100% - ${drawerWidth}px)` } }}
      >
        <Toolbar sx={{ display: { sm: "none" } }} />
      </Box>
    </Box>
  );
};

export default ResponsiveDrawer;

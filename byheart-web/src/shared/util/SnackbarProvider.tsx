import { Alert, Snackbar } from "@mui/material";
import React, { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../../app/hooks";
import { selectSnackbar, setSnackbar } from "../uiSlice";

const SnackbarProvider: React.FC = ({ children }) => {
  const [open, setOpen] = React.useState(false);
  const snackbar = useAppSelector(selectSnackbar);
  const dispatch = useAppDispatch();

  useEffect(() => {
    if (snackbar) setOpen(true);
  }, [snackbar]);

  const handleClose = (event: React.SyntheticEvent | Event, reason?: string) => {
    if (reason === 'clickaway') return;
    setOpen(false);
    dispatch(setSnackbar(undefined));
  };

  return (<div>
    {children}
    {snackbar && (
        <Snackbar
            open={open}
            autoHideDuration={6000}
            onClose={handleClose}
        >
          <Alert onClose={handleClose} severity={snackbar.type} sx={{ width: '100%' }}>
            {snackbar?.message}
          </Alert>
        </Snackbar>
    )}
  </div>)
};
export default SnackbarProvider;
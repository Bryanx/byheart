export declare module "@mui/material/styles" {
  interface Theme {
    palette: {
      mode: string;
    };
    status: {
      danger: string;
    };
  }

  // allow configuration using `createTheme`
  interface ThemeOptions {
    palette: {
      mode?: string;
    };
    status?: {
      danger?: string;
    };
  }
}

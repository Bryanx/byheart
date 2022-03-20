import { createTheme, ThemeProvider } from "@mui/material";
import { orange } from "@mui/material/colors";
import "./spec.d.ts";

export default function ThemeWrapper(props: {
  children?: JSX.Element,
}) {

  const theme = createTheme({
    status: {
      danger: orange[500],
    },
  });

  return (
    <ThemeProvider theme={theme}>{props.children}</ThemeProvider>
  )
}

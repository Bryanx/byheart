import {createTheme, ThemeProvider} from "@mui/material";
import {orange} from "@mui/material/colors";
import "./spec.d.ts";

const ThemeWrapper = (props: {
  children?: JSX.Element,
}) => {

  const theme = createTheme({
    status: {
      danger: orange[500],
    },
  });

  return (
    <ThemeProvider theme={theme}>{props.children}</ThemeProvider>
  )
};

export default ThemeWrapper


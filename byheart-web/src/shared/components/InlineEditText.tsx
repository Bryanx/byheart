import React from "react";
import Typography from "@mui/material/Typography";
import { Check } from "@mui/icons-material";
import Button from "@mui/material/Button";
import { TextField, Tooltip } from "@mui/material";

interface InlineEditTextProps {
  value: string;
  onChange: (newValue: string) => void;
  onSubmit: (newValue: string) => void;
  color: string;
}

export const InlineEditText: React.FC<InlineEditTextProps> = ({
  value,
  onChange,
  onSubmit,
  color,
}) => {
  const [showTextField, setShowTextField] = React.useState(false);

  return (
    <>
      {!showTextField && (
        <Button
          color="txt"
          sx={{ width: "100%", justifyContent: "start" }}
          onClick={() => setShowTextField(true)}
        >
          <Typography variant="body1">{value}</Typography>
        </Button>
      )}
      {showTextField && (
        <>
          <TextField
            autoFocus
            variant="outlined"
            size="small"
            value={value}
            fullWidth
            onChange={({ target }) => onChange(target.value)}
            onBlur={() => {
              setShowTextField(false);
              onSubmit(value);
            }}
            onKeyPress={({ key }) => {
              if (key === "Enter") {
                setShowTextField(false);
                onSubmit(value);
              }
            }}
            sx={{
              "& .MuiOutlinedInput-root": {
                "& fieldset": {
                  borderColor: color,
                },
                "&:hover fieldset": {
                  borderColor: color,
                },
                "&.Mui-focused fieldset": {
                  borderColor: color,
                  borderWidth: "1px",
                },
              },
            }}
            InputProps={{
              sx: { pr: 0.5 },
              endAdornment: (
                <Tooltip
                  arrow
                  enterDelay={300}
                  title={
                    <div>
                      <span style={{ color: "txt.main" }}>Save changes</span>
                      <br />
                      <span style={{ color: "txt.main", opacity: 0.5 }}>Hotkey: ⏎</span>
                    </div>
                  }
                >
                  <Button
                    variant="contained"
                    startIcon={<Check />}
                    size="small"
                    onClick={() => setShowTextField(false)}
                    sx={{
                      m: 0,
                      px: 3,
                      backgroundColor: color,
                    }}
                  >
                    Save
                  </Button>
                </Tooltip>
              ),
            }}
          />
        </>
      )}
    </>
  );
};

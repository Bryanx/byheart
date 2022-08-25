const colors = require("tailwindcss/colors");

module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  darkMode: "class",
  theme: {
    colors: {
      primary: "#5C6BC0",
      "primary-dark": "#3F51B5",
      bgcolor: colors.white,
      bgcolordark: "#0E121D",
      transparent: "transparent",
      black: colors.black,
      white: colors.white,
      gray: colors.coolGray,
    },
  },
  plugins: [],
};

export class ColorUtil {
  static argbToRGB(color?: number) {
    if (color === undefined) return "";
    else return "#" + ("000000" + (color & 0xffffff).toString(16)).slice(-6);
  }
}

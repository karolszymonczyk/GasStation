package utils;

public interface ErrorUtils {
  
  static boolean checkInt(String input) {
    int i;
    try {
      i = Integer.parseInt(input);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  static boolean checkFloat(String input) {
    float f;
    try {
      f = Float.parseFloat(input);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }
}

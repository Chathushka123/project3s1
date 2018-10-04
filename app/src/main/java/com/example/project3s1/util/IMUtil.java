package com.example.project3s1.util;


import android.database.DefaultDatabaseErrorHandler;

import com.example.project3s1.constants.Colour;

public class IMUtil {
    public static int[] extractRgb(int compressed) {
        int[] rgb = new int[3];

        rgb[0] = (compressed >> 16) & 0xff;
        rgb[1] = (compressed >> 8) & 0xff;
        rgb[2] = (compressed) & 0xff;

        return rgb;
    }

    public static int colourRound(int val) {
        if (val >= 0 && val < 64) {
            val = 0;
        } else if (val >= 64 && val < 192) {
            val = 128;
        } else {
            val = 255;

        }
        return val;
    }

  /*  public static String findColour(int[] rgb) {
        String colour = "no";
        int r = colourRound(rgb[0]);
        int g = colourRound(rgb[1]);
        int b = colourRound(rgb[2]);

        if (r == 0 && g == 0 && b == 0) {
            colour = "black";
        } else if (r == 0 && g == 255 && b == 0) {
            colour = "lime";
        } else if (r == 0 && g == 0 && b == 255) {
            colour = "blue";
        } else if (r == 0 && g == 128 && b == 128) {
            colour = "teal";
        } else if (r == 0 && g == 0 && b == 128) {
            colour = "navy";
        } else if (r == 0 && g == 255 && b == 255) {
            colour = "cyan";
        } else if (r == 0 && g == 128 && b == 0) {
            colour = "grean";
        } else if (r == 128 && g == 128 && b == 128) {
            colour = "gray";
        } else if (r == 128 && g == 0 && b == 0) {
            colour = "maroon";
        } else if (r == 128 && g == 128 && b == 0) {
            colour = "olive";
        } else if (r == 128 && g == 0 && b == 128) {
            colour = "purple";
        } else if (r == 255 && g == 255 && b == 255) {
            colour = "white";
        } else if (r == 255 && g == 0 && b == 0) {
            colour = "red";
        } else if (r == 255 && g == 255 && b == 0) {
            colour = "yellow";
        } else if (r == 255 && g == 0 && b == 255) {
            colour = "magenta";
        }


        return colour;
    }*/

    public static int hexValue(int[] rgb){

      int r = colourRound(rgb[0]);
      int g = colourRound(rgb[1]);
      int b = colourRound(rgb[2]);
      int hex;

      hex = ((r & 0xff) << 16) + ((g & 0xff) << 8) + (b & 0xff);
      return hex;
    }

    public static String findColour(int[] rgb)
    {
      int hexValue=hexValue(rgb);

      switch(hexValue){
          case Colour.BLACK: return "BLACK";
          case Colour.WHITE: return "WHITE";
          case Colour.RED:   return "RED";
          case Colour.LIME:  return "LIME";
          case Colour.BLUE:  return "BLUE";
          case Colour.YELLOW:return "YELLOW";
          case Colour.CYAN:  return "CYAN";
          case Colour.MAGENTA: return "MAGENTA";
          case Colour.GRAY:  return "GRAY";
          case Colour.MAROON:return "MAROON";
          case Colour.OLIVE: return "OLIVE";
          case Colour.GREEN: return "GREEN";
          case Colour.PURPLE: return "PURPLE";
          case Colour.TEAL:  return "TEAL";
          case Colour.NAVY:  return "NAVY";
      }
  }
}

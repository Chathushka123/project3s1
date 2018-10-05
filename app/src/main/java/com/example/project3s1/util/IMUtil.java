package com.example.project3s1.util;


import com.example.project3s1.constants.Colour;

public class IMUtil
{
    public static int[] extractRgb(int compressed)
    {
        int[] rgb = new int[3];

        rgb[0] = (compressed >> 16) & 0xff;
        rgb[1] = (compressed >> 8) & 0xff;
        rgb[2] = (compressed) & 0xff;

        return rgb;
    }


    public static int colourRound(int val)
    {

        if (val >= 0 && val < 64) {
            val = 0;
        } else if (val >= 64 && val < 192) {
            val = 128;
        } else {
            val = 255;

        }

        return val;
    }

    public static int hexValue(int[] rgb)
    {
        int r = colourRound(rgb[0]);
        int g = colourRound(rgb[1]);
        int b = colourRound(rgb[2]);
        int hex;

        hex = ((r & 0xff) << 16) + ((g & 0xff) << 8) + (b & 0xff);

        return hex;
    }

    public static String findColour(int[] rgb)
    {
        String colour = "null";

        int hexValue = hexValue(rgb);

        switch (hexValue) {
            case Colour.BLACK:
                return "BLACK";
            case Colour.WHITE:
                return "WHITE";
            case Colour.RED:
                return "RED";
            case Colour.LIME:
                return "LIME";
            case Colour.BLUE:
                return "BLUE";
            case Colour.YELLOW:
                return "YELLOW";
            case Colour.CYAN:
                return "CYAN";
            case Colour.MAGENTA:
                return "MAGENTA";
            case Colour.GRAY:
                return "GRAY";
            case Colour.MAROON:
                return "MAROON";
            case Colour.OLIVE:
                return "OLIVE";
            case Colour.GREEN:
                return "GREEN";
            case Colour.PURPLE:
                return "PURPLE";
            case Colour.TEAL:
                return "TEAL";
            case Colour.NAVY:
                return "NAVY";

        }

        return colour;
    }
}

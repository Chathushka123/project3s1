package com.example.project3s1.util;

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
}

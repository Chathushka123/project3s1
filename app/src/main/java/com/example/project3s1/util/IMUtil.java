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

   /* public static String findColour1(int[] rgb){
        String colour;
        int r=0; int g=0; int b=0;

         r=rgb[0];
         g=rgb[1];
         b=rgb[2];

        int max=0;

        if(g>=b && g>=r){
            max=g;
        }
        else if(b>=g && b>=r){
            max=b;
        }
        else {
            max=r;
        }
        int x=10;
        switch(max){
            case x :colour="red"; break;
            case g :colour="grean"; break;
            case b :colour="blue"; break;
            default:colour="white";
        }
            if(max==r){
                colour="red";
            }
            else if(max==g){
                colour="grean";
            }
            else{
                colour="blue";
            }
        return colour;
    }*/


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

    public static String findColour(int[] rgb)
    {
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
    }
}

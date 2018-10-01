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

    public static String findColour(int[] rgb){
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
       /* int x=10;
        switch(max){
            case x :colour="red"; break;
            case g :colour="grean"; break;
            case b :colour="blue"; break;
            default:colour="white";
        }*/
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
    }
}

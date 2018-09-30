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
        int r=rgb[0];
        int g=rgb[1];
        int b=rgb[2];

        int max;

        if(g>=b && g>=r){
            max=g;
        }
        else if(b>=g && b>=r){
            max=b;
        }
        else {
            max=r;
        }

        /*switch(max){
            case r :colour="red"; break;
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

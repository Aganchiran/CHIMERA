package com.aganchiran.chimera.chimeracore.dice;

public class AnimaDice {

    public static int getRoll(){
        int openThreshold = 90;

        int roll = (int) Math.round(Math.random() * 100);
        int rollSum = roll;

        while (roll >= openThreshold){
            roll = (int) Math.round(Math.random() * 100);
            openThreshold++;
            rollSum += roll;
        }

        return rollSum;
    }

}

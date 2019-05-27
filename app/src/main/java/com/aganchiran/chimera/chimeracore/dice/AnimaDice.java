package com.aganchiran.chimera.chimeracore.dice;

import java.util.Random;

public class AnimaDice {

    public static int getRollOpen(){
        int openThreshold = 90;

        final Random random = new Random();
        int roll = random.nextInt(100) + 1;
        int rollSum = roll;

        while (roll >= openThreshold){
            roll = random.nextInt(100) + 1;
            openThreshold++;
            rollSum += roll;
        }

        return rollSum;
    }

    public static int getRoll(){
        final Random random = new Random();
        return random.nextInt(100) + 1;
    }

}

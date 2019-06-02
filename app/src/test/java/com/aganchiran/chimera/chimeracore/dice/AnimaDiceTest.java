package com.aganchiran.chimera.chimeracore.dice;

import org.junit.Test;

import static org.junit.Assert.*;

public class AnimaDiceTest {

    @Test
    public void getRollOpen() {
        for (long i = 0 ; i < 10000000 ; i++){
            int roll = AnimaDice.getRollOpen();
            assert roll >= 1;
        }
    }

    @Test
    public void getRoll() {
        for (int i = 0 ; i < 10000000 ; i++){
            int roll = AnimaDice.getRoll();
            assert roll <= 100 && roll >= 1;
        }
    }
}
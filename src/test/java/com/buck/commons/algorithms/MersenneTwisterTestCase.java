/*
 * Copyright 2010-2013 Robert J. Buck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.buck.commons.algorithms;

import com.buck.commons.io.ByteBufferInputStream;
import com.buck.commons.io.ByteBufferOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Tests the MersenneTwister class.
 *
 * @author Robert J. Buck
 */
public class MersenneTwisterTestCase {
    /**
     * Tests the code.
     */
    @Test
    public void testMersenneTwister() {

        MersenneTwister r;

        r = new MersenneTwister(new int[]{0x123, 0x234, 0x345, 0x456});
        for (int j = 0; j < 1000; j++) {
            // first, convert the int from signed to "unsigned"
            long l = (long) r.nextInt();
            if (l < 0) {
                l += 4294967296L;  // max int value
            }
            String s = String.valueOf(l);
            while (s.length() < 10) {
                s = " " + s;  // buffer
            }
        }

        // SPEED TEST

        final long SEED = 4357;

        {
            MersenneTwister s = new MersenneTwister(SEED);
            MersenneTwister t = new MersenneTwister(SEED);
            Assert.assertTrue(s.stateEquals(t));
        }
        {
            MersenneTwister s = new MersenneTwister(SEED);
            s.nextChar();
            MersenneTwister t = new MersenneTwister(SEED);
            Assert.assertFalse(s.stateEquals(t));
        }
        {
            MersenneTwister s = new MersenneTwister(SEED);
            s.nextChar();
            MersenneTwister t = null;
            try {
                t = (MersenneTwister) s.clone();
            } catch (CloneNotSupportedException e) {
                Assert.fail();
            }
            Assert.assertTrue(t != null && s.stateEquals(t));
        }
        {
            MersenneTwister s = new MersenneTwister(SEED);
            Assert.assertTrue(s.stateEquals(s));
        }
        {
            MersenneTwister s = new MersenneTwister(SEED);
            Assert.assertFalse(s.stateEquals(null));
        }

        {
            boolean caught = false;
            int[] arr = {};
            r = new MersenneTwister();
            try {
                r.setSeed(arr);
            } catch (IllegalArgumentException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }

        r = new MersenneTwister(SEED);
        for (int j = 0; j < 100000000; j++) {
            r.nextInt();
        }

        // TEST TO COMPARE TYPE CONVERSION BETWEEN

        r = new MersenneTwister(SEED);
        for (int j = 0; j < 1000; j++) {
            r.nextBoolean();
        }

        r = new MersenneTwister(SEED);
        for (int j = 0; j < 1000; j++) {
            r.nextChar();
        }

        r = new MersenneTwister(SEED);
        for (int j = 0; j < 1000; j++) {
            r.nextBoolean(j / 999.0);
        }

        r = new MersenneTwister(SEED);
        for (int j = 0; j < 1000; j++) {
            r.nextBoolean(j / 999.0f);
        }
        {
            boolean caught = false;
            try {
                double p = -0.1;
                r.nextBoolean(p);
            } catch (IllegalArgumentException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            try {
                double p = 1.1;
                r.nextBoolean(p);
            } catch (IllegalArgumentException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            try {
                float p = -0.1f;
                r.nextBoolean(p);
            } catch (IllegalArgumentException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            try {
                float p = 1.1f;
                r.nextBoolean(p);
            } catch (IllegalArgumentException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }

        byte[] bytes = new byte[1000];
        r = new MersenneTwister(SEED);
        r.nextBytes(bytes);

        r = new MersenneTwister(SEED);
        for (int j = 0; j < 1000; j++) {
            r.nextByte();
        }

        r = new MersenneTwister(SEED);
        for (int j = 0; j < 1000; j++) {
            r.nextShort();
        }

        r = new MersenneTwister(SEED);
        for (int j = 0; j < 1000; j++) {
            r.nextInt();
        }
        r = new MersenneTwister(SEED);
        for (int j = 0; j < 1000; j++) {
            r.nextInt(Integer.MAX_VALUE);
        }
        {
            boolean caught = false;
            try {
                r.nextInt(-1);
            } catch (IllegalArgumentException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }

        r = new MersenneTwister(SEED);
        int max = 1;
        for (int j = 0; j < 1000; j++) {
            r.nextInt(max);
            max *= 2;
            if (max <= 0) {
                max = 1;
            }
        }

        r = new MersenneTwister(SEED);
        for (int j = 0; j < 1000; j++) {
            r.nextLong();
        }
        {
            boolean caught = false;
            try {
                r.nextLong(-1);
            } catch (IllegalArgumentException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }

        r = new MersenneTwister(SEED);
        long max2 = 1;
        for (int j = 0; j < 1000; j++) {
            r.nextLong(max2);
            max2 *= 2;
            if (max2 <= 0) {
                max2 = 1;
            }
        }

        r = new MersenneTwister(SEED);
        for (int j = 0; j < 1000; j++) {
            r.nextFloat();
        }

        r = new MersenneTwister(SEED);
        for (int j = 0; j < 1000; j++) {
            r.nextDouble();
        }

        r = new MersenneTwister(SEED);
        for (int j = 0; j < 1000; j++) {
            r.nextGaussian();
        }
    }

    @Test
    public void testSavingMersenneTwisterState() {
        MersenneTwister mtA = new MersenneTwister();
        mtA.nextInt();

        boolean caught = false;
        ByteBufferOutputStream bbos = new ByteBufferOutputStream();
        try {
            mtA.writeState(new DataOutputStream(bbos));

            MersenneTwister mtB = new MersenneTwister();
            mtB.nextInt();

            ByteBuffer buffer = bbos.getBuffer();
            buffer.flip();
            ByteBufferInputStream bbis = new ByteBufferInputStream(buffer);
            mtB.readState(new DataInputStream(bbis));

            Assert.assertTrue(mtA.stateEquals(mtB));
        } catch (IOException e) {
            caught = true;
        }
        Assert.assertFalse(caught);
    }
}

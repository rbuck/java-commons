/*
 * Copyright 2010 Robert J. Buck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.buck.commons.algorithms;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Tests the BinaryData class.
 *
 * @author Robert J. Buck
 */
public final class BinaryDataTestCase {

    /**
     * Test bit mask operations in BinaryData class.
     */
    @Test
    public void testBitMaskOperations() {
        // test short conversions
        {
            byte[] array = {0x00, 0x00, 0x00, 0x00};
            int offset = 1;
            byte rvalue = Byte.MIN_VALUE;
            do {
                BinaryData.storeByteAtOffset(array, offset, rvalue);
                byte lvalue = BinaryData.loadByteAtOffset(array, offset);
                Assert.assertEquals(lvalue, rvalue);
                // reset for next iteration
                array[offset] = 0;
                array[offset + 1] = 0;
                rvalue++;
            } while (rvalue < Byte.MAX_VALUE);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.storeByteAtOffset(array, -1, (byte) 0);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.storeByteAtOffset(array, 8, (byte) 0);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.loadByteAtOffset(array, -1);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.loadByteAtOffset(array, 8);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        // test short conversions
        {
            byte[] array = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            int offset = 1;
            short rvalue = Short.MIN_VALUE;
            do {
                BinaryData.storeShortAtOffset(array, offset, rvalue);
                short lvalue = BinaryData.loadShortAtOffset(array, offset);
                Assert.assertEquals(lvalue, rvalue);
                // reset for next iteration
                array[offset] = 0;
                array[offset + 1] = 0;
                rvalue++;
            } while (rvalue < Short.MAX_VALUE);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.storeShortAtOffset(array, -1, (short) 0);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.storeShortAtOffset(array, 8, (short) 0);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.loadShortAtOffset(array, -1);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.loadShortAtOffset(array, 8);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        // test int conversions
        {
            byte[] array = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            int offset = 1;
            Random rng = new Random();
            int lo = rng.nextInt();
            int hi;
            final int testRange = 1000000;
            if ((Integer.MAX_VALUE - testRange) < lo) {
                hi = lo;
                lo = hi - testRange;
            } else {
                hi = lo + testRange;
            }
            int rvalue = lo;
            do {
                BinaryData.storeIntAtOffset(array, offset, rvalue);
                int lvalue = BinaryData.loadIntAtOffset(array, offset);
                Assert.assertEquals(lvalue, rvalue);
                // reset for next iteration
                array[offset] = 0;
                array[offset + 1] = 0;
                rvalue++;
            } while (rvalue < hi);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.storeIntAtOffset(array, -1, 0);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.storeIntAtOffset(array, 8, 0);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.loadIntAtOffset(array, -1);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.loadIntAtOffset(array, 8);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        // test long conversions
        {
            // limit the number of tests, because otherwise they run for hours
            doLongTest(Long.MIN_VALUE, Long.MIN_VALUE + 1000);
            doLongTest(-1000, 1000);
            doLongTest(Long.MAX_VALUE - 1000, Long.MAX_VALUE);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.storeLongAtOffset(array, -1, 0);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.storeLongAtOffset(array, 8, 0);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.loadLongAtOffset(array, -1);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            byte[] array = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            try {
                BinaryData.loadLongAtOffset(array, 8);
            } catch (IndexOutOfBoundsException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
    }

    private void doLongTest(long start, long end) {
        byte[] array = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        int offset = 1;
        long rvalue = start;
        do {
            BinaryData.storeLongAtOffset(array, offset, rvalue);
            long lvalue = BinaryData.loadLongAtOffset(array, offset);
            Assert.assertEquals(lvalue, rvalue);
            Assert.assertEquals(array[0], 0);
            Assert.assertEquals(array[9], 0);
            // reset for next iteration
            array[offset] = 0;
            array[offset + 1] = 0;
            array[offset + 2] = 0;
            array[offset + 3] = 0;
            array[offset + 4] = 0;
            array[offset + 5] = 0;
            array[offset + 6] = 0;
            array[offset + 7] = 0;
            rvalue++;
        } while (rvalue < end);
    }
}

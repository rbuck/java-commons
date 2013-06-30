/*
 * Copyright 2010-2013 Robert J. Buck
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

import com.buck.commons.i18n.ResourceBundle;

/**
 * Utilities to convert primitive data types to and from byte arrays.
 *
 * @author Robert J. Buck
 */
@SuppressWarnings("ALL")
public final class BinaryData {

    /**
     * The size of a byte.
     */
    public static final int sizeOfByte;

    static {
        sizeOfByte = 1;
    }

    /**
     * The size of a short.
     */
    public static final int sizeOfShort;

    static {
        sizeOfShort = 2;
    }

    /**
     * The size of an int.
     */
    public static final int sizeOfInt;

    static {
        sizeOfInt = 4;
    }

    /**
     * The size of a long.
     */
    public static final int sizeOfLong;

    static {
        sizeOfLong = 8;
    }

    /**
     * Store a byte primitive type at the indicated offset in the byte array.
     * Perform full range checks and throw an exception if the caller specifies
     * an illegal offset.
     *
     * @param array  the byte array to store the byte into
     * @param offset the offset at which to store the byte
     * @param value  the byte data to store at array[offset]
     * @throws IndexOutOfBoundsException if the offset is less than zero or
     *                                   greater than the array length minus the
     *                                   sizeof a byte.
     */
    public static void storeByteAtOffset(byte[] array, int offset, byte value) throws IndexOutOfBoundsException {
        if (!(offset < 0) && !(offset > array.length - sizeOfByte)) {
            storeByteAtOffsetUnsafe(array, offset, value);
        } else {
            Object[] arguments = {array.length, offset};
            String message = ResourceBundle.formatResourceBundleMessage(BinaryData.class,
                    "BINARY_DATA_ARRAY_BOUNDS_WRITE_ERROR", arguments);
            throw new IndexOutOfBoundsException(message);
        }
    }

    /**
     * Store a byte primitive type at the indicated offset in the byte array.
     * This method does no range checks, and therefore is considered unsafe;
     * callers must perform range checks themselves.
     *
     * @param array  the byte array to store the byte into
     * @param offset the offset at which to store the byte
     * @param value  the byte data to store at array[offset]
     */
    public static void storeByteAtOffsetUnsafe(byte[] array, int offset, byte value) {
        array[offset] = value;
    }

    /**
     * Load a byte primitive type located at the indicated offset in the byte
     * array. Perform full range checks and throw an exception if the caller
     * specifies an illegal offset.
     *
     * @param array  the byte array to load the byte from
     * @param offset the offset from which to load the byte
     * @return the byte data loaded from the array at offset
     * @throws IndexOutOfBoundsException if the offset is less than zero or
     *                                   greater than the array length minus the
     *                                   sizeof a byte.
     */
    public static byte loadByteAtOffset(byte[] array, int offset) throws IndexOutOfBoundsException {
        if (!(offset < 0) && !(offset > array.length - sizeOfByte)) {
            return loadByteAtOffsetUnsafe(array, offset);
        } else {
            Object[] arguments = {array.length, offset};
            String message = ResourceBundle.formatResourceBundleMessage(BinaryData.class,
                    "BINARY_DATA_ARRAY_BOUNDS_READ_ERROR", arguments);
            throw new IndexOutOfBoundsException(message);
        }
    }

    /**
     * Load a short primitive type located at the indicated offset in the byte
     * array. This method does no range checks, and therefore is considered
     * unsafe; callers must perform range checks themselves.
     *
     * @param array  the byte array to load the byte from
     * @param offset the offset from which to load the byte
     * @return the byte data loaded from the array at offset
     */
    public static byte loadByteAtOffsetUnsafe(byte[] array, int offset) {
        return array[offset];
    }

    /**
     * Store a short primitive type at the indicated offset in the byte array.
     * Perform full range checks and throw an exception if the caller specifies
     * an illegal offset.
     *
     * @param array  the byte array to store the short into
     * @param offset the offset at which to store the short
     * @param value  the short data to store at array[offset]
     * @throws IndexOutOfBoundsException if the offset is less than zero or
     *                                   greater than the array length minus the
     *                                   sizeof a short.
     */
    public static void storeShortAtOffset(byte[] array, int offset, short value) throws IndexOutOfBoundsException {
        if (!(offset < 0) && !(offset > array.length - sizeOfShort)) {
            storeShortAtOffsetUnsafe(array, offset, value);
        } else {
            Object[] arguments = {array.length, offset};
            String message = ResourceBundle.formatResourceBundleMessage(BinaryData.class,
                    "BINARY_DATA_ARRAY_BOUNDS_WRITE_ERROR", arguments);
            throw new IndexOutOfBoundsException(message);
        }
    }

    /**
     * Store a short primitive type at the indicated offset in the byte array.
     * This method does no range checks, and therefore is considered unsafe;
     * callers must perform range checks themselves.
     *
     * @param array  the byte array to store the short into
     * @param offset the offset at which to store the short
     * @param value  the short data to store at array[offset]
     */
    @SuppressWarnings({"OctalInteger"})
    public static void storeShortAtOffsetUnsafe(byte[] array, int offset, short value) {
        array[offset] = (byte) (value >>> 010);
        array[offset + 1] = (byte) (value & 0x00FF);
    }

    /**
     * Load a short primitive type located at the indicated offset in the byte
     * array. Perform full range checks and throw an exception if the caller
     * specifies an illegal offset.
     *
     * @param array  the byte array to load the short from
     * @param offset the offset from which to load the short
     * @return the short data loaded from the array at offset
     * @throws IndexOutOfBoundsException if the offset is less than zero or
     *                                   greater than the array length minus the
     *                                   sizeof a short.
     */
    public static short loadShortAtOffset(byte[] array, int offset) throws IndexOutOfBoundsException {
        if (!(offset < 0) && !(offset > array.length - sizeOfShort)) {
            return loadShortAtOffsetUnsafe(array, offset);
        } else {
            Object[] arguments = {array.length, offset};
            String message = ResourceBundle.formatResourceBundleMessage(BinaryData.class,
                    "BINARY_DATA_ARRAY_BOUNDS_READ_ERROR", arguments);
            throw new IndexOutOfBoundsException(message);
        }
    }

    /**
     * Load a short primitive type located at the indicated offset in the byte
     * array. This method does no range checks, and therefore is considered
     * unsafe; callers must perform range checks themselves.
     *
     * @param array  the byte array to load the short from
     * @param offset the offset from which to load the short
     * @return the short data loaded from the array at offset
     */
    @SuppressWarnings({"OctalInteger"})
    public static short loadShortAtOffsetUnsafe(byte[] array, int offset) {
        return (short) ((array[offset] & 0x00FF) << 010 |
                (array[offset + 1] & 0x00FF)
        );
    }

    /**
     * Store a int primitive type at the indicated offset in the byte array.
     * Perform full range checks and throw an exception if the caller specifies
     * an illegal offset.
     *
     * @param array  the byte array to store the long into
     * @param offset the offset at which to store the long
     * @param value  the int data to store at array[offset]
     * @throws IndexOutOfBoundsException if the offset is less than zero or
     *                                   greater than the array length minus the
     *                                   sizeof a long.
     */
    public static void storeIntAtOffset(byte[] array, int offset, int value) throws IndexOutOfBoundsException {
        if (!(offset < 0) && !(offset > array.length - sizeOfInt)) {
            storeIntAtOffsetUnsafe(array, offset, value);
        } else {
            Object[] arguments = {array.length, offset};
            String message = ResourceBundle.formatResourceBundleMessage(BinaryData.class,
                    "BINARY_DATA_ARRAY_BOUNDS_WRITE_ERROR", arguments);
            throw new IndexOutOfBoundsException(message);
        }
    }

    /**
     * Store a long primitive type at the indicated offset in the byte array.
     * This method does no range checks, and therefore is considered unsafe;
     * callers must perform range checks themselves.
     *
     * @param array  the byte array to store the long into
     * @param offset the offset at which to store the long
     * @param value  the long data to store at array[offset]
     */
    @SuppressWarnings({"OctalInteger"})
    public static void storeIntAtOffsetUnsafe(byte[] array, int offset, int value) {
        array[offset] = (byte) (value >>> 030);
        array[offset + 1] = (byte) (value >>> 020);
        array[offset + 2] = (byte) (value >>> 010);
        array[offset + 3] = (byte) (value & 0x000000FF);
    }

    /**
     * Load a long primitive type located at the indicated offset in the byte
     * array. Perform full range checks and throw an exception if the caller
     * specifies an illegal offset.
     *
     * @param array  the byte array to load the long from
     * @param offset the offset from which to load the long
     * @return the long data loaded from the array at offset
     * @throws IndexOutOfBoundsException if the offset is less than zero or
     *                                   greater than the array length minus the
     *                                   sizeof a long.
     */
    public static int loadIntAtOffset(byte[] array, int offset) throws IndexOutOfBoundsException {
        if (!(offset < 0) && !(offset > array.length - sizeOfInt)) {
            return loadIntAtOffsetUnsafe(array, offset);
        } else {
            Object[] arguments = {array.length, offset};
            String message = ResourceBundle.formatResourceBundleMessage(BinaryData.class,
                    "BINARY_DATA_ARRAY_BOUNDS_READ_ERROR", arguments);
            throw new IndexOutOfBoundsException(message);
        }
    }

    /**
     * Load a long primitive type located at the indicated offset in the byte
     * array. This method does no range checks, and therefore is considered
     * unsafe; callers must perform range checks themselves.
     *
     * @param array  the byte array to load the long from
     * @param offset the offset from which to load the long
     * @return the long data loaded from the array at offset
     */
    @SuppressWarnings({"OctalInteger"})
    public static int loadIntAtOffsetUnsafe(byte[] array, int offset) {
        return ((array[offset] & 0x000000FF) << 030 |
                (array[offset + 1] & 0x000000FF) << 020 |
                (array[offset + 2] & 0x000000FF) << 010 |
                (array[offset + 3] & 0x000000FF)
        );
    }

    /**
     * Store a long primitive type at the indicated offset in the byte array.
     * Perform full range checks and throw an exception if the caller specifies
     * an illegal offset.
     *
     * @param array  the byte array to store the long into
     * @param offset the offset at which to store the long
     * @param value  the long data to store at array[offset]
     * @throws IndexOutOfBoundsException if the offset is less than zero or
     *                                   greater than the array length minus the
     *                                   sizeof a long.
     */
    public static void storeLongAtOffset(byte[] array, int offset, long value) throws IndexOutOfBoundsException {
        if (!(offset < 0) && !(offset > array.length - sizeOfLong)) {
            storeLongAtOffsetUnsafe(array, offset, value);
        } else {
            Object[] arguments = {array.length, offset};
            String message = ResourceBundle.formatResourceBundleMessage(BinaryData.class,
                    "BINARY_DATA_ARRAY_BOUNDS_WRITE_ERROR", arguments);
            throw new IndexOutOfBoundsException(message);
        }
    }

    /**
     * Store a long primitive type at the indicated offset in the byte array.
     * This method does no range checks, and therefore is considered unsafe;
     * callers must perform range checks themselves.
     *
     * @param array  the byte array to store the long into
     * @param offset the offset at which to store the long
     * @param value  the long data to store at array[offset]
     */
    @SuppressWarnings({"OctalInteger"})
    public static void storeLongAtOffsetUnsafe(byte[] array, int offset, long value) {
        array[offset] = (byte) (value >>> 070);
        array[offset + 1] = (byte) (value >>> 060);
        array[offset + 2] = (byte) (value >>> 050);
        array[offset + 3] = (byte) (value >>> 040);
        array[offset + 4] = (byte) (value >>> 030);
        array[offset + 5] = (byte) (value >>> 020);
        array[offset + 6] = (byte) (value >>> 010);
        array[offset + 7] = (byte) (value & 0x00000000000000FFL);
    }

    /**
     * Load a long primitive type located at the indicated offset in the byte
     * array. Perform full range checks and throw an exception if the caller
     * specifies an illegal offset.
     *
     * @param array  the byte array to load the long from
     * @param offset the offset from which to load the long
     * @return the long data loaded from the array at offset
     * @throws IndexOutOfBoundsException if the offset is less than zero or
     *                                   greater than the array length minus the
     *                                   sizeof a long.
     */
    public static long loadLongAtOffset(byte[] array, int offset) throws IndexOutOfBoundsException {
        if (!(offset < 0) && !(offset > array.length - sizeOfLong)) {
            return loadLongAtOffsetUnsafe(array, offset);
        } else {
            Object[] arguments = {array.length, offset};
            String message = ResourceBundle.formatResourceBundleMessage(BinaryData.class,
                    "BINARY_DATA_ARRAY_BOUNDS_READ_ERROR", arguments);
            throw new IndexOutOfBoundsException(message);
        }
    }

    /**
     * Load a long primitive type located at the indicated offset in the byte
     * array. This method does no range checks, and therefore is considered
     * unsafe; callers must perform range checks themselves.
     *
     * @param array  the byte array to load the long from
     * @param offset the offset from which to load the long
     * @return the long data loaded from the array at offset
     */
    @SuppressWarnings({"OctalInteger"})
    public static long loadLongAtOffsetUnsafe(byte[] array, int offset) {
        return (
                (array[offset] & 0x00000000000000FFL) << 070 |
                        (array[offset + 1] & 0x00000000000000FFL) << 060 |
                        (array[offset + 2] & 0x00000000000000FFL) << 050 |
                        (array[offset + 3] & 0x00000000000000FFL) << 040 |
                        (array[offset + 4] & 0x00000000000000FFL) << 030 |
                        (array[offset + 5] & 0x00000000000000FFL) << 020 |
                        (array[offset + 6] & 0x00000000000000FFL) << 010 |
                        (array[offset + 7] & 0x00000000000000FFL)
        );
    }
}

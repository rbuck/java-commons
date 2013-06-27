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

package com.buck.commons.io;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

/**
 * Tests the ByteBufferOutputStream class.
 *
 * @author Robert J. Buck
 */
public class ByteBufferOutputStreamTestCase {

    private void assertByteArraysIdentical(String testPrefix, byte[] expect, byte[] result) {
        Assert.assertEquals(testPrefix + "byte array lengths", expect.length, result.length);
        for (int i = 0; i < expect.length; i++) {
            Assert.assertEquals(expect[i], result[i]);
        }
    }

    private void internalTestToArray(String testPrefix, ByteBuffer buffer) throws IOException {
        final byte[] expect = {0, 1, 2, 3, 4, 5, 6, 7};
        {
            ByteBufferOutputStream bbos = new ByteBufferOutputStream(buffer);
            try {
                bbos.write(expect);
                byte[] result = bbos.toByteArray();
                Assert.assertNotNull(testPrefix, result);
                assertByteArraysIdentical(testPrefix, expect, result);
            } finally {
                bbos.close();
            }
        }
    }

    @Test
    public void testToArray() throws IOException {
        internalTestToArray("Indirect toArray: ", ByteBuffer.allocate(8));
        internalTestToArray("Direct toArray: ", ByteBuffer.allocateDirect(8));
    }

    public void internalTestToString(String testPrefix, ByteBuffer buffer) throws IOException {
        ByteBufferOutputStream bbos = new ByteBufferOutputStream(buffer);
        try {
            final String expect = "hello world";
            bbos.write(expect.getBytes());
            final String result = bbos.toString();
            Assert.assertEquals(testPrefix, expect, result);
        } finally {
            bbos.close();
        }
    }

    @Test
    public void testToString() throws IOException {
        internalTestToString("Indirect toString: ", ByteBuffer.allocate(64));
        internalTestToString("Direct toString: ", ByteBuffer.allocateDirect(64));
    }

    @Test
    public void testToArrayFlip() {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.putFloat(7.8f);

        final int position = buffer.position();
        byte[] out = new byte[position];
        buffer.rewind();
        buffer.get(out);
        buffer.position(position);
    }

    private void internalTestWriteTo(String testPrefix, ByteBuffer buffer) throws IOException {
        ByteBufferOutputStream bbos = new ByteBufferOutputStream(buffer);
        try {
            final String expect = "hello world";
            bbos.write(expect.getBytes());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                bbos.writeTo(baos);
                bbos.write(expect.getBytes()); // must be able to occur after a write
                final String result = new String(baos.toByteArray());
                Assert.assertEquals(testPrefix, expect, result);
            } finally {
                baos.close();
            }
        } finally {
            bbos.close();
        }
    }

    @Test
    public void testWriteTo() throws IOException {
        {
            internalTestWriteTo("Indirect writeTo: ", ByteBuffer.allocate(64));
            internalTestWriteTo("Direct writeTo: ", ByteBuffer.allocateDirect(64));
        }
        {
            final byte[] ia = "word".getBytes("US-ASCII");
            ByteBufferOutputStream bbos = new ByteBufferOutputStream();
            bbos.close(); // asserts no effect!
            bbos.write(ia);
            Assert.assertEquals(4, bbos.size());
            final byte[] oa = bbos.toByteArray();
            assertByteArraysIdentical("write(byte[]) -> toByteArray()", ia, oa);
            bbos.reset();
            Assert.assertEquals(0, bbos.size());
        }
        {
            final byte[] ia = "word".getBytes("US-ASCII");
            ByteBufferOutputStream bbos = new ByteBufferOutputStream();
            bbos.close(); // asserts no effect!
            bbos.write(ia);
            Assert.assertEquals(4, bbos.size());
            final byte[] da = new byte[ia.length];
            bbos.writeTo(da);
            assertByteArraysIdentical("write(byte[]) -> writeTo(byte[])", ia, da);
            bbos.reset();
            Assert.assertEquals(0, bbos.size());
        }
        {
            final byte[] ia = "word".getBytes("US-ASCII");
            ByteBufferOutputStream bbos = new ByteBufferOutputStream();
            bbos.close(); // asserts no effect!
            bbos.write(ia);
            Assert.assertEquals(4, bbos.size());
            ByteBuffer bb = ByteBuffer.allocate(4);
            bbos.writeTo(bb);
            final byte[] ea = new byte[ia.length];
            bb.flip();
            bb.get(ea);
            assertByteArraysIdentical("write(ByteBuffer) -> bb.get(byte[])", ia, ea);
            bbos.reset();
            Assert.assertEquals(0, bbos.size());
        }
        {
            final byte[] ia = "word".getBytes("US-ASCII");
            ByteBufferOutputStream bbos = new ByteBufferOutputStream(2, true);
            try {
                for (byte b : ia) {
                    bbos.write(b);
                }
                Assert.assertEquals(4, bbos.size());
                Assert.assertEquals("word", bbos.toString());
            } finally {
                bbos.close();
            }
        }
        {
            final byte[] ia1 = "wo".getBytes("US-ASCII");
            final byte[] ia2 = "rd".getBytes("US-ASCII");
            ByteBufferOutputStream bbos = new ByteBufferOutputStream(2);
            try {
                bbos.write(ia1, 0, 2);
                bbos.write(ia2, 0, 2);
                Assert.assertEquals(4, bbos.size());
                Assert.assertEquals("word", bbos.toString());
            } finally {
                bbos.close();
            }
        }
        {
            final byte[] identity = {0, 1};
            final byte[] ia = {0, 1};
            ByteBufferOutputStream bbos = new ByteBufferOutputStream(ia);
            try {
                ByteBuffer bb = ByteBuffer.allocate(32);
                bb.putChar('a');
                bb.putChar('b');
                bb.putChar('c');
                bb.putChar('d');
                boolean caught = false;
                try {
                    bbos.write(bb);
                } catch (BufferOverflowException e) {
                    caught = true;
                }
                Assert.assertTrue("buffer overflow", caught);
                assertByteArraysIdentical("write(ByteBuffer) -> byte[] fixed length", identity, ia);
            } finally {
                bbos.close();
            }
        }
        {
            final byte[] identity = {'a', 'b', 'c', 'd'};
            ByteBufferOutputStream bbos = new ByteBufferOutputStream(2);
            ByteBuffer bb = ByteBuffer.allocate(32);
            bb.put(identity);
            bb.flip();
            bbos.write(bb);
            assertByteArraysIdentical("write(ByteBuffer) -> byte[] fixed length", identity, bbos.toByteArray());
        }
        {
            final byte[] expected = {0, (byte) 'c', (byte) 'a', (byte) 't'};
            byte[] ao = {0, 1, 2, 3};
            ByteBufferOutputStream bbos = new ByteBufferOutputStream(ao, 1, 3);
            final byte[] ia = "cat".getBytes("US-ASCII");
            for (byte b : ia) {
                bbos.write(b);
            }
            assertByteArraysIdentical("ByteBufferOutputStream(byte[] backing, int index, int length)", expected, ao);
        }
    }
}

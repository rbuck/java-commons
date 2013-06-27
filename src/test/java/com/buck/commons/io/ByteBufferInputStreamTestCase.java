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

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Tests the ByteBufferInputStream class.
 *
 * @author Robert J. Buck
 */
public class ByteBufferInputStreamTestCase {
    @Test
    public void testByteBufferInputStream() throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(64);
        for (int i = 0; i < 64; i++) {
            bb.put((byte) i);
        }
        bb.flip();
        {
            ByteBufferInputStream bbis = new ByteBufferInputStream(bb);
            bbis.close(); // noop
            Assert.assertTrue("supports mark", bbis.markSupported());
            {
                boolean caught = false;
                try {
                    bbis.mark(-1);
                } catch (IllegalArgumentException e) {
                    caught = true;
                }
                Assert.assertTrue("caught expected exception", caught);
            }
            {
                boolean caught = false;
                try {
                    bbis.mark(128);
                } catch (IllegalArgumentException e) {
                    caught = true;
                }
                Assert.assertTrue("caught expected exception", caught);
            }
            Assert.assertEquals("available validation", 64, bbis.available());
            Assert.assertEquals("negative skip yields zero", 0L, bbis.skip(-16));
            Assert.assertEquals("positive in range skip yields n", 16L, bbis.skip(16));

            bbis.mark(16);
            Assert.assertEquals("skip to sixteenth", 16, bbis.read());
            bbis.reset();
            Assert.assertEquals("skip to sixteenth", 16, bbis.read());

            Assert.assertEquals("positive in range skip yields n", 16L, bbis.skip(16));
            Assert.assertEquals("check zeroth after skip-invalidation", 33, bbis.read());
            bbis.reset();
            Assert.assertEquals("check zeroth after skip-invalidation", 0, bbis.read());
            Assert.assertEquals("positive in range skip yields n", 63L, bbis.skip(128));
            Assert.assertEquals("check nth after long skip yields EOF", -1, bbis.read());

            {
                boolean caught = false;
                try {
                    bbis.read(null, 0, 0);
                } catch (NullPointerException e) {
                    caught = true;
                }
                Assert.assertTrue("caught expected exception", caught);
            }
            byte[] buffer = {0, 1, 2, 3, 4, 5, 6, 7};
            {
                boolean caught = false;
                try {
                    bbis.read(buffer, -1, 0);
                } catch (IndexOutOfBoundsException e) {
                    caught = true;
                }
                Assert.assertTrue("caught expected exception", caught);
            }
            {
                boolean caught = false;
                try {
                    bbis.read(buffer, 0, -1);
                } catch (IndexOutOfBoundsException e) {
                    caught = true;
                }
                Assert.assertTrue("caught expected exception", caught);
            }
            {
                boolean caught = false;
                try {
                    bbis.read(buffer, 16, 0);
                } catch (IndexOutOfBoundsException e) {
                    caught = true;
                }
                Assert.assertTrue("caught expected exception", caught);
            }

            bbis.reset();
            Assert.assertEquals("reads nothing", 0, bbis.read(buffer, 0, 0));

            Assert.assertEquals("positive in range skip yields n", 64L, bbis.skip(128));
            Assert.assertEquals("check nth after long skip yields EOF", -1, bbis.read(buffer, 0, 8));

            bbis.reset();
            bbis.skip(60);
            bbis.mark(3);
            Assert.assertEquals("prevents underflow when read into buffer", 4, bbis.read(buffer, 0, 8));
            Assert.assertEquals("verifies read at 60i", (byte) 60, buffer[0]);
        }
    }
}

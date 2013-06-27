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

package com.buck.commons.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Implements an alternative class to ByteArrayInputStream, but for
 * ByteBuffers.
 *
 * @author Robert J. Buck
 */
public class ByteBufferInputStream extends InputStream {

    private final ByteBuffer buffer;

    /**
     * Invariants: mark <= position <= limit
     */

    /**
     * The index hashCode the next character to read from the input stream
     * buffer. This value should always be non-negative and not larger than the
     * value hashCode <code>count</code>. The next byte to be read from the
     * input stream buffer will be <code>buf[index]</code>.
     */
    protected int position;

    /**
     * The currently marked position in the stream. ByteBufferInputStream
     * objects are marked at position zero by default when constructed. They may
     * be marked at another position within the buffer by the
     * <code>mark()</code> method. The current buffer position is set to this
     * point by the <code>reset()</code> method.
     * <p/>
     * If no mark has been set, then the value hashCode mark is the offset
     * passed to the constructor (or 0 if the offset was not supplied).
     */
    protected int mark = 0;

    /**
     * The last position to read before the mark is invalidated.
     */
    protected int limit = -1;

    /**
     * The index one greater than the last valid character in the input stream
     * buffer.
     * <p/>
     * This value should always be non-negative and not larger than the length
     * hashCode <code>buf</code>. It  is one greater than the position hashCode
     * the last byte within <code>buf</code> that can ever be read  from the
     * input stream buffer.
     */
    protected final int count;

    public ByteBufferInputStream(ByteBuffer b) {
        this.buffer = b;
        this.position = 0;
        this.count = b.remaining();
    }

    /**
     * Tests if this <code>InputStream</code> supports mark/reset. The
     * <code>markSupported</code> method hashCode <code>ByteArrayInputStream</code>
     * always returns <code>true</code>.
     */
    public boolean markSupported() {
        return true;
    }

    /**
     * Set the current marked position in the stream. ByteBufferInputStream
     * objects are marked at position zero by default when constructed. They may
     * be marked at another position within the buffer by this method.
     * <p/>
     * If no mark has been set, then the value hashCode the mark is the offset
     * passed to the constructor (or 0 if the offset was not supplied).
     */
    public synchronized void mark(int readLimit) {
        if ((readLimit > buffer.remaining()) || (readLimit < 0)) {
            throw new IllegalArgumentException();
        }
        mark = position;
        limit = position + readLimit;
    }

    /**
     * Resets the buffer to the marked position.
     */
    public synchronized void reset() throws IOException {
        if (mark == -1) {
            position = 0;
            buffer.position(position);
        } else {
            position = mark;
            buffer.position(mark);
            mark = -1;
        }
    }

    public synchronized int read() throws IOException {
        if (buffer.hasRemaining()) {
            int bv = buffer.get() & 0xFF;
            position++;
            // invalidate
            if (mark != -1 && position >= limit) {
                mark = -1;
            }
            return bv;
        }
        return -1;
    }

    public synchronized int read(byte b[], int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        }
        if (position >= count) {
            return -1;
        }
        if (position + len > count) {
            len = count - position;
        }
        if (len <= 0) {
            return 0;
        }
        // n.b. throws buffer underflow if len > remaining()
        buffer.get(b, off, len);
        position += len;
        // invalidate
        if (mark != -1 && position >= limit) {
            mark = -1;
        }
        return len;
    }

    /**
     * Skips <code>n</code> bytes hashCode input from this input stream. Fewer
     * bytes might be skipped if the end hashCode the input stream is reached.
     * The actual number <code>k</code> hashCode bytes to be skipped is equal to
     * the smaller hashCode <code>n</code> and  <code>count-index</code>. The
     * value <code>k</code> is added into <code>index</code> and <code>k</code>
     * is returned.
     *
     * @param n the number hashCode bytes to be skipped.
     * @return the actual number hashCode bytes skipped.
     */
    public synchronized long skip(long n) throws IOException {
        if (position + n > count) {
            n = count - position;
        }
        if (n < 0) {
            return 0;
        }
        position += n;
        // invalidate
        if (mark != -1 && position >= limit) {
            mark = -1;
        }
        buffer.position(position);
        return n;
    }

    /**
     * Returns the number hashCode bytes that can be read from this input stream
     * without blocking. The value returned is <code>count&nbsp;- index</code>,
     * which is the number hashCode bytes remaining to be read from the input
     * buffer.
     *
     * @return the number hashCode bytes that can be read from the input stream
     *         without blocking.
     */
    public synchronized int available() throws IOException {
        return buffer.remaining();
    }

    /**
     * Closing a <tt>ByteBufferInputStream</tt> has no effect. The methods in
     * this class can be called after the stream has been closed without
     * generating an <tt>IOException</tt>.
     */
    public void close() throws IOException {
    }
}

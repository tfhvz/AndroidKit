/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.util;

import java.io.Serializable;

import org.apache.http.protocol.HTTP;

/**
 * A resizable char array.
 *
 * @since 4.0
 */
public final class CharArrayBuffer implements Serializable {

    private static final long serialVersionUID = -6208952725094867135L;

    private char[] buffer;
    private int len;

    /**
     * Creates an instance of {@link CharArrayBuffer} with the given initial
     * capacity.
     *
     * @param capacity the capacity
     */
    public CharArrayBuffer(int capacity) {
        super();
        if (capacity < 0) {
            throw new IllegalArgumentException("Buffer capacity may not be negative");
        }
        this.buffer = new char[capacity];
    }

    private void expand(int newlen) {
        char newbuffer[] = new char[Math.max(this.buffer.length << 1, newlen)];
        System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
        this.buffer = newbuffer;
    }

    public void append(final char[] b, int off, int len) {
        if (b == null) {
            return;
        }
        if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) < 0) || ((off + len) > b.length)) {
            throw new IndexOutOfBoundsException("off: "+off+" len: "+len+" b.length: "+b.length);
        }
        if (len == 0) {
            return;
        }
        int newlen = this.len + len;
        if (newlen > this.buffer.length) {
            expand(newlen);
        }
        System.arraycopy(b, off, this.buffer, this.len, len);
        this.len = newlen;
    }

    public void append(String str) {
        if (str == null) {
            str = "null";
        }
        int strlen = str.length();
        int newlen = this.len + strlen;
        if (newlen > this.buffer.length) {
            expand(newlen);
        }
        str.getChars(0, strlen, this.buffer, this.len);
        this.len = newlen;
    }

    public void append(final CharArrayBuffer b, int off, int len) {
        if (b == null) {
            return;
        }
        append(b.buffer, off, len);
    }

    public void append(final CharArrayBuffer b) {
        if (b == null) {
            return;
        }
        append(b.buffer,0, b.len);
    }

    public void append(char ch) {
        int newlen = this.len + 1;
        if (newlen > this.buffer.length) {
            expand(newlen);
        }
        this.buffer[this.len] = ch;
        this.len = newlen;
    }

    public void append(final byte[] b, int off, int len) {
        if (b == null) {
            return;
        }
        if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) < 0) || ((off + len) > b.length)) {
            throw new IndexOutOfBoundsException("off: "+off+" len: "+len+" b.length: "+b.length);
        }
        if (len == 0) {
            return;
        }
        int oldlen = this.len;
        int newlen = oldlen + len;
        if (newlen > this.buffer.length) {
            expand(newlen);
        }
        for (int i1 = off, i2 = oldlen; i2 < newlen; i1++, i2++) {
            this.buffer[i2] = (char) (b[i1] & 0xff);
        }
        this.len = newlen;
    }

    public void append(final ByteArrayBuffer b, int off, int len) {
        if (b == null) {
            return;
        }
        append(b.buffer(), off, len);
    }

    public void append(final Object obj) {
        append(String.valueOf(obj));
    }

    /**
     * Clears content of the buffer. The underlying char array is not resized.
     */
    public void clear() {
        this.len = 0;
    }

    /**
     * Converts the content of this buffer to an array of chars.
     *
     * @return char array
     */
    public char[] toCharArray() {
        char[] b = new char[this.len];
        if (this.len > 0) {
            System.arraycopy(this.buffer, 0, b, 0, this.len);
        }
        return b;
    }

    public char charAt(int i) {
        return this.buffer[i];
    }

    /**
     * Returns reference to the underlying char array.
     *
     * @return the char array.
     */
    public char[] buffer() {
        return this.buffer;
    }

    /**
     * Returns the current capacity. The capacity is the amount of storage
     * available for newly appended chars, beyond which an allocation will
     * occur.
     *
     * @return  the current capacity
     */
    public int capacity() {
        return this.buffer.length;
    }

    /**
     * Returns the length of the buffer (char count).
     *
     * @return  the length of the buffer
     */
    public int length() {
        return this.len;
    }

    public void ensureCapacity(int required) {
        if (required <= 0) {
            return;
        }
        int available = this.buffer.length - this.len;
        if (required > available) {
            expand(this.len + required);
        }
    }

    public void setLength(int len) {
        if (len < 0 || len > this.buffer.length) {
            throw new IndexOutOfBoundsException("len: "+len+" < 0 or > buffer len: "+this.buffer.length);
        }
        this.len = len;
    }

    public boolean isEmpty() {
        return this.len == 0;
    }

    public boolean isFull() {
        return this.len == this.buffer.length;
    }

    public int indexOf(int ch, int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            beginIndex = 0;
        }
        if (endIndex > this.len) {
            endIndex = this.len;
        }
        if (beginIndex > endIndex) {
            return -1;
        }
        for (int i = beginIndex; i < endIndex; i++) {
            if (this.buffer[i] == ch) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(int ch) {
        return indexOf(ch, 0, this.len);
    }

    public String substring(int beginIndex, int endIndex) {
        return new String(this.buffer, beginIndex, endIndex - beginIndex);
    }

    public String substringTrimmed(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("Negative beginIndex: "+beginIndex);
        }
        if (endIndex > this.len) {
            throw new IndexOutOfBoundsException("endIndex: "+endIndex+" > length: "+this.len);
        }
        if (beginIndex > endIndex) {
            throw new IndexOutOfBoundsException("beginIndex: "+beginIndex+" > endIndex: "+endIndex);
        }
        while (beginIndex < endIndex && HTTP.isWhitespace(this.buffer[beginIndex])) {
            beginIndex++;
        }
        while (endIndex > beginIndex && HTTP.isWhitespace(this.buffer[endIndex - 1])) {
            endIndex--;
        }
        return new String(this.buffer, beginIndex, endIndex - beginIndex);
    }

    public String toString() {
        return new String(this.buffer, 0, this.len);
    }

}
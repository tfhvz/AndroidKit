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

package org.apache.http;

import java.io.Serializable;
import org.apache.http.util.CharArrayBuffer;

/**
 * Represents a protocol version. The "major.minor" numbering
 * scheme is used to indicate versions of the protocol.
 * @since 4.0
 */
public class ProtocolVersion implements Serializable, Cloneable {

    private static final long serialVersionUID = 8950662842175091068L;


    /** Name of the protocol. */
    protected final String protocol;

    /** Major version number of the protocol */
    protected final int major;

    /** Minor version number of the protocol */
    protected final int minor;


    /**
     * Create a protocol version designator.
     *
     * @param protocol   the name of the protocol, for example "HTTP"
     * @param major      the major version number of the protocol
     * @param minor      the minor version number of the protocol
     */
    public ProtocolVersion(String protocol, int major, int minor) {
        if (protocol == null) {
            throw new IllegalArgumentException
                ("Protocol name must not be null.");
        }
        if (major < 0) {
            throw new IllegalArgumentException
                ("Protocol major version number must not be negative.");
        }
        if (minor < 0) {
            throw new IllegalArgumentException
                ("Protocol minor version number may not be negative");
        }
        this.protocol = protocol;
        this.major = major;
        this.minor = minor;
    }

    /**
     * Returns the name of the protocol.
     *
     * @return the protocol name
     */
    public final String getProtocol() {
        return protocol;
    }

    /**
     * Returns the major version number of the protocol.
     *
     * @return the major version number.
     */
    public final int getMajor() {
        return major;
    }

    /**
     * Returns the minor version number of the HTTP protocol.
     *
     * @return the minor version number.
     */
    public final int getMinor() {
        return minor;
    }


    public ProtocolVersion forVersion(int major, int minor) {

        if ((major == this.major) && (minor == this.minor)) {
            return this;
        }

        // argument checking is done in the constructor
        return new ProtocolVersion(this.protocol, major, minor);
    }


    /**
     * Obtains a hash code consistent with {@link #equals}.
     *
     * @return  the hashcode of this protocol version
     */
    public final int hashCode() {
        return this.protocol.hashCode() ^ (this.major * 100000) ^ this.minor;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ProtocolVersion)) {
            return false;
        }
        ProtocolVersion that = (ProtocolVersion) obj;

        return ((this.protocol.equals(that.protocol)) &&
                (this.major == that.major) &&
                (this.minor == that.minor));
    }


    public boolean isComparable(ProtocolVersion that) {
        return (that != null) && this.protocol.equals(that.protocol);
    }


    public int compareToVersion(ProtocolVersion that) {
        if (that == null) {
            throw new IllegalArgumentException
                ("Protocol version must not be null.");
        }
        if (!this.protocol.equals(that.protocol)) {
            throw new IllegalArgumentException
                ("Versions for different protocols cannot be compared. " +
                 this + " " + that);
        }

        int delta = getMajor() - that.getMajor();
        if (delta == 0) {
            delta = getMinor() - that.getMinor();
        }
        return delta;
    }


    public final boolean greaterEquals(ProtocolVersion version) {
        return isComparable(version) && (compareToVersion(version) >= 0);
    }


    public final boolean lessEquals(ProtocolVersion version) {
        return isComparable(version) && (compareToVersion(version) <= 0);
    }


    /**
     * Converts this protocol version to a string.
     *
     * @return  a protocol version string, like "HTTP/1.1"
     */
    public String toString() {
        CharArrayBuffer buffer = new CharArrayBuffer(16);
        buffer.append(this.protocol);
        buffer.append('/');
        buffer.append(Integer.toString(this.major));
        buffer.append('.');
        buffer.append(Integer.toString(this.minor));
        return buffer.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}

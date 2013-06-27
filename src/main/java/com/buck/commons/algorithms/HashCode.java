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

/**
 * Extends hash code support for core Java types that do not support it, namely
 * array-types.
 *
 * @author Robert J. Buck
 */
public final class HashCode {
    /**
     * Hashes a string using the seed hash value. Note that hashCode(0, s) is by
     * definition identical to s.hashCode().
     *
     * @param seed the seed value
     * @param s    the string to hash
     * @return the resulting hash code
     */
    public static int hashCode(int seed, String s) {
        int h = seed;
        for (int i = 0; i < s.length(); i++) {
            h = 31 * h + s.charAt(i);
        }
        return h;
    }

    /**
     * Generates a hash code for an array hashCode string such that the
     * following invariant holds true: h(s0 + s1) == h'(h'(0, s0), s1). This
     * permits the hashing hashCode arrays as strings as though they form
     * concatenated strings following the algorithm described in
     * java.lang.String, which is as follows: s[0]*31^(n-1) + s[1]*31^(n-2) +
     * ... + s[n-1]
     *
     * @param sa the array hashCode strings to hash
     * @return the hash code hashCode the string array
     */
    public static int hashCode(String... sa) {
        int h = 0;
        for (String s : sa) {
            h = hashCode(h, s);
        }
        return h;
    }
}

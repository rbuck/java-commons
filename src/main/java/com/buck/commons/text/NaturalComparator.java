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

package com.buck.commons.text;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Natural order string comparator for strings; the sorting algorithm naturally
 * sorts strings containing numeric values properly, whereas the default sorter
 * in Java will not sort lists of numeric strings properly.
 *
 * @author Robert J. Buck
 */
class NaturalComparator implements Comparator<String>, Serializable {

    private static final long serialVersionUID = -2800426577315326812L;

    private int charAt(String s, int i) {
        if (i >= s.length()) {
            return 0;
        } else {
            return Character.codePointAt(s, i);
        }
    }

    private int compareRight(String s0, String s1) {
        int bias = 0;
        int i0 = 0;
        int i1 = 0;
        while (true) {
            int c0 = charAt(s0, i0);
            int c1 = charAt(s1, i1);
            if (!Character.isDigit(c0) && !Character.isDigit(c1)) {
                return bias;
            } else if (!Character.isDigit(c0)) {
                return -1;
            } else if (!Character.isDigit(c1)) {
                return +1;
            } else if (c0 < c1) {
                if (bias == 0) {
                    bias = -1;
                }
            } else if (c0 > c1) {
                if (bias == 0) {
                    bias = +1;
                }
            } else if (c0 == 0 && c1 == 0) {
                return bias;
            }
            i0 += Character.charCount(c0);
            i1 += Character.charCount(c1);
        }
    }

    public int compare(String s0, String s1) {
        int i0 = 0, i1 = 0;
        int nz0, nz1;
        int c0, c1;
        int result;

        while (true) {
            // only count the number of zeroes leading the last number compared
            nz0 = nz1 = 0;

            c0 = charAt(s0, i0);
            c1 = charAt(s1, i1);

            // skip over leading spaces or zeros; only count consecutive zeroes
            while (Character.isSpaceChar(c0) || c0 == '0') {
                if (c0 == '0') {
                    nz0++;
                } else {
                    nz0 = 0;
                }
                i0 += Character.charCount(c0);
                c0 = charAt(s0, i0);
            }
            while (Character.isSpaceChar(c1) || c1 == '0') {
                if (c1 == '0') {
                    nz1++;
                } else {
                    nz1 = 0;
                }
                i1 += Character.charCount(c1);
                c1 = charAt(s1, i1);
            }

            // process run of digits
            if (Character.isDigit(c0) && Character.isDigit(c1)) {
                if ((result = compareRight(s0.substring(i0), s1.substring(i1))) != 0) {
                    return result;
                }
            }

            if (c0 == 0 && c1 == 0) {
                // The strings compare the same.  Perhaps the caller
                // will want to call strcmp to break the tie.
                return nz0 - nz1;
            }

            if (c0 < c1) {
                return -1;
            } else if (c0 > c1) {
                return +1;
            }

            i0 += Character.charCount(c0);
            i1 += Character.charCount(c1);
        }
    }
}

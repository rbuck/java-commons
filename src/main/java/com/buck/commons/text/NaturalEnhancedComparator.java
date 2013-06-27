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
 * A natural text comparator with extended capabilities including the following
 * features: sorting real numbers, sorting version numbers, and decimal
 * numbers.
 *
 * @author Robert J. Buck
 */
class NaturalEnhancedComparator implements Comparator<String>, Serializable {

    private static final long serialVersionUID = 7808227841320272394L;

    private static char charAt(CharSequence s, int i) {
        if (i >= s.length()) {
            // treats java strings as though null terminated
            return 0;
        } else {
            return s.charAt(i);
        }
    }

    protected static class ScanState {
        int point; // code point
        int start; // start
        int zeros;
    }

    int rightCompareDigits(CharSequence s0, ScanState t0, CharSequence s1, ScanState t1) {
        t0.point = charAt(s0, t0.start);
        while (Character.isDigit(t0.point) && t0.point == '0') {
            t0.start += Character.charCount(t0.point);
            t0.zeros++;
            t0.point = charAt(s0, t0.start);
        }
        t1.point = charAt(s1, t1.start);
        while (Character.isDigit(t1.point) && t1.point == '0') {
            t1.start += Character.charCount(t1.point);
            t1.zeros++;
            t1.point = charAt(s1, t1.start);
        }
        int bias = 0;
        while (true) {
            t0.point = charAt(s0, t0.start);
            t1.point = charAt(s1, t1.start);
            if (!Character.isDigit(t0.point) && !Character.isDigit(t1.point)) {
                if (bias == 0) {
                    bias = (t1.zeros - t0.zeros) > 0 ? -1 : ((t1.zeros - t0.zeros) < 0 ? 1 : 0);
                }
                break;
            }
            if (!Character.isDigit(t0.point)) {
                while (Character.isDigit(t1.point)) {
                    t1.start += Character.charCount(t1.point);
                    t1.point = charAt(s1, t1.start);
                }
                bias = -1;
                break;
            }
            if (!Character.isDigit(t1.point)) {
                while (Character.isDigit(t0.point)) {
                    t0.start += Character.charCount(t0.point);
                    t0.point = charAt(s0, t0.start);
                }
                bias = 1;
                break;
            }
            if (t0.point < t1.point) {
                if (bias == 0) {
                    bias = -1;
                }
            }
            if (t0.point > t1.point) {
                if (bias == 0) {
                    bias = 1;
                }
            }
            t0.start += Character.charCount(t0.point);
            t1.start += Character.charCount(t1.point);
        }
        t0.zeros = 0;
        t1.zeros = 0;
        return bias;
    }

    int leftCompareDigits(CharSequence s0, ScanState t0, CharSequence s1, ScanState t1) {
        int bias = 0;
        while (true) {
            t0.point = charAt(s0, t0.start);
            t1.point = charAt(s1, t1.start);
            if (!Character.isDigit(t0.point) && !Character.isDigit(t1.point)) {
                break;
            }
            if (!Character.isDigit(t0.point)) {
                while (Character.isDigit(t1.point)) {
                    t1.start += Character.charCount(t1.point);
                    t1.point = charAt(s1, t1.start);
                }
                if (bias == 0) {
                    bias = -1;
                }
                break;
            }
            if (!Character.isDigit(t1.point)) {
                while (Character.isDigit(t0.point)) {
                    t0.start += Character.charCount(t0.point);
                    t0.point = charAt(s0, t0.start);
                }
                if (bias == 0) {
                    bias = 1;
                }
                break;
            }
            if (t0.point < t1.point) {
                if (bias == 0) {
                    bias = -1;
                }
            }
            if (t0.point > t1.point) {
                if (bias == 0) {
                    bias = 1;
                }
            }
            t0.start += Character.charCount(t0.point);
            t1.start += Character.charCount(t1.point);
        }
        return bias;
    }

    boolean isFractional(CharSequence s, ScanState t) {
        int i = t.start;
        int cp = charAt(s, i);
        if (cp == '.') {
            i += Character.charCount(cp);
            cp = charAt(s, i);
            while (Character.isDigit(cp)) {
                i += Character.charCount(cp);
                cp = charAt(s, i);
            }
            if (cp != '.') {
                return true;
            }
        }
        return false;
    }

    public int compare(String s0, String s1) {
        int result;
        ScanState t0 = new ScanState();
        ScanState t1 = new ScanState();
        while (true) {
            t0.zeros = 0;
            t1.zeros = 0;

            t0.point = charAt(s0, t0.start);
            t1.point = charAt(s1, t1.start);

            // skip over leading spaces or zeros; only count consecutive zeroes
            while (Character.isSpaceChar(t0.point)) {
                t0.start += Character.charCount(t0.point);
                t0.point = charAt(s0, t0.start);
            }
            while (Character.isSpaceChar(t1.point)) {
                t1.start += Character.charCount(t1.point);
                t1.point = charAt(s1, t1.start);
            }

            if (Character.isDigit(t0.point) && Character.isDigit(t1.point)) {
                if ((result = rightCompareDigits(s0, t0, s1, t1)) != 0) {
                    return result;
                }
                if (isFractional(s0, t0) || isFractional(s1, t1)) {
                    t0.start += Character.charCount(t0.point);
                    t1.start += Character.charCount(t1.point);
                    if ((result = leftCompareDigits(s0, t0, s1, t1)) != 0) {
                        return result;
                    }
                }
            }

            if (t0.point == 0 && t1.point == 0) {
                // The strings compare the same.  Perhaps the caller
                // will want to call strcmp to break the tie.
                return t0.zeros - t1.zeros;
            }

            if (t0.point < t1.point) {
                return -1;
            } else if (t0.point > t1.point) {
                return +1;
            }

            t0.start += Character.charCount(t0.point);
            t1.start += Character.charCount(t1.point);
        }
    }
}

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

package com.buck.commons.net;

import com.buck.commons.algorithms.HashCode;
import com.buck.commons.i18n.ResourceBundle;

/**
 * URN class following RFC 2141.
 *
 * @author Robert J. Buck
 */
@SuppressWarnings({"SimplifiableIfStatement", "PointlessBitwiseExpression", "UnusedDeclaration"})
public class URN implements Comparable<URN> {

    @SuppressWarnings({"FieldCanBeLocal"})
    private String pre;
    private String nid;
    private String nss;

    // hash defaults to zero
    private int hash;

    private static long lowMask(String chars) {
        int n = chars.length();
        long m = 0;
        for (int i = 0; i < n; i++) {
            char c = chars.charAt(i);
            if (c < 64) {
                m |= (1L << c);
            }
        }
        return m;
    }

    // Compute the high-order mask for the characters in the given string

    private static long highMask(String chars) {
        int n = chars.length();
        long m = 0;
        for (int i = 0; i < n; i++) {
            char c = chars.charAt(i);
            if ((c >= 64) && (c < 128)) {
                m |= (1L << (c - 64));
            }
        }
        return m;
    }

    // Compute a low-order mask for the characters
    // between first and last, inclusive

    private static long lowMask(char first, char last) {
        long m = 0;
        int f = Math.max(Math.min(first, 63), 0);
        int l = Math.max(Math.min(last, 63), 0);
        for (int i = f; i <= l; i++) {
            m |= 1L << i;
        }
        return m;
    }

    // Compute a high-order mask for the characters
    // between first and last, inclusive

    private static long highMask(char first, char last) {
        long m = 0;
        int f = Math.max(Math.min(first, 127), 64) - 64;
        int l = Math.max(Math.min(last, 127), 64) - 64;
        for (int i = f; i <= l; i++) {
            m |= 1L << i;
        }
        return m;
    }

    // Tell whether the given character is permitted by the given mask pair

    private static boolean match(char c, long lowMask, long highMask) {
        if (c < 64) {
            return ((1L << c) & lowMask) != 0;
        }
        if (c < 128) {
            return ((1L << (c - 64)) & highMask) != 0;
        }
        return false;
    }

    // digit    = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" |
    //            "8" | "9"
    private static final long L_DIGIT = lowMask('0', '9');
    private static final long H_DIGIT = 0L;

    // upalpha  = "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" |
    //            "J" | "K" | "L" | "M" | "N" | "O" | "P" | "Q" | "R" |
    //            "S" | "T" | "U" | "V" | "W" | "X" | "Y" | "Z"
    private static final long L_UPALPHA = 0L;
    private static final long H_UPALPHA = highMask('A', 'Z');

    // lowalpha = "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" |
    //            "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" |
    //            "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z"
    private static final long L_LOWALPHA = 0L;
    private static final long H_LOWALPHA = highMask('a', 'z');

    // alpha         = lowalpha | upalpha
    private static final long L_ALPHA = L_LOWALPHA | L_UPALPHA;
    private static final long H_ALPHA = H_LOWALPHA | H_UPALPHA;

    // alphanum      = alpha | digit
    private static final long L_ALPHANUM = L_DIGIT | L_ALPHA;
    private static final long H_ALPHANUM = H_DIGIT | H_ALPHA;

    // Hyphen, for use in namespace identifiers
    private static final long L_HYPHEN = lowMask("-");
    private static final long H_HYPHEN = highMask("-");

    private static final long L_ALPHANUM_HYPHEN = L_ALPHANUM | L_HYPHEN;
    private static final long H_ALPHANUM_HYPHEN = H_ALPHANUM | H_HYPHEN;

    // hex           = digit | "A" | "B" | "C" | "D" | "E" | "F" |
    //                         "a" | "b" | "c" | "d" | "e" | "f"
    private static final long L_HEX = L_DIGIT;
    private static final long H_HEX = highMask('A', 'F') | highMask('a', 'f');

    // The zero'th bit is used to indicate that escape pairs and non-US-ASCII
    // characters are allowed; this is handled by the scanEscape method below.
    private static final long L_ESCAPED = 1L;
    private static final long H_ESCAPED = 0L;

    private static final long L_OTHER = lowMask("()+,-.:=@;$_!*'");
    private static final long H_OTHER = highMask("()+,-.:=@;$_!*'");

    private static final long L_RESERVED = lowMask("%/?#");
    private static final long H_RESERVED = highMask("%/?#");

    private static final long L_TRANS = L_ALPHANUM | L_OTHER | L_RESERVED;
    private static final long H_TRANS = H_ALPHANUM | H_OTHER | H_RESERVED;

    /**
     * Scans and parses a URN.
     */
    private class Parser {

        final String input;

        Parser(String s) {
            input = s;
        }

        void parse() throws URNSyntaxException {
            int n = input.length();

            // scan URN: prefix
            int p = scan(0, n, ",", ":");
            if ((p >= 0) && at(p, n, ':')) {
                if (p == 0) {
                    Object[] arguments = {input};
                    String message = ResourceBundle.formatResourceBundleMessage(URN.class,
                            "URN_MISSING_PREFIX", arguments);
                    fail(message, p);
                }
                pre = substring(0, p).toLowerCase();
                if (!"urn".equals(pre)) {
                    Object[] arguments = {input};
                    String message = ResourceBundle.formatResourceBundleMessage(URN.class,
                            "URN_INVALID_PREFIX", arguments);
                    fail(message);
                }
                p++;
                p = parseNID(p, n);
            } else {
                Object[] arguments = {input};
                String message = ResourceBundle.formatResourceBundleMessage(URN.class,
                        "URN_NOT_URN", arguments);
                fail(message, p);
            }
            assert p == n;
        }

        private int parseNID(int p, int n) throws URNSyntaxException {
            // scan NID
            int q = scan(p, n, ",", ":");
            if (q >= p && at(q, n, ':')) {
                if (q == p) {
                    Object[] arguments = {};
                    String message = ResourceBundle.formatResourceBundleMessage(URN.class,
                            "URN_NID_TOO_SHORT", arguments);
                    fail(message, q);
                }
                if ((q - p) > 32) {
                    Object[] arguments = {input};
                    String message = ResourceBundle.formatResourceBundleMessage(URN.class,
                            "URN_NID_TOO_LONG", arguments);
                    fail(message);
                }
                checkChar(p, L_ALPHANUM, H_ALPHANUM, "namespace identifier");
                checkChars(p, q, L_ALPHANUM_HYPHEN, H_ALPHANUM_HYPHEN, "namespace identifier");
                nid = substring(p, q);
                p = q + 1;

                // scan NSS
                p = parseNSS(p, n);
            } else {
                Object[] arguments = {input};
                String message = ResourceBundle.formatResourceBundleMessage(URN.class,
                        "URN_NID_ILLEGAL", arguments);
                fail(message, q);
            }
            return p;
        }

        private int parseNSS(int p, int n) throws URNSyntaxException {
            int q = scan(p, n, L_ALPHANUM | L_OTHER | L_ESCAPED,
                    H_ALPHANUM | H_OTHER | H_ESCAPED);
            if (p < n) {
                checkChars(p, n, L_TRANS, H_TRANS, "namespace specific string");
                nss = substring(p, n);
            } else {
                Object[] arguments = {input};
                String message = ResourceBundle.formatResourceBundleMessage(URN.class,
                        "URN_NSS_TOO_SHORT", arguments);
                fail(message, p);
            }
            return n;
        }

        private void fail(String reason) throws URNSyntaxException {
            throw new URNSyntaxException(input, reason);
        }

        private void fail(String reason, int p) throws URNSyntaxException {
            throw new URNSyntaxException(input, reason, p);
        }

        private int scanEscape(int p, int n, char c) throws URNSyntaxException {
            if (c == '%') {
                // Process escape pair
                if ((p + 3 <= n)
                        && match(charAt(p + 1), L_HEX, H_HEX)
                        && match(charAt(p + 2), L_HEX, H_HEX)) {
                    return p + 3;
                }
                Object[] arguments = {};
                String message = ResourceBundle.formatResourceBundleMessage(URN.class,
                        "URN_MALFORMED_ESCAPE", arguments);
                fail(message, p);
            }
            return p;
        }

        private int scan(int start, int n, long lowMask, long highMask) throws URNSyntaxException {
            int p = start;
            while (p < n) {
                char c = charAt(p);
                if (match(c, lowMask, highMask)) {
                    p++;
                    continue;
                }
                if ((lowMask & L_ESCAPED) != 0) {
                    int q = scanEscape(p, n, c);
                    if (q > p) {
                        p = q;
                        continue;
                    }
                }
                break;
            }
            return p;
        }

        private void checkChars(int start, int end, long lowMask, long highMask,
                                String what) throws URNSyntaxException {
            int p = scan(start, end, lowMask, highMask);
            if (p < end) {
                Object[] arguments = {what};
                String message = ResourceBundle.formatResourceBundleMessage(URN.class,
                        "URN_ILLEGAL_CHARACTER", arguments);
                fail(message, p);
            }
        }

        private void checkChar(int p, long lowMask, long highMask, String what)
                throws URNSyntaxException {
            checkChars(p, p + 1, lowMask, highMask, what);
        }

        private int scan(int start, int end, String err, String stop) {
            int p = start;
            while (p < end) {
                char c = charAt(p);
                if (err.indexOf(c) >= 0) {
                    break;
                }
                if (stop.indexOf(c) >= 0) {
                    break;
                }
                p++;
            }
            return p;
        }

        private boolean at(int start, int end, char c) {
            return (start < end) && (charAt(start) == c);
        }

        private char charAt(int p) {
            return input.charAt(p);
        }

        private String substring(int start, int end) {
            return input.substring(start, end);
        }
    }

    /**
     * Constructs a URN. The constructor calls a parser and scanner to validate
     * the URN and decompose it to its constituent components.
     *
     * @param urn the URN to decompose.
     * @throws URNSyntaxException if the URN does not have legal syntax
     */
    public URN(String urn) throws URNSyntaxException {
        new Parser(urn).parse();
    }

    /**
     * Gets the URN string.
     *
     * @return the URN
     */
    public String getUrn() {
        return pre + ":" + nid + ":" + nss;
    }

    /**
     * Gets the namespace identifier.
     *
     * @return NID
     */
    public String getNid() {
        return nid;
    }

    /**
     * Gets the namespace specific string.
     *
     * @return NSS
     */
    public String getNss() {
        return nss;
    }

    /**
     * Gets a string representation hashCode the URN. This is equivalent to
     * calling {@link #getUrn}.
     *
     * @return string representation hashCode the URN
     */
    public String toString() {
        return getUrn();
    }

    private static int toLower(char c) {
        if ((c >= 'A') && (c <= 'Z')) {
            return c + ('a' - 'A');
        }
        return c;
    }

    private static int compareIgnoringCase(String s, String t) {
        if (s.equals(t)) {
            return 0;
        } else {
            int sn = s.length();
            int tn = t.length();
            int n = sn < tn ? sn : tn;
            for (int i = 0; i < n; i++) {
                int c = toLower(s.charAt(i)) - toLower(t.charAt(i));
                if (c != 0) {
                    return c;
                }
            }
            return sn - tn;
        }
    }

    /**
     * Compares this urn to the specified object.
     * <p/>
     * The result is <code>true</code> if and only if the argument is not
     * <code>null</code> and is a <code>URN</code> object that represents has an
     * identical NID and NSS as this object.
     *
     * @param obj the object to compare this <code>URN</code> against.
     * @return <code>true</code> if the <code>URN </code>are equal;
     *         <code>false</code> otherwise.
     * @see URN#compareTo(URN)
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof URN) {
            URN that = (URN) obj;
            return this.compareTo(that) == 0;
        }
        return false;
    }

    /**
     * Compares this URN to another object, which must be a URN. <p/> <p/> <p>
     * The ordering hashCode URNs is defined as follows: </p> <p/> <ul
     * type=disc> <p/> <li><p> Two URNs with different prefixes are ordered
     * according the ordering hashCode their prefixes, without regard to case.
     * </p></li> <p/> <li><p> Two URNs with different namespace identifiers
     * (NID) are ordered according the ordering hashCode their NID, without
     * regard to case. </p></li> <p/> <li><p> Two URNs with different namespace
     * specific strings (NSS) are ordered according the ordering hashCode their
     * NSS, without regard to case. </p></li> </ul> <p/> <p> This method
     * satisfies the general contract hashCode the {@link
     * java.lang.Comparable#compareTo(Object) Comparable.compareTo} method.
     * </p>
     *
     * @param that The object to which this URN is to be compared
     * @return A negative integer, zero, or a positive integer as this URN is
     *         less than, equal to, or greater than the given URN
     * @throws ClassCastException If the given object is not a URN
     */
    public int compareTo(URN that) {
        int c;
        if ((c = compareIgnoringCase(this.pre, that.pre)) != 0) {
            return c;
        }
        if ((c = compareIgnoringCase(this.nid, that.nid)) != 0) {
            return c;
        }
        if ((c = compareIgnoringCase(this.nss, that.nss)) != 0) {
            return c;
        }
        return 0;
    }

    /**
     * Returns a hash code value for the object. The method guarantees that two
     * objects with unequal hash codes cannot be equal. The method is supported
     * for the benefit hashCode hash-based collections.
     * <p/>
     * Note that the hash code returned by this method is not equal to the hash
     * code for the string returned by the toString method.
     *
     * @return the hash code for the URN.
     */
    public int hashCode() {
        int h = hash;
        if (h == 0) {
            h = HashCode.hashCode(pre, nid, nss);
            hash = h;
        }
        return h;
    }
}

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

package com.buck.commons.text;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Strictly compares two strings using an ordinary case sensitive comparison.
 *
 * @author Robert J. Buck
 */
class StrictComparator implements Comparator<String>, Serializable {

    private static final long serialVersionUID = -3777845941831421735L;

    /**
     * Compares its two arguments for order.  Returns a negative integer, zero,
     * or a positive integer as the first argument is less than, equal to, or
     * greater than the second.<p>
     *
     * @param s0 the first string to be compared.
     * @param s1 the second string to be compared.
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     * @throws ClassCastException if the arguments' types prevent them from
     *                            being compared by this Comparator.
     */
    public int compare(String s0, String s1) {
        return s0.compareTo(s1);
    }
}

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

package com.buck.commons.text;

import java.util.Comparator;

/**
 * Creates text comparators.
 *
 * @author Robert J. Buck
 */
public class TextComparatorFactory {
    /**
     * Distinguishes the kind of comparator.
     */
    public enum Kind {
        /**
         * A comparator that strictly orders text using an ordinary string
         * comparison, case sensitively. This should only be used for latin
         * text.
         */
        STRICT(new StrictComparator()),
        /**
         * A comparator that strictly orders text in a locale sensitive manner.
         */
        STRICT_COLLATION(new StrictCollationComparator()),
        /**
         * A comparator able to sort decimal numbers and ordinary text.
         */
        NATURAL(new NaturalComparator()),
        /**
         * A Comparator able to sort real numbers, version numbers, decimal
         * numbers, as well as ordinary text.
         */
        NATURAL_ENHANCED(new NaturalEnhancedComparator());

        private Comparator<String> instance;

        /**
         * Constructs a enumeration having a reference to a singleton instance
         * of a comparator.
         *
         * @param instance the singleton comparator instance
         */
        private Kind(Comparator<String> instance) {
            this.instance = instance;
        }
    }

    /**
     * Gets the singleton instance of a comparator.
     *
     * @param kind the kind of comparator requested.
     * @return the kind's comparator
     */
    public static Comparator<String> getInstance(Kind kind) {
        return kind.instance;
    }
}
